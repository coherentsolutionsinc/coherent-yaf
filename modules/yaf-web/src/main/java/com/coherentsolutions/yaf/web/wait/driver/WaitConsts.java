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

package com.coherentsolutions.yaf.web.wait.driver;

/**
 * The enum Wait consts.
 */
//todo may be made it dependent on profile or read args from props
public enum WaitConsts {

    /**
     * Super fast wait consts.
     */
    SUPER_FAST(2, 50),
    /**
     * Fast wait consts.
     */
    FAST(5, 50),
    /**
     * Medium wait consts.
     */
    MEDIUM(15, 100),
    /**
     * Slow wait consts.
     */
    SLOW(30, 300),
    /**
     * Extra slow wait consts.
     */
    EXTRA_SLOW(60, 600),
    /**
     * Empty wait consts.
     */
    EMPTY(-1, -1);

    private final int timeOutInSeconds;
    private final int pollIntervalInMillis;

    WaitConsts(int timeOutInSeconds, int pollIntervalInMillis) {
        this.timeOutInSeconds = timeOutInSeconds;
        this.pollIntervalInMillis = pollIntervalInMillis;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name();
    }

    /**
     * Gets time out in seconds.
     *
     * @return the time out in seconds
     */
    public int getTimeOutInSeconds() {
        return timeOutInSeconds;
    }

    /**
     * Gets poll interval in millis.
     *
     * @return the poll interval in millis
     */
    public int getPollIntervalInMillis() {
        return pollIntervalInMillis;
    }

}
