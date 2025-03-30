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


import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.enums.DriverScope;
import com.coherentsolutions.yaf.core.events.EventsService;
import com.coherentsolutions.yaf.core.events.driver.DriverStopEvent;
import com.coherentsolutions.yaf.core.exception.DriverYafException;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.coherentsolutions.yaf.core.utils.StaticContextAccessor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The type Drivers store.
 */
@Data
@Accessors(chain = true)
public class DriversStore {

    /**
     * The Driver holder map.
     */
    Map<Device, DriverHolder> driverHolderMap;

    /**
     * Instantiates a new Drivers store.
     */
    public DriversStore() {
        this.driverHolderMap = new ConcurrentHashMap<>();
    }

    /**
     * Gets required driver.
     *
     * @param device the device
     * @return the required driver
     * @throws DriverYafException the driver yaf exception
     */
    public DriverHolder getRequiredDriver(Device device) throws DriverYafException {
        DriverHolder holder = getDriver(device);
        if (holder == null) {
            throw new DriverYafException("Unable to get driver holder for " + device.getName());
        }
        return holder;
    }

    /**
     * Gets driver.
     *
     * @param device the device
     * @return the driver
     */
    public DriverHolder getDriver(Device device) {
        return driverHolderMap.get(device);
    }

    /**
     * Add driver drivers store.
     *
     * @param device       the device
     * @param driverHolder the driver holder
     * @return the drivers store
     */
    public DriversStore addDriver(Device device, DriverHolder driverHolder) {
        driverHolderMap.put(device, driverHolder);
        return this;
    }

    /**
     * Find inited driver by type driver holder.
     *
     * @param deviceType the device type
     * @return the driver holder
     */
    public DriverHolder findInitedDriverByType(DeviceType deviceType) {
        return driverHolderMap.entrySet().stream().filter(e -> e.getKey().getType().equals(deviceType))
                .map(e -> e.getValue()).findFirst().orElse(null);
    }

    /**
     * Clear all.
     */
    public void clearAll() {
        driverHolderMap.keySet().forEach(key -> {
            removeDriver(key);
        });
    }

    /**
     * Clear all.
     *
     * @param scope the scope
     */
    public void clearAll(DriverScope scope) {
        driverHolderMap.entrySet().stream().filter(e -> e.getValue().getScope().equals(scope))
                .forEach(e -> removeDriver(e.getKey()));
    }

    /**
     * Clear all besides.
     *
     * @param scope the scope
     */
    public void clearAllBesides(DriverScope scope) {
        driverHolderMap.entrySet().stream().filter(e -> !e.getValue().getScope().equals(scope))
                .forEach(e -> removeDriver(e.getKey()));
    }

    /**
     * Remove driver.
     *
     * @param device the device
     */
    public void removeDriver(Device device) {
        DriverHolder holder = driverHolderMap.remove(device);
        EventsService eventsService = StaticContextAccessor.getBean(EventsService.class);
        if (holder != null) {
            holder.quit();

            DriverStopEvent stopEvent = new DriverStopEvent();
            stopEvent.setDriverHolder(holder);
            eventsService.sendEvent(stopEvent);
        }
    }

}
