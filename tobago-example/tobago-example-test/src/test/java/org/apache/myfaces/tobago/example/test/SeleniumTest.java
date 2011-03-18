package org.apache.myfaces.tobago.example.test;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleniumException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public abstract class SeleniumTest {

  private static final String CONTEXT_PATH = "tobago-example-test";
  private static final String SERVLET_MAPPING = "faces";

  public static final String CONTAINS_A_404 = "contains a 404";
  public static final String HAS_ERROR_SEVERITY = "has error severity";
  public static final String IS_BROKEN = "is broken";

  private DefaultSelenium selenium;

  @Before
  public void setUp() throws Exception {
    selenium = createSeleniumClient();
    selenium.start();
  }

  @After
  public void tearDown() throws Exception {
    selenium.stop();
  }

  protected DefaultSelenium getSelenium() {
    return selenium;
  }

  protected DefaultSelenium createSeleniumClient() throws Exception {
    return new DefaultSelenium("localhost", 4444, "*firefox", "http://localhost:8080/");
  }

  protected void checkPage() {
    final String location = selenium.getLocation();
    final String html = getHtmlSource();
    Assert.assertFalse(format(CONTAINS_A_404, location, html, ""), pageNotFound());
    try {
      if (isErrorOnPage()) {
        Assert.fail(format(HAS_ERROR_SEVERITY, location, html, getErrors()));
      }
    } catch (SeleniumException e) {
      Assert.fail(format(IS_BROKEN, location, html, "Not a Tobago page? Exception=" + e));
    }
  }

  public String format(String error, String location, String html, String options) {
    StringBuilder b = new StringBuilder();
    b.append(error);
    b.append("\nPage URL: ");
    b.append(location);
    b.append("\n");
    b.append(options);
    b.append("\n---------------------------------------------------------------------------------------------------\n");
    b.append(html);
    b.append("\n---------------------------------------------------------------------------------------------------\n");
    return b.toString();
  }

  /**
   * Was the page not found?
   *
   * @return True if the page not found.
   */
  // XXX might be improved, I didn't find a way to read the HTTP status code
  protected boolean pageNotFound() {
    return selenium.getHtmlSource().contains("404");
  }

  /**
   * Checks the page for the Tobago JavaScript Logging Framework and tests its severity.
   *
   * @return True if the severity level of the page is error
   * @throws com.thoughtworks.selenium.SeleniumException
   *          If the page is not a Tobago page, or any other problem with JavaScrpt or the page.
   */
  protected boolean isErrorOnPage() throws SeleniumException {
    String errorSeverity = selenium.getEval("window.LOG.getMaximumSeverity() >= window.LOG.ERROR");
    return Boolean.parseBoolean(errorSeverity);
  }

  protected String getErrors() throws SeleniumException {
    String errors = selenium.getEval("window.LOG.getMessages(window.LOG.INFO)");
    return errors;
  }

  protected String getHtmlSource() {
    return selenium.getHtmlSource();
  }

  protected static String createUrl(String page) {
    Assert.assertTrue("Page name must start with a slash.", page.startsWith("/"));
    return '/' + CONTEXT_PATH + '/' + SERVLET_MAPPING + page;
  }
}
