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

package com.coherentsolutions.yaf.web.utils;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * The type Browser local storage.
 */
@Service
public class BrowserLocalStorage extends WebUtils {

    /**
     * The constant LS.
     */
    public static final String LS = "window.localStorage";

    /**
     * Execute java script object.
     *
     * @param script the script
     * @param args   the args
     * @return the object
     */
    protected Object executeJavaScript(String script, Object... args) {
        return ((JavascriptExecutor) getWebDriver()).executeScript(script, args);
    }

    /**
     * Remove item.
     *
     * @param itemName the item name
     */
    public void removeItem(String itemName) {
        executeJavaScript(LS + ".removeItem('" + itemName + "');");
    }

    /**
     * Is item present boolean.
     *
     * @param itemName the item name
     * @return the boolean
     */
    public boolean isItemPresent(String itemName) {
        return getItemValue(itemName) != null;
    }

    /**
     * Gets item value.
     *
     * @param itemName the item name
     * @return the item value
     */
    public String getItemValue(String itemName) {
        return (String) executeJavaScript("return " + LS + ".getItem('" + itemName + "');");
    }

    /**
     * Gets length.
     *
     * @return the length
     */
    public Long getLength() {
        return (Long) executeJavaScript("return " + LS + ".length;");
    }

    /**
     * Sets item.
     *
     * @param itemName the item name
     * @param value    the value
     */
    public void setItem(String itemName, String value) {
        executeJavaScript(String.format(LS + ".setItem('%s','%s');", itemName, value));
    }

    /**
     * Clear.
     */
    public void clear() {
        executeJavaScript(LS + "clear();");
    }

    /**
     * Gets all.
     *
     * @return the all
     * @throws JsonProcessingException the json processing exception
     */
    public Map<String, String> getAll() throws JsonProcessingException {
        String ls = (String) executeJavaScript("return JSON.stringify(" + LS + ");");
        return new ObjectMapper().readValue(ls, new TypeReference<Map<String, String>>() {
        });
    }
}
