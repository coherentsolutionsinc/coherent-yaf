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

package com.coherentsolutions.yaf.zephyr.scale;

import com.coherentsolutions.yaf.core.config.ModuleProperties;
import com.coherentsolutions.yaf.core.consts.Consts;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for Zephyr Scale integration.
 * <p>
 * This class extends {@link ModuleProperties} and provides additional properties
 * specific to Zephyr Scale, such as base URL, API key, project key, root folder,
 * cycle name, and cycle key.
 * </p>
 * <p>
 * These properties are loaded from the application's configuration file with the
 * prefix defined by {@link Consts#FRAMEWORK_NAME} + ".zephyr.scale".
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties(prefix = Consts.FRAMEWORK_NAME + ".zephyr.scale")
public class ZephyrScaleProperties extends ModuleProperties {

    /* Zephyr Scale properties */
    private String baseUrl = "https://api.zephyrscale.smartbear.com/v2/";
    private String apiKey;
    private String projectKey;
    private String folders;
    private String cycleName;
    private String cycleKey;
    private ZephyrScaleExecutionStrategy executionStrategy = ZephyrScaleExecutionStrategy.UPDATE;

    /* JIRA properties */
    private String jiraBaseUrl;
    private String jiraUser;
    private String jiraApiKey;
}
