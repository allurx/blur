/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.blur.test;

import io.allurx.blur.Blur;
import io.allurx.blur.annotation.Strings;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * This class tests the blurring functionality of the {@link Array} class.
 * <p>
 * The blurring process masks sensitive data in an array of strings.
 *
 * @author allurx
 */
class ArrayTest {

    @Test
    void blur() {

        // Initialize an array with sensitive strings
        var before = new String[]{"123456", "123456", "123456"};

        // Apply the blur method to the array
        var after = Blur.blur(before, new AnnotatedTypeToken<@Strings String[]>() {
        });

        // Verify that each string in the blurred array is masked
        Arrays.stream(after).forEach(s -> Assertions.assertEquals("******", s));
    }
}
