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

package com.coherentsolutions.yaf.exec.config.json.reader;


import com.coherentsolutions.yaf.core.exception.EnvSetupYafException;
import com.coherentsolutions.yaf.core.exec.model.Config;
import com.coherentsolutions.yaf.core.exec.model.Environment;
import com.coherentsolutions.yaf.core.exec.model.Execution;
import com.coherentsolutions.yaf.core.exec.model.ExecutionConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Base execution reader.
 */
@Slf4j
public abstract class BaseExecutionReader implements ExecutionReader {

    /**
     * Gets execution.
     *
     * @param configurationName the configuration name
     * @return the execution
     */
    protected abstract Execution getExecution(String configurationName);

    public ExecutionConfiguration getExecutionConfiguration(String configurationName) {
        Execution execution = getExecution(configurationName);

        ExecutionConfiguration configuration = new ExecutionConfiguration();
        configuration.setName(execution.getName());
        configuration.setSetup(execution.getSetup());
        // configuration.setApplications(execution.getApplications());

        // calculate execution plan
        Map<String, Environment> environments = new HashMap<>();
        if (execution.getConfigs() == null) {
            // we have no additional configurations, just envs
            environments = execution.getEnvs();
        } else {
            for (Config c : execution.getConfigs()) {
                for (Environment e : execution.getEnvs().values()) {
                    try {
                        Environment ee = e.clone();
                        ee.setConfigs(c);
                        environments.put(ee.getId(), ee);
                    } catch (CloneNotSupportedException cloneNotSupportedException) {
                        throw new EnvSetupYafException("Unable to clone environment!", cloneNotSupportedException);
                    }
                }
            }
        }

        configuration.setEnvironments(environments);

        return configuration;
    }

//    /**
//     * Gets config data.
//     *
//     * @param spService the sp service
//     * @return the config data
//     */
//    public abstract Map<String, Map<String, Object>> getConfigData(SpService spService);

}
