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
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

abstract class FrontendBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final Network NETWORK = Network.newNetwork();
  private static final String SELENIUM_ALIAS = "selenium";
  private static final String TOMCAT_ALIAS = "tomcat";
  public static final int SELENIUM_PORT = 4444;
  public static final int TOMCAT_PORT = 8080;

  @Container
  private static final GenericContainer<?> SELENIUM_FIREFOX =
      new GenericContainer<>(DockerImageName
          .parse("selenium/standalone-firefox:137.0.1"))
          .withNetwork(NETWORK).withNetworkAliases(SELENIUM_ALIAS).withExposedPorts(SELENIUM_PORT)
          .waitingFor(Wait.forHttp("/").forPort(SELENIUM_PORT))
          .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(SELENIUM_ALIAS)).withSeparateOutputStreams());

  @Container
  private static final GenericContainer<?> TOMCAT =
      new GenericContainer<>(DockerImageName.parse("myfaces/tobago-example-demo"))
          .withNetwork(NETWORK).withNetworkAliases(TOMCAT_ALIAS).withExposedPorts(TOMCAT_PORT)
          .waitingFor(Wait.forHttp("/").forPort(TOMCAT_PORT))
          .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(TOMCAT_ALIAS)).withSeparateOutputStreams());

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

  WebDriver getWebDriver() throws MalformedURLException, UnknownHostException {
    if (firefoxDriver == null || ((RemoteWebDriver) firefoxDriver).getSessionId() == null) {
      final String host = InetAddress.getLocalHost().getHostAddress();
      final int port = SELENIUM_FIREFOX.getFirstMappedPort();
      firefoxDriver = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), new FirefoxOptions());
    }
    return firefoxDriver;
  }

  List<WebElement> getJasmineResults(WebDriver webDriver, String path)
      throws MalformedURLException, UnknownHostException {
    final FluentWait<WebDriver> fluentWait = new FluentWait<>(webDriver)
        .withTimeout(Duration.ofSeconds(60))
        .pollingEvery(Duration.ofSeconds(1))
        .ignoring(NoSuchElementException.class);
    try {
      fluentWait.until(driver -> driver.findElement(By.className("jasmine-overall-result")));
    } catch (TimeoutException e) {
      Assertions.fail(path + " timeout");
    }

    String errorDetail = getErrorDetail();
    if (errorDetail != null) {
      Assertions.fail(errorDetail);
    }

    return webDriver.findElements(By.cssSelector(".jasmine-symbol-summary li"));
  }

  private String getErrorDetail() throws MalformedURLException, UnknownHostException {
    try {
      WebElement failures = getWebDriver().findElement(By.cssSelector(".jasmine-errored"));
      return failures.getText();
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  void parseJasmineResults(List<WebElement> results, String path) throws MalformedURLException, UnknownHostException {
    Assertions.assertTrue(results.size() > 0, path + " no results detected");

    boolean fail = false;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\n");
    stringBuilder.append(path);
    for (WebElement result : results) {
      stringBuilder.append("\n");
      if ("jasmine-passed".equals(result.getAttribute("class"))) {
        stringBuilder.append("✅ passed");
      } else {
        stringBuilder.append("❌ failed");
        fail = true;
      }
      stringBuilder.append(": ");
      stringBuilder.append(result.getAttribute("title"));
    }
    if (fail) {
      stringBuilder.append("\n");
      stringBuilder.append("failures details:\n");
      stringBuilder.append(getFailureDetails());
    }
    LOG.info(stringBuilder.toString());

    for (WebElement result : results) {
      Assertions.assertEquals("jasmine-passed", result.getAttribute("class"),
          path + " " + result.getAttribute("title"));
    }
  }

  private String getFailureDetails() throws MalformedURLException, UnknownHostException {
    WebElement failures = getWebDriver().findElement(By.cssSelector(".jasmine-failures"));
    return failures.getText();
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

  String getTomcatUrl() {
    return "http://" + TOMCAT_ALIAS + ":" + TOMCAT_PORT;
  }
}
