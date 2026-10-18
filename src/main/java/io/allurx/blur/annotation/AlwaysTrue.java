/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.blur.annotation;

/**
 * A condition that always returns true.
 * <p>
 * This class implements the {@link Condition} interface and is used when a condition
 * should always be considered met, regardless of the input object.
 *
 * @author allurx
 */
public class AlwaysTrue implements Condition<Object> {

    /**
     * Default constructor
     */
    public AlwaysTrue() {
    }

    @Override
    public boolean required(Object input) {
        return true;
    }

}
