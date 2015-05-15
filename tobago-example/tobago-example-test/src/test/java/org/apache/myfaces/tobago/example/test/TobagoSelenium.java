/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.example.test;

import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.webdriven.ElementFinder;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import com.thoughtworks.selenium.webdriven.WebDriverCommandProcessor;
import com.thoughtworks.selenium.webdriven.commands.GetAttribute;
import com.thoughtworks.selenium.webdriven.commands.GetElementHeight;
import com.thoughtworks.selenium.webdriven.commands.GetElementPositionLeft;
import com.thoughtworks.selenium.webdriven.commands.GetElementPositionTop;
import com.thoughtworks.selenium.webdriven.commands.GetText;
import com.thoughtworks.selenium.webdriven.commands.GetValue;
import com.thoughtworks.selenium.webdriven.commands.IsElementPresent;
import com.thoughtworks.selenium.webdriven.commands.IsTextPresent;
import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TobagoSelenium extends WebDriverBackedSelenium {

  public static final String ERROR_ON_SERVER = "error on server";
  public static final String HAS_ERROR_SEVERITY = "has error severity";
  public static final String IS_BROKEN = "is broken";

  private String browserUrl;

  public TobagoSelenium(final WebDriver baseDriver, final String baseUrl) {
    super(baseDriver, baseUrl);
    this.browserUrl = baseUrl;
    if (commandProcessor instanceof WebDriverCommandProcessor) {
      WebDriverCommandProcessor webDriverCommandProcessor = (WebDriverCommandProcessor) commandProcessor;
      JavascriptLibrary javascriptLibrary = new JavascriptLibrary();
      ElementFinder elementFinder = new ElementFinder(javascriptLibrary);
      // the following methods are used by SeleniumIDE but not implement in legacy RC backed WebDriver,
      // see also WebDriverCommandProcessor.setUpMethodMap()
      webDriverCommandProcessor.addMethod("waitForElementPresent", new WaitForElementPresent(elementFinder));
      webDriverCommandProcessor.addMethod("waitForElementNotPresent", new WaitForElementNotPresent(elementFinder));
      webDriverCommandProcessor.addMethod("waitForValue", new WaitForValue(elementFinder));
      webDriverCommandProcessor.addMethod("clickAndWait", new ClickAndWait(elementFinder));
      webDriverCommandProcessor.addMethod("verifyValue", new VerifyValue(elementFinder));
      webDriverCommandProcessor.addMethod("assertAttribute", new AssertAttribute(javascriptLibrary, elementFinder));
      webDriverCommandProcessor.addMethod("verifyText", new VerifyText(javascriptLibrary, elementFinder));
      webDriverCommandProcessor.addMethod("verifyTextPresent", new VerifyTextPresent(javascriptLibrary));
      webDriverCommandProcessor.addMethod("assertValue", new VerifyValue(elementFinder));
      webDriverCommandProcessor.addMethod("assertElementHeight", new AssertElementHeight(elementFinder));
      webDriverCommandProcessor.addMethod("assertElementPositionLeft", new AssertElementPositionLeft(elementFinder));
      webDriverCommandProcessor.addMethod("assertElementPositionTop", new AssertElementPositionTop(elementFinder));
    }
  }

  public String command(final String command, final String parameter1, final String parameter2) {
    if (StringUtils.isNotBlank(parameter2)) {
      return commandProcessor.doCommand(command, new String[]{parameter1, parameter2});
    } else  {
      return commandProcessor.doCommand(command, new String[]{parameter1});
    }
  }

  public void killSession() {
    open(browserUrl + "KillSession");
  }

  public void checkPage() {
    final String location = getLocation();
    final String html = getHtmlSource();
    if (errorOnServer()) {
      Assert.fail(format(ERROR_ON_SERVER, location, html, ""));
    }
    if (location.endsWith(".xhtml") || location.endsWith(".jspx")) {
      try {
        if (isErrorOnPage()) {
          Assert.fail(format(HAS_ERROR_SEVERITY, location, html, "TobagoAssert.failed"));
        }
        if (!getHtmlSource().contains("class=\"tobago-page\"")) {
          Assert.fail(format(HAS_ERROR_SEVERITY, location, html, TobagoSelenium.IS_BROKEN));
        }
      } catch (final SeleniumException e) {
        Assert.fail(format(IS_BROKEN, location, html, "Not a Tobago page? Exception=" + e));
      }
    }
  }

  public String format(final String error, final String location, final String html, final String options) {
    return error + "\nPage URL: " + location + "\n" + options
        + "\n---------------------------------------------------------------------------------------------------\n"
        + html
        + "\n---------------------------------------------------------------------------------------------------\n";
  }

  /**
   * Happen an exception from MyFaces on the page?
   */
  protected boolean errorOnServer() {
    return getHtmlSource().contains("An Error Occurred");
  }

  /**
   * Checks the page for the Tobago JavaScript Logging Framework and tests its severity.
   *
   * @return True if the severity level of the page is error
   * @throws com.thoughtworks.selenium.SeleniumException
   *          If the page is not a Tobago page, or any other problem with JavaScript or the page.
   */
  protected boolean isErrorOnPage() throws SeleniumException {
    final String errorSeverity = getEval("window.TobagoAssert && window.TobagoAssert.failed");
    return Boolean.parseBoolean(errorSeverity);
  }


  class WaitForElementPresent extends IsElementPresent {
    public WaitForElementPresent(final ElementFinder finder) {
      super(finder);
    }

    @Override
    protected Boolean handleSeleneseCommand(final WebDriver driver, final String locator, final String ignored) {
      long timeout = getTimeout("");
      new Wait() {
        @Override
        public boolean until() {
          return WaitForElementPresent.super.handleSeleneseCommand(driver, locator, ignored);
        }
      }.wait(String.format("Timed out waiting for %s. Waited %s", locator, timeout), timeout);
      return true;
    }
  }

  class WaitForElementNotPresent extends IsElementPresent {
    public WaitForElementNotPresent(final ElementFinder finder) {
      super(finder);
    }

    @Override
    protected Boolean handleSeleneseCommand(final WebDriver driver, final String locator, final String ignored) {
      long timeout = getTimeout("");
      new Wait() {
        @Override
        public boolean until() {
          return !WaitForElementNotPresent.super.handleSeleneseCommand(driver, locator, ignored);
        }
      }.wait(String.format("Timed out waiting for %s. Waited %s", locator, timeout), timeout);
      return true;
    }
  }

  class WaitForValue extends GetValue {
    public WaitForValue(final ElementFinder finder) {
      super(finder);
    }

    @Override
    protected String handleSeleneseCommand(final WebDriver driver, final String locator, final String expectedValue) {
      long timeout = getTimeout("");
      new Wait() {
        @Override
        public boolean until() {
          String value = WaitForValue.super.handleSeleneseCommand(driver, locator, expectedValue);
          return expectedValue.equals(value);
        }
      }.wait(String.format("Timed out waiting for %s. Waited %s", locator, timeout), timeout);
      return null;
    }
  }

  class ClickAndWait extends WaitForPageToLoad {
    private final ElementFinder finder;

    public ClickAndWait(final ElementFinder finder) {
      this.finder = finder;
    }

    @Override
    protected Void handleSeleneseCommand(final WebDriver driver, final String locator, final String value) {
      WebElement element = finder.findElement(driver, locator);
      element.click();
      return super.handleSeleneseCommand(driver, Long.toString(getTimeout("")), null);
    }
  }

  class VerifyValue extends GetValue {
    public VerifyValue(final ElementFinder finder) {
      super(finder);
    }

    @Override
    protected String handleSeleneseCommand(final WebDriver driver, final String locator, final String expectedValue) {
      String value = super.handleSeleneseCommand(driver, locator, expectedValue);
      if (StringUtils.isEmpty(value)) {
        value = null;
      }
      Assert.assertEquals(expectedValue, value);
      return value;
    }
  }

  class VerifyText extends GetText {
    public VerifyText(final JavascriptLibrary library, final ElementFinder finder) {
      super(library, finder);
    }

    @Override
    protected String handleSeleneseCommand(final WebDriver driver, final String locator, final String expectedText) {
      String text = super.handleSeleneseCommand(driver, locator, expectedText);
      text = text.replaceAll("\n", "");
      Assert.assertEquals(expectedText, text);
      return text;
    }
  }

  class AssertElementHeight extends GetElementHeight {
    public AssertElementHeight(final ElementFinder finder) {
      super(finder);
    }

    @Override
    protected Number handleSeleneseCommand(final WebDriver driver, final String locator, final String expectedHeight) {
      Number height = super.handleSeleneseCommand(driver, locator, expectedHeight);
      Assert.assertEquals(Integer.parseInt(expectedHeight), height);
      return height;
    }
  }

  class AssertElementPositionLeft extends GetElementPositionLeft {
    public AssertElementPositionLeft(final ElementFinder finder) {
      super(finder);
    }

    @Override
    protected Number handleSeleneseCommand(final WebDriver driver, final String locator,
                                           final String expectedPosition) {
      Number position = super.handleSeleneseCommand(driver, locator, expectedPosition);
      Assert.assertEquals(expectedPosition, Integer.toString(position.intValue()-1));
      return position;
    }
  }

  class AssertElementPositionTop extends GetElementPositionTop {
    public AssertElementPositionTop(final ElementFinder finder) {
      super(finder);
    }

    @Override
    protected Number handleSeleneseCommand(final WebDriver driver, final String locator,
                                           final String expectedPosition) {
      Number position = super.handleSeleneseCommand(driver, locator, expectedPosition);
      Assert.assertEquals(expectedPosition, Integer.toString(position.intValue()-1));
      return position;
    }
  }

  class VerifyTextPresent extends IsTextPresent {
    public VerifyTextPresent(final JavascriptLibrary js) {
      super(js);
    }

    @Override
    protected Boolean handleSeleneseCommand(final WebDriver driver, final String pattern, final String ignored) {
      boolean isTextPresent = super.handleSeleneseCommand(driver, pattern, ignored);
      Assert.assertTrue(isTextPresent);
      return true;
    }
  }

  class AssertAttribute extends GetAttribute {
    public AssertAttribute(final JavascriptLibrary library, final ElementFinder finder) {
      super(library, finder);
    }

    @Override
    protected String handleSeleneseCommand(final WebDriver driver, final String attributeLocator,
                                           final String expectedValue) {
      String attributeValue = super.handleSeleneseCommand(driver, attributeLocator, null);
      Assert.assertEquals(expectedValue, attributeValue);
      return attributeValue;
    }
  }
}
