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
 * Annotation for marking {@link String} objects as sensitive.
 * The default blurring rule masks all characters in the string.
 *
 * @author allurx
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parse(handler = StringHandler.class, annotation = Strings.class)
public @interface Strings {

    /**
     * Specifies the number of leading UTF-16 {@code char} units to retain.
     * Ignored when {@link #regexp()} is not empty. Otherwise, this value must be nonnegative,
     * and its sum with {@link #endOffset()} must not exceed the input length.
     *
     * @return The number of leading units to retain, defaults to 0.
     */
    int startOffset() default 0;

    /**
     * Specifies the number of trailing UTF-16 {@code char} units to retain.
     * Ignored when {@link #regexp()} is not empty. Otherwise, this value must be nonnegative,
     * and its sum with {@link #startOffset()} must not exceed the input length.
     *
     * @return The number of trailing units to retain, defaults to 0.
     */
    int endOffset() default 0;

    /**
     * If {@code regexp} is not empty, {@link #startOffset()} and {@link #endOffset()} are ignored.
     *
     * @return A regular expression to match the sensitive part of the data.
     */
    String regexp() default "";

    /**
     * Specifies the placeholder character to replace sensitive information.
     * Replaces each selected UTF-16 {@code char} unit, preserving the input length.
     *
     * @return The placeholder character, defaults to '*'.
     */
    char placeholder() default '*';

    /**
     * Specifies the condition under which the input should be blurred.
     *
     * @return The condition class, defaults to {@link AlwaysTrue}.
     */
    Class<? extends Condition<?>> condition() default AlwaysTrue.class;

}
