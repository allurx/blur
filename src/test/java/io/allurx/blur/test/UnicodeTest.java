/*
 * Copyright 2026 allurx
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
package io.allurx.blur.test;

import io.allurx.blur.Blur;
import io.allurx.blur.annotation.Name;
import io.allurx.blur.annotation.Strings;
import io.allurx.kit.base.reflection.AnnotatedTypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests blurring Unicode strings with offsets and regular expressions.
 *
 * @author allurx
 */
class UnicodeTest {

    @Test
    void name() {
        var before = new String[]{"张三", "𠮷田", "张𠮷", "𠮷野𠮷"};
        var after = Blur.blur(before, new AnnotatedTypeToken<@Name String[]>() {
        });

        Assertions.assertArrayEquals(new String[]{"张*", "𠮷*", "张**", "𠮷***"}, after);
    }

    @Test
    void offsets() {
        var before = new String[]{"𠮷野😀", "A𠮷B", "𠮷😀"};
        var after = Blur.blur(before, new AnnotatedTypeToken<@Strings(startOffset = 1, endOffset = 1) String[]>() {
        });

        Assertions.assertArrayEquals(new String[]{"𠮷*😀", "A**B", "𠮷😀"}, after);
    }

    @Test
    void multipleOffsets() {
        var before = "A𠮷中😀B";
        var after = Blur.blur(before, new AnnotatedTypeToken<@Strings(startOffset = 2, endOffset = 2) String>() {
        });

        Assertions.assertEquals("A𠮷*😀B", after);
    }

    @Test
    void maskAll() {
        var before = new String[]{"𠮷😀", ""};
        var after = Blur.blur(before, new AnnotatedTypeToken<@Strings String[]>() {
        });

        Assertions.assertArrayEquals(new String[]{"****", ""}, after);
    }

    @Test
    void invalidOffsets() {
        var before = "𠮷田";
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Blur.blur(before, new AnnotatedTypeToken<@Strings(startOffset = 3) String>() {
                }));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Blur.blur(before, new AnnotatedTypeToken<@Strings(endOffset = 3) String>() {
                }));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Blur.blur(before, new AnnotatedTypeToken<@Strings(startOffset = 1, endOffset = 2) String>() {
                }));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Blur.blur(before, new AnnotatedTypeToken<@Strings(startOffset = -1) String>() {
                }));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Blur.blur(before, new AnnotatedTypeToken<@Strings(endOffset = -1) String>() {
                }));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Blur.blur(before, new AnnotatedTypeToken<@Strings(startOffset = Integer.MAX_VALUE,
                        endOffset = Integer.MAX_VALUE) String>() {
                }));
    }

    @Test
    void regexpOverridesOffsets() {
        var before = "A𠮷B";
        var after = Blur.blur(before, new AnnotatedTypeToken<@Strings(regexp = "(𠮷)", placeholder = '#',
                startOffset = -1, endOffset = Integer.MAX_VALUE) String>() {
        });

        Assertions.assertEquals("A##B", after);
    }

    @Test
    void unmatchedAndEmptyRegexp() {
        var before = "𠮷田";
        var after = Blur.blur(before, new AnnotatedTypeToken<@Strings(regexp = "absent") String>() {
        });
        Assertions.assertEquals(before, after);

        after = Blur.blur(before, new AnnotatedTypeToken<@Strings(regexp = "(?=.)") String>() {
        });
        Assertions.assertEquals(before, after);
    }

    @Test
    void regexpBoundaries() {
        // A failed or empty match makes find() retry inside the surrogate pair.
        var before = "𠮷x";
        var after = Blur.blur(before, new AnnotatedTypeToken<@Strings(regexp = "(?<!\\A).") String>() {
        });
        Assertions.assertEquals("***", after);

        after = Blur.blur(before, new AnnotatedTypeToken<@Strings(regexp = "^|\\uDFB7") String>() {
        });
        Assertions.assertEquals("**x", after);

        // A back reference can end between the two halves of a later surrogate pair.
        before = "\uD842-\uD842\uDFB7";
        after = Blur.blur(before, new AnnotatedTypeToken<@Strings(regexp = "(\\uD842)-\\1") String>() {
        });
        Assertions.assertEquals("****", after);

        before = "a𠮷";
        after = Blur.blur(before, new AnnotatedTypeToken<@Strings(regexp = "a(?=𠮷)|(?<=a)𠮷") String>() {
        });
        Assertions.assertEquals("***", after);
    }
}
