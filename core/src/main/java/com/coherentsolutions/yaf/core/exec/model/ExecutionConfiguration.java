/*
 * MIT License
 *
 * Copyright (c) 2021 - 2024 Coherent Solutions Inc.
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

package com.coherentsolutions.yaf.core.exec.model;


import com.coherentsolutions.yaf.core.consts.Consts;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Execution configuration.
 */
@Data
@Accessors(chain = true)
public class ExecutionConfiguration {

    /**
     * The constant DEFAULT_ENV.
     */
    public static String DEFAULT_ENV = "default_env";

    /**
     * The Name.
     */
    String name;
    /**
     * The Environments.
     */
    Map<String, Environment> environments;
    /**
     * The Setup.
     */
    RunParallelSetup setup;

    // Map<String, Application> applications;

    /**
     * Gets default.
     *
     * @return the default
     */
    public static ExecutionConfiguration getDefault() {
        ExecutionConfiguration configuration = new ExecutionConfiguration();
        configuration.setName(Consts.DEFAULT);
        Map<String, Environment> environments = new HashMap<>();
        environments.put(DEFAULT_ENV, Environment.getDefaultEnv());
        configuration.setEnvironments(environments);
        return configuration;
    }
}
