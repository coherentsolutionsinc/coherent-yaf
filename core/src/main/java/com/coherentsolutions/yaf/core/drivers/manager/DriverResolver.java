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

package com.coherentsolutions.yaf.core.drivers.manager;


import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import org.springframework.stereotype.Component;

/**
 * The type Driver resolver.
 */
@Component
public abstract class DriverResolver {

    //TODO 112
//    @Autowired(required = false)
//    DriverLogProcessor loggingManager;

    /**
     * Can resolve boolean.
     *
     * @param device the device
     * @return the boolean
     */
    public boolean canResolve(Device device) {
        return device.getType().equals(getResolverType());
    }

    /**
     * Init driver driver holder.
     *
     * @param device the device
     * @return the driver holder
     */
    public abstract DriverHolder initDriver(Device device);

    /**
     * Gets resolver type.
     *
     * @return the resolver type
     */
    public abstract DeviceType getResolverType();

    /**
     * Empty farm boolean.
     *
     * @param device the device
     * @return the boolean
     */
    protected boolean emptyFarm(Device device) {
        return device.getFarm() == null;
    }

    /**
     * Farm name boolean.
     *
     * @param device the device
     * @param name   the name
     * @return the boolean
     */
    protected boolean farmName(Device device, String name) {
        return !emptyFarm(device) && device.getFarm().getName().equalsIgnoreCase(name);
    }

    //TODO 112
//    protected DriverHolder buildHolder(Device device, WebDriver webDriver, DriverProperties properties) {
//        DriverHolder driverHolder = new DriverHolder(device, properties);
//        driverHolder.setDriver(webDriver);
//        return driverHolder;
//    }
//
//    // TODO validate that logging works
//    protected Capabilities buildCapabilitiesFromEnv(Device device) {
//        DesiredCapabilities capabilities = new DesiredCapabilities(device.getCapabilities());
//        if (loggingManager != null) {
//            LoggingPreferences loggingPreferences = loggingManager.getLoggingConfig();
//            if (loggingPreferences != null) {
//                capabilities.setCapability("goog:loggingPrefs", loggingPreferences);
//            }
//        }
//        return capabilities;
//    }

}
