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

package com.coherentsolutions.yaf.web.pages;

import com.coherentsolutions.yaf.web.components.product.*;
import com.coherentsolutions.yaf.web.pom.WebPage;
import com.coherentsolutions.yaf.web.pom.by.SelectorType;
import com.coherentsolutions.yaf.web.pom.by.YafSetParam;
import com.coherentsolutions.yaf.web.pom.by.YafSetSelector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class ProductsPage extends WebPage<WebElement> {

    @YafSetParam(key = "containerId", value = "featuredProducts")
    @YafSetParam(key = "oneId", value = "sub1")
    @YafSetParam(key = "twoId", value = "lab1")
    private ProductContainer featureProductsContainer;

    @YafSetParam(key = "containerId", value = "newArrivals")
    @YafSetParam(key = "oneId", value = "sub2")
    @YafSetParam(key = "twoId", value = "lab2")
    private ProductContainer newArrivalsContainer;

    @FindBy(css = "#newArrivals #na2")
    Button button;

    Button rootLocatorButton;

    @FindBy(css = "#fp2")
    ButtonHolder findByButtonHolder;

    @YafSetSelector(field = "root", value = "#na2")
    ButtonHolder setSelectorButtonHolder;

    @YafSetSelector(field = "uniqueLocator", value = "#featuredProducts #fp2 .product-price")
    RootlessSample rootlessSample;

    NotInheritedRootComponent notInheritedRootComponent;

    public String getFeaturedProductPrice() {
        ProductCard productCard = createWebComponentBuilder(ProductCard.class)
                .withYafSetSelector("uniqueElement", "//div[@class=\"product\" and @id=\"fp2\"]", SelectorType.XPATH)
                .build();
        return productCard.getProductPrice();
    }

    public String getNewArrivalProductPrice() {
        ProductContainer productContainer = createWebComponentBuilder(ProductContainer.class)
                .withYafSetParams(Map.of("containerId", "newArrivals"))
                .build();
        return productContainer.getProductCard().getPrice().getText();
    }

}
