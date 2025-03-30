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

import com.coherentsolutions.yaf.core.consts.Consts;
import lombok.SneakyThrows;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

/**
 * Service class which helps to get content of attachments in readable format.
 *
 */

@Service
public class EmailContentParser {

	/**
	 * Method which parses PDF attachment content to readable format
	 * @param attachment provide email attachment which jas PDF content type
	 * @return parsef pdf content in String
	 */
	@SneakyThrows
	public static String parsePDFContent (Attachment attachment) {
		String content = null;
		if (attachment.getContentType().toLowerCase().contains(Consts.PDF_TYPE.toLowerCase())) {
			PDDocument document = Loader.loadPDF(attachment.getContentStream().readAllBytes());
			PDFTextStripper pdfTextStripper = new PDFTextStripper();
			content = pdfTextStripper.getText(document);
			document.close();
		}
		return content;
	}

}