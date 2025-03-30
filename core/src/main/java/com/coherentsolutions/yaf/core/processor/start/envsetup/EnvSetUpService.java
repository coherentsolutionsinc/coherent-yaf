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

package com.coherentsolutions.yaf.core.processor.start.envsetup;


import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.events.test.TestStartEvent;
import com.coherentsolutions.yaf.core.exec.model.Environment;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.coherentsolutions.yaf.core.listener.YafListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Env set up service.
 */
@Service
public class EnvSetUpService extends YafListener {

    /**
     * The Prepared devices.
     */
    Map<Device, Map<String, Object>> preparedDevices = new HashMap<>();
    /**
     * The Device setup processors.
     */
    @Autowired(required = false)
    List<DeviceSetupProcessor> deviceSetupProcessors;

    /**
     * Test start.
     *
     * @param event the event
     */
    @EventListener
    @Order(2)
    public void testStart(TestStartEvent event) {
        if (deviceSetupProcessors != null) {
            TestExecutionContext testExecutionContext = getTestExecutionContext();
            Environment env = testExecutionContext.getEnv();
            List<Device> unpreparedDevices = new ArrayList<>();
            env.getDeviceList().forEach(device -> {
                if (preparedDevices.containsKey(device)) {
                    testExecutionContext.setParams(preparedDevices.get(device));
                } else {
                    unpreparedDevices.add(device);
                }
            });
            if (false) { // todo
                if (!unpreparedDevices.isEmpty()) {
                    ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(5);
                    CountDownLatch latch = new CountDownLatch(unpreparedDevices.size());
                    unpreparedDevices.forEach(device -> {
                        try {
                            Map<String, Object> preporatorParams = WORKER_THREAD_POOL.submit(() -> {

                                DeviceSetupProcessor preporator = deviceSetupProcessors.stream()
                                        .filter(p -> p.canHandle(device, testExecutionContext)).findFirst()
                                        .orElse(null);
                                Map<String, Object> res = null;
                                if (preporator != null && device != null) {
                                    res = preporator.prepareDevice(device, testExecutionContext);
                                }
                                // prepapre e
                                latch.countDown();
                                return res;

                            }).get();
                            preparedDevices.put(device, preporatorParams);
                            testExecutionContext.setParams(preporatorParams);
                        } catch (Exception e) {
                            // TODO
                        }
                    });
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
