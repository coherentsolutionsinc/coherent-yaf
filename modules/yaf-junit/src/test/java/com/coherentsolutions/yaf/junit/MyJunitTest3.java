package com.coherentsolutions.yaf.junit;

import lombok.SneakyThrows;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest
public class MyJunitTest3 {// extends VariantSpringBase{

    @SneakyThrows
    @org.junit.jupiter.api.Test
    void computes() {
        String v = Variants.CURRENT.get(); // "a" then "b"
        // assertions per variant
        TimeUnit.SECONDS.sleep(new Random().nextInt(3));
        System.out.println(Thread.currentThread().getId() + " GET " + v);
    }

    @SneakyThrows
    @org.junit.jupiter.api.Test
    void computes2() {
        String v = Variants.CURRENT.get(); // "a" then "b"
        // assertions per variant
        TimeUnit.SECONDS.sleep(new Random().nextInt(3));
        System.out.println(Thread.currentThread().getId() + " GET2 " + v);
    }

}
