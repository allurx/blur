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

package io.allurx.blur.handler;

import io.allurx.annotation.parser.handler.AnnotationHandler;
import io.allurx.annotation.parser.util.InstanceCreators;
import io.allurx.blur.annotation.Condition;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Base class for handling sensitive {@link CharSequence} annotations.
 * Provides useful methods for blurring sensitive data.
 *
 * @param <A> The type of the sensitive annotation
 * @param <T> The type of the object to be blurred
 * @author allurx
 */
public abstract class AbstractCharSequenceHandler<T extends CharSequence, A extends Annotation> implements AnnotationHandler<T, A, T> {

    /**
     * Default constructor
     */
    public AbstractCharSequenceHandler() {
    }

    /**
     * Cache for regular expressions.
     */
    private static final ConcurrentMap<String, Pattern> PATTERN_CACHE = new ConcurrentHashMap<>();

    /**
     * Determines if blurring is required based on the given condition.
     *
     * @param input          The original character sequence object
     * @param conditionClass The {@link Class} of the condition
     * @return {@code true} if blurring is required; {@code false} otherwise
     */
    public boolean required(T input, Class<? extends Condition<?>> conditionClass) {
        @SuppressWarnings("unchecked")
        Class<? extends Condition<T>> clazz = (Class<? extends Condition<T>>) conditionClass;
        return InstanceCreators.find(clazz).create().required(input);
    }

    /**
     * Blurs the input based on the provided regular expression or offsets.
     *
     * @param input       The original character sequence object
     * @param regexp      The regular expression for matching
     * @param start       The starting offset of sensitive information
     * @param end         The ending offset of sensitive information
     * @param placeholder The character to replace sensitive information
     * @return A char array representing the blurred character sequence
     */
    public final char[] blur(T input, String regexp, int start, int end, char placeholder) {
        return !regexp.isEmpty() ? blur(input, regexp, placeholder) : blur(input, start, end, placeholder);
    }

    /**
     * Blurs the input based on the provided regular expression.
     *
     * @param input       The original character sequence object
     * @param regexp      The regular expression for matching
     * @param placeholder The character to replace sensitive information
     * @return A char array representing the blurred character sequence
     */
    private char[] blur(T input, String regexp, char placeholder) {
        char[] chars = chars(input);
        Matcher matcher = PATTERN_CACHE.computeIfAbsent(regexp, s -> Pattern.compile(regexp)).matcher(input);
        // Replace each character in the matched groups with the placeholder
        while (matcher.find()) {
            // Skip empty strings
            if (!matcher.group().isEmpty()) {
                // Replace each character in the matched group with the placeholder
                replace(chars, matcher.start(), matcher.end(), placeholder);
            }
        }
        return chars;
    }

    /**
     * Blurs the input based on specified start and end offsets.
     *
     * @param input       The original character sequence object
     * @param start       The starting offset of sensitive information
     * @param end         The ending offset of sensitive information
     * @param placeholder The character to replace sensitive information
     * @return A char array representing the blurred character sequence
     */
    private char[] blur(T input, int start, int end, char placeholder) {
        check(start, end, input);
        char[] chars = chars(input);
        replace(chars, start, input.length() - end, placeholder);
        return chars;
    }

    /**
     * Converts the character sequence to a char array.
     *
     * @param input The original character sequence object
     * @return A char array representing the characters in the sequence
     */
    private char[] chars(T input) {
        char[] chars = new char[input.length()];
        IntStream.range(0, input.length()).forEach(i -> chars[i] = input.charAt(i));
        return chars;
    }

    /**
     * Replaces sensitive information in the char array with a placeholder.
     *
     * @param chars       The char array corresponding to the character sequence
     * @param start       The starting index of sensitive information
     * @param end         The ending index of sensitive information
     * @param placeholder The character used to replace sensitive characters
     */
    private void replace(char[] chars, int start, int end, char placeholder) {
        while (start < end) {
            chars[start++] = placeholder;
        }
    }

    /**
     * Validates the legality of the start and end offsets.
     *
     * @param startOffset The starting offset of sensitive information
     * @param endOffset   The ending offset of sensitive information
     * @param input       The original character sequence
     * @throws IllegalArgumentException if offsets are invalid
     */
    private void check(int startOffset, int endOffset, T input) {
        if (startOffset < 0 ||
                endOffset < 0 ||
                startOffset + endOffset > input.length()) {
            throw new IllegalArgumentException(String.format("startOffset: %s, endOffset: %s, input: %s", startOffset, endOffset, input));
        }
    }

}
