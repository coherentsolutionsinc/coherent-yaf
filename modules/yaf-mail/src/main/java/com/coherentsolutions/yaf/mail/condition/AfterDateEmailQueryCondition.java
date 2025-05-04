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

import java.util.Date;
import java.util.function.Predicate;

/**
 * Email Query condition to filter emails by specified date
 */
@Data
@AllArgsConstructor
public class AfterDateEmailQueryCondition implements EmailQueryCondition<Email> {

    /**
     * The Date.
     */
    Date date;

    /**
     * Method to build Predicate for future email filtering
     *
     * @return Predicate if received date of emails more than provided Date
     */
    @Override
    public Predicate<Email> buildPredicate() {
        return o -> o.getReceivedDate().after(date);
    }

    /**
     * Method for Mailinator API filtering.
     *
     * @return query Condition which need to be added to API call.
     */
    public String getApiCondition() {
        return "receiveDate > " + date;
    }
}
