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

package com.coherentsolutions.yaf.core.api;


import com.coherentsolutions.yaf.core.api.auth.YafApiUser;
import com.coherentsolutions.yaf.core.api.properties.ApiProperties;

import java.util.Map;

/**
 * The interface Yaf request.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public interface YafRequest<T, R> {

    /**
     * The constant GET.
     */
    String GET = "GET";
    /**
     * The constant POST.
     */
    String POST = "POST";
    /**
     * The constant PUT.
     */
    String PUT = "PUT";
    /**
     * The constant DELETE.
     */
    String DELETE = "DELETE";

    /**
     * The constant PATCH.
     */
    String PATCH = "PATCH";

    /**
     * Anonymous req t.
     *
     * @return the t
     */
    T anonymousReq();

    /**
     * Req t.
     *
     * @return the t
     */
    T req();

    /**
     * Req t.
     *
     * @param user the user
     * @return the t
     */
    T req(YafApiUser user);

    /**
     * Anonymous req r.
     *
     * @param method the method
     * @param url    the url
     * @param body   the body
     * @return the r
     */
    R anonymousReq(String method, String url, Object... body);

    /**
     * Req r.
     *
     * @param method the method
     * @param url    the url
     * @param body   the body
     * @return the r
     */
    R req(String method, String url, Object... body);

    /**
     * Req r.
     *
     * @param user   the user
     * @param method the method
     * @param url    the url
     * @param body   the body
     * @return the r
     */
    R req(YafApiUser user, String method, String url, Object... body);

    /**
     * Add header yaf request.
     *
     * @param key   the key
     * @param value the value
     * @return the yaf request
     */
    YafRequest addHeader(String key, String value);

    /**
     * Query param yaf request.
     *
     * @param key   the key
     * @param value the value
     * @return the yaf request
     */
    YafRequest queryParam(String key, String value);

    /**
     * Query params yaf request.
     *
     * @param params the params
     * @return the yaf request
     */
    YafRequest queryParams(Map<String, String> params);


    /**
     * Gets props.
     *
     * @return the props
     */
    ApiProperties getProps();

    /**
     * Get r.
     *
     * @param url the url
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R get(String url) throws YafApiRequestException {
        return req(GET, url);
    }

    /**
     * Post r.
     *
     * @param url  the url
     * @param body the body
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R post(String url, Object... body) throws YafApiRequestException {
        return req(POST, url, body);
    }

    /**
     * Patch r.
     *
     * @param url  the url
     * @param body the body
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R patch(String url, Object... body) throws YafApiRequestException {
        return req(PATCH, url, body);
    }

    /**
     * Put r.
     *
     * @param url  the url
     * @param body the body
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R put(String url, Object... body) throws YafApiRequestException {
        return req(PUT, url, body);
    }

    /**
     * Delete r.
     *
     * @param url the url
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R delete(String url) throws YafApiRequestException {
        return req(DELETE, url);
    }

    /**
     * Delete r.
     *
     * @param url  the url
     * @param body the body
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R delete(String url, Object... body) throws YafApiRequestException {
        return req(DELETE, url, body);
    }

    /**
     * Get r.
     *
     * @param user the user
     * @param url  the url
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R get(YafApiUser user, String url) throws YafApiRequestException {
        return req(user, GET, url);
    }

    /**
     * Post r.
     *
     * @param user the user
     * @param url  the url
     * @param body the body
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R post(YafApiUser user, String url, Object... body) throws YafApiRequestException {
        return req(user, POST, url, body);
    }

    /**
     * Patch r.
     *
     * @param user the user
     * @param url  the url
     * @param body the body
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R patch(YafApiUser user, String url, Object... body) throws YafApiRequestException {
        return req(user, PATCH, url, body);
    }

    /**
     * Put r.
     *
     * @param user the user
     * @param url  the url
     * @param body the body
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R put(YafApiUser user, String url, Object... body) throws YafApiRequestException {
        return req(user, PUT, url, body);
    }

    /**
     * Delete r.
     *
     * @param user the user
     * @param url  the url
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R delete(YafApiUser user, String url) throws YafApiRequestException {
        return req(user, DELETE, url);
    }

    /**
     * A get r.
     *
     * @param url the url
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R aGet(String url) throws YafApiRequestException {
        return anonymousReq(GET, url);
    }

    /**
     * A post r.
     *
     * @param url  the url
     * @param body the body
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R aPost(String url, Object... body) throws YafApiRequestException {
        return anonymousReq(POST, url, body);
    }

    /**
     * A patch r.
     *
     * @param url  the url
     * @param body the body
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R aPatch(String url, Object... body) throws YafApiRequestException {
        return anonymousReq(PATCH, url, body);
    }

    /**
     * A put r.
     *
     * @param url  the url
     * @param body the body
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R aPut(String url, Object... body) throws YafApiRequestException {
        return anonymousReq(PUT, url, body);
    }

    /**
     * A delete r.
     *
     * @param url the url
     * @return the r
     * @throws YafApiRequestException the yaf api request exception
     */
    default R aDelete(String url) throws YafApiRequestException {
        return anonymousReq(DELETE, url);
    }

}
