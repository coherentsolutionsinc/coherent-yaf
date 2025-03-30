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

package com.coherentsolutions.yaf.web.pom.component.grid;

import com.coherentsolutions.yaf.web.pom.WebComponent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.WebElement;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * The type Grid cell.
 *
 * @param <T> the type parameter
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@Order()
public class GridCell<T extends WebElement> extends WebComponent<T> {

    T root;
    /**
     * The Text.
     */
    String text;
    /**
     * The Is header.
     */
    boolean isHeader;

    /**
     * Gets extra.
     *
     * @param <R> the type parameter
     * @param cls the cls
     * @return the extra
     */
    public <R extends GridCellExtra<T>> R getExtra(Class<R> cls) {
        return createWebComponentBuilder(cls).rootElement(getRoot()).build();
    }

    /**
     * Get text string.
     *
     * @return the string
     */
    public String getText() {
        return getRoot().getText();
    }

}