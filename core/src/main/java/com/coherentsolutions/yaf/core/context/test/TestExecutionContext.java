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

package com.coherentsolutions.yaf.core.context.test;


import com.coherentsolutions.yaf.core.context.execution.ExecutionContext;
import com.coherentsolutions.yaf.core.drivers.manager.DriverManager;
import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.drivers.model.DriversStore;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.enums.DriverScope;
import com.coherentsolutions.yaf.core.events.test.TestStartEvent;
import com.coherentsolutions.yaf.core.exception.DriverYafException;
import com.coherentsolutions.yaf.core.exec.model.Environment;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.coherentsolutions.yaf.core.processor.test.TestMethodArgProcessor;
import com.coherentsolutions.yaf.core.test.model.TestInfo;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Test execution context.
 */
@SuppressWarnings("ALL")
/**
 * Attention, do not autowire this bean, cause it is thread local!. You can get proper instance from application
 * context.
 */
@Data
@Accessors(chain = true)
// @Component
// @org.springframework.context.annotation.Scope(Consts.SCOPE_THREADLOCAL)
@Slf4j
public abstract class TestExecutionContext implements Serializable, DisposableBean {

    /**
     * The Test name.
     */
    protected String testName;
    /**
     * The Suite name.
     */
    protected String suiteName;

    /**
     * The Test info.
     */
    protected TestInfo testInfo;

    /**
     * The Start time.
     */
    protected Long startTime;
    /**
     * The Env.
     */
    protected Environment env;

    /**
     * The Params.
     */
    protected Map<String, Object> params;

    /**
     * The Execution context.
     */
    @Autowired
    protected ExecutionContext executionContext;

    /**
     * The Application context.
     */
    @Autowired
    protected ApplicationContext applicationContext;

    /**
     * The Driver manager.
     */
    @Autowired
    protected DriverManager driverManager;

    /**
     * The Thread drives store.
     */
    protected DriversStore threadDrivesStore;

    /**
     * The Method arg processors.
     */
    @Autowired
    List<TestMethodArgProcessor> methodArgProcessors;

    /**
     * The Used in test drivers.
     */
    @Getter
    Set<DriverHolder> usedInTestDrivers;

    /**
     * Instantiates a new Test execution context.
     */
    public TestExecutionContext() {
        log.info("Building test context for thread {}", Thread.currentThread().getName());
        threadDrivesStore = new DriversStore();
        params = new HashMap<>();
        usedInTestDrivers = new HashSet<>(); // todo think about other option to mark DH to context
    }

    /**
     * Find driver holder in context driver holder.
     *
     * @param device the device
     * @return the driver holder
     */
    protected abstract DriverHolder findDriverHolderInContext(Device device);

    /**
     * Add driver to context.
     *
     * @param device       the device
     * @param driverHolder the driver holder
     */
    protected abstract void addDriverToContext(Device device, DriverHolder driverHolder);

    /**
     * Gets driver holder.
     *
     * @param deviceType the device type
     * @param name       the name
     * @return the driver holder
     * @throws DriverYafException the driver yaf exception
     */
    protected abstract DriverHolder getDriverHolder(DeviceType deviceType, String... name) throws DriverYafException;

    /**
     * Gets web driver holder.
     *
     * @param name the name
     * @return the web driver holder
     * @throws DriverYafException the driver yaf exception
     */
    public DriverHolder getWebDriverHolder(String... name) throws DriverYafException {
        return getDriverHolder(DeviceType.WEB, name);
    }

    /**
     * Gets mobile driver holder.
     *
     * @param name the name
     * @return the mobile driver holder
     * @throws DriverYafException the driver yaf exception
     */
    public DriverHolder getMobileDriverHolder(String... name) throws DriverYafException {
        return getDriverHolder(DeviceType.MOBILE, name);
    }

    /**
     * Gets desktop driver holder.
     *
     * @param name the name
     * @return the desktop driver holder
     * @throws DriverYafException the driver yaf exception
     */
    public DriverHolder getDesktopDriverHolder(String... name) throws DriverYafException {
        return getDriverHolder(DeviceType.DESKTOP, name);
    }

    /**
     * Build from test start event.
     *
     * @param event the event
     * @param env   the env
     */
    public void buildFromTestStartEvent(TestStartEvent event, Environment env) {
        initFromTestStartEvent(event, env);
        if (testInfo.getTestMethodParams() != null) {
            testInfo.getTestMethodParams().entrySet().forEach(e -> {
                TestMethodArgProcessor processor = methodArgProcessors.stream()
                        .filter(p -> p.canProcess(e.getKey(), e.getValue())).findFirst().orElse(null);
                if (processor != null) {
                    processor.processTestMethodArg(e.getKey(), e.getValue(), this);
                }
            });
        }
    }

    /**
     * Init from test start event.
     *
     * @param event the event
     * @param env   the env
     */
    protected abstract void initFromTestStartEvent(TestStartEvent event, Environment env);

    /**
     * Add prams.
     *
     * @param p the p
     */
    public void addPrams(Map<String, Object> p) {
        params.putAll(p);
    }

    /**
     * Add pram.
     *
     * @param key the key
     * @param val the val
     */
    public void addPram(String key, Object val) {
        params.put(key, val);
    }

    /**
     * Gets param.
     *
     * @param key the key
     * @return the param
     */
    public Object getParam(String key) {
        return params.get(key);
    }

    /**
     * Gets string param.
     *
     * @param key the key
     * @return the string param
     */
    public String getStringParam(String key) {
        return (String) getParam(key);
    }

    /**
     * Add string params.
     *
     * @param stringParams the string params
     */
    public void addStringParams(Map<String, String> stringParams) {
        params.putAll(stringParams.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())));
    }

    /**
     * Find inited driver holder by type driver holder.
     *
     * @param deviceType the device type
     * @return the driver holder
     */
    public DriverHolder findInitedDriverHolderByType(DeviceType deviceType) {
        DriverHolder holder = usedInTestDrivers.stream().filter(d -> d.getDevice().getType().equals(deviceType))
                .findFirst().orElse(null);
        if (holder == null) {
            // lets search in execution context
            holder = executionContext.findInitedDriverHolderByType(suiteName, deviceType);
        }
        return holder;
    }

    // public Stream<Map.Entry<Device, DriverHolder>> initedDriverHolders() {
    // Map<Device, DriverHolder> driverHolderMap = threadDrivesStore.getDriverHolderMap();
    // driverHolderMap.putAll(executionContext.initedDriverHoldersBySuite(suiteName));
    // return driverHolderMap.entrySet().stream();
    // }

    /**
     * Clear before new test.
     */
    protected void clearBeforeNewTest() {
        startTime = null;
        testInfo = null;
        // remove all drivers from testMethodScope
        threadDrivesStore.clearAll(DriverScope.METHOD);
        usedInTestDrivers.clear();
    }

    @Override
    public void destroy() throws Exception {
        clearContext();
    }

    /**
     * Clear context.
     */
    public void clearContext() {
        log.debug("Clearing context");
        // we need to close all drivers
        threadDrivesStore.clearAll();
        params.clear();
        usedInTestDrivers.clear();
    }
}
