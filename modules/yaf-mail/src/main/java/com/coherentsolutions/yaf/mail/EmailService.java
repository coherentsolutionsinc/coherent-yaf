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

public interface EmailService {


    void sendEmail(Email email);

    default Email readSingleEmail(EmailQueryCondition... a) {
        return readSingleEmail(null, a);
    }

    default Email readSingleEmail(EmailQuerySettings settings, EmailQueryCondition... a) {
        List<Email> emails = readEmails(settings, a);
        if (emails != null && !emails.isEmpty()) {
            return emails.get(0);
        }
        return null;
    }

    default List<Email> readEmails(EmailQueryCondition... a) {
        return readEmails(null, a);
    }

    List<Email> readEmails(EmailQuerySettings settings, EmailQueryCondition... a);

	List<Email> waitForEmails(EmailQuerySettings settings, EmailQueryCondition... a);

	default List<Email> waitForEmails(Runnable r, EmailQueryCondition... a) {
		return waitForEmails(r, null, a);
    }

	List<Email> waitForEmails(Runnable r, EmailQuerySettings settings, EmailQueryCondition... a);


    String getMessageData(Email email);

    Document getMessageDataHtml(Email email);

    List<MultiPartData> getMessageDataMultipart(Email email);

    /**
     * Method gets all multi parts of emails checks that the part is an attachment
     * @param email Original Email get getting attachments
     * @return List of attachments in the email
     */
    List<Attachment> getAttachments(Email email);
}
