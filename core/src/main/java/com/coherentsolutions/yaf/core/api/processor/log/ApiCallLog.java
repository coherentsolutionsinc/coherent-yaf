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

package com.coherentsolutions.yaf.core.api.processor.log;


import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.log.TestLogData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * The type Api call log.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ApiCallLog extends TestLogData {

    /**
     * The Method.
     */
    String method;
    /**
     * The Url.
     */
    String url;
    /**
     * The Status code.
     */
    int statusCode;
    /**
     * The Req headers.
     */
    Map<String, String> reqHeaders;
    /**
     * The Resp headers.
     */
    Map<String, String> respHeaders;
    /**
     * The Req body.
     */
    Object reqBody;
    /**
     * The Resp body.
     */
    Object respBody;

    @Override
    @JsonIgnore
    public String getLogDataName() {
        return method + "::" + url;
    }

    @Override
    @JsonIgnore
    public String getContentType() {
        return Consts.JSON_TYPE;
    }

    @Override
    @JsonIgnore
    public String getFileExt() {
        return Consts.JSON;
    }

    @Override
    @JsonIgnore
    public byte[] getData() throws Exception {
        return new ObjectMapper().writeValueAsBytes(this);
    }
}
