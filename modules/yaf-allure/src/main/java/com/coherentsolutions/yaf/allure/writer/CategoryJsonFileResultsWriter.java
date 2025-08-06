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

package com.coherentsolutions.yaf.allure.writer;

import com.coherentsolutions.yaf.allure.AllureProperties;
import com.coherentsolutions.yaf.allure.preprocessor.KnownIssuesBeforeWritePreProcessor;
import com.coherentsolutions.yaf.core.utils.ExtResourceUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 * The type Category json file results writer.
 */
@Service
@Slf4j
public class CategoryJsonFileResultsWriter implements YafAllureResultsWriter {

    private final String FILE_NAME = "categories.json";

    /**
     * The Known issues before write processor.
     */
    @Autowired(required = false)
    KnownIssuesBeforeWritePreProcessor knownIssuesBeforeWriteProcessor;

    /**
     * The Allure properties.
     */
    @Autowired
    AllureProperties allureProperties;

    /**
     * The Object mapper.
     */
    @Autowired
    ObjectMapper objectMapper;

    /**
     * The Resource loader.
     */
    @Autowired
    ResourceLoader resourceLoader;

    @SneakyThrows
    @Override
    public void writeToResults(WrapperForAllureWriter writer) {
        String customPath = allureProperties.getCategoriesJsonFilePath();
        ArrayNode result = objectMapper.createArrayNode();
        if (customPath != null) {
            Resource customAllureCategories = ExtResourceUtils.resolveResource(customPath, resourceLoader, false);
            if (customAllureCategories!=null && customAllureCategories.exists()) {
                JsonNode customCategory;
                customCategory = objectMapper.readTree(customAllureCategories.getContentAsByteArray());
                if (customCategory.isArray()) {
                    customCategory.forEach(result::add);
                }
            }
        }
        if (knownIssuesBeforeWriteProcessor != null) {
            JsonNode knownIssuesNode = knownIssuesBeforeWriteProcessor.getDefaultJsonNode();
            result.add(knownIssuesNode);
        }
        if (!result.isEmpty()) {
            byte[] bytes = objectMapper.writeValueAsBytes(result);
            writer.write(FILE_NAME, bytes);
        }
    }

}
