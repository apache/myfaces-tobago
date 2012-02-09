package org.apache.myfaces.tobago.example.test.tc.tree;

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
public class TreeMarkedSeleniumTest extends MultiSuffixSeleniumTest {

  public TreeMarkedSeleniumTest(String suffix) {
    super(suffix);
  }

  @Test
  public void testHtmlResource() throws InterruptedException {

    final String unmarked = "tobago-treeNode";
    final String marked = unmarked + " " + "tobago-treeNode-markup-marked";

    // load page
    open("/tc/tree/tree-marked-with-model.");
    Assert.assertEquals(
        "Node 1 should not be marked!", unmarked, getSelenium().getAttribute("page:tree:1:node@class"));
    Assert.assertEquals(
        "Node 2 should not be marked!", unmarked, getSelenium().getAttribute("page:tree:2:node@class"));

    // click on tree node 1
    getSelenium().click("page:tree:1:command");
    getSelenium().waitForPageToLoad("5000");

    Assert.assertEquals(
        "Node 1 should be marked now!", marked, getSelenium().getAttribute("page:tree:1:node@class"));
    Assert.assertEquals(
        "Node 2 should not be marked!", unmarked, getSelenium().getAttribute("page:tree:2:node@class"));
  }

}
