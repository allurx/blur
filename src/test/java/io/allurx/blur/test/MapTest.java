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

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class tests the blurring functionality of the {@link Map} class.
 * <p>
 * The blurring process masks sensitive data in a map of strings.
 *
 * @author allurx
 */
class MapTest {

    @Test
    void blur() {

        // Create a map with integer keys and sensitive string values
        var before = IntStream.range(0, 10)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), Object::toString));

        // Apply the blur method to the map
        var after = Blur.blur(before, new AnnotatedTypeToken<Map<Integer, @Strings String>>() {
        });

        // Verify that each string in the blurred map is masked
        after.forEach((k, v) -> Assertions.assertEquals("*", v));
    }
}
