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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class tests the blurring functionality of the {@link Collection} class.
 * <p>
 * The blurring process masks sensitive data in a collection of strings.
 *
 * @author allurx
 */
class CollectionTest {

    @Test
    void blur() {
        // Create a list containing sensitive strings
        var before = IntStream.range(0, 10)
                .mapToObj(value -> "123456")
                .collect(Collectors.toList());

        // Apply the blur method to the collection
        var after = Blur.blur(before, new AnnotatedTypeToken<List<@Strings String>>() {
        });

        // Verify that each string in the blurred collection is masked
        after.forEach(s -> Assertions.assertEquals("******", s));
    }
}
