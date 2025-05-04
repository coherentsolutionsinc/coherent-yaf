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

package com.coherentsolutions.yaf.core.utils;


import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * The type Date utils.
 */
@Service
public class DateUtils {

    /**
     * Gets current date time by pattern and zone.
     *
     * @param pattern the pattern
     * @param zone    the zone
     * @return the current date time by pattern and zone
     */
    public static String getCurrentDateTimeByPatternAndZone(String pattern, String zone) {
        ZonedDateTime now = isNotEmpty(zone) ? ZonedDateTime.now(ZoneId.of(zone)) : ZonedDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Gets current date time by pattern.
     *
     * @param pattern the pattern
     * @return the current date time by pattern
     */
    public static String getCurrentDateTimeByPattern(String pattern) {
        return getCurrentDateTimeByPatternAndZone(pattern, null);
    }

    /**
     * Gets date from string by pattern.
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the date from string by pattern
     */
    public static Date getDateFromStringByPattern(String date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Gets month value from string by pattern.
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the month value from string by pattern
     */
    public static int getMonthValueFromStringByPattern(String date, String pattern) {
        return getDateFromStringByPattern(date, pattern).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .getMonthValue();
    }
}
