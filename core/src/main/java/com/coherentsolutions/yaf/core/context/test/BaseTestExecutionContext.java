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


import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.events.driver.DriverStartEvent;
import com.coherentsolutions.yaf.core.events.test.TestStartEvent;
import com.coherentsolutions.yaf.core.exception.DriverYafException;
import com.coherentsolutions.yaf.core.exec.model.Environment;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.coherentsolutions.yaf.core.test.model.TestInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * The type Base test execution context.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Component
@org.springframework.context.annotation.Scope(Consts.SCOPE_THREADLOCAL)
@Slf4j
@Order()
// TODO think about this class, may be refactor
public class BaseTestExecutionContext extends TestExecutionContext {

    protected DriverHolder findDriverHolderInContext(Device device) {
        DriverHolder driverHolder = threadDrivesStore.getDriver(device);
        if (driverHolder == null) {
            driverHolder = executionContext.getDriverForSuite(suiteName, device);
        }
        return driverHolder;
    }

    protected void addDriverToContext(Device device, DriverHolder driverHolder) {

        switch (driverHolder.getScope()) {
            case METHOD:
            case CLASS: {
                threadDrivesStore.addDriver(device, driverHolder);
                break;
            }
            case SUITE:
            case EXECUTION: {
                executionContext.addDriver(suiteName, device, driverHolder);
                break;
            }
        }
        usedInTestDrivers.add(driverHolder);
    }

    protected DriverHolder getDriverHolder(DeviceType deviceType, String... name) throws DriverYafException {
        String driverName = name.length != 0 && !StringUtils.isEmpty(name[0]) ? name[0] : null;
        String driverLogString = "driver for " + deviceType + (driverName == null ? "" : " with name " + driverName)
                + " [" + Thread.currentThread().getName() + "]";

        log.trace("Searching " + driverLogString);
        Device device = driverName != null ? env.findByName(driverName) : env.findByType(deviceType);
        // search already initiated driver
        DriverHolder driverHolder = findDriverHolderInContext(device);
        if (driverHolder == null) {
            // no driver found, lets create the new driver
            log.info("Create new " + driverLogString);
            driverHolder = driverManager.getDriver(device);
            if (driverHolder == null) {
                throw new DriverYafException("Unable to create new " + driverLogString);
            }
            // send new driver event
            applicationContext.publishEvent(new DriverStartEvent().setDriverHolder(driverHolder).setTestInfo(testInfo));
        }
        addDriverToContext(device, driverHolder);
        return driverHolder;
    }

    public void initFromTestStartEvent(TestStartEvent event, Environment env) {
        // clear test context, cause it could be populated from prev test,
        // cause it is thread local and our tests could be executed thread per class
        clearBeforeNewTest();
        this.env = env;
        TestInfo testInfo = event.getTestInfo();
        this.suiteName = testInfo.getSuiteInfo().getSuiteName();
        this.testName = testInfo.getTestName();
        this.startTime = event.getTimestamp();
        addStringParams(testInfo.getTestParams());
        this.testInfo = testInfo;

    }

}
