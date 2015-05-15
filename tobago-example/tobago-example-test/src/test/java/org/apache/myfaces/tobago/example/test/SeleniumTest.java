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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public abstract class SeleniumTest {

  private static final String CONTEXT_PATH = "";
  //  private static final String CONTEXT_PATH = "tobago-example-test";
  private static final String SERVLET_MAPPING = "faces";

  private static TobagoSelenium selenium;

  @BeforeClass
  public static void setUp() throws Exception {
    WebDriver driver = new FirefoxDriver();
    String baseUrl = "http://localhost:8080/";
    selenium = new TobagoSelenium(driver, baseUrl);
  }

  @AfterClass
  public static void tearDown() throws Exception {
    selenium.stop();
  }

  protected TobagoSelenium getSelenium() {
    return selenium;
  }

  protected static String createUrl(final String page) {
    Assert.assertTrue("Page name must start with a slash.", page.startsWith("/"));
    if (CONTEXT_PATH.length() == 0) {
      return '/' + SERVLET_MAPPING + page;
    } else {
      return '/' + CONTEXT_PATH + '/' + SERVLET_MAPPING + page;
    }
  }
}
