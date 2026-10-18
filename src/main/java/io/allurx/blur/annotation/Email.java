/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.annotation;

import io.allurx.annotation.parser.handler.Parse;
import io.allurx.blur.handler.EmailHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Masks email addresses between the first code point and the first '@' by default.
 *
 * @author allurx
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parse(handler = EmailHandler.class, annotation = Email.class)
public @interface Email {

    /**
     * Leading Unicode code points to retain; ignored for a nonempty {@link #regexp()}.
     * Must be nonnegative, and the sum with {@link #endOffset()} must not exceed the input's code point count.
     *
     * @return the leading code point count
     */
    int startOffset() default 0;

    /**
     * Trailing Unicode code points to retain; ignored for a nonempty {@link #regexp()}.
     * Must be nonnegative, and the sum with {@link #startOffset()} must not exceed the input's code point count.
     *
     * @return the trailing code point count
     */
    int endOffset() default 0;

    /**
     * A nonempty pattern overrides {@link #startOffset()} and {@link #endOffset()}.
     * Masks whole matches; empty matches are ignored. Boundaries inside surrogate pairs
     * expand outward to mask the entire pair.
     * <p>
     * The anchored, possessive default avoids repeated scanning and backtracking;
     * its bounded lookbehind accommodates a leading surrogate pair.
     *
     * @return the masking pattern, or empty to use offsets
     */
    String regexp() default "(?<=\\A[^@]{1,2})[^@]*+(?=@)";

    /**
     * Replaces each selected UTF-16 {@code char}, preserving the input length.
     *
     * @return the replacement character
     */
    char placeholder() default '*';

    /**
     * Controls whether the input is masked.
     *
     * @return the condition class
     */
    Class<? extends Condition<?>> condition() default AlwaysTrue.class;

}
