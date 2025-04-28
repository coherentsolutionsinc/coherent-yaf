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
import com.slack.api.webhook.Payload;

/**
 * The interface Slack execution report message payload.
 */
public interface SlackExecutionReportMessagePayload {

    /**
     * Gets report payload.
     *
     * @param report the report
     * @return the report payload
     */
    Payload getReportPayload(ExecutionReport report);

    /**
     * Gets report chat post message request.
     *
     * @param report the report
     * @return the report chat post message request
     */
    ChatPostMessageRequest getReportChatPostMessageRequest(ExecutionReport report);
}
