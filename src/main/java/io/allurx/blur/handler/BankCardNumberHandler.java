/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.handler;

import io.allurx.blur.annotation.BankCardNumber;

/**
 * Handler for processing bank card numbers.
 * This class applies the defined blurring rules to sensitive bank card information.
 *
 * @author allurx
 */
public class BankCardNumberHandler extends AbstractCharSequenceHandler<String, BankCardNumber> {

    /**
     * Default constructor
     */
    public BankCardNumberHandler() {
    }

    /**
     * Handles the bank card number by applying the blurring rules defined in the annotation.
     *
     * @param input      The original bank card number string
     * @param annotation The {@link BankCardNumber} annotation containing blurring configurations
     * @return The blurred bank card number if required, otherwise returns the original number
     */
    @Override
    public String handle(String input, BankCardNumber annotation) {
        return required(input, annotation.condition())
                ? String.valueOf(blur(input, annotation.regexp(), annotation.startOffset(), annotation.endOffset(), annotation.placeholder()))
                : input;
    }

}
