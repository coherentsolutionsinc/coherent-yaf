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

package com.coherentsolutions.yaf.exec.config.json.reader.json;


import com.coherentsolutions.yaf.core.enums.DriverScope;
import com.coherentsolutions.yaf.core.enums.ParallelMode;
import com.coherentsolutions.yaf.core.exception.EnvSetupYafException;
import com.coherentsolutions.yaf.core.exec.model.Config;
import com.coherentsolutions.yaf.core.exec.model.Environment;
import com.coherentsolutions.yaf.core.exec.model.Execution;
import com.coherentsolutions.yaf.core.exec.model.device.Device;
import com.coherentsolutions.yaf.core.exec.model.farm.Farm;
import com.coherentsolutions.yaf.core.utils.FileUtils;
import com.coherentsolutions.yaf.core.utils.PropertiesUtils;
import com.coherentsolutions.yaf.exec.config.json.reader.BaseExecutionReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.coherentsolutions.yaf.core.utils.FileUtils.getResourceJson5File;

/**
 * The type Json files configuration reader.
 */
@Slf4j
public class JsonFilesConfigurationReader extends BaseExecutionReader {

    /**
     * The Execute folder.
     */
    String executeFolder;
    /**
     * The Device folder.
     */
    String deviceFolder;
    /**
     * The Farm folder.
     */
    String farmFolder;
    /**
     * The App folder.
     */
    String appFolder;
    /**
     * The Env folder.
     */
    String envFolder;
    /**
     * The Config folder.
     */
    String configFolder;

    /**
     * The Mapper.
     */
    ObjectMapper mapper;

    /**
     * Instantiates a new Json files configuration reader.
     */
    public JsonFilesConfigurationReader() {
        mapper = FileUtils.getObjectMapper();
        normalizeAndParametrizePaths();
    }

    private void normalizeAndParametrizePaths() {
        executeFolder = PropertiesUtils.getPropertyValue("executeFolder", "execute") + File.separator;
        deviceFolder = PropertiesUtils.getPropertyValue("deviceFolder", "device") + File.separator;
        configFolder = PropertiesUtils.getPropertyValue("configFolder", "config");
        farmFolder = PropertiesUtils.getPropertyValue("farmFolder", "farm");
        appFolder = PropertiesUtils.getPropertyValue("appFolder", "app");
        envFolder = PropertiesUtils.getPropertyValue("envFolder", "env");
    }

    @Override
    protected Execution getExecution(String configurationName) {
        try {

            Execution execution = mapper.readValue(getResourceJson5File(configurationName, executeFolder),
                    Execution.class);

            Map<String, Device> deviceMap = readAllFiles(deviceFolder, new TypeReference<Map<String, Device>>() {
            });
            deviceMap.entrySet().forEach(e -> e.getValue().setName(e.getKey()));

            // load external envs if any
            if (!CollectionUtils.isEmpty(execution.getEnvsNames())) {
                if (getFile(envFolder).exists()) {
                    Map<String, Environment> envMap = readAllFiles(envFolder,
                            new TypeReference<Map<String, Environment>>() {
                            });
                    envMap.entrySet().forEach(e -> e.getValue().setName(e.getKey()));
                    execution.setEnvs(execution.getEnvsNames().stream().map(name -> envMap.get(name))
                            .collect(Collectors.toMap(Environment::getName, e -> e)));
                } else {
                    throw new EnvSetupYafException("Unable to find external env folder");
                }
            }

            // load external config if any
            if (!CollectionUtils.isEmpty(execution.getConfigNames())) {
                if (getFile(configFolder).exists()) {
                    Map<String, Config> configMap = readAllFiles(configFolder,
                            new TypeReference<Map<String, Config>>() {
                            });
                    execution.setConfigs(execution.getConfigNames().stream().map(name -> configMap.get(name))
                            .collect(Collectors.toList()));
                } else {
                    throw new EnvSetupYafException("Unable to find external config folder");
                }
            }

            // process devices
            List<Farm> currentFarms = null;
            if (!CollectionUtils.isEmpty(execution.getFarmsNames())) {
                if (getFile(farmFolder).exists()) {
                    Map<String, Farm> farmMap = readAllFiles(farmFolder, new TypeReference<Map<String, Farm>>() {
                    });
                    farmMap.entrySet().forEach(e -> e.getValue().setName(e.getKey()));
                    currentFarms = execution.getFarmsNames().stream().map(name -> farmMap.get(name))
                            .collect(Collectors.toList());
                } else {
                    throw new EnvSetupYafException("Unable to find external farms folder");
                }
            }
            // make farms final
            List<Farm> farms = currentFarms == null ? new ArrayList<>() : new ArrayList<>(currentFarms);
            ParallelMode parallelMode = execution.getSetup().getParallelMode();

            execution.getEnvs().values().forEach(e -> {
                e.setDeviceList(e.getDevices().stream().map(name -> {
                    Device device = deviceMap.get(name);

                    Farm farm = farms.stream().filter(f -> f.getSupportedTypes().contains(device.getType())).findFirst()
                            .orElse(null);
                    device.setFarm(farm);

                    switch (parallelMode) {
                        case CLASSES -> device.setScope(DriverScope.CLASS);
                        case METHODS -> device.setScope(DriverScope.METHOD);
                    }
                    return device;
                }).collect(Collectors.toList()));
            });

            return execution;
        } catch (Exception e) {
            throw new EnvSetupYafException("Unable to read execution configuration. " + e.getMessage(), e);
        }
    }

//    @Override
//    public Map<String, Map<String, Object>> getConfigData(SpService spService) {
//        Map<String, Map<String, Object>> configData = new HashMap<>();
//        try {
//            File dataFolder = getFile("data");
//            if (dataFolder.exists()) {
//                File[] subFolders = dataFolder.listFiles();
//                for (File sub : subFolders) {
//                    if (sub.isDirectory()) {
//                        String sudDataFolderName = sub.getName();
//                        // get data type reader from SPI
//                        ConfigurationDataReader configurationDataReader = spService
//                                .getConfigurationDataReader(sudDataFolderName);
//                        if (configurationDataReader != null) {
//                            // read proper dir
//                            Map<String, Object> map = readAllFiles("data" + File.separator + sudDataFolderName,
//                                    new TypeReference<Map<String, Object>>() {
//                                    });
//                            configData.put(configurationDataReader.getType(), map);
//                        }
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            // log.error(e.getMessage(), e);
//        }
//        return configData;
//    }

    private <T> Map<String, T> readAllFiles(String dir, TypeReference reference) throws FileNotFoundException {
        Map<String, T> dataMap = new HashMap<>();

        File[] filesList = getFile(dir).listFiles();
        for (File file : filesList) {
            try {
                Map<String, T> map = (Map<String, T>) mapper.readValue(file, reference);
                dataMap.putAll(map);
            } catch (Exception ex) {
                log.error("Could not read file " + file.getAbsolutePath(), ex);
            }
        }
        return dataMap;
    }

    private File getFile(String path) throws FileNotFoundException {
        return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + executeFolder + path);
    }

//    @Override
//    public String getType() {
//        return Consts.JSON;
//    }
}
