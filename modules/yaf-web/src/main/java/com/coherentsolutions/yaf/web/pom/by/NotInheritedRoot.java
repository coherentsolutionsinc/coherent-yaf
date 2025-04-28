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

package com.coherentsolutions.yaf.web.pom.by;


import java.lang.annotation.*;

/**
 * <p>
 * This annotation can be used to mark Component fields in a {@link com.coherentsolutions.yaf.web.pom.WebPage} class
 * to specify that the root element should not be inherited from a parent class.
 * </p>
 * <p>
 * * Example:
 * * </p>
 * * <pre>
 *  * {@code
 *  * public class SeriousComponent extends WebPage<WebElement> {
 *  *
 *  *     @FindBy
 *  *     private WebElement root;
 *  *
 *  *     @NotInheritedRoot
 *  *     @FindBy
 *  *     private AwesomeComponent awesomeComponent;
 *  * }
 *  * }*
 *  * </pre>
 * <p>
 * Additionally, this annotation can be used on a {@link com.coherentsolutions.yaf.web.pom.WebComponent}'s root element.
 * In this case, the specified {@link com.coherentsolutions.yaf.web.pom.WebComponent} will not inherit any root element
 * and will always use locator specified for its root element.
 * </p>
 * <p>
 * * Example:
 * * </p>
 * * <pre>
 *  * {@code
 *  * public class AwesomeComponent extends WebComponent<WebElement> {
 *  *
 *  *     @NotInheritedRoot
 *  *     @FindBy
 *  *     private WebElement root;
 *  * }
 *  * }*
 *  * </pre>
 * <p>
 * The annotation is retained at runtime and can be used by reflection-based
 * frameworks to alter the behavior of element lookup.
 * </p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NotInheritedRoot {

}
