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

package com.coherentsolutions.yaf.core.log.properties;


import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

/**
 * The type Level converter.
 */
@Component
@ConfigurationPropertiesBinding
public class LevelConverter implements Converter<String, Level> {

    @Override
    public Level convert(String from) {
        from = from.toLowerCase();
        Level level;
        switch (from) {
            case "all": {
                level = Level.ALL;
                break;
            }
            case "info": {
                level = Level.INFO;
                break;
            }
            case "config": {
                level = Level.CONFIG;
                break;
            }
            case "severe": {
                level = Level.SEVERE;
                break;
            }
            case "fine": {
                level = Level.FINE;
                break;
            }
            case "warning": {
                level = Level.WARNING;
                break;
            }
            default: {
                level = Level.OFF;
            }
        }
        return level;
    }
}
