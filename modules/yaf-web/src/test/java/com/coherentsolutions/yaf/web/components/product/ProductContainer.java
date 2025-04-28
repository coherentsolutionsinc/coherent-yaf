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

package com.coherentsolutions.yaf.web.components.product;

import com.coherentsolutions.yaf.web.pom.WebComponent;
import com.coherentsolutions.yaf.web.pom.by.SelectorType;
import com.coherentsolutions.yaf.web.pom.by.YafSetSelector;
import lombok.Data;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * The type Product container.
 */
@Data
public class ProductContainer extends WebComponent<WebElement> {

    @FindBy(css = "#${containerId}")
    private WebElement root;

    @FindBy(xpath = ".//div[@id='${oneId}']/label[@id='${twoId}']")
    private WebElement subtitle;

    private ProductCard productCard;

    @YafSetSelector(field = "uniqueElement", value = "//div[@class=\"product\" and @id=\"na1\"]", type = SelectorType.XPATH)
    private ProductCard productCardWithNewArrivalFirstCardPrice;

    /**
     * The Not inherited root component.
     */
    NotInheritedRootComponent notInheritedRootComponent;

    /**
     * Gets feature card second.
     *
     * @return the feature card second
     */
    public ProductCard getFeatureCardSecond() {
        return createWebComponentBuilder(ProductCard.class)
                .withYafSetSelector("root", "//div[@class=\"product\" and @id=\"fp2\"]", SelectorType.XPATH)
                .build();
    }

    /**
     * Gets new arrival card second.
     *
     * @return the new arrival card second
     */
    public ProductCard getNewArrivalCardSecond() {
        return createWebComponentBuilder(ProductCard.class)
                .withYafSetSelector("root", "//div[@class=\"product\" and @id=\"na2\"]", SelectorType.XPATH)
                .build();
    }

}
