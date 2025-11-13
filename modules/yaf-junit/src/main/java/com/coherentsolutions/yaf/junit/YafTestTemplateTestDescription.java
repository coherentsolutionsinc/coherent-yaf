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
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.TestTemplateTestDescriptor;
import org.junit.jupiter.engine.execution.JupiterEngineExecutionContext;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class YafTestTemplateTestDescription extends TestTemplateTestDescriptor {

    String variant;

    public YafTestTemplateTestDescription(TestTemplateTestDescriptor descriptor, JupiterConfiguration configuration, String variant) {
        this(descriptor.getUniqueId().append("variant", variant), descriptor.getTestClass(), descriptor.getTestMethod(), configuration, variant);
    }

    YafTestTemplateTestDescription(UniqueId uniqueId, Class<?> testClass, Method testMethod, JupiterConfiguration configuration, String variant) {
        super(uniqueId, testClass, testMethod, configuration);
        this.variant = variant;
    }

    @SneakyThrows
    public void modifyDisplayName() {
        String displayName = getDisplayName() + "[" + variant + "]";
        Field field = AbstractTestDescriptor.class.getDeclaredField("displayName");
        field.setAccessible(true);
        field.set(this, displayName);
    }

    @Override
    public JupiterEngineExecutionContext execute(JupiterEngineExecutionContext context, DynamicTestExecutor dynamicTestExecutor) throws Exception {
        return super.execute(context, new YafDynamicTestExecutor(dynamicTestExecutor, variant));
//        System.out.println(Thread.currentThread().getId() +" Ex2222ecuting " + getDisplayName());
//        Current.set(variant);
//        try {
//            JupiterEngineExecutionContext resp = null;
//            try {
//                resp = super.execute(context, new YafDynamicTestExecutor(dynamicTestExecutor, variant));
//            } catch (Exception e) {
//                //TODO revalidate
//                throw new RuntimeException(e);
//            }
//            return resp;
//        } finally {
//            Current.clear();
//        }
    }


    public static class YafDynamicTestExecutor implements DynamicTestExecutor {

        final DynamicTestExecutor dynamicTestExecutor;
        final String variant;

        public YafDynamicTestExecutor(DynamicTestExecutor dynamicTestExecutor, String variant) {
            this.dynamicTestExecutor = dynamicTestExecutor;
            this.variant = variant;
        }

        @Override
        public void execute(TestDescriptor testDescriptor) {
            try {
                Current.set(variant);
                dynamicTestExecutor.execute(testDescriptor);
            } finally {
                Current.clear();
            }
        }

        @Override
        public Future<?> execute(TestDescriptor testDescriptor, EngineExecutionListener executionListener) {
            return dynamicTestExecutor.execute(testDescriptor, executionListener);
        }

        @Override
        public void awaitFinished() throws InterruptedException {
            dynamicTestExecutor.awaitFinished();
        }
    }

}
