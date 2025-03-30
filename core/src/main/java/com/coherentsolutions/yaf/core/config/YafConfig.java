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

package com.coherentsolutions.yaf.core.config;


import com.coherentsolutions.yaf.core.bean.YafBean;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.pom.YafComponent;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The type Yaf config.
 */
@ComponentScan(basePackages = {"com.coherentsolutions"}, includeFilters = @ComponentScan.Filter(YafComponent.class))
@EnableCaching
@EnableAsync
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableScheduling
@Slf4j
@Order()
public class YafConfig implements AppConfig {

    /**
     * The Arguments.
     */
    protected ApplicationArguments arguments;

    /**
     * The Generic application context.
     */
    @Autowired
    GenericApplicationContext genericApplicationContext;

    /**
     * The Messages path.
     */
    @Value("${spring.messages.basename:messages}") // todo config
            String messagesPath;

    /**
     * Instantiates a new Yaf config.
     *
     * @param args the args
     */
    public YafConfig(ApplicationArguments args) {
        log.info("Building general execution config");
        arguments = args;
    }

    /**
     * Task executor task executor.
     *
     * @return the task executor
     */
//
    // @Bean
    // public static CustomScopeConfigurer testScopeConfigurer() {
    // CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
    // customScopeConfigurer.addScope(Consts.SCOPE_THREADLOCAL, new SimpleThreadScope());
    // return customScopeConfigurer;
    // }
    //
    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    /**
     * Faker faker.
     *
     * @return the faker
     */
    @Bean
    public Faker faker() {
        return new Faker(Locale.getDefault());
    }

    /**
     * Supported classes list.
     *
     * @return the list
     */
    @Bean(name = "supportedClasses")
    public List<Class> supportedClasses() {
        List<Class> classes = new ArrayList<>();
        classes.add(YafBean.class);
        return classes;
    }

    /**
     * Object mapper object mapper.
     *
     * @return the object mapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        return objectMapper;
    }


    // // no need, lets use simple cache config
    // @Bean
    // public CacheManager cacheManager() {
    // SimpleCacheManager cacheManager = new SimpleCacheManager();
    // List<Cache> caches = new ArrayList<>();
    // caches.add(new ConcurrentMapCache("yafBeanFields"));
    // caches.add(new ConcurrentMapCache("beanFields"));
    // caches.add(new ConcurrentMapCache("beanNames"));
    // caches.add(new ConcurrentMapCache("classChildren"));
    // caches.add(new ConcurrentMapCache("annotation"));
    // caches.add(new ConcurrentMapCache("annotations"));
    // caches.add(new ConcurrentMapCache("beanWaits"));
    // cacheManager.setCaches(caches);
    // return cacheManager;
    // }

    /**
     * Message source resource bundle message source.
     *
     * @return the resource bundle message source
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames(messagesPath);
        source.setDefaultLocale(Locale.US);
        source.setUseCodeAsDefaultMessage(true);
        source.setFallbackToSystemLocale(true);

        return source;
    }

    /**
     * Locale locale.
     *
     * @return the locale
     */
    @Bean
    public Locale locale() {
        return Locale.getDefault();
    }


    /**
     * The type Device type converter.
     */
    @Component
    @ConfigurationPropertiesBinding
    public static class DeviceTypeConverter implements Converter<String, DeviceType> {

        @Override
        public DeviceType convert(String from) {
            return DeviceType.valueOfName(from.toLowerCase());

        }
    }

    // interceptors

    // @PostConstruct
    // public void registerAspects() {
    //
    // for (MethodCallInterceptor mci: mciList){
    // AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    // pointcut.setExpression(mci.getExpression());
    // Advisor a = new DefaultPointcutAdvisor(pointcut, loggingMethodInterceptor(mci));
    // //genericApplicationContext.registerBean("asd"+Math.random(), Advisor.class, ()->a);
    // beanFactory.registerSingleton("service..."+Math.random(), a);
    // }
    //
    //
    // }

    // @Bean
    // @Qualifier("advicorManager")
    // public String advisorManager(){
    //
    // for (MethodCallInterceptor mci: mciList){
    // AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    // pointcut.setExpression(mci.getExpression());
    // Advisor a = new DefaultPointcutAdvisor(pointcut, loggingMethodInterceptor(mci));
    // genericApplicationContext.registerBean("asd"+Math.random(), Advisor.class, ()->a);
    // }
    // return "dddd";
    // }

    // @Bean
    // public Advisor advisor() {
    // AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    // pointcut.setExpression(methodCallInterceptor.getExpression());
    // return new DefaultPointcutAdvisor(pointcut, loggingMethodInterceptor());
    // }

    // @Autowired
    // MethodCallInterceptor methodCallInterceptor;
    //
    // @Bean
    // public MethodInterceptor loggingMethodInterceptor() {
    // return invocation -> {
    // Map<String, Object> params = methodCallInterceptor.onStart(invocation.getThis(), invocation.getMethod(),
    // invocation.getArguments());
    // Object result = null;
    // Throwable ex = null;
    // try {
    // return invocation.proceed();
    // } catch (Throwable e) {
    // ex = e;
    // throw e;
    // } finally {
    // methodCallInterceptor.onFinish(invocation.getThis(), invocation.getMethod(),
    // invocation.getArguments(), result, ex, params);
    // }
    // };
    // }
    //
    // @Bean
    // public Advisor advisor() {
    // AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    // pointcut.setExpression(methodCallInterceptor.getExpression());
    // return new DefaultPointcutAdvisor(pointcut, loggingMethodInterceptor());
    // }

}
