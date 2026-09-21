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
 * Base class for masking annotated {@link CharSequence}s.
 * Offsets count Unicode code points, not grapheme clusters; output preserves UTF-16 length.
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
     * Masks nonempty whole regex matches (group 0), ignoring offsets; an empty regex
     * instead retains {@code start} leading and {@code end} trailing code points.
     * Matching uses the original input and skips empty matches. Mask boundaries expand
     * to cover surrogate pairs. With no matches, content is unchanged. The input is not modified.
     *
     * @param input       The original character sequence object
     * @param regexp      The regular expression for matching
     * @param start       The number of leading Unicode code points to preserve
     * @param end         The number of trailing Unicode code points to preserve
     * @param placeholder The character to replace sensitive information
     * @return A new char array containing the blurred character sequence
     * @throws IllegalArgumentException if {@code regexp} is empty and an offset is negative
     *                                  or the offsets together exceed the input code point count
     * @throws java.util.regex.PatternSyntaxException if {@code regexp} is not a valid regular expression
     */
    public final char[] blur(T input, String regexp, int start, int end, char placeholder) {
        return !regexp.isEmpty() ? blur(input, regexp, placeholder) : blur(input, start, end, placeholder);
    }

    private char[] blur(T input, String regexp, char placeholder) {
        char[] chars = chars(input);
        Matcher matcher = PATTERN_CACHE.computeIfAbsent(regexp, s -> Pattern.compile(regexp)).matcher(input);
        while (matcher.find()) {
            if (matcher.start() != matcher.end()) {
                replace(input, chars, matcher.start(), matcher.end(), placeholder);
            }
        }
        return chars;
    }

    private char[] blur(T input, int start, int end, char placeholder) {
        check(start, end, input);
        int from = Character.offsetByCodePoints(input, 0, start);
        int to = Character.offsetByCodePoints(input, input.length(), -end);
        char[] chars = chars(input);
        replace(input, chars, from, to, placeholder);
        return chars;
    }

    private char[] chars(T input) {
        char[] chars = new char[input.length()];
        IntStream.range(0, input.length()).forEach(i -> chars[i] = input.charAt(i));
        return chars;
    }

    /**
     * Expands boundaries using the original input so earlier replacements cannot hide surrogate pairs.
     */
    private void replace(T input, char[] chars, int start, int end, char placeholder) {
        if (start == end) return;
        if (start > 0 && Character.isSurrogatePair(input.charAt(start - 1), input.charAt(start))) {
            start--;
        }
        if (end < input.length() && Character.isSurrogatePair(input.charAt(end - 1), input.charAt(end))) {
            end++;
        }
        while (start < end) {
            chars[start++] = placeholder;
        }
    }

    private void check(int startOffset, int endOffset, T input) {
        int codePointCount = Character.codePointCount(input, 0, input.length());
        if (startOffset < 0 ||
                endOffset < 0 ||
                startOffset > codePointCount ||
                endOffset > codePointCount - startOffset) {
            throw new IllegalArgumentException("startOffset: %s, endOffset: %s, inputCodePointCount: %s"
                    .formatted(startOffset, endOffset, codePointCount));
        }
    }

}
