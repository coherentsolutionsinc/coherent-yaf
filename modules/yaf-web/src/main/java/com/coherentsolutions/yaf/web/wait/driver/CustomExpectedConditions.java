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

package com.coherentsolutions.yaf.web.wait.driver;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Utility class providing custom expected conditions for WebDriver waits.
 * <p>
 * This class contains various methods to create custom expected conditions
 * for checking the presence, absence, and attributes of web elements in the DOM.
 * </p>
 * <p>
 * The methods in this class are designed to be used with Selenium WebDriver
 * to facilitate more complex wait conditions that are not provided by default.
 * </p>
 */
@Slf4j
@UtilityClass
public class CustomExpectedConditions {

    private String getElementLogMessage(List list) {
        return list.size() > 1 ? "elements" : "element";
    }

    /**
     * Returns an ExpectedCondition that checks if all elements located by the given locators are visible.
     *
     * @param locators the list of locators to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> visibilityOfAllElementsLocatedBy(final List<By> locators) {
        return new ExpectedCondition<>() {
            public Boolean apply(WebDriver driver) {
                List<WebElement> elements = locators.stream().map(driver::findElement).toList();
                if (elements.isEmpty()) {
                    return null;
                }
                return elements.stream().allMatch(WebElement::isDisplayed);
            }

            public String toString() {
                return String.format("visibility of %s located by %s", getElementLogMessage(locators), locators);
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if all elements located by the given locators are clickable.
     *
     * @param locators the list of locators to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> elementsToBeClickableForLocators(final List<By> locators) {
        return new ExpectedCondition<>() {
            public Boolean apply(WebDriver driver) {
                List<WebElement> elements = locators.stream().map(driver::findElement).toList();
                if (elements.isEmpty()) {
                    return null;
                }
                return elements.stream().allMatch(element -> ExpectedConditions.elementToBeClickable(element).apply(driver) != null);
            }

            public String toString() {
                return String.format("%s located by %s to be clickable", getElementLogMessage(locators), locators);
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if all elements in the given list are clickable.
     *
     * @param elements the list of elements to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> elementsToBeClickableForWebElements(final List<WebElement> elements) {
        return new ExpectedCondition<>() {
            public Boolean apply(WebDriver driver) {
                return elements.stream().allMatch(element -> ExpectedConditions.elementToBeClickable(element).apply(driver) != null);
            }

            public String toString() {
                return String.format("%s to be clickable %s", getElementLogMessage(elements), elements);
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if all elements located by the given locators are removed from the DOM.
     *
     * @param locators the list of locators to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> allRemovedFromDomForLocators(final List<By> locators) {
        return new ExpectedCondition<>() {
            public Boolean apply(WebDriver driver) {
                return locators.stream().allMatch(x -> removedBy(x, driver));
            }

            public String toString() {
                return String.format("Expected %s located by %s to be removed from the DOM", getElementLogMessage(locators), locators);
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if all elements in the given list are removed from the DOM.
     *
     * @param elements the list of elements to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> allRemovedFromDomForWebElements(final List<WebElement> elements) {
        return new ExpectedCondition<>() {
            public Boolean apply(WebDriver driver) {
                return elements.stream().allMatch(CustomExpectedConditions::elementIsRemoved);
            }

            public String toString() {
                return String.format("specified %s to be removed from the DOM", getElementLogMessage(elements));
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if all elements located by the given locators are present in the DOM.
     *
     * @param locators the list of locators to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> allPresentInDomForLocators(final List<By> locators) {
        return new ExpectedCondition<>() {
            public Boolean apply(WebDriver driver) {
                return locators.stream().noneMatch(x -> removedBy(x, driver));
            }

            public String toString() {
                return String.format("%s located by %s to be present in the DOM", getElementLogMessage(locators), locators);
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if all elements in the given list are present in the DOM.
     *
     * @param elements the list of elements to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> allPresentInDomForWebElements(final List<WebElement> elements) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return elements.stream().noneMatch(CustomExpectedConditions::elementIsRemoved);
            }

            public String toString() {
                return String.format("specified %s to be present in the DOM", getElementLogMessage(elements));
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if any element located by the given locators exists in the DOM.
     *
     * @param locators the list of locators to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> anyPresentInDomForLocators(final List<By> locators) {
        return new ExpectedCondition<>() {
            public Boolean apply(WebDriver driver) {
                for (By el : locators) {
                    if (!driver.findElements(el).isEmpty()) {
                        return true;
                    }
                }
                return false;
            }

            public String toString() {
                return String.format("any %s located by %s to exist in the DOM", getElementLogMessage(locators), locators);
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if any element located by the given locators is removed from the DOM.
     *
     * @param locators the list of locators to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> anyRemovedFromDomForLocators(final List<By> locators) {
        return new ExpectedCondition<>() {
            public Boolean apply(WebDriver driver) {
                for (By el : locators) {
                    if (driver.findElements(el).isEmpty()) {
                        return true;
                    }
                }
                return false;
            }

            public String toString() {
                return String.format("any %s located by %s to be removed from the DOM", getElementLogMessage(locators), locators);
            }
        };
    }

    /**
     * Retrieves the value of the specified attribute or CSS property of the given web element.
     * <p>
     * This method first attempts to get the value of the specified attribute. If the attribute
     * is not present or its value is empty, it then attempts to get the value of the specified
     * CSS property.
     * </p>
     *
     * @param element the web element to retrieve the attribute or CSS value from
     * @param name    the name of the attribute or CSS property
     * @return the value of the attribute or CSS property, or null if neither is present
     */
    private String getAttributeOrCssValue(WebElement element, String name) {
        String value = element.getAttribute(name);
        return (value == null || value.isEmpty()) ? element.getCssValue(name) : value;
    }

