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

package com.coherentsolutions.yaf.mail.domain;

import com.coherentsolutions.yaf.core.config.ModuleProperties;
import com.coherentsolutions.yaf.core.consts.Consts;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Data
@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties(prefix = Consts.FRAMEWORK_NAME + ".mail")
public class MailProperties extends ModuleProperties {

	private String userEmail;
	private String password;

	private String defaultFolder = "inbox";
	private String storeProtocol = "imap";
	private String storeHost;
	private int storePort = 993;
	private boolean sslEnabled = true;
	private int storeTTL = 1800000;

	private String transportProtocol = "smtp";
	private String transportHost;
	private int transportPort = 587;
	private boolean startTlsEnable = true;

	private Properties props;
	private long poolingTime = 2000L;
	private long delayTime = 0L;
	private long timeout = 60000L;
	private boolean parseHtml = false;
	private int defaultPage = 1;
	private int defaultPageSize = 10;
}