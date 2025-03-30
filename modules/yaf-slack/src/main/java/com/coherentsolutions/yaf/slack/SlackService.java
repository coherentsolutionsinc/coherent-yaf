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

import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.report.domain.ExecutionReport;
import com.coherentsolutions.yaf.report.events.ExecutionReportReadyEvent;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".slack.enabled", havingValue = "true")
public class SlackService {

    @Autowired
    private SlackProperties properties;

    @Autowired(required = false)
    private SlackExecutionReportMessagePayload reportMessagePayload;

    /**
     * Listens for the execution report ready event and publishes the report
     * to Slack if the Slack report feature is enabled.
     *
     * @param event The event that contains the execution report to be published.
     */
    @EventListener
    public void publishReport(ExecutionReportReadyEvent event) {
        if (properties.isSendReport()) {
            // send general report to slack
            ExecutionReport report = event.getReport();
            if (reportMessagePayload != null) {
                if (StringUtils.isNotBlank(properties.getApiToken())) {
                    chatPostMessage(reportMessagePayload.getReportChatPostMessageRequest(report));
                } else if (StringUtils.isNotBlank(properties.getWebhookUrl())) {
                    sendWebHookMessage(reportMessagePayload.getReportPayload(report));
                } else {
                    log.error("Neither Slack's apiToken nor webhookUrl is set, please update application.properties");
                }
            } else {
                log.error("Unable to send slack report cause message payload bean is null");
            }
        }
    }

    /**
     * Sends a chat message to a Slack channel using the provided chat post message request
     * and channel ID.
     *
     * @param chatPostMessageRequest The request object containing the message details
     *                               to be posted to Slack.
     * @param channelId              The ID of the Slack channel where the message will be posted.
     * @return A {@link ChatPostMessageResponse} object containing the response from the Slack API,
     * or {@code null} if an error occurs while attempting to send the message.
     */
    public ChatPostMessageResponse chatPostMessage(ChatPostMessageRequest chatPostMessageRequest, String channelId) {
        try {
            chatPostMessageRequest.setChannel(channelId);
            return Slack.getInstance().methods(properties.getApiToken()).chatPostMessage(chatPostMessageRequest);
        } catch (IOException | SlackApiException e) {
            log.error("Issue appeared while trying to post chat message: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Sends a chat message to a Slack channel using the provided chat post message request.
     *
     * @param chatPostMessageRequest The request object containing the message details
     *                               to be posted to Slack.
     * @return A {@link ChatPostMessageResponse} object containing the response from the Slack API,
     * or {@code null} if an error occurs while attempting to send the message.
     */
    public ChatPostMessageResponse chatPostMessage(ChatPostMessageRequest chatPostMessageRequest) {
        return chatPostMessage(chatPostMessageRequest, properties.getChannelId());
    }

    /**
     * Sends a webhook message to Slack using the provided payload.
     *
     * @param payload The payload containing the message details to be sent via the Slack webhook.
     * @return A {@link WebhookResponse} object containing the response from the Slack webhook,
     * or {@code null} if an error occurs while attempting to send the message.
     */
    public WebhookResponse sendWebHookMessage(Payload payload) {
        try {
            return Slack.getInstance().send(properties.getWebhookUrl(), payload);
        } catch (IOException e) {
            log.error("Issue appeared while trying to send WebHook message: {}", e.getMessage(), e);
            return null;
        }
    }
}
