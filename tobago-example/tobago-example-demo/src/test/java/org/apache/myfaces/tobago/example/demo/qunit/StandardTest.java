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
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class StandardTest extends SeleniumBase {

  private static final Logger LOG = LoggerFactory.getLogger(StandardTest.class);

  /**
   * To test only a singe page, just change browser setup, 'portContextPath' and/or 'path'.
   * Start the docker container with mvn -Pdocker-qunit-tests docker:start
   */
  @Test
  void testSinglePage() throws MalformedURLException, UnknownHostException, UnsupportedEncodingException {
    final Browser browser = Browser.chrome;
    final String portContextPath = serverPortWithContextPath[0];
    final String path = "content/10-intro/intro.xhtml";

    LOG.info("browser: " + browser + " - port and context path: " + portContextPath + " - path: " + path);

    setupBrowser(browser);
    setupWebDriver(portContextPath, path, false);

    parseQUnitResults(browser, portContextPath, path);

    webDriver.quit();
  }

  @ParameterizedTest
  @MethodSource("standardTestProvider")
  void testStandard(Browser browser, String portContextPath, String path) throws MalformedURLException,
      UnknownHostException, UnsupportedEncodingException {
    LOG.info("browser: " + browser + " - port and context path: " + portContextPath + " - path: " + path);

    setupBrowser(browser);
    setupWebDriver(portContextPath, path, false);

    parseQUnitResults(browser, portContextPath, path);

    webDriver.quit();
  }

  private static Stream<Arguments> standardTestProvider() throws IOException {
    final List<String> paths = Files.walk(Paths.get("src/main/webapp/content/"))
        .filter(Files::isRegularFile)
        .map(Path::toString)
        .filter(s -> s.endsWith(".test.js"))
        .map(s -> s.substring("src/main/webapp/".length()))
        .sorted()
        .map(s -> s.substring(0, s.length() - 8) + ".xhtml")
        .collect(Collectors.toList());

    List<Arguments> arguments = new LinkedList<>();

    for (Browser browser : Browser.values()) {
      for (String portWithContextPath : serverPortWithContextPath) {
        for (String path : paths) {
          arguments.add(Arguments.of(browser, portWithContextPath, path));
        }
      }
    }

    return arguments.stream();
  }
}
