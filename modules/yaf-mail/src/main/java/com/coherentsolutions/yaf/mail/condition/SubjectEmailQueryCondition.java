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

package com.coherentsolutions.yaf.mail.condition;

import com.coherentsolutions.yaf.mail.domain.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Predicate;

/**
 * Email Query condition to filter emails by subject
 */
@Data
@AllArgsConstructor
public class SubjectEmailQueryCondition implements EmailQueryCondition<Email> {

    /**
     * The Subject.
     */
    String subject;

    /**
     * Method to build Predicate for future email filtering
     *
     * @return Predicate if provided emails subjects corresponds the specified subject.
     */
    @Override
    public Predicate<Email> buildPredicate() {
        return o -> o.getSubject().equals(subject);
    }

    /**
     * Method for Mailinator API filtering.
     *
     * @return query Condition which need to be added to API call.
     */
    @Override
    public String getApiCondition() {
        return "subject=" + subject;
    }
}
