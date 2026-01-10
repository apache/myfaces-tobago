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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.List;

@Testcontainers
class BasicTest extends FrontendBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static LocalTime startTime;

  @BeforeAll
  public static void setup() {
    startTime = LocalTime.now();
  }

  @Test
  void verifyExceptionTest() throws MalformedURLException, UnknownHostException, UnsupportedEncodingException {
    final String path = "error/exception.xhtml";

    final String base = path.substring(0, path.length() - 6);
    final String url = getTomcatUrl() + "/test.xhtml?base=" + URLEncoder.encode(base, "UTF-8") + "&basicTest=true";

    WebDriver webDriver = getWebDriver();
    webDriver.get(url);

    List<WebElement> results = getJasmineResults(webDriver, path);
    Assertions.assertTrue(results.size() > 0, "no results detected");
    for (WebElement result : results) {
      if ("has no exception".equals(result.getAttribute("title"))) {
        Assertions.assertEquals("jasmine-failed", result.getAttribute("class"), result.getAttribute("title"));
      } else {
        Assertions.assertEquals("jasmine-passed", result.getAttribute("class"), result.getAttribute("title"));
      }
    }
  }

  @Test
  void verify404Test() throws MalformedURLException, UnknownHostException, UnsupportedEncodingException {
    final String path = "error/404.xhtml";

    final String base = path.substring(0, path.length() - 6);
    final String url = getTomcatUrl() + "/test.xhtml?base=" + URLEncoder.encode(base, "UTF-8") + "&basicTest=true";

    WebDriver webDriver = getWebDriver();
    webDriver.get(url);

    List<WebElement> results = getJasmineResults(webDriver, path);
    Assertions.assertTrue(results.size() > 0, "no results detected");
    for (WebElement result : results) {
      if ("has no 404".equals(result.getAttribute("title"))) {
        Assertions.assertEquals("jasmine-failed", result.getAttribute("class"), result.getAttribute("title"));
      } else {
        Assertions.assertEquals("jasmine-passed", result.getAttribute("class"), result.getAttribute("title"));
      }
    }
  }
}
