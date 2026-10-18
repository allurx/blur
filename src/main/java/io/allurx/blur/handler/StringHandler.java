/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.handler;

import io.allurx.blur.annotation.Strings;

/**
 * Handler for processing {@link String} objects.
 * This class applies the defined blurring rules to sensitive string information.
 *
 * @author allurx
 */
public class StringHandler extends AbstractCharSequenceHandler<String, Strings> {

    /**
     * Default constructor
     */
    public StringHandler() {
    }

    /**
     * Handles the string by applying the blurring rules defined in the annotation.
     *
     * @param input      The original string
     * @param annotation The {@link Strings} annotation containing blurring configurations
     * @return The blurred string if required; otherwise, returns the original string
     */
    @Override
    public String handle(String input, Strings annotation) {
        return required(input, annotation.condition())
                ? String.valueOf(blur(input, annotation.regexp(), annotation.startOffset(), annotation.endOffset(), annotation.placeholder()))
                : input;
    }


}
