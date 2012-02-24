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
public class Tobago986SeleniumTest extends MultiSuffixSeleniumTest {

  public Tobago986SeleniumTest(String suffix) {
    super(suffix);
  }

  @Test
  public void test() throws InterruptedException {

    // load page
    open("/tc/sheet/sheet-sort.");

    sort(0, "A");
    sort(0, "Z");

    sort(1, "Z");
    sort(1, "Y");

    sort(2, "Y");
    sort(2, "X");

    sort(3, "X");
    sort(3, "W");

    sort(4, "W");
    sort(4, "V");

    sort(5, "V");
    sort(5, "U");
  }

  private void sort(int column, String exprected) {
    getSelenium().click("page:sheet::header_box_" + column);
    waitForAjaxComplete();
    String first = getSelenium().getText("page:sheet:0:out");
    Assert.assertEquals(exprected, first);
  }
}
