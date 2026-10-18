/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.test.model;

import io.allurx.blur.annotation.BankCardNumber;
import io.allurx.blur.annotation.Email;
import io.allurx.blur.annotation.IdCardNumber;
import io.allurx.blur.annotation.Name;
import io.allurx.blur.annotation.PhoneNumber;

/**
 * @author allurx
 */
public class Father extends Parent {

    @Name
    public String name = "father";

    @PhoneNumber
    public String phoneNumber = "19962000002";

    @Email
    public String email = "444444@qq.com";

    @IdCardNumber
    public String idCardNumber = "321181198301096001";

    @BankCardNumber
    public String bankCardNumber = "6222600260001072441";

}
