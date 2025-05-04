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

import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.zephyr.scale.constant.JiraApiUrl;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import static io.restassured.RestAssured.given;

/**
 * Client for interacting with the Jira API.
 * <p>
 * This service handles authentication and requests to the Jira API. It is configured
 * to use basic authentication with the credentials provided in the {@link ZephyrScaleProperties}.
 * </p>
 * <p>
 * The client is conditionally enabled based on the property {@code yaf.zephyr.scale.jira.enabled}.
 * </p>
 */
@Slf4j
@Getter
@Service
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".zephyr.scale.jira.enabled", havingValue = "true")
public class JiraClient {

    private final ZephyrScaleProperties zephyrScaleProperties;
    private final RequestSpecification requestSpecification;

    /**
     * Constructs a new JiraClient with the specified properties.
     * <p>
     * Initializes the RestAssured request specification with basic authentication
     * using the Jira user and API key from the provided properties.
     * </p>
     *
     * @param zephyrScaleProperties the properties containing Jira configuration
     */
    @Autowired
    public JiraClient(ZephyrScaleProperties zephyrScaleProperties) {
        this.zephyrScaleProperties = zephyrScaleProperties;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        PreemptiveBasicAuthScheme basicAuth = new PreemptiveBasicAuthScheme();
        basicAuth.setUserName(zephyrScaleProperties.getJiraUser());
        basicAuth.setPassword(zephyrScaleProperties.getJiraApiKey());
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(zephyrScaleProperties.getJiraBaseUrl())
                .setAuth(basicAuth).setContentType(ContentType.JSON).build();
    }


    /**
     * Retrieves the Jira issue ID for the given issue key.
     * <p>
     * This method sends a GET request to the Jira API to fetch the issue details
     * and extracts the issue ID from the response.
     * </p>
     *
     * @param issueKey the key of the Jira issue
     * @return the ID of the Jira issue
     */
    public Integer getJiraIssueId(String issueKey) {
        try {
            return given(requestSpecification).get(JiraApiUrl.GET_ISSUE_BY_KEY, issueKey).jsonPath().getInt("id");
        } catch (Exception e) {
            log.error("Failed to get Jira issue ID for issue key: {}", issueKey, e);
            return null;
        }
    }
}
