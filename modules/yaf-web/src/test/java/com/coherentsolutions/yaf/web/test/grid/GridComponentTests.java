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