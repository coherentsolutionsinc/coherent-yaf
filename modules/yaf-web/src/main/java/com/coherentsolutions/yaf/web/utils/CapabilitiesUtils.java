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

package com.coherentsolutions.yaf.web.utils;


import org.openqa.selenium.Dimension;

/**
 * The type Capabilities utils.
 */
public class CapabilitiesUtils {

    /**
     * Convert string format resolution into Dimension object
     * @param resolution string implementation of dimension in {width}x{height} format
     */
    public static Dimension getDimensionFromResolution(String resolution) {
        if (resolution != null && !resolution.isEmpty()) {
            resolution = resolution.toLowerCase();
            String[] res = resolution.split("x");
            try {
                int cW = Integer.parseInt(res[0]);
                int cH = Integer.parseInt(res[1]);
                return new Dimension(cW, cH);
            } catch (NumberFormatException e) {
                //
            }
        }
        return null;
    }
}
