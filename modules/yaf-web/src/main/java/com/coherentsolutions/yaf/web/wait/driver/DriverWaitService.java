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

import com.coherentsolutions.yaf.core.context.test.TestExecutionContext;
import com.coherentsolutions.yaf.core.drivers.model.DriverHolder;
import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.exception.DriverYafException;
import com.coherentsolutions.yaf.core.utils.YafBeanUtils;
import com.coherentsolutions.yaf.web.wait.driver.waits.attribute.AttributeWait;
import com.coherentsolutions.yaf.web.wait.driver.waits.clickable.ClickableWait;
import com.coherentsolutions.yaf.web.wait.driver.waits.cookie.CookieWait;
import com.coherentsolutions.yaf.web.wait.driver.waits.load.LoadWait;
import com.coherentsolutions.yaf.web.wait.driver.waits.presence.PresenceWait;
import com.coherentsolutions.yaf.web.wait.driver.waits.text.TextWait;
import com.coherentsolutions.yaf.web.wait.driver.waits.visibility.VisibilityWait;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * The type Driver wait service.
 */
@Service
@Order()
@Slf4j
public class DriverWaitService {

    /**
     * The Bean utils.
     */
    @Autowired
    YafBeanUtils beanUtils;

    /**
     * The Wait properties.
     */
    @Autowired
    WaitProperties waitProperties;

    /**
     * General wait r.
     *
     * @param <R>   the type parameter
     * @param waits the waits
     * @return the r
     */
    protected <R> R generalWait(BaseWait... waits) {
        if (waits.length > 1) {
            // todo may be add concurrency?
            for (BaseWait w : waits) {
                waitSingleWait(w);
            }
            return null; // todo what to return????
        } else {
            return waitSingleWait(waits[0]);
        }
    }

    /**
     * Wait for r.
     *
     * @param <R>   the type parameter
     * @param waits the waits
     * @return the r
     */
    public <R> R waitFor(BaseWait... waits) {
        try {
            return generalWait(waits);
        } catch (Exception ex) {
            BaseWait bw = Arrays.stream(waits).filter(w -> w.shouldThrowAssertError(ex)).findAny().orElse(null);
            if (bw != null) {
                throw new AssertionError(ex.getMessage());
            } else {
                throw ex;
            }
        }
    }

