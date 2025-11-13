/*
 * MIT License
 *
 * Copyright (c) 2021 - 2025 Coherent Solutions Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.coherentsolutions.yaf.junit;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;
import java.util.concurrent.TimeUnit;

//@ExtendWith(SpringExtension.class)
//@Execution(ExecutionMode.SAME_THREAD)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest
public class MyJunitTest extends YafBaseJunitTest {

    public int sharedInt;

    public MyJunitTest() {
        System.out.println(11111111);
    }

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Before All");
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("Before Each");
    }

    @SneakyThrows
    @Test
    public void testSum() {
        sharedInt = 123;
        String variant = Current.getEnvName();
        System.out.println(variant + " sum " + sharedInt);
        TimeUnit.SECONDS.sleep(new Random().nextInt(3));
        System.out.println(Thread.currentThread().getId() + "  → testSum running with variant: " + variant);
        // Your test logic that uses the variant
        assert variant != null : "Variant should not be null";
        assert variant.equals("A") || variant.equals("B") : "Variant should be A or B";
    }


    @ParameterizedTest
    @ValueSource(strings = {"arg1", "arg2"})
    void testParametrized(String input) {
        System.out.println(Current.getEnvName() + " param " + sharedInt);
        System.out.println(Thread.currentThread().getId() + " -- " + Current.getEnvName() + " running with argument: " + input);
        ;
    }


    @Test
    public void testMultiply() {
        System.out.println(Current.getEnvName() + " mumlty " + sharedInt);
        String variant = Current.getEnvName();
        System.out.println(Thread.currentThread().getId() + " -- " + Current.getEnvName() + " MULTIPLY running with argument: " + variant);
//        Allure.step("Get current variant", () -> {
//            System.out.println("  → testMultiply running with variant: " + variant);
//            Allure.parameter("variant", variant);
//        });
//
//        Allure.step("Execute test logic", () -> {
//            assert variant != null;
//            System.out.println("  → Variant " + variant + " test passed");
//        });
//
//        Allure.addAttachment("Test Result", "Test passed for variant: " + variant);
    }
}

