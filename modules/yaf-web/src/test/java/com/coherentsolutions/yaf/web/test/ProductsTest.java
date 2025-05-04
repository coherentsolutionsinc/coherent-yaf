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

package com.coherentsolutions.yaf.web.test;

import com.coherentsolutions.yaf.core.exception.GeneralYafException;
import com.coherentsolutions.yaf.core.utils.FileUtils;
import com.coherentsolutions.yaf.web.BaseJUnitWebTest;
import com.coherentsolutions.yaf.web.components.product.ProductCard;
import com.coherentsolutions.yaf.web.pages.ProductsPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The type Products test.
 */
class ProductsTest extends BaseJUnitWebTest {

	private static String filePath;
    /**
     * The Products page.
     */
    @Autowired
	ProductsPage productsPage;

    /**
     * Sets up.
     */
    @BeforeAll
	public static void setUp() {
		filePath = getUrl("componentsPage");
	}

    /**
     * Open url.
     */
    @BeforeEach
	public void openUrl() {
		((WebDriver) getWebDriver().getDriver()).get(filePath);
	}

    /**
     * Test yaf set param for first container.
     */
    @Test
	public void testYafSetParamForFirstContainer() {
		WebElement featuredProductRoot = productsPage.getFeatureProductsContainer().getRoot();
		WebElement featuredProductSubtitle = productsPage.getFeatureProductsContainer().getSubtitle();
		Assertions.assertAll(() -> {
			Assertions.assertEquals("Available Featured Products list\nProduct A $25.00 View Featured Product 1 Product B $30.00 View Featured Product 2", featuredProductRoot.getText());
			Assertions.assertEquals("Available Featured Products list", featuredProductSubtitle.getText());
		});
	}

    /**
     * Test yaf set param for second container.
     */
    @Test
	public void testYafSetParamForSecondContainer() {
		WebElement newArrivalsRoot = productsPage.getNewArrivalsContainer().getRoot();
		WebElement newArrivalsSubtitle = productsPage.getNewArrivalsContainer().getSubtitle();
		Assertions.assertAll(() -> {
			Assertions.assertEquals("Available New Products list\nProduct X $40.00 View New Products 1 Product Y $50.00 View New Products 2", newArrivalsRoot.getText());
			Assertions.assertEquals("Available New Products list", newArrivalsSubtitle.getText());
		});
	}

    /**
     * Test nested component without annotations.
     */
    @Test
	public void testNestedComponentWithoutAnnotations() {
		ProductCard featuredProduct = productsPage.getFeatureProductsContainer().getProductCard();
		ProductCard newArrivalsFirst = productsPage.getNewArrivalsContainer().getProductCard();
		WebElement newArrivalsProductCardPrice = newArrivalsFirst.getPrice();
		Assertions.assertAll(() -> {
			Assertions.assertEquals("Product A", featuredProduct.getName().getText());
			Assertions.assertEquals("Product X", newArrivalsFirst.getName().getText());
			Assertions.assertEquals("$40.00", newArrivalsProductCardPrice.getText());
		});
	}

    /**
     * Test find by nested component.
     */
    @Test
	public void testFindByNestedComponent() {
		WebElement button = productsPage.getFindByButtonHolder().getButton().getRoot();
		Assertions.assertEquals("View Featured Product 2", button.getText());
	}

    /**
     * Test non inherited root find by nested component.
     */
    @Test
	public void testNonInheritedRootFindByNestedComponent() {
		WebElement nonInheritedFindByButton = productsPage.getFindByButtonHolder().getNonInheritedFindByButton().getRoot();
		Assertions.assertEquals("View Featured Product 1", nonInheritedFindByButton.getText());
	}

    /**
     * Test yaf set selector nested component.
     */
    @Test
	public void testYafSetSelectorNestedComponent() {
		WebElement button = productsPage.getSetSelectorButtonHolder().getButton().getRoot();
		Assertions.assertEquals("View New Products 2", button.getText());
	}

