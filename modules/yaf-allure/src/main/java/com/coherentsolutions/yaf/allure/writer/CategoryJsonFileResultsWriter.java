package com.coherentsolutions.yaf.allure.writer;

import com.coherentsolutions.yaf.allure.AllureProperties;
import com.coherentsolutions.yaf.allure.preprocessor.KnownIssuesBeforeWritePreProcessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.qameta.allure.AllureResultsWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * The type Category json file results writer.
 */
@Service
@Slf4j
public class CategoryJsonFileResultsWriter implements YafAllureResultsWriter {

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
    public void writeToResults(AllureResultsWriter writer) {
        String customPath = allureProperties.getCategoriesJsonFilePath();
        Resource customAllureCategories = resourceLoader.getResource(customPath);
        ArrayNode result = objectMapper.createArrayNode();
        JsonNode customCategory = null;
        if (customAllureCategories != null) {
            customCategory = objectMapper.readTree(customAllureCategories.getContentAsByteArray());
            if (customCategory.isArray()) {
                customCategory.forEach(result::add);
            }
        }
        if (knownIssuesBeforeWriteProcessor != null) {
            JsonNode knownIssuesNode = knownIssuesBeforeWriteProcessor.getDefaultJsonNode();
            result.add(knownIssuesNode);
        }
        if (!result.isEmpty()) {
            byte[] bytes = objectMapper.writeValueAsBytes(result);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            writer.write("categories.json", inputStream);
        }
    }
}
