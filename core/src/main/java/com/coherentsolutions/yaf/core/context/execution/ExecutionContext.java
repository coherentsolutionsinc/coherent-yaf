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

package com.coherentsolutions.yaf.core.context.execution;


import com.coherentsolutions.yaf.core.config.AppConfig;
import com.coherentsolutions.yaf.core.context.Context;
import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.drivers.model.DriversStore;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.events.EventsService;
import com.coherentsolutions.yaf.core.events.global.ExecutionFinishEvent;
import com.coherentsolutions.yaf.core.events.global.SuiteFinishEvent;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Execution context.
 */
@Component
@Slf4j
public abstract class ExecutionContext extends Context implements Serializable, DisposableBean {

    /**
     * The Properties.
     */
// list of all application properties (files, system properties, env), maven?, suite
    @Setter
    @Getter
    protected Properties properties;

    /**
     * The Test env.
     */
    @Setter
    @Getter
    protected String testEnv; // TODO env?

    /**
     * The Yaf config.
     */
// guidelines
    @Autowired
    @Setter
    @Getter
    protected AppConfig yafConfig;

    /**
     * The Global driver store.
     */
    @Getter
    protected DriversStore globalDriverStore;
    /**
     * The Suite drivers store.
     */
    @Getter
    protected Map<String, DriversStore> suiteDriversStore;

    /**
     * The Events service.
     */
    @Autowired
    protected EventsService eventsService;

    /**
     * Instantiates a new Execution context.
     */
    public ExecutionContext() {
        globalDriverStore = new DriversStore();
        suiteDriversStore = new ConcurrentHashMap<>();
        params = new ConcurrentHashMap<>();
    }

    /**
     * Find inited driver holder by type driver holder.
     *
     * @param suiteName  the suite name
     * @param deviceType the device type
     * @return the driver holder
     */
    public DriverHolder findInitedDriverHolderByType(String suiteName, DeviceType deviceType) {
        DriversStore suiteDS = suiteDriversStore.get(suiteName);
        DriverHolder holder = null;
        if (suiteDS != null) {
            holder = suiteDS.findInitedDriverByType(deviceType);
        }
        if (holder == null) {
            holder = globalDriverStore.findInitedDriverByType(deviceType);
        }
        return holder;
    }

    /**
     * Inited driver holders by suite map.
     *
     * @param suiteName the suite name
     * @return the map
     */
// TODO refactor!!! do not grab all drivers, mark each with thread id
    public Map<Device, DriverHolder> initedDriverHoldersBySuite(String suiteName) {
        DriversStore suiteDS = suiteDriversStore.get(suiteName);
        Map<Device, DriverHolder> holders = new HashMap<>();
        if (suiteDS != null) {
            holders.putAll(suiteDS.getDriverHolderMap());
        }
        holders.putAll(globalDriverStore.getDriverHolderMap());
        return holders;
    }

    /**
     * Gets driver for suite.
     *
     * @param suiteName the suite name
     * @param device    the device
     * @return the driver for suite
     */
    public abstract DriverHolder getDriverForSuite(String suiteName, Device device);

    /**
     * Add driver.
     *
     * @param suiteName    the suite name
     * @param device       the device
     * @param driverHolder the driver holder
     */
    public abstract void addDriver(String suiteName, Device device, DriverHolder driverHolder);

    @Override
    public void destroy() throws Exception {
        clear();
    }

    @Override
    protected void clear() {
        log.debug("Clearing execution context");
        // we need to close all drivers
        globalDriverStore.clearAll();
        suiteDriversStore.values().forEach(ds -> ds.clearAll());
        eventsService.sendEvent(new ExecutionFinishEvent().setEndTime(LocalDateTime.now()));
    }

    /**
     * After suite.
     *
     * @param event the event
     */
    @EventListener
    @Order(1)
    public void afterSuite(SuiteFinishEvent event) {
        DriversStore suiteStore = this.suiteDriversStore.remove(event.getSuiteInfo().getSuiteName());
        if (suiteStore != null) {
            suiteStore.clearAll();
        }
    }
}
