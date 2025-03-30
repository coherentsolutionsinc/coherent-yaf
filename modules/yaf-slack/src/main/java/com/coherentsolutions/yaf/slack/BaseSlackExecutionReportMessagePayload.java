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

package com.coherentsolutions.yaf.slack;

import com.coherentsolutions.yaf.report.domain.ExecutionReport;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.webhook.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

@Component
public class BaseSlackExecutionReportMessagePayload implements SlackExecutionReportMessagePayload {

    @Override
    public Payload getReportPayload(ExecutionReport report) {
        LayoutBlock[] blocks = prepareBlocks(report);
        return Payload.builder().blocks(asBlocks(blocks)).build();
    }

    @Override
    public ChatPostMessageRequest getReportChatPostMessageRequest(ExecutionReport report) {
        LayoutBlock[] blocks = prepareBlocks(report);
        return ChatPostMessageRequest.builder().blocks(asBlocks(blocks)).build();
    }

    public LayoutBlock[] prepareBlocks(ExecutionReport report) {
        List<LayoutBlock> sectionBlocks = new ArrayList<>();
        sectionBlocks.add(SectionBlock.builder().text(markdownText("*Test Result Report*")).build());
        sectionBlocks.add(SectionBlock.builder().text(markdownText(String.format("Environment: %s", report.getTestEnv()))).build());
        sectionBlocks.add(SectionBlock.builder().text(markdownText(String.format("Environment config: %s", report.getEnvConfigName()))).build());
        report.getSuiteReports().forEach((k, v) -> {
            sectionBlocks.addAll(asBlocks(divider()));
            sectionBlocks.add(section(section -> section.text(markdownText(String.format("Suite name: %s", k)))));
            sectionBlocks.add(section(section -> section.text(markdownText(v.getStringStats()))));
            sectionBlocks.addAll(asBlocks(divider()));
        });
        return sectionBlocks.toArray(new LayoutBlock[0]);
    }
}