    /**
     * Returns an ExpectedCondition that checks if the specified attribute of all elements located by the given locators contains the specified value.
     *
     * @param locators  the list of locators to check
     * @param attribute the attribute to check
     * @param value     the expected value of the attribute
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> attributeToBeForLocators(final List<By> locators, String attribute, String value) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                List<WebElement> elements = locators.stream().map(driver::findElement).toList();
                return elements.stream().allMatch(element -> getAttributeOrCssValue(element, attribute).equals(value));
            }

            public String toString() {
                return String.format("attribute %s of %s located by %s to have the exact value: %s", attribute, getElementLogMessage(locators), locators, value);
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if the specified attribute of all elements in the given list has the specified value.
     *
     * @param elements  the list of elements to check
     * @param attribute the attribute to check
     * @param value     the expected value of the attribute
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> attributeToBeForWebElements(final List<WebElement> elements, String attribute, String value) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return elements.stream().allMatch(element -> getAttributeOrCssValue(element, attribute).equals(value));
            }

            public String toString() {
                return String.format("attribute %s of %s specified elements to have the exact value: %s", attribute, getElementLogMessage(elements), value);
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if the specified attribute of all elements located by the given locators contains the specified value.
     *
     * @param locators  the list of locators to check
     * @param attribute the attribute to check
     * @param value     the expected value of the attribute
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> attributeContainsForLocators(final List<By> locators, String attribute, String value) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                List<WebElement> elements = locators.stream().map(driver::findElement).toList();
                return elements.stream().allMatch(element -> getAttributeOrCssValue(element, attribute).contains(value));
            }

            public String toString() {
                return String.format("attribute %s of %s located by %s to contain the value: %s", attribute, getElementLogMessage(locators), locators, value);
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if the specified attribute of all elements in the given list contains the specified value.
     *
     * @param elements  the list of elements to check
     * @param attribute the attribute to check
     * @param value     the expected value of the attribute
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> attributeContainsForWebElements(final List<WebElement> elements, String attribute, String value) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return elements.stream().allMatch(element -> getAttributeOrCssValue(element, attribute).contains(value));
            }

            public String toString() {
                return String.format("attribute %s of %s specified elements to contain the value: %s", attribute, getElementLogMessage(elements), value);
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if a cookie with the specified name is present.
     *
     * @param cookieName the name of the cookie to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> cookieIsPresent(String cookieName) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                Set<Cookie> cookies = driver.manage().getCookies();
                if (!cookies.isEmpty()) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(cookieName)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            public String toString() {
                return "presence of cookieName" + cookieName;
            }
        };
    }

    /**
     * Checks if the element located by the given locator is removed from the DOM.
     *
     * @param locator the locator to check
     * @param driver  the WebDriver instance
     * @return true if the element is removed, false otherwise
     */
    private boolean removedBy(By locator, WebDriver driver) {
        List<WebElement> elements = driver.findElements(locator);
        return elements.isEmpty();
    }

    /**
     * Checks if the given element is removed from the DOM.
     *
     * @param element the element to check
     * @return true if the element is removed, false otherwise
     */
    private boolean elementIsRemoved(WebElement element) {
        try {
            element.isDisplayed();
            return false;
        } catch (NoSuchElementException ignore) {
            return true;
        } catch (StaleElementReferenceException ignore) {
            //in case if the element is removed but new the same element has been added to DOM
            return false;
        }
    }

