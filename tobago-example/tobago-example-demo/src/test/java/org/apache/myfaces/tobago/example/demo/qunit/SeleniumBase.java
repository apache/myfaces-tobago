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

package org.apache.myfaces.tobago.example.demo.qunit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;

abstract class SeleniumBase {

  private static final Logger LOG = LoggerFactory.getLogger(SeleniumBase.class);

  private WebDriver webDriver;

  @AfterEach
  void tearDown() {
    if (webDriver != null) {
      webDriver.quit();
    }
  }

  enum Browser {
    chrome, firefox
  }

  static String[] getServerPortWithContextPath() {
    return new String[]{"8081/tobago-example-demo"};
  }

  void setupBrowser(final Browser browser) throws MalformedURLException {
    MutableCapabilities options = null;
    switch (browser) {
      case chrome:
        options = new ChromeOptions();
        break;
      case firefox:
      default:
        options = new FirefoxOptions();
        break;
    }

    webDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), new DesiredCapabilities(options));
  }

  void setupWebDriver(final String portContextPath, final String path, final boolean accessTest)
      throws UnknownHostException, UnsupportedEncodingException {
    final String hostAddress = InetAddress.getLocalHost().getHostAddress();
    final String base = path.substring(0, path.length() - 6);
    final String url = "http://" + hostAddress + ":" + portContextPath + "/test.xhtml?base="
        + URLEncoder.encode(base, "UTF-8") + (accessTest ? "&accessTest=true" : "");
    webDriver.get(url);
  }

  WebDriver getWebDriver() {
    return webDriver;
  }

  /**
   * Wait for the qunit-banner web element and return it.
   * If the web element is available, the execution of qunit test should be done and it is safe to parse the results.
   *
   * @return qunit-banner web element
   */
  WebElement waitForQUnitBanner() {
    final FluentWait<WebDriver> fluentWait = new FluentWait<>(webDriver)
        .withTimeout(Duration.ofSeconds(90))
        .pollingEvery(Duration.ofSeconds(1))
        .ignoring(NoSuchElementException.class);

    WebElement qunitBanner = fluentWait.until(driver -> driver.findElement(By.id("qunit-banner")));
    fluentWait.until(ExpectedConditions.attributeToBeNotEmpty(qunitBanner, "class"));

    return qunitBanner;
  }

  void parseQUnitResults(final Browser browser, final String portContextPath, final String path)
      throws UnknownHostException {
    WebElement qunitBanner = waitForQUnitBanner();
    WebElement qunitTestResult = webDriver.findElement(By.id("qunit-testresult"));
    WebElement qunitTests = webDriver.findElement(By.id("qunit-tests"));

    final List<WebElement> testCases = qunitTests.findElements(By.xpath("li"));
    Assert.assertTrue("There must be at least one test case.", testCases.size() > 0);

    final boolean testFailed = qunitBanner.getAttribute("class").equals("qunit-fail");

    int testCaseCount = 1;
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(qunitTestResult.getAttribute("textContent"));
    stringBuilder.append("\n");

    if (testFailed) {
      for (final WebElement testCase : testCases) {
        final String testName = getText(testCase, "test-name");
        final String testStatus = testCase.getAttribute("class").toUpperCase();

        stringBuilder.append(testCaseCount++);
        stringBuilder.append(". ");
        stringBuilder.append(testStatus);
        stringBuilder.append(": ");
        stringBuilder.append(testName);
        stringBuilder.append(" (");
        stringBuilder.append(getText(testCase, "runtime"));
        stringBuilder.append(")\n");

        final WebElement assertList = testCase.findElement(By.className("qunit-assert-list"));
        final List<WebElement> asserts = assertList.findElements(By.tagName("li"));
        int assertCount = 1;
        for (final WebElement assertion : asserts) {
          final String assertStatus = assertion.getAttribute("class");

          stringBuilder.append("- ");
          if (assertCount <= 9) {
            stringBuilder.append("0");
          }
          stringBuilder.append(assertCount++);
          stringBuilder.append(". ");
          stringBuilder.append(assertStatus);
          stringBuilder.append(": ");
          stringBuilder.append(getText(assertion, "test-message"));
          stringBuilder.append(getText(assertion, "runtime"));
          stringBuilder.append("\n");

          final String assertExpected = getText(assertion, "test-expected");
          if (!"null".equals(assertExpected)) {
            stringBuilder.append("-- ");
            stringBuilder.append(assertExpected);
            stringBuilder.append("\n");
          }
          final String assertResult = getText(assertion, "test-actual");
          if (!"null".equals(assertResult)) {
            stringBuilder.append("-- ");
            stringBuilder.append(assertResult);
            stringBuilder.append("\n");
          }
          final String assertSource = getText(assertion, "test-source");
          if (!"null".equals(assertSource)) {
            stringBuilder.append("-- ");
            stringBuilder.append(assertSource);
            stringBuilder.append("\n");
          }
        }

        stringBuilder.append(getText(testCase, "qunit-source"));
        stringBuilder.append("\n\n");
      }
    }

    final String hostAddress = InetAddress.getLocalHost().getHostAddress();
    final String url = "http://" + hostAddress + ":" + portContextPath + "/" + path;
    if (testFailed) {
      final String message = "\n❌ FAILED: Tests with '" + browser + "' for " + url + "\n" + stringBuilder.toString();
      LOG.warn(message);
      Assert.fail(message);
    } else {
      final String message = "\n✅ PASSED: Tests with '" + browser + "' for " + url + "\n" + stringBuilder.toString();
      LOG.info(message);
      Assert.assertTrue(message, true);
    }
  }

  private String getText(final WebElement webElement, final String className) {
    final List<WebElement> elements = webElement.findElements(By.className(className));
    if (elements.size() > 0) {
      return elements.get(0).getAttribute("textContent");
    } else {
      return "null";
    }
  }
}
