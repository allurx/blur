/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
/**
 * blur test module
 *
 * @author allurx
 */
module io.allurx.blur.test {
    requires org.junit.jupiter;
    requires io.allurx.blur;
    opens io.allurx.blur.test;
    opens io.allurx.blur.test.model;
}
