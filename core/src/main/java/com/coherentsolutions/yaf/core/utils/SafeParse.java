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


/**
 * The type Safe parse.
 */
public class SafeParse {

    /**
     * Parse int integer.
     *
     * @param str      the str
     * @param defValue the def value
     * @return the integer
     */
    public static Integer parseInt(String str, Integer... defValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            if (defValue.length > 0) {
                return defValue[0];
            }
            return null;
        }
    }

    /**
     * Parse boolean boolean.
     *
     * @param str      the str
     * @param defValue the def value
     * @return the boolean
     */
    public static boolean parseBoolean(String str, boolean... defValue) {
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception nfe) {
            if (defValue.length > 0) {
                return defValue[0];
            }
            return false;
        }
    }
}
