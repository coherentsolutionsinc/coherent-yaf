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

import com.coherentsolutions.yaf.mail.EmailProcessor;
import com.coherentsolutions.yaf.mail.domain.Email;
import jakarta.mail.Session;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Mailinator email processor.
 *
 * @param <T> the type parameter
 */
@Service
public class MailinatorEmailProcessor<T> implements EmailProcessor<T> {
	/**
	 * Method which gets default information from email
	 *
	 * @param originalEmails
	 * @return
	 */
	@Override
	public List<Email> processDefaultEmailInfo(List<T> originalEmails) {
		return null;
	}

	/**
	 * Method for a possibility to get extra info during initial email reading.
	 * Added for a possibility to override the method in case extra info is required by default
	 *
	 * @param email
	 * @param originalEmail
	 * @return
	 */
	@Override
	public Email processAdditionalEmailInfo(Email email, T originalEmail) {
		return email;
	}

	/**
	 * Read more info for filtered emails only.
	 *
	 * @param emails
	 * @return
	 */
	@Override
	public List<Email> processExtraInfoForFilteredEmails(List<Email> emails) {
		return emails;
	}

	/**
	 * Method which allows us convert Email to an object which will be used by Email Service type
	 *
	 * @param email
	 * @param session
	 * @return
	 */
	@Override
	public T processEmail(Email email, Session session) {
		return null;
	}
}