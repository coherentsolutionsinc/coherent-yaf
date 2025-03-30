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

import com.coherentsolutions.yaf.web.pom.by.NotInheritedRoot;
import com.coherentsolutions.yaf.web.pom.by.SelectorType;
import com.coherentsolutions.yaf.web.pom.by.YafSetSelector;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Data
public class ButtonHolder extends BaseWebComp {

    @FindBy(css = "#${containerId}")
    private WebElement root;

    @YafSetSelector(field = "root", value = ".product-link")
    private Button button;

    @NotInheritedRoot
    @FindBy(xpath = ".//a[@class='product-link']")
    private Button nonInheritedFindByButton;

    @NotInheritedRoot
    @YafSetSelector(field = "root", value = ".//div[@id='fp2']/span[@class='product-price']", type = SelectorType.XPATH)
    private Button nonInheritedYafSetSelectorButton;;

    public Button createButton() {
        return createWebComponentBuilder(Button.class).build();
    }

    public Button createButtonWithRootElement() {
        return createWebComponentBuilder(Button.class).rootElement(root).build();
    }

    public Button createButtonWithRootBy() {
        return createWebComponentBuilder(Button.class).rootBy(By.cssSelector("#na2")).build();
    }
}
