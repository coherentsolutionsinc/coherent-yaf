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

package com.coherentsolutions.yaf.web.pom.login;

/*-
 * #%L
 * Yaf Web Module
 * %%
 * Copyright (C) 2020 - 2021 CoherentSolutions
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.coherentsolutions.yaf.web.pom.WebPage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * The type Hack login page.
 *
 * @param <R> the type parameter
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Component
public abstract class HackLoginPage<R> extends WebPage {


    /**
     * The Login store service.
     */
    protected LoginStoreService loginStoreService;

    /**
     * The Url.
     */
    protected String url;

    /**
     * Instantiates a new Hack login page.
     *
     * @param loginStoreService the login store service
     */
    public HackLoginPage(LoginStoreService loginStoreService) {
        this.loginStoreService = loginStoreService;
    }

    /**
     * Login r.
     *
     * @param url      the url
     * @param userName the user name
     * @param pass     the pass
     * @return the r
     */
    public R login(String url, String userName, String pass) {
        this.url = url;
        Map<String, Object> authData = loginStoreService.getAuthDataForUser(userName);
        if (authData != null && isAuthDataValid(authData)) {
            return loginViaHack(authData, userName, pass);
        } else {
            LoginResp loginResp = loginViaUi(userName, pass);
            loginStoreService.setAuthDataFroUser(userName, loginResp.getAuthData());
            return loginResp.getResp();
        }
    }

    /**
     * Login via ui login resp.
     *
     * @param userName the user name
     * @param pass     the pass
     * @return the login resp
     */
    protected abstract LoginResp loginViaUi(String userName, String pass);

    /**
     * Login via hack r.
     *
     * @param authData the auth data
     * @param userName the user name
     * @param pass     the pass
     * @return the r
     */
    protected abstract R loginViaHack(Map<String, Object> authData, String userName, String pass);

    /**
     * Is auth data valid boolean.
     *
     * @param authData the auth data
     * @return the boolean
     */
    protected abstract boolean isAuthDataValid(Map<String, Object> authData);

    /**
     * The type Login resp.
     */
    @Data
    public class LoginResp {
        /**
         * The Auth data.
         */
        Map<String, Object> authData;
        /**
         * The Resp.
         */
        R resp;
    }

}
