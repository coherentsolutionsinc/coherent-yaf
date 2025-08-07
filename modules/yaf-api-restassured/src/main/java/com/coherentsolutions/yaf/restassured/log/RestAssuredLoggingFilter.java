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

package com.coherentsolutions.yaf.restassured.log;

import com.coherentsolutions.yaf.core.api.YafApiMetric;
import com.coherentsolutions.yaf.core.api.processor.log.ApiCallLog;
import com.coherentsolutions.yaf.core.api.processor.log.ApiLogger;
import com.coherentsolutions.yaf.core.events.EventsService;
import com.coherentsolutions.yaf.core.events.test.ApiCallStartEvent;
import com.coherentsolutions.yaf.core.log.properties.ApiLogProperties;
import com.coherentsolutions.yaf.core.log.properties.TestFinishProperties;
import com.coherentsolutions.yaf.core.metrics.YafMetricsService;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static java.util.logging.Level.OFF;

/**
 * The type Rest assured logging filter.
 */
@Data
@Accessors(chain = true)
@Component
@Slf4j
public class RestAssuredLoggingFilter implements Filter, ApiLogger {

    /**
     * The Properties.
     */
    @Autowired
    TestFinishProperties properties;

    /**
     * The Events service.
     */
    /*
     * Event Listener
     */
    @Autowired
    protected EventsService eventsService;

    @Autowired(required = false)
    protected YafMetricsService metricsService;

    /**
     * The Logs.
     */
    ThreadLocal<List<ApiCallLog>> logs = new ThreadLocal<>();

    /**
     * Build api call log api call log.
     *
     * @param requestSpec the request spec
     * @param response    the response
     * @return the api call log
     */
    public ApiCallLog buildApiCallLog(FilterableRequestSpecification requestSpec, Response response) {
        log.debug("Making {} req to {}", requestSpec.getMethod(), requestSpec.getURI());
        ApiLogProperties p = properties.getApiLog();
        Level level = p.getLevel();
        if (!OFF.equals(level)) {
            int statusCode = response.getStatusCode();
            if (!(p.isLogOnlyFails() && statusCode >= 200 && statusCode < 300)) {
                ApiCallLog apiCallLog = new ApiCallLog();
                apiCallLog.setMethod(requestSpec.getMethod()).setUrl(requestSpec.getURI())
                        .setStatusCode(response.getStatusCode());
                if (level.equals(Level.ALL)) {
                    apiCallLog.setReqHeaders(getHeaders(requestSpec.getHeaders()))
                            .setRespHeaders(getHeaders(response.getHeaders()));
                    apiCallLog.setReqBody(requestSpec.getBody()).setRespBody(response.getBody().prettyPrint());
                }
                log.debug(apiCallLog.toString());
                eventsService.sendEvent(buildApiCallEvent(apiCallLog));
                return apiCallLog;
            }
        }
        metricsService.addMetric(new YafApiMetric(requestSpec.getMethod(), requestSpec.getURI(), response.statusCode(), response.time()));
        return null;
    }

    /**
     * Build api call event api call start event.
     *
     * @param apiCallLog the api call log
     * @return the api call start event
     */
    public ApiCallStartEvent buildApiCallEvent(ApiCallLog apiCallLog) {
        return new ApiCallStartEvent().setApiCallLog(apiCallLog);
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        ApiCallLog log = buildApiCallLog(requestSpec, response);
        if (log != null) {
            addApiLog(log);
        }
        return response;
    }

    /**
     * Add api log.
     *
     * @param apiCallLog the api call log
     */
    protected void addApiLog(ApiCallLog apiCallLog) {
        List<ApiCallLog> apiCallLogs = logs.get();
        if (apiCallLogs == null) {
            apiCallLogs = new ArrayList<>();
        }
        apiCallLogs.add(apiCallLog);
        logs.set(apiCallLogs);
    }

    @Override
    public List<ApiCallLog> getThreadLogs() {
        List<ApiCallLog> result = logs.get();
        logs.remove();
        if (result == null) {
            result = Collections.emptyList();
        }
        return result;
    }

    @Override
    public void clearLogs() {
        logs.remove();
    }

    /**
     * Gets headers.
     *
     * @param headers the headers
     * @return the headers
     */
    protected Map<String, String> getHeaders(Headers headers) {
        return headers.asList().stream().collect(Collectors.toMap(h -> h.getName(), h -> h.getValue(), (x1, x2) -> x1));
    }
}
