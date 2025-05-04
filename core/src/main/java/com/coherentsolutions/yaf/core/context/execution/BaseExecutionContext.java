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


import com.coherentsolutions.yaf.core.config.YafConfig;
import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.drivers.model.DriversStore;
import com.coherentsolutions.yaf.core.enums.DriverScope;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.coherentsolutions.yaf.core.utils.PropertiesUtils;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * The type Base execution context.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Component
@Slf4j
@Order
public class BaseExecutionContext extends ExecutionContext {

    /**
     * Active profile
     */
    @Value("${spring.profiles.active:" + Consts.UNKNOWN + "}")
    private String activeProfile;

    /**
     * Instantiates a new Base execution context.
     *
     * @param beanUtils       the bean utils
     * @param propertiesUtils the properties utils
     */
    public BaseExecutionContext(YafBeanUtils beanUtils, PropertiesUtils propertiesUtils) {
        super();
        // validate execution context
        yafConfig = null;
        try {
            // validate that we have at least one config if not - exit
            yafConfig = beanUtils.getBean(YafConfig.class);
        } catch (Exception ex) {
            log.error("You need to specify at least one bean that implements YafConfig!", ex);
            System.exit(1);
        }

        // populate initial context

        properties = propertiesUtils.getAllProperties();
        testEnv = PropertiesUtils.getPropertyValue(Consts.TESTENV, Consts.DEFAULT);

        // try {
        // context.setEnvironmentSettings(propertiesUtils.readAllEnvSettings((String)
        // props.get(Consts.ENV_SETTINGS_FILE)));
        // } catch (GeneralYafException e) {
        // e.printStackTrace();//TODO!!
        // }
    }

    @Override
    public DriverHolder getDriverForSuite(String suiteName, Device device) {
        DriversStore driversStore = suiteDriversStore.get(suiteName);
        DriverHolder driverHolder = null;
        if (driversStore != null) {
            driverHolder = driversStore.getDriver(device);
        }
        if (driverHolder == null) {
            driverHolder = globalDriverStore.getDriver(device);
        }
        return driverHolder;
    }

    @Override
    public void addDriver(String suiteName, Device device, DriverHolder driverHolder) {
        if (driverHolder.getScope().equals(DriverScope.SUITE)) {
            DriversStore driversStore = suiteDriversStore.get(suiteName);
            if (driversStore == null) {
                driversStore = new DriversStore();
            }
            driversStore.addDriver(device, driverHolder);
            suiteDriversStore.put(suiteName, driversStore);
        } else {
            globalDriverStore.addDriver(device, driverHolder);
        }
    }

}
