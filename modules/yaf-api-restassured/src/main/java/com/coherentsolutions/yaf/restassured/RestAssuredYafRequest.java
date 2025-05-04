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
package com.coherentsolutions.yaf.restassured;

import com.coherentsolutions.yaf.core.api.YafApiRequestException;
import com.coherentsolutions.yaf.core.api.YafRequest;
import com.coherentsolutions.yaf.core.api.auth.YafApiUser;
import com.coherentsolutions.yaf.core.api.properties.ApiProperties;
import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.restassured.log.RestAssuredLoggingFilter;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static io.restassured.RestAssured.given;

/**
 * The type Rest assured yaf request.
 */
@Slf4j
public class RestAssuredYafRequest implements YafRequest<RequestSpecification, Response>, InitializingBean {
    /**
     * The Auth provider.
     */
    protected RestAssuredAuthProvider authProvider;
    /**
     * The Default user.
     */
    protected YafApiUser defaultUser;
    /**
     * The Take user from config.
     */
    protected boolean takeUserFromConfig;
    /**
     * The Headers.
     */
    protected Map<String, String> headers;
    /**
     * The Query params.
     */
    protected Map<String, String> queryParams;
    /**
     * The Multi part specs.
     */
    protected List<MultiPartSpecification> multiPartSpecs;
    /**
     * The Props.
     */
// Be aware, that this properties are set after constructor invocation!
    @Autowired
    @Getter
    protected ApiProperties props;
    /**
     * The Request specification.
     */
    @Setter
    protected RequestSpecification requestSpecification;
    /**
     * The Response specification.
     */
    @Getter
    @Setter
    protected ResponseSpecification responseSpecification;
    /**
     * The Request config.
     */
    protected RestAssuredConfig requestConfig;
    /**
     * The Logging filter.
     */
    @Autowired
    protected RestAssuredLoggingFilter loggingFilter;
    /**
     * The Bean utils.
     */
    @Autowired
    YafBeanUtils beanUtils;
    private Boolean baseUrlEndsWithSlash;
    /**
     * The Custom filters.
     */
    ThreadLocal<List<Filter>> customFilters;

    /**
     * Instantiates a new Rest assured yaf request.
     *
     * @param authProvider          the auth provider
     * @param defaultUser           the default user
     * @param takeUserFromConfig    the take user from config
     * @param headers               the headers
     * @param requestSpecification  the request specification
     * @param responseSpecification the response specification
     * @param requestConfig         the request config
     */
    @Builder(setterPrefix = "with")
    public RestAssuredYafRequest(RestAssuredAuthProvider authProvider, YafApiUser defaultUser,
                                 boolean takeUserFromConfig, Map<String, String> headers, RequestSpecification requestSpecification,
                                 ResponseSpecification responseSpecification, RestAssuredConfig requestConfig) {
        this.authProvider = authProvider;
        this.defaultUser = defaultUser;
        this.takeUserFromConfig = takeUserFromConfig;
        this.headers = headers;
        this.requestSpecification = requestSpecification;
        this.responseSpecification = responseSpecification;
        this.requestConfig = requestConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        processRequestSpecification();
    }

    /**
     * Process request specification.
     */
    protected void processRequestSpecification() {
        if (requestSpecification == null) {
            requestSpecification = given();
        }
        if (requestConfig != null) {
            requestSpecification.config(requestConfig);
        }
        if (props != null) {
            requestSpecification.contentType(props.getContentType()).accept(props.getAcceptType());
            if (props.getBaseUrl() != null) {
                requestSpecification.baseUri(props.getBaseUrl());
                baseUrlEndsWithSlash = props.getBaseUrl().endsWith("/");
            }
            if (!props.isValidateSsl()) {
                requestSpecification.relaxedHTTPSValidation();
            }
            if (props.getHeaders() != null) {
                requestSpecification.headers(props.getHeaders());
            }
            if (props.getQueryParams() != null) {
                requestSpecification.queryParams(props.getQueryParams());
            }
        }
        if (authProvider != null) {
            authProvider.setRequest(this);
        }
        requestSpecification.filter(loggingFilter);
    }

    /**
     * Gets request specification.
     *
     * @return the request specification
     */
    public RequestSpecification getRequestSpecification() {
        return given().spec(requestSpecification);
    }

