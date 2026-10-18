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