    /**
     * Wait for bool boolean.
     *
     * @param waits the waits
     * @return the boolean
     */
    public boolean waitForBool(BaseWait... waits) {
        try {
            generalWait(waits);
            return true;
        } catch (Exception ex) {
            log.debug("Skip wait exception {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Wait single wait r.
     *
     * @param <R>  the type parameter
     * @param wait the wait
     * @return the r
     */
    protected <R> R waitSingleWait(BaseWait wait) {
        WebDriverWait webDriverWait = buildWebDriverWait(wait);
        try {
            R r = wait.waitWithDriverWait(webDriverWait);
            return r;
        } catch (Exception ex) {
            if (wait.canRetry(ex)) {
                wait.getRetries().incrementAndGet();
                return waitSingleWait(wait);
            } else {
                throw ex;
            }
        }
    }

    /**
     * Build web driver wait web driver wait.
     *
     * @param wait the wait
     * @return the web driver wait
     */
    protected WebDriverWait buildWebDriverWait(BaseWait wait) {
        WebDriver driver = wait.getDriver();
        if (driver == null) {
            TestExecutionContext testExecutionContext = beanUtils.getBean(TestExecutionContext.class);
            DeviceType deviceType = wait.getDeviceType();
            if (deviceType == null) {
                deviceType = DeviceType.WEB;
            }
            DriverHolder driverHolder = testExecutionContext.findInitedDriverHolderByType(deviceType);
            if (driverHolder != null) {
                driver = (WebDriver) driverHolder.getDriver();
            }
            if (driver == null) {
                throw new DriverYafException("Could not get driver for wait! " + this);
            }
        }
        WaitConsts waitConsts = wait.getWaitConsts();
        if (waitConsts == null || waitConsts.equals(WaitConsts.EMPTY)) {
            return createWebDriverWait(driver, waitProperties.getExplicit().getTimeOut(), waitProperties.getExplicit().getInterval());
        } else {
            return createWebDriverWait(driver, waitConsts.getTimeOutInSeconds(), waitConsts.getPollIntervalInMillis());
        }
    }

    private WebDriverWait createWebDriverWait(WebDriver driver, int interval, int pooling) {
        return new WebDriverWait(driver, Duration.ofSeconds((long) interval * waitProperties.getWaitMultiplication()),
                Duration.ofMillis((long) pooling * waitProperties.getWaitMultiplication()));
    }

    /**
     * Waits for the specified attribute of the given web element to have the specified value.
     *
     * @param element    the web element
     * @param attribute  the attribute to check
     * @param value      the expected value of the attribute
     * @param waitConsts optional wait constants
     */
    public void attributeToBe(WebElement element, String attribute, String value, WaitConsts... waitConsts) {
        waitFor(new AttributeWait(element, waitConsts).attribute(attribute).value(value).presence(true));
    }

    /**
     * Waits for the specified attribute of the web element located by the given locator to have the specified value.
     *
     * @param by         the locator of the web element
     * @param attribute  the attribute to check
     * @param value      the expected value of the attribute
     * @param waitConsts optional wait constants
     */
    public void attributeToBe(By by, String attribute, String value, WaitConsts... waitConsts) {
        waitFor(new AttributeWait(by, waitConsts).attribute(attribute).value(value).presence(true));
    }

    /**
     * Waits for the specified attribute of the given web element to contain the specified value.
     *
     * @param element    the web element
     * @param attribute  the attribute to check
     * @param value      the value to be contained in the attribute
     * @param waitConsts optional wait constants
     */
    public void attributeContains(WebElement element, String attribute, String value, WaitConsts... waitConsts) {
        waitFor(new AttributeWait(element, waitConsts).attribute(attribute).value(value).presence(true).exactMatch(false));
    }

    /**
     * Waits for the specified attribute of the web element located by the given locator to contain the specified value.
     *
     * @param by         the locator of the web element
     * @param attribute  the attribute to check
     * @param value      the value to be contained in the attribute
     * @param waitConsts optional wait constants
     */
    public void attributeContains(By by, String attribute, String value, WaitConsts... waitConsts) {
        waitFor(new AttributeWait(by, waitConsts).attribute(attribute).value(value).presence(true).exactMatch(false));
    }

    /**
     * Waits for the specified attribute of the given web element to not have the specified value.
     *
     * @param element    the web element
     * @param attribute  the attribute to check
     * @param value      the value that the attribute should not have
     * @param waitConsts optional wait constants
     */
    public void attributeNotToBe(WebElement element, String attribute, String value, WaitConsts... waitConsts) {
        waitFor(new AttributeWait(element, waitConsts).attribute(attribute).value(value).presence(false));
    }

    /**
     * Waits for the specified attribute of the web element located by the given locator to not have the specified value.
     *
     * @param by         the locator of the web element
     * @param attribute  the attribute to check
     * @param value      the value that the attribute should not have
     * @param waitConsts optional wait constants
     */
    public void attributeNotToBe(By by, String attribute, String value, WaitConsts... waitConsts) {
        waitFor(new AttributeWait(by, waitConsts).attribute(attribute).value(value).presence(false));
    }

    /**
     * Waits for the specified attribute of the given web element to not contain the specified value.
     *
     * @param element    the web element
     * @param attribute  the attribute to check
     * @param value      the value that the attribute should not have
     * @param waitConsts optional wait constants
     */
    public void attributeNotContains(WebElement element, String attribute, String value, WaitConsts... waitConsts) {
        waitFor(new AttributeWait(element, waitConsts).attribute(attribute).value(value).presence(false).exactMatch(false));
    }

    /**
     * Waits for the specified attribute of the web element located by the given locator to not contain the specified value.
     *
     * @param by         the locator of the web element
     * @param attribute  the attribute to check
     * @param value      the value that the attribute should not have
     * @param waitConsts optional wait constants
     */
    public void attributeNotContains(By by, String attribute, String value, WaitConsts... waitConsts) {
        waitFor(new AttributeWait(by, waitConsts).attribute(attribute).value(value).presence(false).exactMatch(false));
    }

    /**
     * Waits for the specified CSS attribute of the web element located by the given locator to be loaded.
     * <p>
     * This method waits until the specified CSS attribute of the web element located by the given locator
     * has stabilized, meaning its value has remained the same for at least two consecutive checks.
     * </p>
     *
     * @param by               the locator of the web element to check
     * @param attributeName the CSS attribute to check
     * @param waitConsts       optional wait constants
     */
    public void attributeIsLoaded(By by, String attributeName, WaitConsts... waitConsts) {
        waitFor(new LoadWait(by, waitConsts).forAttribute(attributeName));
    }

    /**
     * Waits for the specified CSS attribute of the given web element to be loaded.
     * <p>
     * This method waits until the specified CSS attribute of the given web element
     * has stabilized, meaning its value has remained the same for at least two consecutive checks.
     * </p>
     *
     * @param element          the web element to check
     * @param attributeName the CSS attribute to check
     * @param waitConsts       optional wait constants
     */
    public void attributeIsLoaded(WebElement element, String attributeName, WaitConsts... waitConsts) {
        waitFor(new LoadWait(element, waitConsts).forAttribute(attributeName));
    }

    /**
     * Waits for the given web element to be clickable.
     *
     * @param element    the web element
     * @param waitConsts optional wait constants
     */
    public void clickable(WebElement element, WaitConsts... waitConsts) {
        waitFor(new ClickableWait(element, waitConsts));
    }

    /**
     * Waits for the given web element to not be clickable.
     *
     * @param element    the web element
     * @param waitConsts optional wait constants
     */
    public void notClickable(WebElement element, WaitConsts... waitConsts) {
        waitFor(new ClickableWait(element, waitConsts).clickable(false));
    }

    /**
     * Waits for the web element located by the given locator to be clickable.
     *
     * @param by         the locator of the web element
     * @param waitConsts optional wait constants
     */
    public void clickable(By by, WaitConsts... waitConsts) {
        waitFor(new ClickableWait(by, waitConsts));
    }

    /**
     * Waits for the web element located by the given locator to not be clickable.
     *
     * @param by         the locator of the web element
     * @param waitConsts optional wait constants
     */
    public void notClickable(By by, WaitConsts... waitConsts) {
        waitFor(new ClickableWait(by, waitConsts).clickable(false));
    }

    /**
     * Waits for the specified cookie to be present.
     *
     * @param cookieName the name of the cookie
     * @param waitConsts optional wait constants
     */
    public void isCookiePresent(String cookieName, WaitConsts... waitConsts) {
        waitFor(new CookieWait(cookieName, waitConsts).presence(true));
    }

    /**
     * Waits for the specified cookie to not be present.
     *
     * @param cookieName the name of the cookie
     * @param waitConsts optional wait constants
     */
    public void isCookieNotPresent(String cookieName, WaitConsts... waitConsts) {
        waitFor(new CookieWait(cookieName, waitConsts).presence(false));
    }

    /**
     * Waits for the position of the given web element to be loaded.
     *
     * @param element    the web element
     * @param waitConsts optional wait constants
     */
    public void positionIsLoaded(WebElement element, WaitConsts... waitConsts) {
        waitFor(new LoadWait(element, waitConsts).forPosition(true));
    }

    /**
     * Waits for the position of the web element located by the given locator to be loaded.
     *
     * @param by         the locator of the web element
     * @param waitConsts optional wait constants
     */
    public void positionIsLoaded(By by, WaitConsts... waitConsts) {
        waitFor(new LoadWait(by, waitConsts).forPosition(true));
    }

    /**
     * Waits for the presence of the given web element.
     *
     * @param element the web element
     */
    public void presenceOf(WebElement element, WaitConsts... waitConsts) {
        waitFor(new PresenceWait(element, waitConsts));
    }

    /**
     * Waits for the presence of the web element located by the given locator.
     *
     * @param by the locator of the web element
     */
    public void presenceOfElementLocatedBy(By by, WaitConsts... waitConsts) {
        waitFor(new PresenceWait(by, waitConsts));
    }

    /**
     * Waits for the absence of the given web element.
     *
     * @param element the web element
     */
    public void absenceOf(WebElement element, WaitConsts... waitConsts) {
        waitFor(new PresenceWait(element, waitConsts).presence(false));
    }

    /**
     * Waits for the absence of the web element located by the given locator.
     *
     * @param by the locator of the web element
     */
    public void absenceOfElementLocatedBy(By by, WaitConsts... waitConsts) {
        waitFor(new PresenceWait(by, waitConsts).presence(false));
    }

    /**
     * Waits for the presence of any web element located by the given list of locators.
     *
     * @param by         the list of locators
     * @param waitConsts optional wait constants
     */
    public void presenceOfAnyElementLocatedBy(List<By> by, WaitConsts... waitConsts) {
        waitFor(new PresenceWait((By) null, waitConsts).anyElementMatch(true).setLocators(by));
    }

    /**
     * Waits for the absence of any web element located by the given list of locators.
     *
     * @param by         the list of locators
     * @param waitConsts optional wait constants
     */
    public void absenceOfAnyElementLocatedBy(List<By> by, WaitConsts... waitConsts) {
        waitFor(new PresenceWait((By) null, waitConsts).anyElementMatch(true).presence(false).setLocators(by));
    }

    /**
     * Waits for the presence of all web elements located by the given list of locators.
     *
     * @param by         the list of locators
     * @param waitConsts optional wait constants
     */
    public void presenceOfAllElementsLocatedBy(List<By> by, WaitConsts... waitConsts) {
        waitFor(new PresenceWait((By) null, waitConsts).setLocators(by));
    }

    /**
     * Waits for the absence of all web elements located by the given list of locators.
     *
     * @param by         the list of locators
     * @param waitConsts optional wait constants
     */
    public void absenceOfAllElementsLocatedBy(List<By> by, WaitConsts... waitConsts) {
        waitFor(new PresenceWait((By) null, waitConsts).presence(false).setLocators(by));
    }

    /**
     * Waits for the visibility of the given web element.
     *
     * @param element    the web element
     * @param waitConsts optional wait constants
     */
    public void visibilityOf(WebElement element, WaitConsts... waitConsts) {
        waitFor(new VisibilityWait(element, waitConsts));
    }

    /**
     * Waits for the invisibility of the given web element.
     *
     * @param element    the web element
     * @param waitConsts optional wait constants
     */
    public void invisibilityOf(WebElement element, WaitConsts... waitConsts) {
        waitFor(new VisibilityWait(element, waitConsts).visible(false));
    }

    /**
     * Waits for the visibility of the web element located by the given locator.
     *
     * @param by         the locator of the web element
     * @param waitConsts optional wait constants
     */
    public void visibilityOfElementLocatedBy(By by, WaitConsts... waitConsts) {
        waitFor(new VisibilityWait(by, waitConsts));
    }

    /**
     * Waits for the invisibility of the web element located by the given locator.
     *
     * @param by         the locator of the web element
     * @param waitConsts optional wait constants
     */
    public void invisibilityOfElementLocatedBy(By by, WaitConsts... waitConsts) {
        waitFor(new VisibilityWait(by, waitConsts).visible(false));
    }

    /**
     * Waits for the visibility of all web elements located by the given list of locators.
     *
     * @param by         the list of locators
     * @param waitConsts optional wait constants
     */
    public void visibilityOfAllElementsLocatedBy(List<By> by, WaitConsts... waitConsts) {
        waitFor(new VisibilityWait((By) null, waitConsts).setLocators(by));
    }

    /**
     * Waits for the invisibility of all web elements located by the given list of locators.
     *
     * @param by         the list of locators
     * @param waitConsts optional wait constants
     */
    public void invisibilityOfAllElementsLocatedBy(List<By> by, WaitConsts... waitConsts) {
        waitFor(new VisibilityWait((By) null, waitConsts).visible(false).setLocators(by));
    }

    /**
     * Waits for the text of the web element located by the given locator to be equal to the specified text.
     * <p>
     * This method waits until the text of the web element located by the given locator is equal to the specified text.
     * </p>
     *
     * @param by         the locator of the web element
     * @param text       the expected text value
     * @param waitConsts optional wait constants
     */
    public void textToBe(By by, String text, WaitConsts... waitConsts) {
        waitFor(new TextWait(by, waitConsts).text(text));
    }

    /**
     * Waits for the text of the given web element to be equal to the specified text.
     * <p>
     * This method waits until the text of the given web element is equal to the specified text.
     * </p>
     *
     * @param element    the web element
     * @param text       the expected text value
     * @param waitConsts optional wait constants
     */
    public void textToBe(WebElement element, String text, WaitConsts... waitConsts) {
        waitFor(new TextWait(element, waitConsts).text(text));
    }

    /**
     * Waits for the text of the web element located by the given locator to contain the specified text.
     * <p>
     * This method waits until the text of the web element located by the given locator contains the specified text.
     * </p>
     *
     * @param by         the locator of the web element
     * @param text       the expected text value
     * @param waitConsts optional wait constants
     */
    public void textToBePresentInElementLocatedBy(By by, String text, WaitConsts... waitConsts) {
        waitFor(new TextWait(by, waitConsts).text(text).shouldBeEqual(false));
    }

    /**
     * Waits for the text of the given web element to contain the specified text.
     * <p>
     * This method waits until the text of the given web element contains the specified text.
     * </p>
     *
     * @param element    the web element
     * @param text       the expected text value
     * @param waitConsts optional wait constants
     */
    public void textToBePresentInElement(WebElement element, String text, WaitConsts... waitConsts) {
        waitFor(new TextWait(element, waitConsts).text(text).shouldBeEqual(false));
    }

    /**
     * Waits for the text of the given web element to be loaded, i.e. not changed.
     * <p>
     * This method waits until the text of the given web element has stabilized,
     * meaning its value has remained the same for at least two consecutive checks.
     * </p>
     *
     * @param element    the web element to check
     * @param waitConsts optional wait constants
     */
    public void textIsLoaded(WebElement element, WaitConsts... waitConsts) {
        waitFor(new LoadWait(element, waitConsts).forText(true));
    }

}
