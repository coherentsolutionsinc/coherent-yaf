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

package com.coherentsolutions.yaf.core.drivers.model;


import com.coherentsolutions.yaf.core.drivers.properties.DriverProperties;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.enums.DriverScope;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Driver holder.
 *
 * @param <T> the type parameter
 */
@Data
@Accessors(chain = true)
@Slf4j
public abstract class DriverHolder<T> {

    /**
     * The Device.
     */
    @Setter(AccessLevel.PRIVATE)
    protected Device device;

    /**
     * The Scope.
     */
    protected DriverScope scope;
    /**
     * The Init time.
     */
    @Setter(AccessLevel.PRIVATE)
    protected long initTime;
    /**
     * The Driver.
     */
    protected T driver;

    /**
     * The Properties.
     */
    protected DriverProperties properties;

    /**
     * Instantiates a new Driver holder.
     *
     * @param device     the device
     * @param properties the properties
     */
    public DriverHolder(Device device, DriverProperties properties) {
        this.initTime = System.currentTimeMillis();
        this.device = device;
        this.properties = properties;
    }

//    public <T> T getDriver() {
//        return (T) driver;
//    }

    /**
     * Is boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public boolean is(DeviceType type) {
        return device.is(type);
    }

    /**
     * Is web boolean.
     *
     * @return the boolean
     */
    public boolean isWeb() {
        return is(DeviceType.WEB);
    }

    /**
     * Is mobile boolean.
     *
     * @return the boolean
     */
    public boolean isMobile() {
        return is(DeviceType.MOBILE);
    }

    /**
     * Is desktop boolean.
     *
     * @return the boolean
     */
    public boolean isDesktop() {
        return is(DeviceType.DESKTOP);
    }

    /**
     * Quit.
     */
    public abstract void quit();

}
