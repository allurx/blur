/*
 * Copyright 2024 allurx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
