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

package io.allurx.blur.annotation;

import io.allurx.annotation.parser.util.Singleton;

/**
 * Defines a condition to determine if the input requires blurring.
 *
 * @param <T> The type of the input
 * @author allurx
 */
@Singleton
public interface Condition<T> {

    /**
     * Checks if the input needs to be blurred.
     *
     * @param input The input to evaluate
     * @return {@code true} if blurring is required, {@code false} otherwise
     */
    boolean required(T input);

}
