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
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Grid row.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
public class GridRow<T extends WebElement, R extends GridCell<T>> extends WebComponent<T> {

    /**
     * The Root.
     */
    T root;

    /**
     * The Cells.
     */
    List<R> cells;


//    public <T> T getFirstExtra(Class extraClass) {
//        return (T) cells.get(0).getExtra(extraClass);
//    }
//
//    public <T> T getLastExtra(Class extraClass) {
//        return (T) cells.get(cells.size() - 1).getExtra(extraClass);
//    }

    /**
     * Gets first.
     *
     * @return the first
     */
    public R getFirst() {
        return cells.get(0);
    }

    /**
     * Gets last.
     *
     * @return the last
     */
    public R getLast() {
        return cells.get(cells.size() - 1);
    }
}