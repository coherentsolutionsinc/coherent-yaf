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


import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.events.EventsService;
import com.coherentsolutions.yaf.core.events.global.ExecutionFinishEvent;
import com.coherentsolutions.yaf.core.events.global.ExecutionStartEvent;
import com.coherentsolutions.yaf.core.events.global.SuiteStartEvent;
import com.coherentsolutions.yaf.core.events.test.TestFinishEvent;
import com.coherentsolutions.yaf.core.test.model.TestInfo;
import com.coherentsolutions.yaf.report.domain.ExecutionReport;
import com.coherentsolutions.yaf.report.domain.SuiteReport;
import com.coherentsolutions.yaf.report.domain.TestReport;
import com.coherentsolutions.yaf.report.events.ExecutionReportReadyEvent;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * The type Report service.
 */
@Data
@Accessors(chain = true)
@Slf4j
@Service
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".report.enabled", havingValue = "true")
public class ReportService {

    /**
     * The Execution report.
     */
    ExecutionReport executionReport;

    /**
     * The Events service.
     */
    @Autowired
    EventsService eventsService;

    /**
     * The Print report service.
     */
    @Autowired
    PrintReportService printReportService;

    /**
     * Instantiates a new Report service.
     */
    public ReportService() {
        executionReport = new ExecutionReport();
    }

    /**
     * Execution start event.
     *
     * @param executionStartEvent the execution start event
     */
    @EventListener
    public void executionStartEvent(ExecutionStartEvent executionStartEvent) {
        executionReport.setStartTime(executionStartEvent.getStartTime()).setProfile(executionStartEvent.getProfile())
                .setEnvConfigName(executionStartEvent.getEnvConfigName()).setTestEnv(executionStartEvent.getTestEnv())
                .setEnvProps(executionStartEvent.getEnvProps());
    }

    /**
     * Execution finish event.
     *
     * @param executionFinishEvent the execution finish event
     */
    @EventListener
    public void executionFinishEvent(ExecutionFinishEvent executionFinishEvent) {
        executionReport.setEndTime(executionFinishEvent.getEndTime());
        printReportService.printReport(executionReport);
        eventsService.sendEvent(new ExecutionReportReadyEvent().setReport(executionReport));
    }

    /**
     * Suite start event.
     *
     * @param suiteStartEvent the suite start event
     */
    @EventListener
    public void suiteStartEvent(SuiteStartEvent suiteStartEvent) {
        SuiteReport suiteReport = new SuiteReport();
        suiteReport.setSuiteId(suiteStartEvent.getSuiteInfo().getSuiteId())
                .setName(suiteStartEvent.getSuiteInfo().getSuiteName())
                .setSuiteParams(suiteStartEvent.getSuiteInfo().getSuiteParams());
        executionReport.getSuiteReports().put(suiteReport.getSuiteId(), suiteReport);

    }

    /**
     * Test finish event.
     *
     * @param testFinishEvent the test finish event
     */
    @EventListener
    public void testFinishEvent(TestFinishEvent testFinishEvent) {
        TestInfo testInfo = testFinishEvent.getTestInfo();
        String suiteId = testInfo.getSuiteInfo().getSuiteId();

        TestReport testReport = new TestReport(testFinishEvent.getTestResult());
        BeanUtils.copyProperties(testInfo, testReport);
        BeanUtils.copyProperties(testFinishEvent, testReport);

        SuiteReport suiteReport = executionReport.getSuiteReports().get(suiteId);
        if (suiteReport != null) {
            suiteReport.getTestReports().add(testReport);
        } else {
            log.error("Unable to add test result to report, cause suite can not be found!");
        }
    }
}
