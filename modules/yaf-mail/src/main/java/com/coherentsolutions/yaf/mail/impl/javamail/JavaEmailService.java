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

package com.coherentsolutions.yaf.mail.impl.javamail;

import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.mail.EmailService;
import com.coherentsolutions.yaf.mail.condition.EmailNumberMoreQueryCondition;
import com.coherentsolutions.yaf.mail.condition.EmailQueryCondition;
import com.coherentsolutions.yaf.mail.domain.*;
import com.coherentsolutions.yaf.mail.utils.EmailUtilsService;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.SneakyThrows;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Primary
public class JavaEmailService implements EmailService {

    @Autowired
    StoreService storeService;

    @Autowired
    MailProperties properties;

    @Autowired
    EmailUtilsService utilsService;

    @Autowired
    JavaEmailProcessor emailProcessor;

    /**
     * Method to send a provided email
     *
     * @param email
     */

    @SneakyThrows
    @Override
    public void sendEmail(Email email) {
        Session session = storeService.initTransportSession();
        MimeMessage message = (MimeMessage) emailProcessor.processEmail(email, session);
        Transport.send(message, properties.getUserEmail(), properties.getPassword());
    }

    /**
     * Base method which allows to get Emails, by default limited by pageSize property
     *
     * @param settings In case custom settings are provided it will override default settings to new ones
     * @param a        In case it's provided found emails will be filtered
     * @return list of emails which got based on custom settings and filtered by provideded conditions
     */
    @SneakyThrows
    @Override
    public List<Email> readEmails(EmailQuerySettings settings, EmailQueryCondition... a) {
        Folder emailFolder = storeService.getEmailFolder(settings);
        int messageCount = emailFolder.getMessageCount();
        int start = utilsService.getStartMessage(messageCount, settings, properties);
        int end = utilsService.getEndMessage(messageCount, settings, properties);
        Message[] messages = emailFolder.getMessages(start, end);
        List<Email> emails = emailProcessor.processDefaultEmailInfo(List.of(messages));
        if (a.length > 0) {
            Predicate<Email> predicates = utilsService.buildFilterCondition(a);
            if (predicates != null) {
                emails = emails.stream().filter(predicates).collect(Collectors.toList());
            }
        }
        emails = emailProcessor.processExtraInfoForFilteredEmails(emails);
        return emails;
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
        AtomicReference<List<Email>> list = new AtomicReference<>(new ArrayList<>());
        try {
            Awaitility.await()
                    .atLeast(utilsService.getDelayTime(settings, properties), TimeUnit.MILLISECONDS)
                    .atMost(utilsService.getTimeout(settings, properties), TimeUnit.MILLISECONDS)
                    .with()
                    .pollInterval(utilsService.getPoolingTime(settings, properties), TimeUnit.MILLISECONDS)
                    .until(() -> {
                        List<Email> receivedEmails = readEmails(a);
                        if (receivedEmails.isEmpty()) {
                            return false;
                        } else {
                            list.set(receivedEmails);
                            return true;
                        }
                    });
        } catch (ConditionTimeoutException ex) {
            return null; //need to cover negative cases.
        }
        return list.get();
    }

    /**
     * the method allows us to wait for new email which is sent by a runnable method;
     *
     * @param r        runnable method which send new email
     * @param settings In case custom settings are provided it will override default settings to new ones
     * @param a        In case it's provided it will wait for email which corresponds provided conditions
     * @return filtered email will be returned
     */
    @SneakyThrows
    @Override
    public List<Email> waitForEmails(Runnable r, EmailQuerySettings settings, EmailQueryCondition... a) {
        int before = getLatestEmailId(settings);
        EmailQueryCondition idMore = new EmailNumberMoreQueryCondition(before);
        EmailQueryCondition[] allConditions = utilsService.addCondition(idMore, a);
        r.run();
        return waitForEmails(settings, allConditions);
    }

    /**
     * Get the latest email number
     *
     * @param settings
     * @return
     */
    @SneakyThrows
    private int getLatestEmailId(EmailQuerySettings settings) {
        Folder emailFolder = storeService.getEmailFolder(settings);
        Message[] messages = emailFolder.getMessages();
        Message message = messages[messages.length - 1];
        return message.getMessageNumber();
    }

