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

package com.coherentsolutions.yaf.core.condition;


import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.enums.YafEnum;
import com.coherentsolutions.yaf.core.exec.model.device.BrowserDevice;
import com.coherentsolutions.yaf.core.exec.model.device.DesktopDevice;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.coherentsolutions.yaf.core.exec.model.device.MobileDevice;
import org.springframework.stereotype.Component;

/**
 * The type Base condition matcher.
 */
@Component
public class BaseConditionMatcher implements ConditionMatcher {

    @Override
    public int matchScore(Device device, YafCondition condition, Class cls, TestExecutionContext testExecutionContext) {

        int score = getCompareEnumScore(device.getType(), condition.deviceType())
                + matchScreenResolution(device, condition, testExecutionContext);
        if (device instanceof BrowserDevice browserDevice) {
            score += getCompareEnumScore(browserDevice.getBrowser(), condition.browser())
                    + getCompareEnumScore(browserDevice.getOs(), condition.os())
                    + getCompareScore(browserDevice.getBrowserVersion(), condition.browserVersion());
        } else if (device instanceof MobileDevice mobileDevice) {
            score += getCompareEnumScore(mobileDevice.getMobileOs(), condition.mobileOs())
                    + getCompareScore(mobileDevice.getMobileOsVersion(), condition.mobileOsVersion())
                    + getCompareScore(mobileDevice.isSimulator(), condition.simulator());
        } else if (device instanceof DesktopDevice desktopDevice) {
            score += getCompareEnumScore(desktopDevice.getOs(), condition.os())
                    + getCompareScore(desktopDevice.getOsVersion(), condition.osVersion());
        }
        return score;
    }

    // todo select proper place for that
    private int matchScreenResolution(Device device, YafCondition condition,
                                      TestExecutionContext testExecutionContext) {
        // TODO !!! Check for mobile /desktop possible way to set resolution
        if (condition.width() == 0 && condition.height() == 0) {
            return 0;
        } else {
            if (device.getCapabilities() == null) {
                return 0;
            } else {
                int score = 0;
                if (condition.width() != 0 && device.getScreenWidth() != null) {
                    score += device.getScreenWidth() <= condition.width() ? 1 : -1;
                }
                if (condition.height() != 0 && device.getScreenHeight() != null) {
                    score += device.getScreenHeight() <= condition.height() ? 1 : -1;
                }
                return score;
            }
        }
    }

    /**
     * Gets compare enum score.
     *
     * @param actual   the actual
     * @param expected the expected
     * @return the compare enum score
     */
    protected int getCompareEnumScore(YafEnum actual, YafEnum expected) {
        if (actual == null || expected.toString().equalsIgnoreCase("other")) {
            return 0;
        }
        return compareBoolean(actual.equals(expected));
    }

    /**
     * Gets compare score.
     *
     * @param actual   the actual
     * @param expected the expected
     * @return the compare score
     */
    protected int getCompareScore(String actual, String expected) {
        if (actual == expected) {
            return 1;
        } else if (expected.equals("")) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Gets compare score.
     *
     * @param actual   the actual
     * @param expected the expected
     * @return the compare score
     */
    protected int getCompareScore(int actual, int expected) {
        if (actual == expected) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Gets compare score.
     *
     * @param actual   the actual
     * @param expected the expected
     * @return the compare score
     */
    protected int getCompareScore(boolean actual, boolean expected) {
        if (actual == expected) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Compare boolean int.
     *
     * @param actual the actual
     * @return the int
     */
    protected int compareBoolean(boolean actual) {
        if (actual) {
            return 1;
        } else {
            return -1;
        }
    }
}
