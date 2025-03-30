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

package com.coherentsolutions.yaf.data.reader;


import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.DataYafException;
import com.coherentsolutions.yaf.core.exception.GeneralYafException;
import com.coherentsolutions.yaf.core.template.TemplateService;
import com.coherentsolutions.yaf.core.test.YafTest;
import com.coherentsolutions.yaf.core.test.model.TestInfo;
import com.coherentsolutions.yaf.data.CustomTemplateFunctions;
import com.coherentsolutions.yaf.data.YafData;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Base file reader.
 */
@Data
@Accessors(chain = true)
public abstract class BaseFileReader implements DataReader {

    /**
     * The Properties.
     */
    @Autowired
    DataFactoryProperties properties;

    /**
     * The Template service.
     */
    @Autowired
    TemplateService templateService;

    /**
     * The Project path.
     */
    Path projectPath = Paths.get(System.getProperty("user.dir"));

    /**
     * The Custom template functions list.
     */
    @Autowired(required = false)
    List<CustomTemplateFunctions> customTemplateFunctionsList;

    /**
     * Gets file ext.
     *
     * @return the file ext
     */
    protected String getFileExt() {
        return "." + properties.getFileExt();
    }

    /**
     * Gets file data.
     *
     * @param yafData              the yaf data
     * @param testExecutionContext the test execution context
     * @return the file data
     * @throws GeneralYafException the general yaf exception
     */
    protected String getFileData(YafData yafData, TestExecutionContext testExecutionContext)
            throws GeneralYafException {
        String fileName = null;

        if (!yafData.fileName().isEmpty()) {
            fileName = yafData.fileName();
        } else {
            TestInfo testInfo = testExecutionContext.getTestInfo();
            YafTest yafTest = testInfo.getYafTest();
            if (yafTest != null) {
                fileName = yafTest.dataFile();
                if (fileName == null) {
                    fileName = yafTest.name();
                }
            }
            if (fileName == null) {
                fileName = testInfo.getTestMethodName();
            }
        }
        if (fileName == null) {
            throw new GeneralYafException(
                    "Unable to load data file for test " + testExecutionContext.getTestInfo().getFullTestName());
        } else {
            return processTemplate(fileName);
        }
    }

    /**
     * Process template string.
     *
     * @param templateName the template name
     * @return the string
     * @throws DataYafException the data yaf exception
     */
    public String processTemplate(String templateName) throws DataYafException {
        Map<String, Object> ctx = new HashMap<>();
        if (customTemplateFunctionsList != null) {
            customTemplateFunctionsList.forEach(ct -> {
                ctx.put(ct.getKeyName(), ct);
            });
        }
        return templateService.processTemplate(properties.getDataFolder() + "/" + templateName, ctx);
    }

}
