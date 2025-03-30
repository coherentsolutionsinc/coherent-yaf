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

package com.coherentsolutions.yaf.core.exec.model;


import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.enums.Browser;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.enums.DriverScope;
import com.coherentsolutions.yaf.core.exec.model.device.BrowserDevice;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The type Environment.
 */
@Data
@Accessors(chain = true)
public class Environment implements Cloneable {

    /**
     * The Name.
     */
    String name;
    /**
     * The Devices.
     */
    List<String> devices;
    /**
     * The Configs.
     */
    Map<String, String> configs;
    /**
     * The Device list.
     */
    @JsonIgnore
    List<Device> deviceList;
    /**
     * The Id.
     */
    @JsonIgnore
    String id;

    /**
     * Gets default env.
     *
     * @return the default env
     */
    public static Environment getDefaultEnv() {
        Environment env = new Environment();
        env.setName(Consts.DEFAULT);
        BrowserDevice browserDevice = new BrowserDevice();
        browserDevice.setName(Consts.DEFAULT).setScope(DriverScope.EXECUTION);
        browserDevice.setBrowser(Browser.CHROME).setBrowserVersion(Consts.LATEST);
        env.addDevice(browserDevice);
        return env;
    }

    /**
     * Find by name device.
     *
     * @param name the name
     * @return the device
     */
    public Device findByName(String name) {
        return findByPredicate(e -> e.getName().equals(name));
    }

    /**
     * Find by type device.
     *
     * @param type the type
     * @return the device
     */
    public Device findByType(DeviceType type) {
        return findByPredicate(e -> e.getType().equals(type));
    }

    /**
     * Find by predicate device.
     *
     * @param predicate the predicate
     * @return the device
     */
    public Device findByPredicate(Predicate<Device> predicate) {
        return deviceList.stream().filter(predicate).findFirst().orElse(null);
    }

    /**
     * Add device.
     *
     * @param device the device
     */
    public void addDevice(Device device) {
        if (deviceList == null) {
            deviceList = new ArrayList<>();
        }
        deviceList.add(device);
    }

    @Override
    public Environment clone() throws CloneNotSupportedException {
        Environment e = (Environment) super.clone();
        // TODO think about deep clone deviceList
        return e;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    @JsonIgnore
    public String getId() {
        if (id == null) {
            id = name + "::" + (configs != null
                    ? Base64.getEncoder().encodeToString(configs.values().stream().collect(Collectors.joining("_")).getBytes())
                    : "");
        }
        return id;
    }
}
