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

package com.coherentsolutions.yaf.testng.dataprovider.annotations;

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

import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.testng.dataprovider.DataProcessor;
import com.coherentsolutions.yaf.testng.dataprovider.model.Args;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.annotation.ElementType.METHOD;

/**
 * The interface Random int source.
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
@Source(processor = RandomIntSource.Processor.class)
public @interface RandomIntSource {

    /**
     * Size int.
     *
     * @return the int
     */
    int size() default 1;

    /**
     * Min int.
     *
     * @return the int
     */
    int min() default Integer.MIN_VALUE;

    /**
     * Max int.
     *
     * @return the int
     */
    int max() default Integer.MAX_VALUE;

    /**
     * Only positive boolean.
     *
     * @return the boolean
     */
    boolean onlyPositive() default true;

    /**
     * Common range boolean.
     *
     * @return the boolean
     */
    boolean commonRange() default true;

    /**
     * The type Processor.
     */
    @Component
    class Processor extends DataProcessor<Args> {

        @Override
        public Class getSupportedAnnotationClass() {
            return RandomIntSource.class;
        }

        @Override
        public List<Args> process(Method method, Annotation annotation, TestExecutionContext testExecutionContext) {
            RandomIntSource source = (RandomIntSource) annotation;
            int min = source.commonRange() ? -100 : source.min();
            int max = source.commonRange() ? 100 : source.max();
            min = source.onlyPositive() ? 0 : min;
            return new Random().ints(source.size(), min, max).boxed().map(i -> Args.build(i))
                    .collect(Collectors.toList());
        }
    }
}
