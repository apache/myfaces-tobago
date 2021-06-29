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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Interaction;
import org.openqa.selenium.interactions.PointerInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Testcontainers
class FrontendTest extends FrontendBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static LocalTime startTime;

  @BeforeAll
  public static void setup() {
    startTime = LocalTime.now();
  }

  /**
   * Call every page with a specific *.test.js.
   */
  @ParameterizedTest
  @MethodSource("standardTestProvider")
  void frontendTest(String path, int testNumber, int testSize)
      throws MalformedURLException, UnknownHostException, UnsupportedEncodingException {

    final String timeLeft = getTimeLeft(FrontendTest.startTime, testSize, testNumber);
    LOG.info("(" + testNumber + "/" + testSize + " | time left: " + timeLeft + ") - path: " + path);

    final String base = path.substring(0, path.length() - 6);
    final String url = getTomcatUrl() + "/test.xhtml?base=" + URLEncoder.encode(base, "UTF-8");

    WebDriver webDriver = getWebDriver();
    webDriver.get(url);

    // move the mouse cursor away to avoid issues with CSS :hover event
    Actions actions = new Actions(webDriver);
    PointerInput pointerInput = new PointerInput(PointerInput.Kind.MOUSE, "default mouse");
    Interaction interaction = pointerInput.createPointerMove(Duration.ZERO, PointerInput.Origin.pointer(), 1, 1);
    actions.tick(interaction).build().perform();

    List<WebElement> results = getJasmineResults(webDriver, path);
    parseJasmineResults(results, path);
  }

  private static Stream<Arguments> standardTestProvider() throws IOException {
    final List<String> paths = getStandardTestPaths();
    final int testSize = paths.size();

    int testNumber = 1;
    List<Arguments> arguments = new LinkedList<>();
    for (String path : paths) {
      arguments.add(Arguments.of(path, testNumber, testSize));
      testNumber++;
    }
    return arguments.stream();
  }
}
