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
import com.coherentsolutions.yaf.core.exception.GeneralYafException;
import com.coherentsolutions.yaf.data.YafData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Json data reader.
 */
@Component
@Slf4j
public class JsonDataReader extends BaseFileReader {

    /**
     * The Mapper.
     */
    @Autowired
    ObjectMapper mapper;

    @Override
    public Object readData(YafData yafData, Class<?> objType, TestExecutionContext executionContext)
            throws GeneralYafException {
        try {
            return mapper.readValue(getFileData(yafData, executionContext), objType);
        } catch (Exception e) {
            log.error("Unable to get field value: " + e.getMessage());
            throw new GeneralYafException(
					"Unable to load data for " + objType + " for file " + yafData.fileName());
        }
    }

}
