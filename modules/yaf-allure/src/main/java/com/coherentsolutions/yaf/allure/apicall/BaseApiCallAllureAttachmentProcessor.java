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

package com.coherentsolutions.yaf.allure.apicall;


import com.coherentsolutions.yaf.core.api.processor.log.ApiCallLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


/**
 * The type Base api call allure attachment processor.
 */
@Service
@Slf4j
@Primary
public class BaseApiCallAllureAttachmentProcessor implements ApiCallAllureAttachmentProcessor {

    public void setAttachmentsFromApiCallLog(ApiCallLog apiCallLog) {
        Object bodyObject = apiCallLog.getReqBody();
        Allure.addAttachment("Request: ", "text/html", String.format("<br> Method: %s </br> <br> URL: %s </br>", apiCallLog.getMethod(), apiCallLog.getUrl()), ".html");
        try {
            if (bodyObject != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                String object = objectMapper.writeValueAsString(bodyObject);
                Allure.addAttachment("Request body: ", "application/json", object, ".json");
            }
        } catch (JsonProcessingException exception) {
            log.error("Error in serializing  Api call body to String", exception.getMessage());
        }

    }
}
