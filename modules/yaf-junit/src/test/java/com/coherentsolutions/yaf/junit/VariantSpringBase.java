package com.coherentsolutions.yaf.junit;

import org.junit.jupiter.api.TestTemplate;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class VariantSpringBase {

    // JUnit discovers THIS once; provider expands into N * (#@Test methods) nodes.
    @TestTemplate
    void __runVariantizedTests() {
        // body is never called; provider intercepts and runs @Test methods instead
    }
}