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

package com.coherentsolutions.yaf.web.pom;

import org.openqa.selenium.WebElement;

/**
 * The type Web page.
 *
 * @param <T> the type parameter
 */
public class WebPage<T extends WebElement> extends WebComponent<T> {
    @Override
    public T getRoot() {
        return null;
    }

    @Override
    public void setRoot(T root) {

    }

//    @Override
//    protected void init(Field field, Object obj, Class fieldType, List<Annotation> annotations, String driverName) {
//        super.init(field, obj, fieldType, annotations, driverName);
//        if (properties.isUsePageFactory()) {
//            locatorFactory.setWebDriver(getWebDriver());
//            PageFactory.initElements(locatorFactory, this);
//        }
//    }
//
//    /**
//     * Create web component c.
//     *
//     * @param <C> the type parameter
//     * @param cls the cls
//     * @return the c
//     */
//    public <C extends WebComponent> C createWebComponent(Class cls) {
//        return createWebComponent(cls, null);
//    }
//
//    /**
//     * Create web component c.
//     *
//     * @param <C>  the type parameter
//     * @param cls  the cls
//     * @param root the root
//     * @return the c
//     */
//    public <C extends WebComponent> C createWebComponent(Class cls, WebElement root) { // TODO think about merging with component
//        C cc = beanUtils.getBean(cls);
//        cc.setRoot(root);
//        cc.setTestExecutionContext(testExecutionContext, null, cc, null, null);
//        return cc;
//    }
//
//    /**
//     * Get page p.
//     *
//     * @param <P> the type parameter
//     * @param cls the cls
//     * @return the p
//     */
//    public <P extends WebPage> P getPage(Class cls) {
//        P page = beanUtils.getYafManagedBean(cls);
//        return page;
//    }
}
