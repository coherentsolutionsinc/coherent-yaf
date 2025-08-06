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

package com.coherentsolutions.yaf.report.metrics;

import com.coherentsolutions.yaf.core.api.YafApiMetric;
import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.events.global.ExecutionFinishEvent;
import com.coherentsolutions.yaf.core.metrics.YafMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".metrics.report.api.enabled", havingValue = "true")
public class YafApiMetricsFileReport {

    @Autowired
    YafMetricsService metricsService;

    @EventListener
    @Order()
    public void executionFinishEvent(ExecutionFinishEvent executionFinishEvent) {
        String basicApiTimingReport = metricsService.getMetrics().stream().filter(m -> m.getType().equals(YafApiMetric.TYPE))
                .map(m -> {
                    YafApiMetric metric = (YafApiMetric) m;
                    return metric.getMethod() + "," + URLEncoder.encode(metric.getUri()) + "," + metric.getStatusCode() + "," + metric.getValue();
                }).collect(Collectors.joining("\n"));
        try {
            Files.write(Path.of("apiReport.csv"),
                    basicApiTimingReport.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


}
