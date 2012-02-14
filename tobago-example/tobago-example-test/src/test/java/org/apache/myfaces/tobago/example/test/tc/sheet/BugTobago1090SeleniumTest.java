package org.apache.myfaces.tobago.example.test.tc.sheet;

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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class BugTobago1090SeleniumTest extends MultiSuffixSeleniumTest {

  public BugTobago1090SeleniumTest(String suffix) {
    super(suffix);
  }

  @Test
  public void testWithoutModel() throws InterruptedException {

    // load page
    open("/tc/sheet/sheet-bug-tobago-1090.");

    // select Venus
    getSelenium().click("page:sheet:2:name");

    // next page
    getSelenium().click("page:sheet::pagingPages::Next");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById('page:sheet:6:name') != null", "5000");

    // select Saturn
    getSelenium().click("page:sheet:6:name");

    // previous page
    getSelenium().click("page:sheet::pagingPages::Prev");
    getSelenium().waitForCondition(
        "selenium.browserbot.getCurrentWindow().document.getElementById('page:sheet:2:name') != null", "5000");

    // Venus is row 3
    Assert.assertEquals(
        "Only Saturn should be selected!",
        ",6,",
        getSelenium().getAttribute("page:sheet::selected@value"));
  }
}
