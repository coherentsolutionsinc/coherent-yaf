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
import org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor;
import org.junit.jupiter.engine.execution.JupiterEngineExecutionContext;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TMTD extends TestMethodTestDescriptor {

    String variant;

    public TMTD(TestMethodTestDescriptor descriptor, JupiterConfiguration configuration, String variant) {
        this(descriptor.getUniqueId().append("variant", variant), descriptor.getTestClass(), descriptor.getTestMethod(), configuration, variant);
    }

    TMTD(UniqueId uniqueId, Class<?> testClass, Method testMethod, JupiterConfiguration configuration, String variant) {
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
    public String getLegacyReportingName() {
        return "NAME" + variant;
    }

    @Override
    public JupiterEngineExecutionContext execute(JupiterEngineExecutionContext context, DynamicTestExecutor dynamicTestExecutor) {
        System.out.println("Ex111ecuting " + getDisplayName());
        Current.set(variant);
        try {
            JupiterEngineExecutionContext resp = super.execute(context, dynamicTestExecutor);
            return resp;
        } finally {
            Current.clear();
        }
    }
}
