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

package com.coherentsolutions.yaf.web;

import com.coherentsolutions.yaf.core.config.YafConfig;
import com.coherentsolutions.yaf.core.exception.GeneralYafException;
import com.coherentsolutions.yaf.core.test.BaseYafTest;
import com.coherentsolutions.yaf.core.utils.FileUtils;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The type Base j unit web test.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {YafConfig.class, TestWebConfig.class})
@TestExecutionListeners(
        listeners = JUnitListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public class BaseJUnitWebTest extends BaseYafTest {

    /**
     * The Yaf bean utils.
     */
    @Autowired
    YafBeanUtils yafBeanUtils;

    /**
     * Gets url.
     *
     * @param name the name
     * @return the url
     */
    protected static String getUrl(String name) {
        try {
            return  FileUtils.getResourceFile(name, "html", "./components/html/", true, false).toURI().toString();
        } catch (GeneralYafException e) {
            throw new RuntimeException(e);
        }
    }
}
