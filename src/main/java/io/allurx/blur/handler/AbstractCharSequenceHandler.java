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
import io.allurx.annotation.parser.util.Instances;
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
 * <p>
 * Masking replaces selected UTF-16 {@code char} units in a copy of the input,
 * preserving its UTF-16 length. The input is not modified.
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
        return Instances.create(clazz).required(input);
    }

    /**
     * Blurs the input based on the provided regular expression or offsets.
     * A non-empty regular expression takes precedence over both offsets and masks
     * each non-empty whole match (group 0), regardless of any capturing groups.
     * Matching uses the original input; no match leaves its content unchanged.
     * <p>
     * An empty regular expression masks the interval from {@code start}, inclusive,
     * to {@code input.length() - end}, exclusive. Both offsets count UTF-16
     * {@code char} units to preserve at the beginning and end of the input.
     *
     * @param input       The original character sequence object
     * @param regexp      The regular expression for matching
     * @param start       The number of leading UTF-16 {@code char} units to preserve
     * @param end         The number of trailing UTF-16 {@code char} units to preserve
     * @param placeholder The character to replace sensitive information
     * @return A new char array containing the blurred character sequence
     * @throws IllegalArgumentException if {@code regexp} is empty and an offset is negative
     *                                  or the offsets together exceed the input length
     * @throws java.util.regex.PatternSyntaxException if {@code regexp} is not a valid regular expression
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
        while (matcher.find()) {
            if (!matcher.group().isEmpty()) {
                replace(chars, matcher.start(), matcher.end(), placeholder);
            }
        }
        return chars;
    }

    /**
     * Blurs the input based on specified start and end offsets.
     *
     * @param input       The original character sequence object
     * @param start       The number of leading UTF-16 {@code char} units to preserve
     * @param end         The number of trailing UTF-16 {@code char} units to preserve
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
     * @param startOffset The number of leading UTF-16 {@code char} units to preserve
     * @param endOffset   The number of trailing UTF-16 {@code char} units to preserve
     * @param input       The original character sequence
     * @throws IllegalArgumentException if offsets are invalid
     */
    private void check(int startOffset, int endOffset, T input) {
        int length = input.length();
        if (startOffset < 0 ||
                endOffset < 0 ||
                startOffset > length ||
                endOffset > length - startOffset) {
            throw new IllegalArgumentException("startOffset: %s, endOffset: %s, inputLength: %s"
                    .formatted(startOffset, endOffset, length));
        }
    }

}
