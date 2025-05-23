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

import com.coherentsolutions.yaf.mail.condition.AfterDateEmailQueryCondition;
import com.coherentsolutions.yaf.mail.condition.SubjectEmailQueryCondition;
import com.coherentsolutions.yaf.mail.domain.Email;
import com.coherentsolutions.yaf.mail.domain.MultiPartData;
import com.coherentsolutions.yaf.mail.impl.mailinator.MailinatorEmailService;
import org.jsoup.nodes.Document;

import java.util.Date;
import java.util.List;

public class SampleTest {

    public static void main(String[] args) {
        EmailService service = new MailinatorEmailService();
        //new JavaEmailService();
        List<Email> emails = service.readEmails();
        Email email = emails.get(10);
        String body = service.getMessageData(email);
        Document html = service.getMessageDataHtml(email);
        List<MultiPartData> multipart = service.getMessageDataMultipart(email);

        List<Email> emails1 = service.readEmails(
                new SubjectEmailQueryCondition("subjectFromDime")
        );

        List<Email> emails2 = service.readEmails(
                new SubjectEmailQueryCondition("subjectFromArtem"),
                new AfterDateEmailQueryCondition(new Date())
        );


    }
}
