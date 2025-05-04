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

package com.coherentsolutions.yaf.zephyr.scale.exception;

/**
 * Custom exception for the Zephyr Scale system.
 * <p>
 * This class extends {@link RuntimeException} and provides constructors to create exceptions
 * with a message, a cause, or both. It also includes a constructor for enabling suppression
 * and writable stack trace.
 * </p>
 */
public class YafZephyrScaleException extends RuntimeException {

    /**
     * Instantiates a new Yaf Zephyr Scale exception.
     *
     * @param message the message
     */
    public YafZephyrScaleException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Yaf Zephyr Scale exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public YafZephyrScaleException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Yaf Zephyr Scale exception.
     *
     * @param cause the cause
     */
    public YafZephyrScaleException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Yaf Zephyr Scale exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    protected YafZephyrScaleException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
