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

package com.coherentsolutions.yaf.selenide;

/*-
 * #%L
 * Yaf Selenide Module
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

import com.codeborne.selenide.Selectors;
import com.coherentsolutions.yaf.core.exception.DriverYafException;
import com.coherentsolutions.yaf.web.pom.shadow.ShadowElement;
import com.coherentsolutions.yaf.web.pom.shadow.ShadowWebComponent;
import org.openqa.selenium.By;

import java.util.List;

/**
 * The type Selenide utils.
 */
public class SelenideUtils {

    /**
     * Gets selector.
     *
     * @param component the component
     * @param element   the element
     * @return the selector
     */
    public static By getSelector(ShadowWebComponent component, ShadowElement element) {
        return getSelector(component, element.value());
    }

    /**
     * Gets selector.
     *
     * @param component       the component
     * @param elementSelector the element selector
     * @return the selector
     */
    public static By getSelector(ShadowWebComponent component, String elementSelector) {

        List<String> shadowRoots = component.getShadowRoots();
        int size = shadowRoots.size();
        if (size > 0) {
            // List<String> reverseList = IntStream.range(0, size).mapToObj(i -> shadowRoots.get(size - 1 - i))
            // .collect(Collectors.toCollection(ArrayList::new));
            String[] roots = new String[size];
            roots = shadowRoots.toArray(roots);
            String mainRoot = roots[0];
            String[] subRoots = new String[0];
            if (size > 1) {
                subRoots = new String[size - 1];
                System.arraycopy(roots, 1, subRoots, 0, subRoots.length);
            }
            return Selectors.shadowCss(elementSelector, mainRoot, subRoots);
        }
        throw new DriverYafException("Unable to get shadow element due root elements are empty!");
    }
}
