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

  public static final String UNMARKED = "tobago-treeNode";
  public static final String MARKED = UNMARKED + " " + "tobago-treeNode-markup-marked";

  public TreeMarkedSeleniumTest(String suffix) {
    super(suffix);
  }

  /**
   * The state is not stored in the model, so a new call of the URL directly will result in a unmarked node 1.
   */
  @Test
  public void testWithoutModel() {
    test("/tc/tree/tree-marked-without-model.", UNMARKED);
    test("/tc/tree/tree-marked-without-model.", UNMARKED);
  }

  /**
   * The state is stored in the model, so a new call of the URL directly will result in a marked node 1,
   * because this information was hold in the session.
   */
  @Test
  public void testWithModel() {
    test("/tc/tree/tree-marked-with-model.", UNMARKED);
    test("/tc/tree/tree-marked-with-model.", MARKED); // because state was stored in the session
    resetState();
  }

  private void test(String name, String initial) {

    // load page
    open(name);
    Assert.assertEquals(
        "Node 1 should not be marked!", initial, getSelenium().getAttribute("page:tree:1:node@class"));
    Assert.assertEquals(
        "Node 2 should not be marked!", UNMARKED, getSelenium().getAttribute("page:tree:2:node@class"));

    // click on tree node 1
    getSelenium().click("page:tree:1:command");
    getSelenium().waitForPageToLoad("5000");

    Assert.assertEquals(
        "Node 1 should be marked now!", MARKED, getSelenium().getAttribute("page:tree:1:node@class"));
    Assert.assertEquals(
        "Node 2 should not be marked!", UNMARKED, getSelenium().getAttribute("page:tree:2:node@class"));
  }

  private void resetState() {
    // click clear to reset the state
    getSelenium().click("page:clear");
    getSelenium().waitForPageToLoad("5000");
  }

}
