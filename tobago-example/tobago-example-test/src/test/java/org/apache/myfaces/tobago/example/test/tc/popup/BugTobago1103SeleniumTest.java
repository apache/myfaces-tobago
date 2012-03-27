package org.apache.myfaces.tobago.example.test.tc.popup;

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

import org.apache.myfaces.tobago.example.test.MultiSuffixSeleniumTest;
import org.apache.myfaces.tobago.util.Parameterized;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class BugTobago1103SeleniumTest extends MultiSuffixSeleniumTest {

  public BugTobago1103SeleniumTest(String suffix) {
    super(suffix);
  }

  @Test
  public void testWithoutModel() throws InterruptedException {

    // XXX not nice: autogenerated id
    final String extraId = isFacelet() ? "" : "j_id_id31tx2:";

    // load page
    open("/tc/popup/popup-bug-tobago-1103.");

    // click on open 1st popup
    getSelenium().click("page:open-0");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById('page:popup-1:open-1') != null", "5000");

    // click on open 2nd popup
    getSelenium().click("page:popup-1:open-1");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById(" +
            "'page:popup-1:popup-2:" + extraId + "picker') != null", "5000");

    // click on open date popup
    getSelenium().click("page:popup-1:popup-2:" + extraId + "picker");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById(" +
            "'page:popup-1:popup-2:" + extraId + "pickerpopup:cancel') != null", "5000");

    // click on close date popup
    getSelenium().click("page:popup-1:popup-2:" + extraId + "pickerpopup:cancel");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById(" +
            "'page:popup-1:popup-2:" + extraId + "pickerpopup:cancel') == null", "5000");

    // click on close 2nd popup
    getSelenium().click("page:popup-1:popup-2:close-2");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById(" +
            "'page:popup-1:popup-2') == null", "5000");

    // click on close 1st popup
    getSelenium().click("page:popup-1:close-1");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById(" +
            "'page:popup-1') == null", "5000");

    //////////////////////// all closed now

    // click on open 1st popup
    getSelenium().click("page:open-0");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById('page:popup-1:open-1') != null", "5000");

    // click on open 2nd popup
    getSelenium().click("page:popup-1:open-1");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById(" +
            "'page:popup-1:popup-2:" + extraId + "picker') != null", "5000");

    // click on open 3rd popup
    getSelenium().click("page:popup-1:popup-2:open-2");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById(" +
            "'page:popup-1:popup-2:popup-3') != null", "5000");

    // click on close date popup
    getSelenium().click("page:popup-1:popup-2:popup-3:close-3");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById(" +
            "'page:popup-1:popup-2:popup-3') == null", "5000");

    // click on close 2nd popup
    getSelenium().click("page:popup-1:popup-2:close-2");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById(" +
            "'page:popup-1:popup-2') == null", "5000");

    // click on close 1st popup
    getSelenium().click("page:popup-1:close-1");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById(" +
            "'page:popup-1') == null", "5000");


  }
}
