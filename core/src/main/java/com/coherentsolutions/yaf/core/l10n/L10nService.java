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

package com.coherentsolutions.yaf.core.l10n;


import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * The type L 10 n service.
 */
@Service
public class L10nService {

    /**
     * The Message source.
     */
    @Autowired
    MessageSource messageSource;

    /**
     * The Default locale.
     */
    @Autowired
    Locale defaultLocale;

    /**
     * The Bean utils.
     */
    @Autowired
    YafBeanUtils beanUtils;

    /**
     * Gets message.
     *
     * @param key    the key
     * @param locale the locale
     * @return the message
     */
    public String getMessage(String key, Locale locale) {
        return getMessage(key, null, locale);
    }

    /**
     * Gets message.
     *
     * @param key    the key
     * @param args   the args
     * @param locale the locale
     * @return the message
     */
    public String getMessage(String key, Object[] args, Locale locale) {
        return messageSource.getMessage(key, args, locale);
    }

    /**
     * Gets message.
     *
     * @param key the key
     * @return the message
     */
    public String getMessage(String key) {
        return getMessage(key, new Object[0]);
    }

    /**
     * Gets message.
     *
     * @param key  the key
     * @param args the args
     * @return the message
     */
    public String getMessage(String key, Object[] args) {
        TestExecutionContext testExecutionContext = beanUtils.tec();
        Locale locale = defaultLocale;
        if (testExecutionContext != null) {
            locale = (Locale) testExecutionContext.getParam(Consts.CTX_LOCALE);
        }
        return getMessage(key, args, locale);
    }

}
