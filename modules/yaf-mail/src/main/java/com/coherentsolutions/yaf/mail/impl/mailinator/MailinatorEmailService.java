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

package com.coherentsolutions.yaf.mail.impl.mailinator;

import com.coherentsolutions.yaf.mail.EmailService;
import com.coherentsolutions.yaf.mail.condition.EmailQueryCondition;
import com.coherentsolutions.yaf.mail.condition.SubjectEmailQueryCondition;
import com.coherentsolutions.yaf.mail.domain.Attachment;
import com.coherentsolutions.yaf.mail.domain.Email;
import com.coherentsolutions.yaf.mail.domain.EmailQuerySettings;
import com.coherentsolutions.yaf.mail.domain.MultiPartData;
import com.coherentsolutions.yaf.mail.utils.EmailUtilsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MailinatorEmailService implements EmailService {

    @Autowired
    EmailUtilsService utilsService;

    @Autowired
    MailinatorEmailProcessor emailProcessor;

    @Override
    public void sendEmail(Email email) {
        throw new NotImplementedException("Not implemented yet!");
    }

    /**
     * Base method which allows to get Emails, by default limited by pageSize property
     *
     * @param settings In case custom settings are provided it will override default settings to new ones
     * @param a        In case it's provided found emails will be filtered
     * @return list of emails which got based on custom settings and filtered by provideded conditions
     */
    @Override
    public List<Email> readEmails(EmailQuerySettings settings, EmailQueryCondition... a) {
        List<Object> messages = null;
        List<Email> emails = emailProcessor.processDefaultEmailInfo(messages);
        throw new NotImplementedException("Not implemented yet!");
    }

    /**
     * the method allows us to wait for new email which is sent recently;
     *
     * @param settings In case custom settings are provided it will override default settings to new ones
     * @param a        In case it's provided it will wait for email which corresponds provided conditions
     * @return filtered email will be returned
     */
    @Override
    public List<Email> waitForEmails(EmailQuerySettings settings, EmailQueryCondition... a) {
        throw new NotImplementedException("Not implemented yet!");
    }

    /**
     * the method allows us to wait for new email which is sent by a runnable method;
     *
     * @param r        runnable method which send new email
     * @param settings In case custom settings are provided it will override default settings to new ones
     * @param a        In case it's provided it will wait for email which corresponds provided conditions
     * @return filtered email will be returned
     */
    @Override
    public List<Email> waitForEmails(Runnable r, EmailQuerySettings settings, EmailQueryCondition... a) {
        throw new NotImplementedException("Not implemented yet!");
    }

    /**
     * Parse content of the Email
     *
     * @param email
     * @return
     */
    @Override
    public String getMessageData(Email email) {
        throw new NotImplementedException("Not implemented yet!");
    }

    /**
     * Parse content of the Email in HTML format
     *
     * @param email
     * @return
     */
    @Override
    public Document getMessageDataHtml(Email email) {
        throw new NotImplementedException("Not implemented yet!");
    }

    /**
     * Parse content of the Email in MultiPart format
     *
     * @param email
     * @return
     */
    @Override
    public List<MultiPartData> getMessageDataMultipart(Email email) {
        throw new NotImplementedException("Not implemented yet!");
    }

    /**
     * Custom filtering approach
     *
     * @param a
     * @return
     */

    protected String buildFilterCondition(EmailQueryCondition... a) {
        String queryString = "";
        for (EmailQueryCondition condition : a) {
            if (condition instanceof SubjectEmailQueryCondition) {
                SubjectEmailQueryCondition subjectEmailQueryCondition = (SubjectEmailQueryCondition) condition;
                queryString += "subject=" + subjectEmailQueryCondition.getSubject();
            }
        }
        return queryString;
    }

    /**
     * Method gets all multi parts of emails checks that the part is an attachment
     * @param email Original Email get getting attachments
     * @return List of attachments in the email
     */
    @Override
    public List<Attachment> getAttachments(Email email) {
        return List.of();
    }
}