    /**
     * Returns an ExpectedCondition that checks if the text of the given web element matches the specified value.
     * <p>
     * This method creates an ExpectedCondition that waits until the text of the specified web element
     * is equal to the given value.
     * </p>
     *
     * @param element the web element to check
     * @param value   the expected text value
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> textToBe(final WebElement element, final String value) {
        return new ExpectedCondition<>() {
            private String currentValue = null;

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    currentValue = element.getText();
                    return currentValue.equals(value);
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("%s element to have text \"%s\". Current text: \"%s\"", element, value, currentValue);
            }
        };
    }

    /**
     * Returns an ExpectedCondition that waits for a value to stabilize over consecutive checks.
     * <p>
     * This method creates an ExpectedCondition that waits until the value supplied by the given function
     * remains the same for at least two consecutive checks.
     * </p>
     *
     * @param <T>           the type of the value to be stabilized
     * @param valueSupplier the function that supplies the value to be checked
     * @param description   a description of the value being checked
     * @return the ExpectedCondition
     */
    public <T> ExpectedCondition<Boolean> waitForStabilization(Function<WebDriver, T> valueSupplier, String description) {
        return new ExpectedCondition<>() {
            private T lastValue = null;
            private int stableCount = 0;

            @Override
            public Boolean apply(WebDriver driver) {
                T currentValue = valueSupplier.apply(driver);

                if (lastValue != null && currentValue.equals(lastValue)) {
                    stableCount++;
                    return stableCount >= 2;
                } else {
                    stableCount = 0;
                }

                lastValue = currentValue;
                return false;
            }

            @Override
            public String toString() {
                return description + " is still updating";
            }
        };
    }

    /**
     * Returns an ExpectedCondition that checks if the position of the element located by the given locator is loaded.
     * <p>
     * This method waits until the position of the element located by the given locator has stabilized,
     * meaning its value has remained the same for at least two consecutive checks.
     * </p>
     *
     * @param by the locator of the element
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> positionIsLoadedForLocator(final By by) {
        return waitForStabilization(
                driver -> driver.findElement(by).getRect(),
                "Position for the element located by " + by
        );
    }

    /**
     * Returns an ExpectedCondition that checks if the position of the given web element is loaded.
     * <p>
     * This method waits until the position of the given web element has stabilized,
     * meaning its value has remained the same for at least two consecutive checks.
     * </p>
     *
     * @param element the web element
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> positionIsLoadedForWebElement(final WebElement element) {
        return waitForStabilization(
                driver -> element.getRect(),
                "Position for the element: " + element
        );
    }

    /**
     * Returns an ExpectedCondition that checks if the specified CSS attribute of the web element located by the given locator is loaded.
     * <p>
     * This method waits until the specified CSS attribute of the web element located by the given locator
     * has stabilized, meaning its value has remained the same for at least two consecutive checks.
     * </p>
     *
     * @param by           the locator of the web element to check
     * @param cssAttribute the CSS attribute to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> attributeIsLoaded(final By by, String cssAttribute) {
        return waitForStabilization(
                driver -> getAttributeOrCssValue(driver.findElement(by), cssAttribute),
                "CSS attribute " + cssAttribute + " for the element located by: " + by
        );
    }

    /**
     * Returns an ExpectedCondition that checks if the specified CSS attribute of the given web element is loaded.
     * <p>
     * This method waits until the specified CSS attribute of the given web element
     * has stabilized, meaning its value has remained the same for at least two consecutive checks.
     * </p>
     *
     * @param element      the web element to check
     * @param cssAttribute the CSS attribute to check
     * @return the ExpectedCondition
     */
    public ExpectedCondition<Boolean> attributeIsLoaded(final WebElement element, String cssAttribute) {
        return waitForStabilization(
                driver -> getAttributeOrCssValue(element, cssAttribute),
                "CSS attribute " + cssAttribute + " for the element: " + element
        );
    }

    /**
     * Returns an ExpectedCondition that checks if the text of the given web element has been updated.
     * <p>
     * This method waits until the text of the given web element has stabilized,
     * meaning its value has remained the same for at least two consecutive checks.
     * </p>
     *
     * @param element the web element to check
     * @return the ExpectedCondition
     */
    public static ExpectedCondition<Boolean> hadElementTextUpdated(final WebElement element) {
        return waitForStabilization(
                driver -> element.getText(),
                "Text for the element: " + element
        );
    }


}