/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.handler;

import io.allurx.blur.annotation.Email;

/**
 * Handler for processing email addresses.
 * This class applies the defined blurring rules to sensitive email information.
 *
 * @author allurx
 */
public class EmailHandler extends AbstractCharSequenceHandler<String, Email> {

    /**
     * Default constructor
     */
    public EmailHandler() {
    }

    /**
     * Handles the email address by applying the blurring rules defined in the annotation.
     *
     * @param input      The original email address string
     * @param annotation The {@link Email} annotation containing blurring configurations
     * @return The blurred email address if required; otherwise, returns the original email
     */
    @Override
    public String handle(String input, Email annotation) {
        return required(input, annotation.condition())
                ? String.valueOf(blur(input, annotation.regexp(), annotation.startOffset(), annotation.endOffset(), annotation.placeholder()))
                : input;
    }

}
