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

package com.coherentsolutions.yaf.core.exec;

import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exec.model.Environment;
import com.coherentsolutions.yaf.core.exec.resolver.ConfigurationResolver;
import com.coherentsolutions.yaf.core.utils.ServiceProviderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Environment service.
 */
@Service
@Slf4j
public class EnvironmentService {

    /**
     * The Stub env.
     */
    @Autowired(required = false)
    Environment stubEnv;

    /**
     * The Configuration resolvers list.
     */
    @Autowired
    List<ConfigurationResolver> configurationResolversList;

    /**
     * Get proper test environment environment.
     *
     * @param envConfName          the env conf name
     * @param testExecutionContext the test execution context
     * @return the environment
     */
    public Environment getProperTestEnvironment(String envConfName, TestExecutionContext testExecutionContext) {
        Environment env;
        if (stubEnv == null) {
            ExecutionService executionService = ServiceProviderUtils.getExecutionService();
            env = executionService.getEnv(envConfName, testExecutionContext);
        } else {
            log.warn("Using stub env bean!");
            env = stubEnv;
        }
        // process env configurations
        if (env.getConfigs() != null) {
            env.getConfigs().entrySet().forEach(e -> {
                ConfigurationResolver configurationResolver = configurationResolversList.stream()
                        .filter(c -> c.getType().equals(e.getKey())).findFirst().orElse(null);
                if (configurationResolver != null) {
                    configurationResolver.applyConfiguration(e.getValue(), testExecutionContext);
                } else {
                    log.error("Unable to find resolver for type {} with value {}", e.getKey(), e.getValue());
                }
            });
        }
        return env;
    }

}
