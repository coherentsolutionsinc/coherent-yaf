/*
 * MIT License
 *
 * Copyright (c) 2021 - 2025 Coherent Solutions Inc.
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

import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Custom request props.
 */
@Data
public class CustomRequestProps {

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
     * The Filters.
     */
    protected List<Filter> filters;

    /**
     * Update request specification.
     *
     * @param rs the rs
     */
    public void updateRequestSpecification(RequestSpecification rs) {
        if (headers != null) {
            rs.headers(headers);
        }
        if (queryParams != null) {
            rs.queryParams(queryParams);
        }
        if (multiPartSpecs != null) {
            multiPartSpecs.forEach(m -> rs.multiPart(m));
            rs.contentType(ContentType.MULTIPART);
        }
        if (filters != null) {
            filters.forEach(f -> rs.filter(f));
        }

    }

    /**
     * Add header.
     *
     * @param key   the key
     * @param value the value
     */
    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }

    /**
     * Add query param.
     *
     * @param key   the key
     * @param value the value
     */
    public void addQueryParam(String key, String value) {
        if (queryParams == null) {
            queryParams = new HashMap<>();
        }
        queryParams.put(key, value);
    }

    /**
     * Add multi part spec.
     *
     * @param m the m
     */
    public void addMultiPartSpec(MultiPartSpecification m) {
        if (multiPartSpecs == null) {
            multiPartSpecs = new ArrayList<>();
        }
        multiPartSpecs.add(m);
    }

    /**
     * Add filter.
     *
     * @param f the f
     */
    public void addFilter(Filter f) {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        filters.add(f);
    }
}