    public RequestSpecification anonymousReq() {
        return getRequestSpecification();
    }

    private YafApiUser getApiUser() {
        YafApiUser result = null;
        try {
            if (takeUserFromConfig) {
                TestExecutionContext testExecutionContext = beanUtils.tec();
                result = (YafApiUser) testExecutionContext.getParam(Consts.CTX_USER);
            } else {
                result = defaultUser;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (result == null) {
            throw new YafApiRequestException("Unable to get default user. Please specify any in configuration!");
        }
        return result;
    }

    public RequestSpecification req() {
        return req(getApiUser());
    }

    public RequestSpecification req(YafApiUser user) {
        if (authProvider == null) {
            throw new YafApiRequestException(
                    "Unable to prepare a request, cause auth provider is null. If you want to make anonym request - use 'anonymousReq' method instead.");
        }
        return authProvider.auth(user, getRequestSpecification());
    }

    // todo may be add general methods and query params and others
    @Override
    public Response anonymousReq(String method, String url, Object... body) {
        return makeRequest(anonymousReq(), method, url, body);
    }

    @Override
    public Response req(String method, String url, Object... body) {
        return req(getApiUser(), method, url, body);
    }

    @Override
    public Response req(YafApiUser user, String method, String url, Object... body) {
        return makeRequest(req(user), method, url, body);
    }

    @Override
    public YafRequest<RequestSpecification, Response> addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    @Override
    public YafRequest<RequestSpecification, Response> queryParam(String key, String value) {
        if (queryParams == null) {
            queryParams = new LinkedHashMap<>();
        }
        queryParams.put(key, value);
        return this;
    }

    @Override
    public YafRequest<RequestSpecification, Response> queryParams(Map<String, String> params) {
        if (queryParams == null) {
            queryParams = new LinkedHashMap<>();
        }
        queryParams.putAll(params);
        return this;
    }

    /**
     * Multi part yaf request.
     *
     * @param multiPart the multi part
     * @return the yaf request
     */
    public YafRequest<RequestSpecification, Response> multiPart(MultiPartSpecification... multiPart) {
        if (multiPartSpecs == null) {
            multiPartSpecs = new ArrayList<>();
        }
        multiPartSpecs.addAll(Arrays.asList(multiPart));
        return this;
    }

    /**
     * Make request response.
     *
     * @param rs     the rs
     * @param method the method
     * @param url    the url
     * @param body   the body
     * @return the response
     */
    protected Response makeRequest(RequestSpecification rs, String method, String url, Object... body) {
        if (body.length > 0) {
            rs.body(body[0]);
        }
        if (baseUrlEndsWithSlash != null) {
            if (!url.startsWith("/") && !baseUrlEndsWithSlash) {
                url = "/" + url;
            }
        }
        if (headers != null) {
            rs.headers(headers);
        }
        if (queryParams != null) {
            rs.queryParams(queryParams);
        }
        if (multiPartSpecs != null) {
            handleMultiPart(rs);
        }
        try {
            ValidatableResponse then = rs.request(method, url).then();
            if (responseSpecification != null) {
                return then.spec(responseSpecification).extract().response();
            }
            return then.extract().response();
        } finally {
            queryParams = null;
            headers = null;
            multiPartSpecs = null;
            cleanCustomFilters();
        }
    }

    private void handleMultiPart(RequestSpecification rs) {
        for (MultiPartSpecification multiPartSpec : multiPartSpecs) {
            rs.multiPart(multiPartSpec);
        }
        rs.contentType(ContentType.MULTIPART);
    }

    /**
     * With custom filter rest assured yaf request.
     *
     * @param filter the filter
     * @return the rest assured yaf request
     */
    public RestAssuredYafRequest withCustomFilter(Filter... filter) {
        if (customFilters == null) {
            customFilters = ThreadLocal.withInitial(ArrayList::new);
        }
        for (Filter f : filter) {
            customFilters.get().add(f);
            this.requestSpecification.filter(f);
        }
        return this;
    }

    private void cleanCustomFilters() {
        if (customFilters != null && customFilters.get() != null) {
            for (Filter customFilter : customFilters.get()) {
                this.requestSpecification.noFiltersOfType(customFilter.getClass());
            }
            customFilters.get().clear();
            customFilters.remove();
            customFilters = null;
        }
    }
}