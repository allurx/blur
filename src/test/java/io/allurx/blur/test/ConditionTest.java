/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.blur.test;

import io.allurx.annotation.parser.util.Singletons;
import io.allurx.blur.Blur;
import io.allurx.blur.annotation.Condition;
import io.allurx.blur.annotation.Strings;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class tests the custom conditions for applying the blurring functionality.
 * <p>
 * It verifies that blurring only occurs based on specified conditions.
 *
 * @author allurx
 */
class ConditionTest {

    @Test
    void reusesConditionAcrossBlurCalls() {
        Singletons.remove(FirstCallCondition.class);
        try {
            var typeToken = new AnnotatedTypeToken<@Strings(condition = FirstCallCondition.class) String>() {
            };

            assertEquals("******", Blur.blur("123456", typeToken));
            assertEquals("123456", Blur.blur("123456", typeToken));
        } finally {
            Singletons.remove(FirstCallCondition.class);
        }
    }

    private static class FirstCallCondition implements Condition<String> {

        private boolean firstCall = true;

        @Override
        public boolean required(String input) {
            boolean required = firstCall;
            firstCall = false;
            return required;
        }
    }

    @Test
    void blur() {

        // Create an array with various string values including null and empty
        var before = new String[]{"", null, "123456"};

        // Apply the blur method with a custom condition
        var after = Blur.blur(before, new AnnotatedTypeToken<@Strings(condition = StringCondition.class) String[]>() {
        });

        // Verify that the results are as expected based on the condition
        assertEquals("", after[0]);
        assertNull(after[1]);
        assertEquals("******", after[2]);
    }

    /**
     * This class defines the conditions under which blurring is applied.
     * <p>
     * Blurring occurs only for non-null and non-empty strings.
     */
    private static class StringCondition implements Condition<String> {

        @Override
        public boolean required(String input) {
            return input != null && !input.isEmpty();
        }
    }
}

