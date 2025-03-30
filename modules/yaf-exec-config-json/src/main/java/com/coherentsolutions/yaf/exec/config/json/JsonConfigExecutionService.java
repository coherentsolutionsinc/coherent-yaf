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

package com.coherentsolutions.yaf.exec.config.json;


import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.EnvSetupYafException;
import com.coherentsolutions.yaf.core.exec.ExecutionService;
import com.coherentsolutions.yaf.core.exec.model.Environment;
import com.coherentsolutions.yaf.core.exec.model.ExecutionConfiguration;
import com.coherentsolutions.yaf.core.exec.model.RunParallelSetup;
import com.coherentsolutions.yaf.core.utils.PropertiesUtils;
import com.coherentsolutions.yaf.exec.config.json.reader.BaseExecutionReader;
import com.coherentsolutions.yaf.exec.config.json.reader.json.JsonFilesConfigurationReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import static com.coherentsolutions.yaf.core.consts.Consts.ENV_SETTINGS_FILE;
import static com.coherentsolutions.yaf.core.exec.model.ExecutionConfiguration.DEFAULT_ENV;

/**
 * The type Execution service.
 */
@Slf4j
public final class JsonConfigExecutionService implements ExecutionService {

    private static JsonConfigExecutionService instance;
    /**
     * The Configuration reader.
     */
    BaseExecutionReader configurationReader;
    /**
     * The Configuration.
     */
    @Getter
    ExecutionConfiguration configuration;

    /**
     * The Configuration data.
     */
//    @Getter
//    Map<String, Map<String, Object>> configurationData;
    private JsonConfigExecutionService() {
        String configurationName = PropertiesUtils.getPropertyValue(ENV_SETTINGS_FILE, null);
        configurationReader = new JsonFilesConfigurationReader();
        if (StringUtils.isEmpty(configurationName)) {
            // return default config
            configuration = ExecutionConfiguration.getDefault();
        } else {
            try {
                configuration = configurationReader.getExecutionConfiguration(configurationName);
                //configurationData = configurationReader.getConfigData(new SpService());
            } catch (Exception e) {
                throw new EnvSetupYafException("Configuration error. " + e.getMessage(), e);
            }
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static JsonConfigExecutionService getInstance() {
        if (instance == null) {
            synchronized (JsonConfigExecutionService.class) {
                instance = new JsonConfigExecutionService();
            }
        }
        return instance;
    }

    /**
     * Gets run parallel setup.
     *
     * @return the run parallel setup
     */
    public RunParallelSetup getRunParallelSetup() {
        return configuration.getSetup() == null ? new RunParallelSetup() : configuration.getSetup();
    }

    /**
     * Gets env.
     *
     * @param name                 the name
     * @param testExecutionContext the test execution context
     * @return the env
     */
    public Environment getEnv(String name, TestExecutionContext testExecutionContext) {
        Environment env = configuration.getEnvironments().get(name);
        if (env == null) {
            env = configuration.getEnvironments().get(DEFAULT_ENV);
        }
        return env;
    }

//    /**
//     * Gets config data value.
//     *
//     * @param configType the config type
//     * @param configKey  the config key
//     * @return the config data value
//     */
//    public Optional<Object> getConfigDataValue(String configType, String configKey) {
//        try {
//            return Optional.of(configurationData.get(configType).get(configKey));
//        } catch (Exception e) {
//            return Optional.empty();
//        }
//    }

}
