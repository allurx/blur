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
