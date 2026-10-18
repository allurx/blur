/*
 * Copyright 2026 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.blur.test;

import io.allurx.annotation.parser.AnnotationParser;
import io.allurx.annotation.parser.type.TypeParser;
import io.allurx.blur.Blur;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AnnotatedType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Verifies null handling through the public Blur entry point and a registered parser.
 *
 * @author allurx
 */
class NullContractTest {

    @Test
    void rejectsNullTypeTokens() {
        assertThrows(NullPointerException.class, () -> Blur.blur("value", null));
        assertThrows(NullPointerException.class, () -> Blur.blur(null, null));
    }

    @Test
    void delegatesNullInputToSupportingParser() {
        var replacement = new Value("created-from-null");
        var parser = new ReplacingParser(replacement);
        AnnotationParser.addTypeParser(parser);
        try {
            var result = Blur.blur(null, new AnnotatedTypeToken<Value>() {
            });

            assertSame(replacement, result);
            assertEquals(1, parser.calls);
            assertNull(parser.received);
        } finally {
            AnnotationParser.removeTypeParser(parser);
        }
    }

    @Test
    void propagatesNullParserResult() {
        var original = new Value("original");
        var parser = new ReplacingParser(null);
        AnnotationParser.addTypeParser(parser);
        try {
            var result = Blur.blur(original, new AnnotatedTypeToken<Value>() {
            });

            assertNull(result);
            assertEquals(1, parser.calls);
            assertSame(original, parser.received);
        } finally {
            AnnotationParser.removeTypeParser(parser);
        }
    }

    private record Value(String value) {
    }

    private static final class ReplacingParser implements TypeParser<Value, AnnotatedType> {

        private final int order = AnnotationParser.randomOrder();
        private final Value replacement;
        private Value received;
        private int calls;

        private ReplacingParser(Value replacement) {
            this.replacement = replacement;
        }

        @Override
        public Value parse(Value input, AnnotatedType type) {
            calls++;
            received = input;
            return replacement;
        }

        @Override
        public boolean support(Object input, AnnotatedType type) {
            return type.getType() == Value.class;
        }

        @Override
        public int order() {
            return order;
        }
    }
}