    /**
     * Parse content of the Email
     *
     * @param email
     * @return
     */
    @SneakyThrows
    @Override
    public String getMessageData(Email email) {
        Message message = getMessageFromEmail(email);
        if (message.isMimeType(Consts.TEXT_TYPE)) {
            return message.getContent().toString();
        }
        if (message.isMimeType(Consts.HTML_TYPE)) {
            if (properties.isParseHtml()) {
                return Jsoup.parse(message.getContent().toString()).text();
            } else {
                return message.getContent().toString();
            }
        }
        if (message.isMimeType("multipart/*")) {
            List<MultiPartData> messageDataMultipart = getMessageDataMultipart(email);
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < messageDataMultipart.size(); i++) {
                MultiPartData multiPartData = messageDataMultipart.get(i);
                result.append(multiPartData.getData().toString());
                if (i != messageDataMultipart.size() - 1) {
                    result.append("\n");
                }
            }
            return result.toString();
        }
        return null;
    }

    /**
     * Parse content of the Email in HTML format
     *
     * @param email
     * @return
     */
    @SneakyThrows
    @Override
    public Document getMessageDataHtml(Email email) {
        Part message = getMessageFromEmail(email);
        if (message.isMimeType(Consts.HTML_TYPE)) {
            return Jsoup.parse(message.getContent().toString());
        }
        if (message.isMimeType(Consts.MULTIPART_TYPE)) {
            List<MultiPartData> listOfHtmlContent = getMessageDataMultipart(email)
                    .stream()
                    .filter(it -> it.getType().equals(Consts.HTML_TYPE))
                    .toList();
            if (!listOfHtmlContent.isEmpty()) {
                StringBuilder htmlBuilder = new StringBuilder();
                for (MultiPartData htmlPartData : listOfHtmlContent) {
                    htmlBuilder.append(htmlPartData.getData().toString());
                }
                return Jsoup.parse(htmlBuilder.toString());
            }
        }
        return null;
    }

    /**
     * Parse content of the Email in MultiPart format
     *
     * @param email
     * @return
     */
    @SneakyThrows
    @Override
    public List<MultiPartData> getMessageDataMultipart(Email email) {
        Message message = getMessageFromEmail(email);
        if (message.isMimeType(Consts.MULTIPART_TYPE)) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return parseMimeMultipart(mimeMultipart);
        }
        return null;
    }

    /**
     * Help method to paese provided content
     *
     * @param mimeMultipart
     * @return
     */
    //TODO refactor to new model
    private List<MultiPartData> parseMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        List<MultiPartData> parts = new ArrayList<>();
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (!isBodyPartAttachment(bodyPart)) {
                if (bodyPart.isMimeType(Consts.TEXT_TYPE)) {
                    MultiPartData<String> data = new MultiPartData<>();
                    data.setType(Consts.TEXT_TYPE);
                    data.setData((String) bodyPart.getContent());
                    parts.add(data);
                }
                if (bodyPart.isMimeType(Consts.PDF_TYPE)) {
                    MultiPartData<String> data = new MultiPartData<>();
                    data.setType(Consts.PDF_TYPE);
                    data.setData(parsePdfContent(bodyPart));
                    parts.add(data);
                }
                if (bodyPart.isMimeType(Consts.HTML_TYPE)) {
                    MultiPartData<String> data = new MultiPartData<>();
                    data.setType(Consts.HTML_TYPE);
                    Document htmlContent = Jsoup.parse(bodyPart.getContent().toString());
                    data.setData(String.valueOf(htmlContent));
                    parts.add(data);
                }
//            else if (bodyPart.getContent() instanceof BASE64DecoderStream) {
////				BASE64DecoderStream base64DecStream = (BASE64DecoderStream) bodyPart.getContent();
////				byte[] byteArray = IOUtils.toByteArray(base64DecStream);
////				byte[] encodedBase64 = Base64.encodeBase64(byteArray);
////				return new String(encodedBase64, StandardCharsets.UTF_8);
//                //ignore images for now;
//                return result.toString();
//            } else {
//                parts.addAll(this.parseBodyPart(bodyPart));
//            }
            }
        }
        return parts;
    }

    /**
     * Help method to check if part of email is attachment
     * @param bodyPart part of email (can be any type)
     * @return true if it's an attachment
     */
    @SneakyThrows
	private boolean isBodyPartAttachment (BodyPart bodyPart) {
        return bodyPart != null && Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition());
    }

    /**
     * Method gets all multi parts of emails checks that the part is an attachment
     * @param email Original Email get getting attachments
     * @return List of attachments in the email
     */
    @SneakyThrows
	public List<Attachment> getAttachments (Email email) {
        List<Attachment> attachments = new ArrayList<>();
        Message message = getMessageFromEmail(email);
        if (message.isMimeType(Consts.MULTIPART_TYPE)) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            for (int i = 0; i < mimeMultipart.getCount(); i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (isBodyPartAttachment(bodyPart)) {
                    Attachment attachment = new Attachment();
                    attachment.setFileName(bodyPart.getFileName());
                    attachment.setContentType(bodyPart.getContentType());
                    attachment.setContentStream(bodyPart.getInputStream());
                    attachments.add(attachment);
                }
            }
        }
        return attachments;
    }

    /**
     * Method which parses pdf format to string if the content is part of email body and not an attachment;
     * @param bodyPart provide multipart body part with PDF format for parsing
     * @return parsed PDF content to String
     */
    @SneakyThrows
	private String parsePdfContent (BodyPart bodyPart){
        MimeBodyPart mimeBodyPart = (MimeBodyPart) bodyPart;
        PDDocument document = Loader.loadPDF(mimeBodyPart.getInputStream().readAllBytes());
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String text = pdfTextStripper.getText(document);
        document.close();
        return text;
    }

    /**
     * get original message from Email
     *
     * @param email
     * @return
     */
    protected Message getMessageFromEmail(Email email) {
        return ((Message) email.getInitialEmail());
    }
}
