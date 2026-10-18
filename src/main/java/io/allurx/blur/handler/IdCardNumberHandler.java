/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.handler;

import io.allurx.blur.annotation.IdCardNumber;

/**
 * Handler for processing identification card numbers.
 * This class applies the defined blurring rules to sensitive ID card information.
 *
 * @author allurx
 */
public class IdCardNumberHandler extends AbstractCharSequenceHandler<String, IdCardNumber> {

    /**
     * Default constructor
     */
    public IdCardNumberHandler() {
    }

    /**
     * Handles the ID card number by applying the blurring rules defined in the annotation.
     *
     * @param input      The original ID card number string
     * @param annotation The {@link IdCardNumber} annotation containing blurring configurations
     * @return The blurred ID card number if required; otherwise, returns the original ID card number
     */
    @Override
    public String handle(String input, IdCardNumber annotation) {
        return required(input, annotation.condition())
                ? String.valueOf(blur(input, annotation.regexp(), annotation.startOffset(), annotation.endOffset(), annotation.placeholder()))
                : input;
    }

}
