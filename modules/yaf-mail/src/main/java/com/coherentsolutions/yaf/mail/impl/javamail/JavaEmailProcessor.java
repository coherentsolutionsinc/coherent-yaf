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

import com.coherentsolutions.yaf.mail.EmailProcessor;
import com.coherentsolutions.yaf.mail.domain.Email;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Java Mail Processor handles which fields must be read by default, additionally to default fields, for filtered emails
 *
 * @param <T> the type parameter
 */
@Service
public class JavaEmailProcessor<T> implements EmailProcessor<T> {
	/**
	 * Method which gets default information from email
	 *
	 * @param originalEmails
	 * @return
	 */
	@SneakyThrows
	public List<Email> processDefaultEmailInfo(List<T> originalEmails) {
		List<Email> emails = new ArrayList<>();
		for (T originalEmail : originalEmails) {
			Message originalMessage = (Message) originalEmail;
			Email<Message> email = new Email<>();
			email.setInitialEmail(originalMessage);
			email.setId(originalMessage.getMessageNumber());
			email.setSubject(originalMessage.getSubject());
			email.setFrom(Arrays.stream(originalMessage.getFrom()).map(Address::toString).toList());
			email.setTo(Arrays.stream(originalMessage.getRecipients(Message.RecipientType.TO)).map(Address::toString).toList());
			processAdditionalEmailInfo(email, originalEmail);
			emails.add(email);
		}
		return emails;
	}

	/**
	 * Method for a possibility to get extra info during initial email reading.
	 * Added for a possibility to override the method in case extra info is required by default
	 *
	 * @param email
	 * @param originalEmail
	 * @return
	 */
	@SneakyThrows
	@Override
	public Email processAdditionalEmailInfo(Email email, T originalEmail) {
		Message originalMessage = (Message) originalEmail;
		email.setReceivedDate(originalMessage.getReceivedDate());
		return email;
	}

	/**
	 * Read more info for filtered emails only.
	 *
	 * @param emails
	 * @return
	 */
	@SneakyThrows
	@Override
	public List<Email> processExtraInfoForFilteredEmails(List<Email> emails) {
		for (Email email : emails) {
			Message originalMessage = (Message) email.getInitialEmail();
			email.setContent(originalMessage.getContent());
		}
		return emails;
	}

	/**
	 * Method which allows us convert Email to an object which will be used by Email Service type
	 *
	 * @param email
	 * @param session
	 * @return
	 */

	@SneakyThrows
	@Override
	public T processEmail(Email email, Session session) {
		//todo need to check - it's just example
		MimeMessage msg = new MimeMessage(session);
		Map<String, String> headers = email.getHeaders();
		for (Map.Entry<String, String> stringStringEntry : headers.entrySet()) {
			msg.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
		}
		msg.setFrom((String) email.getFrom().get(0));
		Address[] replyTo = new InternetAddress[1];
		replyTo[0] = new InternetAddress(email.getReplyTo());
		msg.setReplyTo(replyTo);
		msg.setSubject(email.getSubject());
		msg.setText(email.getContent().toString());
		msg.setSentDate(new Date());
		List<String> emailTo = email.getTo();
		Address[] to = new InternetAddress[emailTo.size()];
		for (int i = 0; i < emailTo.size(); i++) {
			String recipient = emailTo.get(i);
			to[i] = new InternetAddress(recipient, false);
		}
		msg.setRecipients(Message.RecipientType.TO, to);
		return (T) msg;
	}
}