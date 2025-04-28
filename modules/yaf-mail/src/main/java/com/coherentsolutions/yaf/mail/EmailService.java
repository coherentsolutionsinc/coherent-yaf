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

package com.coherentsolutions.yaf.mail;


import com.coherentsolutions.yaf.mail.condition.EmailQueryCondition;
import com.coherentsolutions.yaf.mail.domain.Attachment;
import com.coherentsolutions.yaf.mail.domain.Email;
import com.coherentsolutions.yaf.mail.domain.EmailQuerySettings;
import com.coherentsolutions.yaf.mail.domain.MultiPartData;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * The interface Email service.
 */
public interface EmailService {


    /**
     * Send email.
     *
     * @param email the email
     */
    void sendEmail(Email email);

    /**
     * Read single email email.
     *
     * @param a the a
     * @return the email
     */
    default Email readSingleEmail(EmailQueryCondition... a) {
        return readSingleEmail(null, a);
    }

    /**
     * Read single email email.
     *
     * @param settings the settings
     * @param a        the a
     * @return the email
     */
    default Email readSingleEmail(EmailQuerySettings settings, EmailQueryCondition... a) {
        List<Email> emails = readEmails(settings, a);
        if (emails != null && !emails.isEmpty()) {
            return emails.get(0);
        }
        return null;
    }

    /**
     * Read emails list.
     *
     * @param a the a
     * @return the list
     */
    default List<Email> readEmails(EmailQueryCondition... a) {
        return readEmails(null, a);
    }

    /**
     * Read emails list.
     *
     * @param settings the settings
     * @param a        the a
     * @return the list
     */
    List<Email> readEmails(EmailQuerySettings settings, EmailQueryCondition... a);

    /**
     * Wait for emails list.
     *
     * @param settings the settings
     * @param a        the a
     * @return the list
     */
    List<Email> waitForEmails(EmailQuerySettings settings, EmailQueryCondition... a);

    /**
     * Wait for emails list.
     *
     * @param r the r
     * @param a the a
     * @return the list
     */
    default List<Email> waitForEmails(Runnable r, EmailQueryCondition... a) {
		return waitForEmails(r, null, a);
    }

    /**
     * Wait for emails list.
     *
     * @param r        the r
     * @param settings the settings
     * @param a        the a
     * @return the list
     */
    List<Email> waitForEmails(Runnable r, EmailQuerySettings settings, EmailQueryCondition... a);


    /**
     * Gets message data.
     *
     * @param email the email
     * @return the message data
     */
    String getMessageData(Email email);

    /**
     * Gets message data html.
     *
     * @param email the email
     * @return the message data html
     */
    Document getMessageDataHtml(Email email);

    /**
     * Gets message data multipart.
     *
     * @param email the email
     * @return the message data multipart
     */
    List<MultiPartData> getMessageDataMultipart(Email email);

    /**
     * Method gets all multi parts of emails checks that the part is an attachment
     *
     * @param email Original Email get getting attachments
     * @return List of attachments in the email
     */
    List<Attachment> getAttachments(Email email);
}
