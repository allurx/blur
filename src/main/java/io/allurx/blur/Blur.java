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
package io.allurx.blur;

import io.allurx.annotation.parser.AnnotationParser;
import io.allurx.annotation.parser.type.Cascade;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;

import java.util.Optional;

/**
 * This class provides functionality to blur sensitive information by masking the fields of objects,
 * ensuring the privacy and anonymity of the data.
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
     * @param input The object to be blurred
     * @return A new object with its fields blurred
     */
    public static <T> T blur(T input) {
        return blur(input, new AnnotatedTypeToken<@Cascade T>() {
        });
    }

    /**
     * Blurs the input based on its {@link AnnotatedTypeToken}.
     *
     * @param input     The object to be blurred
     * @param typeToken The {@link AnnotatedTypeToken} representing the type of the input
     * @param <T>       The type of the input
     * @return A new object with its fields blurred
     */
    public static <T> T blur(T input, AnnotatedTypeToken<T> typeToken) {
        return Optional.ofNullable(input)
                .map(t -> typeToken)
                .map(AnnotatedTypeToken::getAnnotatedType)
                .map(annotatedType -> AnnotationParser.parse(input, annotatedType))
                .orElse(input);
    }

}

