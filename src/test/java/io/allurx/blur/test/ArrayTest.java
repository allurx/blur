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
