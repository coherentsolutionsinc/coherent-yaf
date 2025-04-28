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

package com.coherentsolutions.yaf.web.test.grid;

import com.coherentsolutions.yaf.web.BaseJUnitWebTest;
import com.coherentsolutions.yaf.web.components.grid.CustomExtraCell;
import com.coherentsolutions.yaf.web.components.grid.SampleGrid;
import com.coherentsolutions.yaf.web.pages.GridSamplePage;
import com.coherentsolutions.yaf.web.pom.component.grid.GridCell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The type Grid test.
 */
public class GridTest extends BaseJUnitWebTest {

    /**
     * The Grid sample page.
     */
    @Autowired
	GridSamplePage gridSamplePage;

    /**
     * Test.
     */
    @Test
	public void test() {
		String filePath = getUrl("gridPage");
		WebDriver driver = (WebDriver) getWebDriver().getDriver();
		driver.get(filePath);

		SampleGrid sampleGrid = gridSamplePage.getGrid();
		Assertions.assertEquals(3, sampleGrid.getRowCount());
		String secondHeaderText = sampleGrid.getHeaderElements().get(1).getText();
		Assertions.assertEquals("T2 Header 2", secondHeaderText);
		GridCell<WebElement> cell = sampleGrid.getCellByIndex(1, 1);
		Assertions.assertEquals("T2 Data 1 T2 InnerText", cell.getText());

		CustomExtraCell extra = cell.getExtra(CustomExtraCell.class);
		Assertions.assertEquals("T2 InnerText", extra.getInnerCellElement().getText());
	}
}
