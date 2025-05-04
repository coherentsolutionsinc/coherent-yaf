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
import com.coherentsolutions.yaf.core.drivers.properties.GeneralDriverProperties;
import com.coherentsolutions.yaf.core.enums.DriverScope;
import com.coherentsolutions.yaf.core.exception.DriverYafException;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Base driver manager.
 */
@Service
@ConditionalOnSingleCandidate(DriverManager.class)
public class BaseDriverManager implements DriverManager {

    /**
     * The Resolvers.
     */
    @Autowired(required = false)
    List<DriverResolver> resolvers;
    /**
     * The Driver properties.
     */
    @Autowired(required = false)
    GeneralDriverProperties driverProperties;


    @Override
    public DriverHolder getDriver(Device device) throws DriverYafException {
        DriverResolver resolver = resolvers.stream().filter(r -> r.canResolve(device)).findFirst().orElse(null);
        if (resolver != null) {
            DriverHolder holder = resolver.initDriver(device);
            if (holder != null) {
                holder.setScope(getDriversScope(holder));
                if (holder.getDevice().getType() == null) {
                    throw new DriverYafException("Please set proper device type for " + device.getName());
                }
                return holder;
            }
        }
        throw new DriverYafException("Unable to init proper driver for device " + device.getName());
    }

    /**
     * Gets drivers scope.
     *
     * @param holder the holder
     * @return the drivers scope
     */
    protected DriverScope getDriversScope(DriverHolder holder) {
        DriverScope scope = holder.getDevice().getScope();
        if (scope == null) {
            scope = holder.getProperties().getScope();
            if (scope == null) {
                scope = DriverScope.EXECUTION;
            }
        }
        return scope;
    }
}
