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

package com.coherentsolutions.yaf.web.pom.factory;


import com.coherentsolutions.yaf.web.pom.WebComponent;
import com.coherentsolutions.yaf.web.pom.by.YafSetSelector;
import com.coherentsolutions.yaf.web.utils.YafByUtils;
import com.coherentsolutions.yaf.web.utils.by.YafBy;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * The type Default yaf locator factory.
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class DefaultYafLocatorFactory extends YafLocatorFactory {

    /**
     * Build from yaf by abstract annotations.
     *
     * @param by the by
     * @return the abstract annotations
     */
    public static AbstractAnnotations buildFromYafBy(By by) {
        return new AbstractAnnotations() {
            @Override
            public By buildBy() {
                return by;
            }

            @Override
            public boolean isLookupCached() {
                return false;
            }
        };
    }

    public ElementLocator createLocator(Field field) {
        String fieldName = field.getName();
        if (fieldName.equals("root")) {
            if (parent != null || parentRoot != null) {
                return new DefaultElementLocator(getRoot(), buildFromYafBy(By.xpath(".")));
            }
            return null;
        }

        YafSetSelector yafSetSelector = annotationMap.get(fieldName);
        if (yafSetSelector != null) {
            return new DefaultElementLocator(getRoot(), buildFromYafBy(YafByUtils.getYafByFromField(field, yafSetSelector)));
        } else if (yafSetParam != null && !yafSetParam.isEmpty()) {
            return new DefaultElementLocator(getRoot(), buildFromYafBy(YafByUtils.getYafByFromField(field, yafSetParam)));
        } else {
            return new DefaultElementLocator(getRoot(), field);
        }
    }

    /**
     * Get root search context.
     *
     * @return the search context
     */
    protected SearchContext getRoot() {
        return root == null ? ((parent == null && parentRoot == null) ? webDriver : new ParentSearchContext(parent, parentYafBy, parentRoot)) : root;
    }

    /**
     * The type Parent search context.
     */
    @AllArgsConstructor
    public static class ParentSearchContext implements SearchContext {

        /**
         * The Parent.
         */
        WebComponent parent;

        /**
         * The Parent by.
         */
        YafBy parentBy;

        /**
         * The Parent root.
         */
        WebElement parentRoot;

        private WebElement getRoot() {
            WebElement el = parentRoot != null ? parentRoot : parent.getRoot();
            if (parentBy != null) {
                el = el.findElement(parentBy);
            }
            return el;
        }

        @Override
        public List<WebElement> findElements(By by) {
            return getRoot().findElements(by);
        }

        @Override
        public WebElement findElement(By by) {
            return getRoot().findElement(by);
        }
    }
}