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

import com.coherentsolutions.yaf.core.exec.DefaultExecutionService;
import com.coherentsolutions.yaf.core.exec.ExecutionService;
import com.coherentsolutions.yaf.core.utils.ServiceProviderUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.engine.support.hierarchical.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class YafTestEngine extends HierarchicalTestEngine<YafTestEngine.Context> {

    public static final String ENGINE_ID = "yaf-engine";
    private ConfigurationParameters configuration;

    public YafTestEngine() {
        log.info("[YAF-ENGINE] ✓ Engine loaded and registered!");
    }

    @Override
    public String getId() {
        return ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest request, UniqueId uniqueId) {
        EngineDescriptor engine = new EngineDescriptor(uniqueId, "Yaf Test Engine");
        configuration = request.getConfigurationParameters();
//        configuration = new CachingJupiterConfiguration(new DefaultJupiterConfiguration(request.getConfigurationParameters()));


        List<YafDiscoverySelector> list = Stream.concat(request.getSelectorsByType(MethodSelector.class).stream().map(s -> new YafDiscoverySelector(null, s)),
                request.getSelectorsByType(ClassSelector.class).stream().map(s -> new YafDiscoverySelector(s, null))).toList();

        list.forEach(s ->
                addClassDescriptor(engine, uniqueId, s.getJavaClass(), s.getMethodName(), null, configuration)
        );

        // Handle UniqueId selections (for rerunning specific tests) //todo
//        request.getSelectorsByType(UniqueIdSelector.class).forEach(sel -> {
//            UniqueId selectedId = sel.getUniqueId();
//
//            if (!selectedId.hasPrefix(uniqueId)) {
//                return;
//            }
//
//            UniqueIdInfo info = parseUniqueId(selectedId);
//            if (info.className != null) {
//                try {
//                    Class<?> clazz = Class.forName(info.className);
//                    addClassDescriptor(engine, uniqueId, clazz, info.methodName, info.variantName);
//                } catch (ClassNotFoundException e) {
//                    System.err.println("[DISCOVERY] Class not found: " + info.className);
//                }
//            }
//        });

        return engine;
    }

    private void addClassDescriptor(EngineDescriptor engine, UniqueId engineId, Class<?> clazz,
                                    String filterMethod, String filterVariant, ConfigurationParameters configuration) {
        UniqueId clsId = engineId.append("class", clazz.getName());

        // Check if already added
        Optional<? extends TestDescriptor> existing = engine.findByUniqueId(clsId);
        ClassDescriptor clsDesc;
        if (existing.isPresent()) {
            clsDesc = (ClassDescriptor) existing.get();
        } else {
            clsDesc = new ClassDescriptor(clsId, clazz, configuration);
            engine.addChild(clsDesc);
        }

        ExecutionService executionService = ServiceProviderUtils.getSingleSingletonServices(ExecutionService.class);

        if (executionService == null) {
            executionService = DefaultExecutionService.getInstance();
        }
        List<String> configs = executionService.getConfiguration().getEnvironments().keySet()
                .stream().toList(); //todo

        // Get variants from class annotation
        List<String> variants = List.of("A", "B");
        //TODO resolveVariants(clazz);

        // Discover all @Test methods
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Test.class)) {
                continue; // Only process @Test methods
            }

            // If filtering by method name, skip others
            if (filterMethod != null && !method.getName().equals(filterMethod)) {
                continue;
            }

            UniqueId methodId = clsId.append("method", method.getName());

            // Check if method descriptor exists
            Optional<? extends TestDescriptor> existingMethod = clsDesc.findByUniqueId(methodId);
            MethodDescriptor methodDesc;
            if (existingMethod.isPresent()) {
                methodDesc = (MethodDescriptor) existingMethod.get();
            } else {
                methodDesc = new MethodDescriptor(methodId, clazz, method, configuration);
                clsDesc.addChild(methodDesc);
            }

            // Create a test descriptor for each variant
            for (String variant : variants) {
                // If filtering by variant, skip others
                if (filterVariant != null && !variant.equals(filterVariant)) {
                    continue;
                }

                UniqueId variantId = methodId.append("variant", variant);

                // Check if variant descriptor exists
                Optional<? extends TestDescriptor> existingVariant = methodDesc.findByUniqueId(variantId);
                if (!existingVariant.isPresent()) {
                    YafTestMethodDescriptor ddd = new YafTestMethodDescriptor(variantId, clazz, method, configuration);
//                    TestMethodTestDescriptor ddd = new TestMethodTestDescriptor(variantId, clazz, method, configuration);
                    VariantTestDescriptor variantDesc = new VariantTestDescriptor(variantId, clazz, method, variant, null);
                    methodDesc.addChild(variantDesc);
                }
            }
        }
    }

    private UniqueIdInfo parseUniqueId(UniqueId uniqueId) {
        UniqueIdInfo info = new UniqueIdInfo();
        List<UniqueId.Segment> segments = uniqueId.getSegments();

        for (UniqueId.Segment segment : segments) {
            switch (segment.getType()) {
                case "class":
                    info.className = segment.getValue();
                    break;
                case "method":
                    info.methodName = segment.getValue();
                    break;
                case "variant":
                    info.variantName = segment.getValue();
                    break;
            }
        }

        return info;
    }

    @Override
    protected Context createExecutionContext(ExecutionRequest request) {
        return new Context(request.getConfigurationParameters());
    }


    // ===== Execution Context =====

    @Override
    protected HierarchicalTestExecutorService createExecutorService(ExecutionRequest request) {
        ConfigurationParameters config = request.getConfigurationParameters();

        // Read standard JUnit Jupiter parallel execution configuration
        boolean parallelEnabled = config.getBoolean("junit.jupiter.execution.parallel.enabled").orElse(false);

        if (!parallelEnabled) {
            System.out.println("[VARIANTS-ENGINE] Sequential execution mode (parallel disabled)");
            return new SameThreadHierarchicalTestExecutorService();
        }

        // Calculate parallelism based on strategy
        int parallelism = calculateParallelism(config);

        System.out.println("[VARIANTS-ENGINE] ⚡ Parallel execution enabled with parallelism: " + parallelism);

        // Use the platform's built-in parallel executor
        return new ForkJoinPoolHierarchicalTestExecutorService(
                new ParallelExecutionConfiguration() {
                    @Override
                    public int getParallelism() {
                        return parallelism;
                    }

                    @Override
                    public int getMinimumRunnable() {
                        return config.get("junit.jupiter.execution.parallel.config.fixed.max-pool-size",
                                Integer::parseInt).orElse(256 + parallelism);
                    }

                    @Override
                    public int getCorePoolSize() {
                        return parallelism;
                    }

                    @Override
                    public int getMaxPoolSize() {
                        return config.get("junit.jupiter.execution.parallel.config.fixed.max-pool-size",
                                Integer::parseInt).orElse(256 + parallelism);
                    }

                    @Override
                    public int getKeepAliveSeconds() {
                        return config.get("junit.jupiter.execution.parallel.config.fixed.keep-alive-seconds",
                                Integer::parseInt).orElse(30);
                    }
                });
    }

    private int calculateParallelism(ConfigurationParameters config) {
        // Read strategy (fixed or dynamic)
        String strategy = config.get("junit.jupiter.execution.parallel.config.strategy").orElse("dynamic");

        if ("fixed".equals(strategy)) {
            // Use fixed parallelism
            return config.get("junit.jupiter.execution.parallel.config.fixed.parallelism", Integer::parseInt)
                    .orElse(Runtime.getRuntime().availableProcessors());
        } else {
            // Dynamic strategy: parallelism = cores * factor
            double factor = config.get("junit.jupiter.execution.parallel.config.dynamic.factor", Double::parseDouble)
                    .orElse(1.0);
            int cores = Runtime.getRuntime().availableProcessors();
            return Math.max(1, (int) Math.ceil(cores * factor));
        }
    }

    private static class UniqueIdInfo {
        String className;
        String methodName;
        String variantName;
    }

    static class Context implements EngineExecutionContext {
        final ConfigurationParameters configuration;
        final String classesMode;
        final String methodsMode;

        public Context(ConfigurationParameters configuration) {
            this.configuration = configuration;

            // Read execution mode configuration (concurrent, same_thread)
            String defaultMode = configuration.get("junit.jupiter.execution.parallel.mode.default").orElse("same_thread");
            this.classesMode = configuration.get("junit.jupiter.execution.parallel.mode.classes.default")
                    .orElse(defaultMode);
            this.methodsMode = configuration.get("junit.jupiter.execution.parallel.mode.default")
                    .orElse(defaultMode);
        }

        Node.ExecutionMode getClassesExecutionMode() {
            return "concurrent".equalsIgnoreCase(classesMode) ? Node.ExecutionMode.CONCURRENT : Node.ExecutionMode.SAME_THREAD;
        }

        Node.ExecutionMode getMethodsExecutionMode() {
            return "concurrent".equalsIgnoreCase(methodsMode) ? Node.ExecutionMode.CONCURRENT : Node.ExecutionMode.SAME_THREAD;
        }
    }

    // ===== Test Descriptors =====

    static class ClassDescriptor extends AbstractTestDescriptor implements Node<Context> {
        final Class<?> testClass;
        final ConfigurationParameters configuration;

        ClassDescriptor(UniqueId id, Class<?> testClass, ConfigurationParameters configuration) {
            super(id, testClass.getSimpleName(), ClassSource.from(testClass));
            this.testClass = testClass;
            this.configuration = configuration;
        }

        @Override
        public Type getType() {
            return Type.CONTAINER;
        }
    }

    static class MethodDescriptor extends AbstractTestDescriptor implements Node<Context> {
        final Class<?> testClass;
        final Method method;
        final ConfigurationParameters configuration;

        MethodDescriptor(UniqueId id, Class<?> testClass, Method method, ConfigurationParameters configuration) {
            super(id, method.getName(), MethodSource.from(method));
            this.testClass = testClass;
            this.method = method;
            this.configuration = configuration;
        }

        @Override
        public Type getType() {
            return Type.CONTAINER; // Container for variant descriptors
        }
    }

    static class YafTestMethodDescriptor extends AbstractTestDescriptor implements Node<Context> {
        final Class<?> testClass;
        final Method method;
        final ConfigurationParameters configuration;

        YafTestMethodDescriptor(UniqueId id, Class<?> testClass, Method method, ConfigurationParameters configuration) {
            super(id, method.getName(), MethodSource.from(method));
            this.testClass = testClass;
            this.method = method;
            this.configuration = configuration;
        }

        @Override
        public Type getType() {
            return Type.CONTAINER; // Container for variant descriptors
        }
    }

    @Slf4j
    static class VariantTestDescriptor extends AbstractTestDescriptor implements Node<Context> {
        final Class<?> testClass;
        final Method method;
        final String envName;

        YafTestMethodDescriptor ddd;

        VariantTestDescriptor(UniqueId id, Class<?> testClass, Method method, String envName, YafTestMethodDescriptor ddd) {
            super(id, method.getName() + method.getName() + "[" + envName + "]", MethodSource.from(method));//todo revalidate double naming
            this.ddd = ddd;
            this.testClass = testClass;
            this.method = method;
            this.envName = envName;
        }

        @Override
        public Type getType() {
            return Type.TEST;
        }

        @Override
        public SkipResult shouldBeSkipped(Context context) {
            //TODO support skip
            return SkipResult.doNotSkip();
        }

        @Override
        public Context execute(Context context, DynamicTestExecutor executor) throws Exception {
            log.debug("[EXECUTE] Running " + method.getName() + "[" + envName + "]");

            SpringHarness spring = new SpringHarness(testClass);
            spring.beforeAll();
            Object testInstance = testClass.getDeclaredConstructor().newInstance();
            spring.prepare(testInstance);


            Throwable thrown = null;
            try {
                spring.beforeEach(testInstance, method);
                // Make method accessible if needed
                if (!method.canAccess(testInstance)) {
                    method.setAccessible(true);
                }

                try {
                    Current.set(envName);
                    //executor.execute(ddd);
                    method.invoke(testInstance);
                } finally {
                    // Clean up
                    Current.clear();
                }// your invocation
            } catch (Throwable t) {
                thrown = t;
                // report failure to platform
            } finally {
                spring.afterEach(testInstance, method, thrown);
            }

            spring.afterAll();


//            // Create test instance
//            Object testInstance = testClass.getDeclaredConstructor().newInstance();
//
//            // Make method accessible if needed
//            if (!method.canAccess(testInstance)) {
//                method.setAccessible(true);
//            }
//
//            try {
//                Current.set(envName);
//                //executor.execute(ddd);
//                method.invoke(testInstance);
//            } finally {
//                // Clean up
//                Current.clear();
//            }
            return context;
        }
    }
}

