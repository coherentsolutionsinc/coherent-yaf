package com.coherentsolutions.yaf.junit;

import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

//@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest
public class MyJunitTest2 {

    @Autowired
    YafBeanUtils ss;

    @SneakyThrows
    @Test
    public void testSum() {
        String variant = Current.getEnvName();
        TimeUnit.SECONDS.sleep(new Random().nextInt(3));
        System.out.println(Thread.currentThread().getId() + "  → testSum running with v22222riant: " + variant + " utils " + ss.containsBean("a"));

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