    /**
     * Test non inherited root yaf set selector nested component.
     */
    @Test
	public void testNonInheritedRootYafSetSelectorNestedComponent() {
		WebElement nonInheritedYafSetSelectorButton = productsPage.getFindByButtonHolder().getNonInheritedYafSetSelectorButton().getRoot();
		Assertions.assertEquals("$30.00", nonInheritedYafSetSelectorButton.getText());
	}

    /**
     * Test nested component with not inherited root.
     */
    @Test
	public void testNestedComponentWithNotInheritedRoot() {
		WebElement nestedComponentWithNotInheritedRoot = productsPage.getNotInheritedRootComponent().getRoot();
		Assertions.assertEquals("$30.00", nestedComponentWithNotInheritedRoot.getText());
	}

    /**
     * Test not inherited root component on web page.
     */
    @Test
	public void testNotInheritedRootComponentOnWebPage() {
		WebElement componentWithNotInheritedRootOnWebPage = productsPage.getNewArrivalsContainer().getNotInheritedRootComponent().getRoot();
		Assertions.assertEquals("$30.00", componentWithNotInheritedRootOnWebPage.getText());
	}

    /**
     * Test create component with yaf set selector.
     */
    @Test
	public void testCreateComponentWithYafSetSelector() {
		ProductCard featuredProductSecond = productsPage.getFeatureProductsContainer().getFeatureCardSecond();
		String newArrivalSecondPrice = productsPage.getNewArrivalsContainer().getNewArrivalCardSecond().getPrice().getText();
		String featuredProductPrice = productsPage.getFeaturedProductPrice();
		Assertions.assertAll(() -> {
			Assertions.assertEquals("Product B", featuredProductSecond.getName().getText());
			Assertions.assertEquals("$50.00", newArrivalSecondPrice);
			Assertions.assertEquals("$30.00", featuredProductPrice);
		});
	}

    /**
     * Test create component with yaf set param.
     */
    @Test
	public void testCreateComponentWithYafSetParam() {
		String newArrivalProductPrice = productsPage.getNewArrivalProductPrice();
		Assertions.assertEquals("$40.00", newArrivalProductPrice);
	}

    /**
     * Test setting yaf by with yaf set selector.
     */
    @Test
	public void testSettingYafByWithYafSetSelector() {
		String newArrivalFirstPrice = productsPage.getNewArrivalsContainer().getProductCardWithNewArrivalFirstCardPrice().getPrice().getText();
		Assertions.assertEquals("$40.00", newArrivalFirstPrice);
	}

    /**
     * Test component find by on web page.
     */
    @Test
	public void testComponentFindByOnWebPage() {
		WebElement secondNewProductRoot = productsPage.getButton().getRoot();
		WebElement secondNewProductLink = productsPage.getButton().getLink();
		Assertions.assertAll(() -> {
			Assertions.assertEquals("Product Y $50.00 View New Products 2", secondNewProductRoot.getText());
			Assertions.assertEquals("View New Products 2", secondNewProductLink.getText());
		});
	}

    /**
     * Test component without annotations on web page.
     */
    @Test
	public void testComponentWithoutAnnotationsOnWebPage() {
		WebElement featuredProductRoot = productsPage.getRootLocatorButton().getRoot();
		Assertions.assertEquals("View New Products 1", featuredProductRoot.getText());
	}

    /**
     * Test rootless component.
     */
    @Test
	public void testRootlessComponent() {
		WebElement rootlessComponent = productsPage.getRootlessSample().getButton().getRoot();
		Assertions.assertEquals("$30.00", rootlessComponent.getText());
	}

    /**
     * Test nested component in rootless component.
     */
    @Test
	public void testNestedComponentInRootlessComponent() {
		WebElement nestedRootlessComponent = productsPage.getRootlessSample().getNestedButton().getRoot();
		Assertions.assertEquals("View Featured Product 2", nestedRootlessComponent.getText());
	}
}
