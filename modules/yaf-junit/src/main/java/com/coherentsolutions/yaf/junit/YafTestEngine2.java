/*
 * MIT License
 *
 * Copyright (c) 2021 - 2025 Coherent Solutions Inc.
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

package com.coherentsolutions.yaf.junit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.engine.config.DefaultJupiterConfiguration;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor;
import org.junit.jupiter.engine.discovery.DiscoverySelectorResolver;
import org.junit.jupiter.engine.execution.JupiterEngineExecutionContext;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.config.PrefixedConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ForkJoinPoolHierarchicalTestExecutorService;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Slf4j
public class YafTestEngine2 extends HierarchicalTestEngine<JupiterEngineExecutionContext> {

    public static final String ENGINE_ID = "yaf-engine";
    JupiterConfiguration configuration;

    public YafTestEngine2() {
        log.info("[YAF-ENGINE] ✓ Engine loaded and registered!");
    }

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    protected HierarchicalTestExecutorService createExecutorService(ExecutionRequest request) {
        if (configuration.isParallelExecutionEnabled()) {
            return new ForkJoinPoolHierarchicalTestExecutorService(new PrefixedConfigurationParameters(
                    request.getConfigurationParameters(), "junit.jupiter.execution.parallel.config."));
        }
        return super.createExecutorService(request);
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest request, UniqueId uniqueId) {
        configuration = //new CachingJupiterConfiguration(
                new DefaultJupiterConfiguration(request.getConfigurationParameters());
        //);
        JupiterEngineDescriptor engine = new JupiterEngineDescriptor(uniqueId, configuration);
        new DiscoverySelectorResolver().resolveSelectors(request, engine);

        Set<? extends TestDescriptor> children = engine.getChildren();
        for (TestDescriptor classChild : children) {
            //classes
            List<TestDescriptor> toRemove = new ArrayList<>();
            List<TestDescriptor> toAdd = new ArrayList<>();
            for (TestDescriptor methodChild : classChild.getChildren()) {
                TestMethodTestDescriptor tmtd = (TestMethodTestDescriptor) methodChild;
                toRemove.add(tmtd);
                Variants.ALL.forEach(v -> {
                    TMTD variantChild = new TMTD(tmtd, configuration, v);
                    variantChild.modifyDisplayName();
                    toAdd.add(variantChild);
                });
            }
            toRemove.stream().forEach(tmtd -> {
                classChild.removeChild(tmtd);
            });
            toAdd.stream().forEach(tmtd -> {
                classChild.addChild(tmtd);
            });
        }
        return engine;
    }

    @Override
    protected JupiterEngineExecutionContext createExecutionContext(ExecutionRequest request) {
        return new JupiterEngineExecutionContext(request.getEngineExecutionListener(),
                getJupiterConfiguration(request));
    }

    private JupiterConfiguration getJupiterConfiguration(ExecutionRequest request) {
        JupiterEngineDescriptor engineDescriptor = (JupiterEngineDescriptor) request.getRootTestDescriptor();
        return engineDescriptor.getConfiguration();
    }
}

