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
import io.allurx.blur.handler.EmailHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking email addresses as sensitive.
 * The default blurring rule masks all characters between the second character
 * and the '@' symbol.
 *
 * @author allurx
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parse(handler = EmailHandler.class, annotation = Email.class)
public @interface Email {

    /**
     * Specifies the starting offset of sensitive information in the original character sequence.
     *
     * @return The starting position to blur, defaults to 0.
     */
    int startOffset() default 0;

    /**
     * Specifies the ending offset of sensitive information in the original character sequence.
     *
     * @return The ending position to blur, defaults to 0.
     */
    int endOffset() default 0;

    /**
     * Returns a regular expression to match the sensitive part of the email.
     * If this is not empty, it will override the {@link #startOffset()} and
     * {@link #endOffset()} settings.
     *
     * @return A regular expression to match the sensitive part of the email.
     */
    String regexp() default "(?<=.).*(?=@)";

    /**
     * Specifies the placeholder character to replace sensitive information.
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
