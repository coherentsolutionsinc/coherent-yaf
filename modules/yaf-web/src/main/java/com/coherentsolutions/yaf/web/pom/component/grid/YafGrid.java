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

import com.coherentsolutions.yaf.web.pom.component.YafWebComponent;
import com.coherentsolutions.yaf.web.utils.by.YafBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The interface Yaf grid.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public interface YafGrid<T extends WebElement, R extends GridCell<T>> extends YafWebComponent<T> {


    /**
     * Gets table header.
     *
     * @return the table header
     */
    default GridRow getTableHeader() {
        return buildHeaderRow();
    }

    /**
     * Gets row.
     *
     * @param index the index
     * @return the row
     */
    default GridRow<T, R> getRow(int index) {
        return buildRow(findElement(getRowSelector().addSelector("[" + index + "]")));
    }

    /**
     * Gets row by text.
     *
     * @param text the text
     * @return the row by text
     */
    default GridRow<T, R> getRowByText(String text) {
        return buildRow(findElement(getRowSelector().addSelector("//*[contains(text(),'" + text + "')]/ancestor::tr")));
    }

    /**
     * Gets cell by index.
     *
     * @param rowIndex    the row index
     * @param headerIndex the header index
     * @return the cell by index
     */
    default R getCellByIndex(int rowIndex, int headerIndex) {
        return buildCell(findElement(getRowSelector().addSelector("[" + rowIndex + "]/td[" + headerIndex + "]")));
    }

    /**
     * Gets cell by index and header.
     *
     * @param rowIndex the row index
     * @param header   the header
     * @return the cell by index and header
     */
    default R getCellByIndexAndHeader(int rowIndex, String header) {
        return getCellByIndex(rowIndex, findHeaderIndex(header));
    }

    /**
     * Find header index int.
     *
     * @param headerText the header text
     * @return the int
     */
    default int findHeaderIndex(String headerText) {
        List<GridCell<T>> cells = (List<GridCell<T>>) getTableHeader().getCells();
        headerText = headerText.toLowerCase();
        for (int i = 0; i < cells.size(); i++) {
            String text = cells.get(i).getText().toLowerCase();
            if (text.contains(headerText)) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Is grid contains text boolean.
     *
     * @param text the text
     * @return the boolean
     */
    default boolean isGridContainsText(String text) {
        return getRoot().getAttribute("innerHTML").contains(text);
    }

    /**
     * Gets all rows.
     *
     * @return the all rows
     */
    default List<GridRow<T, R>> getAllRows() {
        return findElements(getRowSelector()).stream().map(this::buildRow).collect(Collectors.toList());
    }

    /**
     * Gets number of rows.
     *
     * @return the number of all rows.
     */
    default int getRowCount() {
        return findElements(getRowSelector()).size();
    }

    /**
     * Gets number of columns.
     *
     * @return the number of all columns.
     */
    default int getColumnCount() {
            return getAllRows().get(0).getCells().size();
    }

    /**
     * Refresh.
     */
    default void refresh() {
        throw new NotImplementedException("Refresh is not implemented!");
    }

    @Override
    default T findElement(YafBy by) {
        return (T) getSearchContext().findElement(by);
    }

    @Override
    default List<T> findElements(YafBy by) {
        return (List<T>) getSearchContext().findElements(by);
    }

    default SearchContext getSearchContext() {
        if (getRoot() == null) {
            return getWebDriver();
        } else {
            return getRoot();
        }
    }

    /**
     * Gets header selector.
     *
     * @return the header selector
     */
    default YafBy getHeaderSelector() {
        return YafBy.xpath("./thead/tr/th");
    }

    /**
     * Gets row selector.
     *
     * @return the row selector
     */
    default YafBy getRowSelector() {
        return YafBy.xpath("./tbody/tr");
    }

    /**
     * Gets cell selector.
     *
     * @return the cell selector
     */
    default YafBy getCellSelector() {
        return YafBy.xpath("*");
    }

    /**
     * Gets header elements.
     *
     * @return the header elements
     */
    default List<T> getHeaderElements() {
        return findElements(getHeaderSelector());
    }

    /**
     * Gets row elements.
     *
     * @param row the row
     * @return the row elements
     */
    default List<T> getRowElements(T row) {
        return findNestedElements(row, getCellSelector());
    }

    private GridRow<T, R> buildAnyRow(T row) {
        GridRow<T, R> gridRow = (GridRow<T, R>) createWebComponentBuilder(GridRow.class).rootElement(row).build();
        //PomGenericUtils.createComponenet(GridRow.class, getYafWebHolder());
        boolean isHeader = row == null;
        List<T> elements = isHeader ? getHeaderElements() : getRowElements(row);
        gridRow.setCells((List<R>) elements
                .stream()
                .map(e -> {
                    var cell = buildCell(e);
                    cell.setHeader(isHeader);
                    return cell;
                }).collect(Collectors.toList()));
        return gridRow;
    }

    /**
     * Build header row grid row.
     *
     * @return the grid row
     */
    default GridRow<T, R> buildHeaderRow() {
        return buildAnyRow(null);
    }

    /**
     * Build row grid row.
     *
     * @param row the row
     * @return the grid row
     */
    default GridRow<T, R> buildRow(T row) {
        return buildAnyRow(row);
    }

    /**
     * Build cell r.
     *
     * @param el the el
     * @return the r
     */
    default R buildCell(T el) {
        // Class cls = PomGenericUtils.getSecondGenericType(getClass()); //TODO exception, add default
        GridCell<T> cell = (GridCell) createWebComponentBuilder(GridCell.class).rootElement(el).build();
        //PomGenericUtils.createComponenet(cls, getYafWebHolder());
        //cell.setRoot(el);
        cell.setText(el.getText());
        return (R) cell;
    }


}
