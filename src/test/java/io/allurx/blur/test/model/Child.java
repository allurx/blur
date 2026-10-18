/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */

package io.allurx.blur.test.model;

import io.allurx.annotation.parser.type.Cascade;
import io.allurx.blur.annotation.BankCardNumber;
import io.allurx.blur.annotation.Email;
import io.allurx.blur.annotation.IdCardNumber;
import io.allurx.blur.annotation.Name;
import io.allurx.blur.annotation.Password;
import io.allurx.blur.annotation.PhoneNumber;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author allurx
 */
public class Child<T extends List<@Email String>> {

    @Name(placeholder = '#')
    public String name = "allurx";

    @PhoneNumber
    public String phoneNumber = "19962000001";

    @IdCardNumber
    public String idCardNumber = "321181199301096000";

    @Password
    public String password = "123456789";

    @BankCardNumber
    public String bankCardNumber = "6222600260001072440";

    public List<@Cascade(inherited = true) Parent> parents = Stream.of(new Father(), new Mother()).collect(Collectors.toList());

    @SuppressWarnings("unchecked")
    public T emails = (T) Stream.of("111111@qq.com", "222222@qq.com", "333333@qq.com").collect(Collectors.toList());

}
