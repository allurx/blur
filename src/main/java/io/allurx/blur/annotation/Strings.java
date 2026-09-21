/*
 * Copyright 2024 allurx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.allurx.blur.annotation;

import io.allurx.annotation.parser.handler.Parse;
import io.allurx.blur.handler.StringHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Masks every character in a {@link String} by default.
 *
 * @author allurx
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parse(handler = StringHandler.class, annotation = Strings.class)
public @interface Strings {

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
     *
     * @return the masking pattern, or empty to use offsets
     */
    String regexp() default "";

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
