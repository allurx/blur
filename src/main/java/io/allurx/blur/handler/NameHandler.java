/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.handler;

import io.allurx.blur.annotation.Name;

/**
 * Handler for processing names.
 * This class applies the defined blurring rules to sensitive name information.
 *
 * @author allurx
 */
public class NameHandler extends AbstractCharSequenceHandler<String, Name> {

    /**
     * Default constructor
     */
    public NameHandler() {
    }

    /**
     * Handles the name by applying the blurring rules defined in the annotation.
     *
     * @param input      The original name string
     * @param annotation The {@link Name} annotation containing blurring configurations
     * @return The blurred name if required; otherwise, returns the original name
     */
    @Override
    public String handle(String input, Name annotation) {
        return required(input, annotation.condition())
                ? String.valueOf(blur(input, annotation.regexp(), annotation.startOffset(), annotation.endOffset(), annotation.placeholder()))
                : input;
    }

}