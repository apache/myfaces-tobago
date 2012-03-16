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
public class PagingSeleniumTest extends MultiSuffixSeleniumTest {

  public PagingSeleniumTest(String suffix) {
    super(suffix);
  }

  @Test
  public void test() throws InterruptedException {
    test20Rows();
    test11Rows();
    test1Row();
  }

  private void test20Rows() throws InterruptedException {

    // load page
    open("/tc/sheet/sheet-paging.");

    final String left = select(1, 1);
    Assert.assertTrue("row", left.contains("1"));
    Assert.assertTrue("row", left.contains("20"));
    Assert.assertTrue("row", left.contains("88"));

    final String center = select(1, 2);
    Assert.assertTrue("center", center.contains("1"));
    Assert.assertTrue("center", center.contains("2"));
    Assert.assertTrue("center", center.contains("3"));
    Assert.assertTrue("center", center.contains("4"));
    Assert.assertTrue("center", center.contains("5"));
    Assert.assertFalse("center", center.contains("6"));

    final String right = select(1, 3);
    Assert.assertTrue("right", right.contains("1"));
    Assert.assertTrue("right", right.contains("5"));
  }

  private void test11Rows() throws InterruptedException {

    // load page
    open("/tc/sheet/sheet-paging.");

    final String left = select(2, 1);
    Assert.assertTrue("row", left.contains("1"));
    Assert.assertTrue("row", left.contains("11"));
    Assert.assertTrue("row", left.contains("88"));

    final String center = select(2, 2);
    Assert.assertTrue("center", center.contains("1"));
    Assert.assertTrue("center", center.contains("2"));
    Assert.assertTrue("center", center.contains("3"));
    Assert.assertTrue("center", center.contains("4"));
    Assert.assertTrue("center", center.contains("5"));
    Assert.assertTrue("center", center.contains("6"));
    Assert.assertTrue("center", center.contains("7"));
    Assert.assertTrue("center", center.contains("8"));
    Assert.assertFalse("center", center.contains("9"));

    final String right = select(2, 3);
    Assert.assertTrue("right", right.contains("1"));
    Assert.assertTrue("right", right.contains("8"));
  }

  private void test1Row() throws InterruptedException {

    // load page
    open("/tc/sheet/sheet-paging.");

    final String left = select(3, 1);
    Assert.assertTrue("row", left.contains("1"));
    Assert.assertTrue("row", left.contains("88"));

    final String center = select(3, 2);
    Assert.assertTrue("center", center.contains("1"));
    Assert.assertTrue("center", center.contains("2"));
    Assert.assertTrue("center", center.contains("3"));
    Assert.assertTrue("center", center.contains("4"));
    Assert.assertTrue("center", center.contains("5"));
    Assert.assertTrue("center", center.contains("6"));
    Assert.assertTrue("center", center.contains("7"));
    Assert.assertTrue("center", center.contains("8"));
    Assert.assertTrue("center", center.contains("9"));
    Assert.assertFalse("center", center.contains("10"));

    final String right = select(3, 3);
    Assert.assertTrue("right", right.contains("1"));
    Assert.assertTrue("right", right.contains("88"));
  }

  private String select(int sheet, int pos) {
    return getSelenium().getText("//div[@id='page:s"+sheet+"']//div[@class='tobago-sheet-footer']/span["+pos+"]");
  }
}
