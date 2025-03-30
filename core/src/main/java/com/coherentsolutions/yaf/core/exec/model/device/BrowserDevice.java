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

package com.coherentsolutions.yaf.core.exec.model.device;


import com.coherentsolutions.yaf.core.enums.Browser;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.enums.OS;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * The type Browser device.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BrowserDevice extends Device {

    /**
     * The Os.
     */
    OS os;
    /**
     * The Browser.
     */
    Browser browser;
    /**
     * The Browser version.
     */
    String browserVersion;
    /**
     * The Os version.
     */
    String osVersion;
    /**
     * The Resolution. Browser window size
     * for ex,
     * resolution: "1024x768"
     */
    String resolution;

    /**
     * Browser options arguments.
     * for ex,
     * args: [
     *       "--start-maximized",
     *       "--allow-file-access-from-files"
     *     ]
     */
    List<String> args;

    /**
     * Get device type
     *
     * @return deviceType
     */
    @Override
    public DeviceType getType() {
        // todo refactor, we could not use constructor or abstract approach due json serialization
        return DeviceType.WEB;
    }
}
