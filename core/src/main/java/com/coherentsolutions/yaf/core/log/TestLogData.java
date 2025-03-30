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

package com.coherentsolutions.yaf.core.log;


import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The type Test log data.
 */
@Data
@Accessors(chain = true)
public abstract class TestLogData {

    /**
     * The Timestamp.
     */
    protected long timestamp;
    /**
     * The Charset.
     */
    protected Charset charset = StandardCharsets.UTF_8;

    /**
     * Instantiates a new Test log data.
     */
    public TestLogData() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Gets log data name.
     *
     * @return the log data name
     */
    public abstract String getLogDataName();

    /**
     * Gets content type.
     *
     * @return the content type
     */
    public abstract String getContentType();

    /**
     * Gets file ext.
     *
     * @return the file ext
     */
    public abstract String getFileExt();

    /**
     * Get data byte [ ].
     *
     * @return the byte [ ]
     * @throws Exception the exception
     */
    public abstract byte[] getData() throws Exception;

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
