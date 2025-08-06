/*
 * MIT License
 *
 * Copyright (c) 2021 - 2025 Coherent Solutions Inc.
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
import com.coherentsolutions.yaf.web.components.nestedGrid.NestedGrid;
import com.coherentsolutions.yaf.web.pages.GridSamplePage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The type Grid component tests.
 */
public class GridComponentTests extends BaseJUnitWebTest {

    /**
     * The File path.
     */
    static String filePath;
    /**
     * The Grid sample page.
     */
    @Autowired
	GridSamplePage gridSamplePage;

    /**
     * Sets up.
     */
    @BeforeAll
	public static void setUp() {
		filePath = getUrl("nestedGridComponent");
	}

    /**
     * Open url.
     */
    @BeforeEach
	public void openUrl() {
		((WebDriver) getWebDriver().getDriver()).get(filePath);
	}

    /**
     * Check default grid initialization.
     */
    @Test
	public void checkDefaultGridInitialization() {
		NestedGrid nestedSampleGrid = gridSamplePage.getNestedGrid().getNestedSampleGrid();
		String headerTextForDefaultLocator = nestedSampleGrid.getHeaderElements().get(1).getText();
		Assertions.assertEquals("T2 Header 2", headerTextForDefaultLocator);
	}

    /**
     * Check custom locator grid initialization.
     */
    @Test
	public void checkCustomLocatorGridInitialization() {
		NestedGrid nestedCustomGrid = gridSamplePage.getNestedGrid().getNestedCustomGrid();
		String headerTextForCustomLocator = nestedCustomGrid.getHeaderElements().get(1).getText();
		Assertions.assertEquals("T2 Header 2", headerTextForCustomLocator);
	}
}