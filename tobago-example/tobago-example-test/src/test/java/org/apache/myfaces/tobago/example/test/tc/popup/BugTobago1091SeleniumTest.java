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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class BugTobago1091SeleniumTest extends MultiSuffixSeleniumTest {

  public BugTobago1091SeleniumTest(String suffix) {
    super(suffix);
  }

  @Test
  public void testWithoutModel() throws InterruptedException {

    // load page
    open("/tc/popup/popup-bug-tobago-1091.");
    Assert.assertThat(
        "Checkbox should be checked!",
        getSelenium().getAttribute("page:check@checked"),
        anyOf(is("true"), is("checked")));

    // click on open popup
    getSelenium().click("page:open");
    getSelenium().waitForPageToLoad("5000");

    // click on open popup
    getSelenium().click("page:popup:close");
    getSelenium().waitForPageToLoad("5000");

    Assert.assertThat(
        "Checkbox should be checked!",
        getSelenium().getAttribute("page:check@checked"),
        anyOf(is("true"), is("checked")));
  }
}
