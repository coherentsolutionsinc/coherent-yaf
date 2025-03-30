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

package com.coherentsolutions.yaf.core.bean.factory;


import com.coherentsolutions.yaf.core.bean.field.YafFieldProcessor;
import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.exception.BeanInitYafException;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * The type Yaf context field post processor.
 */
@Data
@Component
@Order()
@Slf4j
public class YafContextFieldPostProcessor implements YafFieldProcessor {

//    /**
//     * The Application context.
//     */
//    @Autowired
//    @Getter
//    protected ApplicationContext applicationContext;
//
//    /**
//     * The Match service.
//     */
//    @Autowired
//    ConditionMatchService matchService;

    /**
     * The Yaf bean utils.
     */
    @Autowired
    YafBeanUtils yafBeanUtils;

    @Override
    public boolean canProcess(Field field, Class fieldType, Object obj, Class objType, List<Annotation> annotations,
                              TestExecutionContext testExecutionContext) {
        return !ClassUtils.isPrimitiveOrWrapper(objType) && !objType.isArray() && !List.class.isAssignableFrom(objType)
                && !Map.class.isAssignableFrom(objType);
    }

    @Override
    public Object processField(Field field, Class fieldType, Object obj, Class objType, List<Annotation> annotations,
                               TestExecutionContext testExecutionContext) throws BeanInitYafException {
        return yafBeanUtils.getYafManagedBeanFromField(field, fieldType, obj, objType, annotations);
    }

//    @Override
//    public Object processField(Field field, Class fieldType, Object obj, Class objType, List<Annotation> annotations,
//                               TestExecutionContext testExecutionContext) throws BeanInitYafException {
//        Map<String, ?> beansOfType = applicationContext.getBeansOfType(fieldType, true, true);
//        Object bean = null;
//        if (beansOfType.size() == 1) {
//            // only one possible impl
//            bean = beansOfType.get(beansOfType.keySet().stream().findFirst().get());
//        } else {
//            // find proper bean according env
//            Environment env = testExecutionContext.getEnv();
//
//            bean = beansOfType.values().parallelStream()
//                    .collect(Collectors.toMap(p -> p,
//                            p -> matchService.matches(p.getClass(), env, testExecutionContext)))
//                    .entrySet().parallelStream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//                    .filter(e -> e.getValue() >= 0).map(e -> e.getKey()).findFirst()
//                    .orElseThrow(() -> new EnvSetupYafException(
//                            "Unable to init " + objType.getCanonicalName() + " due mulpitle variants!"));
//        }
//        if (bean != null) {
//            if (bean instanceof IContextual) {
//                ((IContextual) bean).setTestExecutionContext(testExecutionContext, field, obj, objType, annotations);
//            }
//            // process bean
//            List<Annotation> fieldAnnotations = new ArrayList<>();
//            if (field != null) {
//                fieldAnnotations = Arrays.asList(field.getDeclaredAnnotations());
//            }
//            YafBeanProcessor beanProcessor = applicationContext.getBean(YafBeanProcessor.class);
//            beanProcessor.processBean(bean, fieldAnnotations, testExecutionContext);
//            if (bean instanceof com.coherentsolutions.yaf.core.pom.Component) {
//                ((com.coherentsolutions.yaf.core.pom.Component) bean).configureComponent();
//            }
//        }
//        return bean;
//    }
}
