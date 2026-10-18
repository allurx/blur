/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.handler;

import io.allurx.blur.annotation.PhoneNumber;

/**
 * Handler for processing phone numbers.
 * This class applies the defined blurring rules to sensitive phone number information.
 *
 * @author allurx
 */
public class PhoneNumberHandler extends AbstractCharSequenceHandler<String, PhoneNumber> {

    /**
     * Default constructor
     */
    public PhoneNumberHandler() {
    }

    /**
     * Handles the phone number by applying the blurring rules defined in the annotation.
     *
     * @param input      The original phone number string
     * @param annotation The {@link PhoneNumber} annotation containing blurring configurations
     * @return The blurred phone number if required; otherwise, returns the original phone number
     */
    @Override
    public String handle(String input, PhoneNumber annotation) {
        return required(input, annotation.condition())
                ? String.valueOf(blur(input, annotation.regexp(), annotation.startOffset(), annotation.endOffset(), annotation.placeholder()))
                : input;
    }

}
