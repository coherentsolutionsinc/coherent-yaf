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

package com.coherentsolutions.yaf.web.processor.log.pagesource;


import com.coherentsolutions.yaf.core.log.TestLogData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static com.coherentsolutions.yaf.core.consts.Consts.HTML;
import static com.coherentsolutions.yaf.core.consts.Consts.HTML_TYPE;

/**
 * The type Web page source log.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class WebPageSourceLog extends TestLogData {

    /**
     * The Page url.
     */
    String pageUrl;
    /**
     * The Page title.
     */
    String pageTitle;
    /**
     * The Page source.
     */
    String pageSource;

    @Override
    public String getLogDataName() {
        return pageUrl + ":" + pageTitle + "--" + timestamp;
    }

    @Override
    public String getContentType() {
        return HTML_TYPE;
    }

    @Override
    public String getFileExt() {
        return HTML;
    }

    @Override
    public byte[] getData() throws Exception {
        return pageSource.getBytes(charset);
    }
}
