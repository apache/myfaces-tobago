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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

class StandardTest extends SeleniumBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * To test only a singe page, just change browser setup, 'portContextPath' and/or 'path'.
   * Start the docker container with mvn -Pdocker-qunit-tests docker:start
   */
  @Test
  void testSinglePage() throws MalformedURLException, UnknownHostException, UnsupportedEncodingException {
    final Browser browser = Browser.chrome;
    final String serverUrl = getServerUrls().get(0);
    final String path = "content/10-intro/intro.xhtml";

    LOG.info("browser: " + browser + " - url: " + serverUrl + "/" + path);

    setupWebDriver(browser, serverUrl, path, false);
    parseQUnitResults(browser, serverUrl, path);
  }

  @ParameterizedTest
  @MethodSource("standardTestProvider")
  void testStandard(Browser browser, String serverUrl, String path, LocalTime startTime, int testSize, int testNo)
      throws MalformedURLException, UnsupportedEncodingException {

    double percent = 100 * (double) testNo / testSize;
    final String timeLeft = getTimeLeft(startTime, testSize, testNo);

    LOG.info("(" + String.format("%.2f", percent) + " % complete" + " | time left: " + timeLeft + ")"
        + " browser: " + browser + " - url: " + serverUrl + "/" + path);

    if (isIgnored(serverUrl, path)) {
      logIgnoreMessage(serverUrl, path);
    } else {
      setupWebDriver(browser, serverUrl, path, false);
      parseQUnitResults(browser, serverUrl, path);
    }
  }

  private static Stream<Arguments> standardTestProvider() throws IOException {
    final List<String> paths = getStandardTestPaths();
    return getArguments(paths);
  }
}
