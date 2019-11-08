/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package red.zyc.desensitization;

import red.zyc.desensitization.annotation.EraseSensitive;
import red.zyc.desensitization.metadata.resolver.Resolvers;
import red.zyc.desensitization.metadata.resolver.TypeToken;

import java.util.Optional;

/**
 * @author zyc
 */
public final class Sensitive {

    /**
     * 对象内部域值脱敏，该方法不会改变原对象内部的域值。
     *
     * @param <T>    目标对象类型
     * @param target 目标对象
     * @return 脱敏后的新对象
     */
    public static <T> T desensitize(T target) {
        return desensitize(target, new TypeToken<@EraseSensitive T>() {
        });
    }

    /**
     * 单个值脱敏，该方法不会改变原对象值。
     *
     * @param target    目标对象
     * @param typeToken {@link TypeToken}
     * @param <T>       目标对象类型
     * @return 脱敏后的新对象
     */
    public static <T> T desensitize(T target, TypeToken<T> typeToken) {
        try {
            return Optional.ofNullable(target)
                    .map(t -> typeToken)
                    .map(TypeToken::getAnnotatedType)
                    .map(annotatedType -> Resolvers.resolve(target, annotatedType))
                    .orElse(target);
        } finally {
            Resolvers.clean();
        }
    }
}