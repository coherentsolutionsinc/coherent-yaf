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

package com.coherentsolutions.yaf.testng;

/*-
 * #%L
 * Yaf TestNG Module
 * %%
 * Copyright (C) 2020 - 2021 CoherentSolutions
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.coherentsolutions.yaf.core.exception.GeneralYafException;
import com.coherentsolutions.yaf.core.exec.DefaultExecutionService;
import com.coherentsolutions.yaf.core.exec.ExecutionService;
import com.coherentsolutions.yaf.core.exec.model.RunParallelSetup;
import com.coherentsolutions.yaf.core.utils.PropertiesUtils;
import com.coherentsolutions.yaf.core.utils.ServiceProviderUtils;
import com.coherentsolutions.yaf.testng.utils.TestNgUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

import java.util.List;
import java.util.stream.Collectors;

import static com.coherentsolutions.yaf.core.consts.Consts.*;
import static com.coherentsolutions.yaf.testng.utils.TestNgUtils.cloneAndModifySuite;

/**
 * The type Yaf transformer.
 */
@Slf4j
public class YafTransformer implements IAlterSuiteListener {


    /**
     * Try to load file name to grab all environment settings
     * <p>
     * Search this param in (ordered): - java run param (-D...) - environment var - suite param - application
     * properties?
     * <p>
     * if no one file found use default settings
     *
     * @param xmlSuite the xml suite
     * @return env file name
     */
    protected String getEnvFileName(XmlSuite xmlSuite) {
        String val = PropertiesUtils.getPropertyValue(ENV_SETTINGS_FILE, null);
        if (val == null) {
            val = xmlSuite.getParameter(ENV_SETTINGS_FILE);
            if (val == null) {
                val = DEFAULT;
            }
        }
        return val;
    }

    @Override
    public void alter(List<XmlSuite> suites) {
        log.info("Start TestNG transformation");
        if (suites.size() > 1) {
            throw new IllegalStateException("Please provide only one suite!");
        }

        XmlSuite suite = suites.get(0);

        List<XmlSuite> suitesToProcess = suite.getChildSuites().isEmpty() ? List.of(suite) : suite.getChildSuites();

        for (XmlSuite currentSuite : suitesToProcess) {
            processSuite(currentSuite, suites);
        }
    }

    public void processSuite(XmlSuite suite, List<XmlSuite> suites) {
        String suiteEnvSetting = suite.getParameter(ENV_SETTING_PARAM);
        if (suiteEnvSetting == null || suiteEnvSetting.isEmpty()) {
            // this suite is not processed

            try {

                ExecutionService executionService = ServiceProviderUtils.getSingleSingletonServices(ExecutionService.class);

                if (executionService == null) {
                    executionService = DefaultExecutionService.getInstance();
                }
                List<String> configs = executionService.getConfiguration().getEnvironments().keySet()
                        .stream().collect(Collectors.toList());

                RunParallelSetup runSetUp = executionService.getRunParallelSetup();
                TestNgUtils.setSuiteThreadCount(runSetUp.getSuiteThreadsCount());

                String config0 = configs.get(0);

                if (config0 != null) {
                    String suiteName = suite.getName();

                    suite.setThreadCount(runSetUp.getThreadsCount());
                    if (suite.getParallel().equals(XmlSuite.ParallelMode.NONE)) {
                        suite.setParallel(XmlSuite.ParallelMode.getValidParallel(runSetUp.getParallelMode().toString()));
                    }
                    configs = configs.subList(1, configs.size());

                    for (String execConfig : configs) {
                        suites.add(cloneAndModifySuite(suite, execConfig));
                    }

                    // modify initial suite
                    suite.setName(suiteName + ENV_SEPARATOR + config0);
                    suite.getParameters().put(ENV_SETTING_PARAM, config0);

                    suite.getTests().forEach(t -> {
                        t.setName(t.getName() + ENV_SEPARATOR + config0);
                    });
                } else {
                    System.out.println("Single run----");
                }
            } catch (GeneralYafException ex) {
                log.error("Unable to process execution config! " + ex.getMessage());
                System.exit(1);
            }
        }
    }

}
