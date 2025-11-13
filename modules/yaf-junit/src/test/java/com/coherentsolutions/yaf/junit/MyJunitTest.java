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

import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

//@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest
public class MyJunitTest {

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
        String variant = Current.getEnvName();
        TimeUnit.SECONDS.sleep(new Random().nextInt(3));
        System.out.println(Thread.currentThread().getId() + "  → testSum running with variant: " + variant);
        // Your test logic that uses the variant
        assert variant != null : "Variant should not be null";
        assert variant.equals("A") || variant.equals("B") : "Variant should be A or B";
    }


    @Test
    public void testMultiply() {
        String variant = Current.getEnvName();

        Allure.step("Get current variant", () -> {
            System.out.println("  → testMultiply running with variant: " + variant);
            Allure.parameter("variant", variant);
        });

        Allure.step("Execute test logic", () -> {
            assert variant != null;
            System.out.println("  → Variant " + variant + " test passed");
        });

        Allure.addAttachment("Test Result", "Test passed for variant: " + variant);
    }
}

