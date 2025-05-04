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

package com.coherentsolutions.yaf.core.context;


import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The type Context.
 */
@Data
@Accessors(chain = true)
public abstract class Context implements Serializable {

    /**
     * The Name.
     */
    protected String name;
    /**
     * The Params.
     */
    @Getter
    protected Map<String, Object> params = new ConcurrentHashMap<>();

    /**
     * Add prams.
     *
     * @param p the p
     */
    public void addPrams(Map<String, Object> p) {
        params.putAll(p);
    }

    /**
     * Add string params.
     *
     * @param stringParams the string params
     */
    public void addStringParams(Map<String, String> stringParams) {
        params.putAll(stringParams.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())));
    }

    /**
     * Clear context.
     */
    public void clearContext() {
        name = null;
        params = new ConcurrentHashMap<>();
        clear();
    }

    /**
     * Clear.
     */
    protected abstract void clear();
}
