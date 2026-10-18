/*
 * Copyright 2024 allurx
 * SPDX-License-Identifier: Apache-2.0
 */
package io.allurx.blur.test;

import io.allurx.blur.Blur;
import io.allurx.blur.test.model.Child;
import io.allurx.blur.test.model.Father;
import io.allurx.blur.test.model.Mother;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the blurring functionality of the {@link Field} object.
 * <p>
 * The blurring process masks sensitive data in the object's fields.
 *
 * @author allurx
 */
class ObjectTest {

    @Test
    void blur() {

        var before = new Child<>();
        var after = Blur.blur(before);

        // Validate the blurred fields of the Child object
        assertEquals("a#####", after.name);
        assertEquals("199****0001", after.phoneNumber);
        assertEquals("321181********6000", after.idCardNumber);
        assertEquals("*********", after.password);
        assertEquals("***************2440", after.bankCardNumber);
        assertEquals("1*****@qq.com", after.emails.get(0));
        assertEquals("2*****@qq.com", after.emails.get(1));
        assertEquals("3*****@qq.com", after.emails.get(2));

        // Validate the blurred fields of the Child's Father
        var father = (Father) after.parents.getFirst();
        assertEquals("f*****", father.name);
        assertEquals("199****0002", father.phoneNumber);
        assertEquals("4*****@qq.com", father.email);
        assertEquals("321181********6001", father.idCardNumber);
        assertEquals("***************2441", father.bankCardNumber);
        assertEquals("******", father.password);

        // Validate the blurred fields of the Child's Mother
        var mother = (Mother) after.parents.get(1);
        assertEquals("m*****", mother.name);
        assertEquals("199****0003", mother.phoneNumber);
        assertEquals("5*****@qq.com", mother.email);
        assertEquals("321181********6002", mother.idCardNumber);
        assertEquals("***************2442", mother.bankCardNumber);
        assertEquals("******", mother.password);
    }
}
