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

/**
 * This class tests the blurring functionality of the {@link java.lang.CharSequence} object.
 * <p>
 * The blurring process masks sensitive data in a string.
 *
 * @author allurx
 */
class StringTest {

    @Test
    void blur() {

        // Define a sensitive string
        var before = "123456";

        // Apply the blur method to the string
        var after = Blur.blur(before, new AnnotatedTypeToken<@Strings String>() {
        });

        // Verify that the blurred string is masked
        Assertions.assertEquals("******", after);
    }

}
