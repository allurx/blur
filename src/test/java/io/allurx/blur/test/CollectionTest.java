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
