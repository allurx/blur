/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.blur;

import io.allurx.annotation.parser.AnnotationParser;
import io.allurx.annotation.parser.type.Cascade;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;

/**
 * Entry point for annotation-based masking of values and object fields.
 * Object traversal and copying follow the rules supported by {@link AnnotationParser}.
 *
 * @author allurx
 * @see AnnotatedTypeToken
 */
public final class Blur {

    private Blur() {
    }

    /**
     * Blurs the fields of the input.
     *
     * @param <T>   The type of the input
     * @param input The object to be blurred, which may be {@code null}
     * @return The blurred result, which may be {@code null}
     */
    public static <T> T blur(T input) {
        return blur(input, new AnnotatedTypeToken<@Cascade T>() {
        });
    }

    /**
     * Blurs the input based on its {@link AnnotatedTypeToken}.
     * Handling of {@code null} input is delegated to {@link AnnotationParser}.
     *
     * @param input     The object to be blurred, which may be {@code null}
     * @param typeToken The {@link AnnotatedTypeToken} representing the type of the input
     * @param <T>       The type of the input
     * @return The parser result, which may be {@code null}
     * @throws NullPointerException if {@code typeToken} is {@code null}
     */
    public static <T> T blur(T input, AnnotatedTypeToken<T> typeToken) {
        return AnnotationParser.parse(input, typeToken);
    }

}

