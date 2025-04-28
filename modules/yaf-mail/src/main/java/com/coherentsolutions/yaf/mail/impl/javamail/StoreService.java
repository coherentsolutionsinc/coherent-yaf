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

import com.coherentsolutions.yaf.mail.EmailService;
import com.coherentsolutions.yaf.mail.domain.EmailQuerySettings;
import com.coherentsolutions.yaf.mail.domain.MailProperties;
import com.coherentsolutions.yaf.mail.utils.EmailUtilsService;
import jakarta.mail.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * The type Store service.
 */
@Service
@Slf4j
public class StoreService {

    /**
     * The Utils service.
     */
    @Autowired
	EmailUtilsService utilsService;

    /**
     * The Properties.
     */
    @Autowired
	MailProperties properties;

	@Autowired
	private ApplicationContext applicationContext;

    /**
     * Opening connection to a required folder and cache the connection for future usages
     *
     * @param settings the settings
     * @return email folder
     */
    @SneakyThrows
	@Cacheable(key = "@emailUtilsService.getUserEmail(#settings, @mailProperties) + '_' + @emailUtilsService.getFolderName(#settings, @mailProperties)",
			value = "emailQueryCache")
	public Folder getEmailFolder(EmailQuerySettings settings) {
		return connectAndOpenFolder(settings);
	}

	private Folder connectAndOpenFolder(EmailQuerySettings settings) throws jakarta.mail.MessagingException {
		Session session = initSession();
		Store store = session.getStore(properties.getStoreProtocol());
		store.connect(properties.getStoreHost(), utilsService.getUserEmail(settings, properties),
				utilsService.getUserPassword(settings, properties));
		Folder folder = store.getFolder(properties.getDefaultFolder());
		folder.open(Folder.READ_WRITE);
		return folder;
	}

    /**
     * Evict email folder cache.
     */
    @SneakyThrows
	@CacheEvict(value = "emailQueryCache", allEntries = true)
	@Scheduled(fixedRateString = "#{@mailProperties.getStoreTTL()}")
	public void evictEmailFolderCache() {
		log.info("Evict cache for Cacheable email folder");
	}

	private StoreService getThis() {
		return applicationContext.getBean(StoreService.class);
	}

    /**
     * init session connection
     *
     * @return session entity
     */
    public Session initSession() {
		Properties props = new Properties();
		if (properties.getStoreHost() != null) {
			props.setProperty("mail." + properties.getStoreProtocol() + ".ssl.enable", String.valueOf(properties.isSslEnabled()));
		}
		return Session.getInstance(props);
	}

    /**
     * Init transport session session.
     *
     * @return the session
     */
    public Session initTransportSession() {
		Properties props = new Properties();
		if (properties.getTransportHost() != null) {
			props.setProperty("mail.transport.protocol", properties.getTransportProtocol());
			props.setProperty("mail." + properties.getTransportProtocol() + ".host", properties.getTransportHost());
			props.setProperty("mail." + properties.getTransportProtocol() + ".port", String.valueOf(properties.getTransportPort()));
			props.setProperty("mail." + properties.getTransportProtocol() + ".auth", "true");
			props.setProperty("mail." + properties.getTransportProtocol() + ".starttls.enable", String.valueOf(properties.isStartTlsEnable()));
		}
		return Session.getInstance(props);
	}
}
