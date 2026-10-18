/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.test.model;

import io.allurx.blur.annotation.Password;

/**
 * @author allurx
 */
public abstract class Parent {

    @Password
    public String password = "123456";

}
