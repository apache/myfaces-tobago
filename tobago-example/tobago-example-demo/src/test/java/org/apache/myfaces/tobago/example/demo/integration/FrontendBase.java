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

package org.apache.myfaces.tobago.example.demo.integration;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

abstract class FrontendBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @SuppressWarnings("rawtypes") // this is how to use testcontainers
  @Container
  private static final GenericContainer SELENIUM_FIREFOX =
      new GenericContainer<>(DockerImageName.parse("henningn/selenium-standalone-firefox"))
          .withExposedPorts(4444);

  @SuppressWarnings("rawtypes") // this is how to use testcontainers
  @Container
  private static final GenericContainer TOMCAT =
      new GenericContainer(DockerImageName.parse("myfaces/tobago-example-demo"))
          .withExposedPorts(8080);

  private static WebDriver firefoxDriver;

  @AfterAll
  static void tearDown() {
    if (firefoxDriver != null) {
      firefoxDriver.quit();
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

  WebDriver getWebDriver(final String host, final Integer port) throws MalformedURLException {
    if (firefoxDriver == null || ((RemoteWebDriver) firefoxDriver).getSessionId() == null) {
      firefoxDriver = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), new FirefoxOptions());
    }
    return firefoxDriver;
  }

  List<WebElement> getJasmineResults(WebDriver webDriver, String url) {
    final FluentWait<WebDriver> fluentWait = new FluentWait<>(webDriver)
        .withTimeout(Duration.ofSeconds(60))
        .pollingEvery(Duration.ofSeconds(1))
        .ignoring(NoSuchElementException.class);
    try {
      fluentWait.until(driver -> driver.findElement(By.className("jasmine-overall-result")));
    } catch (TimeoutException e) {
      Assertions.fail(url + " timeout");
    }

    return webDriver.findElements(By.cssSelector(".jasmine-symbol-summary li"));
  }

  void parseJasmineResults(List<WebElement> results, String url) {
    Assertions.assertTrue(results.size() > 0, url + " no results detected");

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\n");
    stringBuilder.append(url);
    for (WebElement result : results) {
      stringBuilder.append("\n");
      if ("jasmine-passed".equals(result.getAttribute("class"))) {
        stringBuilder.append("✅ passed");
      } else {
        stringBuilder.append("❌ failed");
      }
      stringBuilder.append(": ");
      stringBuilder.append(result.getAttribute("title"));
    }
    LOG.info(stringBuilder.toString());

    for (WebElement result : results) {
      Assertions.assertEquals("jasmine-passed", result.getAttribute("class"),
          url + " " + result.getAttribute("title"));
    }
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

  int getFirefoxPort() {
    return SELENIUM_FIREFOX.getFirstMappedPort();
  }

  int getTomcatPort() {
    return TOMCAT.getFirstMappedPort();
  }
}
