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

package com.coherentsolutions.yaf.mail.utils;

import com.coherentsolutions.yaf.mail.condition.EmailQueryCondition;
import com.coherentsolutions.yaf.mail.domain.Email;
import com.coherentsolutions.yaf.mail.domain.EmailQuerySettings;
import com.coherentsolutions.yaf.mail.domain.MailProperties;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * The type Email utils service.
 */
@Service
public class EmailUtilsService {
    /**
     * Check if userEmail overrides default properties by provided  settings
     *
     * @param settings   EmailQuerySettings which will be used if an appropriate property exist
     * @param properties MailProperties to get default setting
     * @return An appropriate user email
     */
    public String getUserEmail(EmailQuerySettings settings, MailProperties properties) {
		if (settings != null) {
			String userEmail = settings.getUserEmail();
			if (userEmail != null && !userEmail.isEmpty()) {
				return userEmail;
			}
		}
		return properties.getUserEmail();
	}

    /**
     * Check if folder Name overrides default properties by provided  settings
     *
     * @param settings   EmailQuerySettings which will be used if an appropriate property exist
     * @param properties MailProperties to get default setting
     * @return An appropriate folder name
     */
    public String getFolderName(EmailQuerySettings settings, MailProperties properties) {
		if (settings != null) {
			String folderName = settings.getFolder();
			if (folderName != null && !folderName.isEmpty()) {
				return folderName;
			}
		}
		return properties.getDefaultFolder();
	}

    /**
     * Check if delay Time overrides default properties by provided  settings
     *
     * @param settings   EmailQuerySettings which will be used if an appropriate property exist
     * @param properties MailProperties to get default setting
     * @return An appropriate delay time
     */
    public long getDelayTime(EmailQuerySettings settings, MailProperties properties) {
		if (settings != null) {
			long initialDelay = settings.getInitialDelay();
			if (initialDelay != 0) {
				return initialDelay;
			}
		}
		return properties.getDelayTime();
	}

    /**
     * Check if Pooling Time overrides default properties by provided  settings
     *
     * @param settings   EmailQuerySettings which will be used if an appropriate property exist
     * @param properties MailProperties to get default setting
     * @return An appropriate pooling time
     */
    public long getPoolingTime(EmailQuerySettings settings, MailProperties properties) {
		if (settings != null) {
			long poolingTime = settings.getPooling();
			if (poolingTime != 0) {
				return poolingTime;
			}
		}
		return properties.getPoolingTime();
	}

    /**
     * Check if Timeout overrides default properties by provided  settings
     *
     * @param settings   EmailQuerySettings which will be used if an appropriate property exist
     * @param properties MailProperties to get default setting
     * @return An appropriate timeout
     */
    public long getTimeout(EmailQuerySettings settings, MailProperties properties) {
		if (settings != null) {
			long timeOut = settings.getTimeOut();
			if (timeOut != 0) {
				return timeOut;
			}
		}
		return properties.getTimeout();
	}

    /**
     * Check if page size overrides default properties by provided  settings
     *
     * @param settings   EmailQuerySettings which will be used if an appropriate property exist
     * @param properties MailProperties to get default setting
     * @return An appropriate page size
     */
    public int getPageSize(EmailQuerySettings settings, MailProperties properties) {
		if (settings != null) {
			int pageSize = settings.getPageSize();
			if (pageSize != 0) {
				return pageSize;
			}
		}
		return properties.getDefaultPageSize();
	}

    /**
     * Check if Page Number overrides default properties by provided  settings
     *
     * @param settings   EmailQuerySettings which will be used if an appropriate property exist
     * @param properties MailProperties to get default setting
     * @return An appropriate page number
     */
    public int getPage(EmailQuerySettings settings, MailProperties properties) {
		if (settings != null) {
			int page = settings.getPage();
			if (page != 0) {
				return page;
			}
		}
		return properties.getDefaultPage();
	}

    /**
     * Calculate start message number based on provided settings and properties
     *
     * @param messageCount number of all messages
     * @param settings     EmailQuerySettings which will be used if an appropriate property exist
     * @param properties   MailProperties to get default setting
     * @return start message number
     */
    public int getStartMessage(int messageCount, EmailQuerySettings settings, MailProperties properties) {
		int pageSize = getPageSize(settings, properties);
		int page = getPage(settings, properties);
		int startMessageNumber = messageCount - (pageSize * page) + 1;
		return Math.max(startMessageNumber, 1);
	}

    /**
     * Calculate end message number based on provided settings and properties
     *
     * @param messageCount number of all messages
     * @param settings     EmailQuerySettings which will be used if an appropriate property exist
     * @param properties   MailProperties to get default setting
     * @return end message number
     */
    public int getEndMessage(int messageCount, EmailQuerySettings settings, MailProperties properties) {
		int page = getPage(settings, properties);
		int pageSize = getPageSize(settings, properties);
		if (page == 1) {
			return messageCount;
		} else {
			return messageCount - (page * pageSize) + pageSize;
		}
	}

    /**
     * Check if user password overrides default properties by provided  settings
     *
     * @param settings   EmailQuerySettings which will be used if an appropriate property exist
     * @param properties MailProperties to get default setting
     * @return An appropriate user password
     */
    public String getUserPassword(EmailQuerySettings settings, MailProperties properties) {
		if (settings != null) {
			String userPassword = settings.getPassword();
			if (userPassword != null && !userPassword.isEmpty()) {
				return userPassword;
			}
		}
		return properties.getPassword();
	}

    /**
     * Method which build Predicates for future emails filtering
     *
     * @param a condition for building predicates
     * @return Email predicate
     */
    public Predicate<Email> buildFilterCondition(EmailQueryCondition... a) {
		if (a.length > 0) {
			Predicate<Email> predicates = email -> true;
			for (EmailQueryCondition emailQueryCondition : a) {
				predicates = predicates.and(emailQueryCondition.buildPredicate());
			}
			return predicates;
		}
		return null;
	}

    /**
     * Method which allows us to combine different Query conditions
     *
     * @param newCondition extra condition which need to be added to existing one
     * @param a            list of conditions where new one should be added
     * @return combined array of conditions
     */
    public EmailQueryCondition[] addCondition(EmailQueryCondition newCondition, EmailQueryCondition... a) {
		EmailQueryCondition[] allConditions = Arrays.copyOf(a, a.length + 1);
		allConditions[a.length] = newCondition;
		return allConditions;
	}
}
