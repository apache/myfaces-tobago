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

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.SeleniumException;
import org.junit.Assert;

public class TobagoSelenium extends DefaultSelenium {

  public static final String ERROR_ON_SERVER = "error on server";
  public static final String HAS_ERROR_SEVERITY = "has error severity";
  public static final String IS_BROKEN = "is broken";

  private String browserUrl;

  public TobagoSelenium(final String browserUrl) {
    super(new HttpCommandProcessor("localhost", 4444, "*firefox", browserUrl)); //XXX should be replaced http://www.seleniumhq.org/docs/appendix_migrating_from_rc_to_webdriver.jsp#migrating-to-webdriver-reference
    this.browserUrl = browserUrl;
  }

  public String command(final String command, final String parameter1, final String parameter2) {
    return commandProcessor.doCommand(command, new String[]{parameter1, parameter2});
  }

  public void killSession() {
    open(browserUrl + "KillSession");
  }

  public void checkPage() {
    final String location = getLocation();
    final String html = getHtmlSource();
    if (errorOnServer()) {
      Assert.fail(format(ERROR_ON_SERVER, location, html, ""));
    }
    if (location.endsWith(".xhtml") || location.endsWith(".jspx")) {
      try {
        if (isErrorOnPage()) {
          Assert.fail(format(HAS_ERROR_SEVERITY, location, html, "TobagoAssert.failed"));
        }
      } catch (final SeleniumException e) {
        Assert.fail(format(IS_BROKEN, location, html, "Not a Tobago page? Exception=" + e));
      }
    }
  }

  public String format(final String error, final String location, final String html, final String options) {
    final StringBuilder b = new StringBuilder();
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
   * Happen an exception from MyFaces on the page?
   */
  protected boolean errorOnServer() {
    return getHtmlSource().contains("An Error Occurred");
  }

  /**
   * Checks the page for the Tobago JavaScript Logging Framework and tests its severity.
   *
   * @return True if the severity level of the page is error
   * @throws com.thoughtworks.selenium.SeleniumException
   *          If the page is not a Tobago page, or any other problem with JavaScript or the page.
   */
  protected boolean isErrorOnPage() throws SeleniumException {
    final String errorSeverity = getEval("window.TobagoAssert && window.TobagoAssert.failed");
    return Boolean.parseBoolean(errorSeverity);
  }
}
