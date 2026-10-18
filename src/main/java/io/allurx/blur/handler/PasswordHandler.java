/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.handler;

import io.allurx.blur.annotation.Password;

/**
 * Handler for processing passwords.
 * This class applies the defined blurring rules to sensitive password information.
 *
 * @author allurx
 */
public class PasswordHandler extends AbstractCharSequenceHandler<String, Password> {

    /**
     * Default constructor
     */
    public PasswordHandler() {
    }

    /**
     * Handles the password by applying the blurring rules defined in the annotation.
     *
     * @param input      The original password string
     * @param annotation The {@link Password} annotation containing blurring configurations
     * @return The blurred password if required; otherwise, returns the original password
     */
    @Override
    public String handle(String input, Password annotation) {
        return required(input, annotation.condition())
                ? String.valueOf(blur(input, annotation.regexp(), annotation.startOffset(), annotation.endOffset(), annotation.placeholder()))
                : input;
    }

}
