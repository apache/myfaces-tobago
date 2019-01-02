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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AccessAllPagesTest extends SeleniumBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Verify qunit test for "no exception".
   */
  @ParameterizedTest
  @MethodSource("verifyNoException404TestProvider")
  void verifyNoExceptionTest(Browser browser, String serverUrl)
      throws MalformedURLException, UnsupportedEncodingException {
    final String path = "error/exception.xhtml";
    LOG.info("browser: " + browser + " - url: " + serverUrl + "/" + path);

    if (isIgnored(serverUrl, path)) {
      logIgnoreMessage(serverUrl, path);
    } else {
      setupWebDriver(browser, serverUrl, path, true);

      final WebDriver webDriver = getWebDriver(browser);
      waitForQUnitBanner(webDriver);
      WebElement qunitTests = webDriver.findElement(By.id("qunit-tests"));

      List<WebElement> results = qunitTests.findElements(By.xpath("li"));
      boolean testException = false;
      for (final WebElement result : results) {
        if ("has no exception".equals(result.findElement(By.className("test-name")).getText())) {
          Assert.assertEquals(result.getAttribute("class"), "fail");
          testException = true;
        }
      }
      Assert.assertTrue("Could not verify 'has no exception' test.", testException);
    }
  }

  /**
   * Verify qunit test for "no 404".
   */
  @ParameterizedTest
  @MethodSource("verifyNoException404TestProvider")
  void verifyNo404Test(Browser browser, String serverUrl) throws MalformedURLException, UnsupportedEncodingException {
    final String path = "error/404.xhtml";
    LOG.info("browser: " + browser + " - url: " + serverUrl + "/" + path);

    if (isIgnored(serverUrl, path)) {
      logIgnoreMessage(serverUrl, path);
    } else {
      setupWebDriver(browser, serverUrl, path, true);

      final WebDriver webDriver = getWebDriver(browser);
      waitForQUnitBanner(webDriver);
      WebElement qunitTests = webDriver.findElement(By.id("qunit-tests"));

      List<WebElement> results = qunitTests.findElements(By.xpath("li"));
      boolean test404 = false;
      for (final WebElement result : results) {
        if ("has no 404".equals(result.findElement(By.className("test-name")).getText())) {
          Assert.assertEquals(result.getAttribute("class"), "fail");
          test404 = true;
        }
      }
      Assert.assertTrue("Could not verify 'has no 404' test.", test404);
    }
  }

  private static Stream<Arguments> verifyNoException404TestProvider() throws IOException {
    List<Arguments> arguments = new LinkedList<>();

    for (Browser browser : Browser.values()) {
      for (String serverUrl : getServerUrls()) {
        arguments.add(Arguments.of(browser, serverUrl));
      }
    }

    return arguments.stream();
  }

  @ParameterizedTest
  @MethodSource("accessAllPagesProvider")
  void testAccessAllPages(Browser browser, String serverUrl, String path, LocalTime startTime, int testSize, int testNo)
      throws MalformedURLException, UnsupportedEncodingException {

    double percent = 100 * (double) testNo / testSize;
    final String timeLeft = getTimeLeft(startTime, testSize, testNo);

    LOG.info("(" + String.format("%.2f", percent) + " % complete" + " | time left: " + timeLeft + ")"
        + " browser: " + browser + " - url: " + serverUrl + "/" + path);

    if (isIgnored(serverUrl, path)) {
      logIgnoreMessage(serverUrl, path);
    } else {
      setupWebDriver(browser, serverUrl, path, true);
      parseQUnitResults(browser, serverUrl, path);
    }
  }

  private static Stream<Arguments> accessAllPagesProvider() throws IOException {
    final List<String> paths = Files.walk(Paths.get("src/main/webapp/content/"))
        .filter(Files::isRegularFile)
        .map(Path::toString)
        .filter(s -> s.endsWith(".xhtml"))
        .filter(s -> !s.contains("/x-"))
        .map(s -> s.substring("src/main/webapp/".length()))
        .filter(s -> !s.startsWith("content/40-test/90000-attic")) // ignore 90000-attic
        .sorted()
        .collect(Collectors.toList());

    for (String standardTestPath : getStandardTestPaths()) {
      paths.remove(standardTestPath); // already tested by standard test
    }

    return getArguments(paths);
  }
}
