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

package com.coherentsolutions.yaf.zephyr.scale.query;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a paging request with parameters for pagination.
 * <p>
 * This abstract class provides the basic structure for a paging request,
 * including the maximum number of results and the starting index. Subclasses
 * should implement the {@link #appendProperParams(Map)} method to add specific
 * parameters to the request.
 * </p>
 */
@Data
@Accessors(chain = true)
public abstract class PagingReq {

    Integer maxResults = 20;
    Integer startAt = 0;

    /**
     * Returns the query parameters for the paging request.
     *
     * @return a map of query parameters
     */
    public Map<String, String> getQueryParams() {
        Map<String, String> params = new HashMap<>();
        if (maxResults != null) {
            params.put("maxResults", String.valueOf(maxResults));
        }
        if (startAt != null) {
            params.put("startAt", String.valueOf(startAt));
        }
        appendProperParams(params);
        return params;
    }

    /**
     * Appends the specific parameters to the given map.
     *
     * @param params the map to append parameters to
     */
    protected abstract void appendProperParams(Map<String, String> params);
}
