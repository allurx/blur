/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.annotation;

import io.allurx.annotation.parser.util.Singleton;

/**
 * Defines a condition to determine if the input requires blurring.
 * Instances are shared across calls through {@link Singleton}. Implementations
 * must be stateless or ensure thread safety.
 *
 * @param <T> The type of the input
 * @author allurx
 */
@Singleton
public interface Condition<T> {

    /**
     * Checks if the input needs to be blurred.
     *
     * @param input The input to evaluate
     * @return {@code true} if blurring is required, {@code false} to return the input unchanged
     */
    boolean required(T input);

}
