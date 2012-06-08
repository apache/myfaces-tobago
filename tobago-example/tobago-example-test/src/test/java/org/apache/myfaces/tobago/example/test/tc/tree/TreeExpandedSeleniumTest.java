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
public class TreeExpandedSeleniumTest extends MultiSuffixSeleniumTest {

  public static final String EXPANDED = "tobago-treeNode-markup-expanded";

  public TreeExpandedSeleniumTest(String suffix) {
    super(suffix);
  }

  /**
   * The state is not stored in the model, so a new call of the URL directly will result in a unmarked node 1.
   */
  @Test
  public void testWithoutModel() {
    // load page
    open("/tc/tree/tree-expanded.");

    // Reset tree expansion state
    getSelenium().click("page:reset");
    getSelenium().waitForPageToLoad("5000");

    // check
    Assert.assertTrue(
        "Node 'Science' should be expanded!",
        getSelenium().getAttribute("page:tree:8:node@class").contains(EXPANDED));

    // click on the toggle of node "Science"
    getSelenium().click("//div[@id='page:tree:8:node']/span/img[2]");

    // check
    Assert.assertTrue(
        "Node 'Science' should not be expanded!",
        !getSelenium().getAttribute("page:tree:8:node@class").contains(EXPANDED));

    // click on the command of node "Games"
    getSelenium().click("//div[@id='page:tree:7:node']/a[1]");
    getSelenium().waitForPageToLoad("5000");

    // check
    Assert.assertTrue(
        "Node 'Science' should not be expanded!",
        !getSelenium().getAttribute("page:tree:8:node@class").contains(EXPANDED));
  }
}
