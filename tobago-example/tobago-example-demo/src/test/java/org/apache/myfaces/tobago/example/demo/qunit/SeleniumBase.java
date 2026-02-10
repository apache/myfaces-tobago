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

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class SeleniumBase {

  private static final Logger LOG = LoggerFactory.getLogger(SeleniumBase.class);

  private static WebDriver chromeDriver;
  private static List<String> serverUrls = new ArrayList<>();
  private static Map<String, String> ignores = new HashMap<>();

  @BeforeAll
  static void setUp() {
    ignores.put(":8083/tobago-example-demo-myfaces-2.3",
        "MyFaces 2.3 don't work with Tomcat 8.5 and openjdk10");
    ignores.put("tobago-example-demo-mojarra-2.0",
        "Ajax events don't work with Mojarra 2.0: https://issues.apache.org/jira/browse/TOBAGO-1589");
    ignores.put("tobago-example-demo-mojarra-2.3",
        "Currently Tobago demo don't run with Mojarra 2.3 on Tomcat 8.5");

    ignores.put("content/40-test/6000-event/event.xhtml",
        "Focus/blur event can only be fired if the browser window is in foreground."
            + " This cannot be guaranteed in selenium tests."
            + " event.test.js contain focus/blur events");

    final String tobago1910 = "TreeSelect: Single selection nodes are not deselected correctly with mojarra: "
        + "https://issues.apache.org/jira/browse/TOBAGO-1910";
    ignores.put("tobago-example-demo-mojarra-2.1/content/20-component/090-tree/01-select/tree-select.xhtml",
        tobago1910);
    ignores.put("tobago-example-demo-mojarra-2.2/content/20-component/090-tree/01-select/tree-select.xhtml",
        tobago1910);
  }

  @AfterAll
  static void tearDown() {
    if (chromeDriver != null) {
      chromeDriver.quit();
    }
  }

  enum Browser {
    chrome
    //, firefox // TODO implement firefox
  }

  static List<String> getServerUrls() throws UnknownHostException, MalformedURLException {
    if (serverUrls.size() <= 0) {
      final String hostAddress = InetAddress.getLocalHost().getHostAddress();

      List<String> ports = new ArrayList<>();
      ports.add("8082"); // Tomcat JRE 8
      ports.add("8083"); // Tomcat JRE 10

      List<String> contextPaths = new ArrayList<>();
      contextPaths.add("tobago-example-demo"); // MyFaces 2.0
      contextPaths.add("tobago-example-demo-myfaces-2.1");
      contextPaths.add("tobago-example-demo-myfaces-2.2");
      contextPaths.add("tobago-example-demo-myfaces-2.3");
      contextPaths.add("tobago-example-demo-mojarra-2.0");
      contextPaths.add("tobago-example-demo-mojarra-2.1");
      contextPaths.add("tobago-example-demo-mojarra-2.2");
      contextPaths.add("tobago-example-demo-mojarra-2.3");

      for (String port : ports) {
        for (String contextPath : contextPaths) {
          String url = "http://" + hostAddress + ":" + port + "/" + contextPath;
          final int status = getStatus(url);
          if (status == 200) {
            serverUrls.add(url);
          } else {
            LOG.warn("\n⚠️ IGNORED: Tests for " + url + ":\n Server status: " + status);
          }
        }
      }
    }

    return serverUrls;
  }

  private static int getStatus(String url) throws MalformedURLException {
    URL siteURL = new URL(url);
    try {
      HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      return connection.getResponseCode();
    } catch (IOException e) {
      return -1;
    }
  }

  static List<String> getStandardTestPaths() throws IOException {
    return Files.walk(Paths.get("src/main/webapp/content/"))
        .filter(Files::isRegularFile)
        .map(Path::toString)
        .filter(s -> s.endsWith(".test.js"))
        .map(s -> s.substring("src/main/webapp/".length()))
        .sorted()
        .map(s -> s.substring(0, s.length() - 8) + ".xhtml")
        .collect(Collectors.toList());
  }

  boolean isIgnored(final String serverUrl, final String path) {
    final String url = serverUrl + "/" + path;
    for (String key : ignores.keySet()) {
      if (url.contains(key)) {
        return true;
      }
    }
    return false;
  }

  void logIgnoreMessage(final String serverUrl, final String path) {
    final String url = serverUrl + "/" + path;
    for (final Map.Entry<String, String> ignore : ignores.entrySet()) {
      if (url.contains(ignore.getKey())) {
        final String message = ignore.getValue();
        LOG.info("\n⚠️ IGNORED: Test for " + url + ":\n" + message);
        return;
      }
    }
  }

  void setupWebDriver(final Browser browser, final String serverUrl, final String path, final boolean accessTest)
      throws MalformedURLException, UnsupportedEncodingException {
    if (Browser.chrome.equals(browser)
        && chromeDriver == null || ((RemoteWebDriver) chromeDriver).getSessionId() == null) {
      chromeDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), new ChromeOptions());
    }

    final String base = path.substring(0, path.length() - 6);
    final String url = serverUrl + "/test.xhtml?base="
        + URLEncoder.encode(base, "UTF-8") + (accessTest ? "&accessTest=true" : "");
    getWebDriver(browser).get(url);
  }

  WebDriver getWebDriver(Browser browser) {
    if (Browser.chrome.equals(browser)) {
      return chromeDriver;
    } else {
      return null;
    }
  }

  static Stream<Arguments> getArguments(final List<String> paths) throws MalformedURLException, UnknownHostException {
    final LocalTime startTime = LocalTime.now();
    final int testSize = Browser.values().length * getServerUrls().size() * paths.size();

    List<Arguments> arguments = new LinkedList<>();

    int testNo = 1;
    for (String serverUrl : getServerUrls()) {
      for (String path : paths) {
        for (Browser browser : Browser.values()) {
          arguments.add(Arguments.of(browser, serverUrl, path, startTime, testSize, testNo));
          testNo++;
        }
      }
    }

    return arguments.stream();
  }

  String getTimeLeft(final LocalTime startTime, final int testSize, final int testNo) {
    final LocalTime now = LocalTime.now();
    final Duration completeWaitTime = Duration.between(startTime, now).dividedBy(testNo).multipliedBy(testSize);
    final LocalTime endTime = LocalTime.from(startTime).plus(completeWaitTime);
    final Duration timeLeft = Duration.between(LocalTime.now(), endTime);

    if (timeLeft.toHours() > 0) {
      return DurationFormatUtils.formatDuration(timeLeft.toMillis(), "H'h' m'm' s's'");
    } else if (timeLeft.toMinutes() > 0) {
      return DurationFormatUtils.formatDuration(timeLeft.toMillis(), "m'm' s's'");
    } else if (timeLeft.toMillis() >= 0) {
      return DurationFormatUtils.formatDuration(timeLeft.toMillis(), "s's'");
    } else {
      return "---";
    }
  }

  /**
   * Wait for the qunit-banner web element and return it.
   * If the web element is available, the execution of qunit test should be done and it is safe to parse the results.
   *
   * @return qunit-banner web element
   */
  WebElement waitForQUnitBanner(final WebDriver webDriver) {
    final FluentWait<WebDriver> fluentWait = new FluentWait<>(webDriver)
        .withTimeout(Duration.ofSeconds(90))
        .pollingEvery(Duration.ofSeconds(1))
        .ignoring(NoSuchElementException.class);

    WebElement qunitBanner = fluentWait.until(driver -> driver.findElement(By.id("qunit-banner")));
    fluentWait.until(ExpectedConditions.attributeToBeNotEmpty(qunitBanner, "class"));

    return qunitBanner;
  }

  void parseQUnitResults(final Browser browser, final String serverUrl, final String path) {
    final WebDriver webDriver = getWebDriver(browser);
    WebElement qunitBanner;
    try {
      qunitBanner = waitForQUnitBanner(webDriver);
    } catch (Exception e) {
      qunitBanner = webDriver.findElement(By.id("qunit-banner"));
    }

    WebElement qunitTestResult = webDriver.findElement(By.id("qunit-testresult"));
    WebElement qunitTests = webDriver.findElement(By.id("qunit-tests"));

    final List<WebElement> testCases = qunitTests.findElements(By.xpath("li"));
    Assertions.assertTrue(testCases.size() > 0, "There must be at least one test case.");

    final boolean testFailed = !qunitBanner.getAttribute("class").equals("qunit-pass");

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

    final String url = serverUrl + "/" + path;
    if (testFailed) {
      final String message = "\n❌ FAILED: Test with '" + browser + "' for " + url + "\n" + stringBuilder.toString();
      LOG.warn(message);
      Assertions.fail(message);
    } else {
      final String message = "\n✅ PASSED: Test with '" + browser + "' for " + url + "\n" + stringBuilder.toString();
      LOG.info(message);
      Assertions.assertTrue(true, message);
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
