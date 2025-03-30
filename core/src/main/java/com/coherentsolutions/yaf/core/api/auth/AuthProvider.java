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

package com.coherentsolutions.yaf.core.api.auth;


import com.coherentsolutions.yaf.core.api.YafApiRequestException;
import com.coherentsolutions.yaf.core.api.YafRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Auth provider.
 *
 * @param <T> the type parameter
 */
public abstract class AuthProvider<T> {

    /**
     * The constant AUTHORIZATION.
     */
    public static final String AUTHORIZATION = "Authorization";
    /**
     * The constant BEARER.
     */
    public static final String BEARER = "Bearer";
    /**
     * The constant BASIC.
     */
    public static final String BASIC = "Basic";
    /**
     * The Tokens.
     */
    @Getter
    protected Map<YafApiUser, AuthToken> tokens;
    /**
     * The Request.
     */
    @Setter
    protected YafRequest request;

    /**
     * Instantiates a new Auth provider.
     */
    public AuthProvider() {
        tokens = new ConcurrentHashMap<>();
    }

    /**
     * Auth t.
     *
     * @param user the user
     * @param req  the req
     * @return the t
     * @throws YafApiRequestException the yaf api request exception
     */
    public T auth(YafApiUser user, T req) throws YafApiRequestException {
        AuthToken token = tokens.get(user);
        if (token == null || token.isExpired()) {
            token = getAuthToken(user);
            if (token.getEx() != null) {
                throw new YafApiRequestException(token.getEx());
            }
            token.setUser(user);
            tokens.put(user, token);
        }
        applyAuth(token, req);
        return req;
    }

    /**
     * Gets auth token.
     *
     * @param user the user
     * @return the auth token
     * @throws YafApiRequestException the yaf api request exception
     */
    protected abstract AuthToken getAuthToken(YafApiUser user) throws YafApiRequestException;

    /**
     * Apply auth.
     *
     * @param token the token
     * @param req   the req
     */
    protected abstract void applyAuth(AuthToken token, T req);

}
