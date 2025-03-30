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

package com.coherentsolutions.yaf.report;


import com.coherentsolutions.yaf.core.exception.DataYafException;
import com.coherentsolutions.yaf.core.template.TemplateService;
import com.coherentsolutions.yaf.report.domain.ExecutionReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * The type Print report service.
 */
@Service
@Slf4j
@Order
public class PrintReportService {

    /**
     * The Template service.
     */
    @Autowired
    @Qualifier("velocity")
    TemplateService templateService;

    /**
     * The Properties.
     */
    @Autowired
    YafReportProperties properties;

    /**
     * Print report.
     *
     * @param executionReport the execution report
     */
    public void printReport(ExecutionReport executionReport) {
        try {
            String result = templateService.processTemplate(properties.getTemplate(), properties.getContextName(),
                    executionReport);
            System.out.println(result);
            if (properties.isLogReport()) {
                log.info(result);
            }
        } catch (DataYafException e) {
            log.error("Unable to print final report", e);
        }

    }
}
