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

import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

//@ExtendWith(SpringExtension.class)
//@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest
public class MyJunitTest2 {

    @Autowired
    YafBeanUtils ss;

    @SneakyThrows
    @Test
    public void testSum48() {
        String variant = Current.getEnvName();
        TimeUnit.SECONDS.sleep(new Random().nextInt(3));
        System.out.println(Thread.currentThread().getId() + "  → XXXXXX testSum running with v22222riant: " + variant + " utils " + ss.containsBean("a"));

        // Your test logic that uses the variant
//        assert variant != null : "Variant should not be null";
//        assert variant.equals("A") || variant.equals("B") : "Variant should be A or B";
//
//        if ("A".equals(variant)) {
//            System.out.println("  → Testing variant A specific behavior");
//        } else if ("B".equals(variant)) {
//            System.out.println("  → Testing variant B specific behavior");
//        }
    }

//    @Test
//    public void testMultiply() {
//        String variant = Current.variant();
//
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
//    }
}

