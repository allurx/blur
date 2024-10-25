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
import io.allurx.blur.annotation.Condition;
import io.allurx.blur.annotation.Strings;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class tests the custom conditions for applying the blurring functionality.
 * <p>
 * It verifies that blurring only occurs based on specified conditions.
 *
 * @author allurx
 */
class ConditionTest {

    @Test
    void blur() {
        // Create an array with various string values including null and empty
        var before = new String[]{"", null, "123456"};

        // Apply the blur method with a custom condition
        var after = Blur.blur(before, new AnnotatedTypeToken<@Strings(condition = StringCondition.class) String[]>() {
        });

        // Verify that the results are as expected based on the condition
        assertEquals("", after[0]);
        assertNull(after[1]);
        assertEquals("******", after[2]);
    }

    /**
     * This class defines the conditions under which blurring is applied.
     * <p>
     * Blurring occurs only for non-null and non-empty strings.
     */
    private static class StringCondition implements Condition<String> {

        @Override
        public boolean required(String input) {
            return input != null && !input.isEmpty();
        }
    }
}

