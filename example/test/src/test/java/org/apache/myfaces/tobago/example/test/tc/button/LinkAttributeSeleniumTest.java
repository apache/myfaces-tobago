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

package org.apache.myfaces.tobago.example.test.tc.button;

import org.apache.myfaces.tobago.example.test.MultiSuffixSeleniumTest;
import org.apache.myfaces.tobago.example.test.Parameterized;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Parameterized.class)
public class LinkAttributeSeleniumTest extends MultiSuffixSeleniumTest {

  public LinkAttributeSeleniumTest(String suffix) {
    super(suffix);
  }

  @Test
  public void testInternalLinkWithSlash() {
    open("/tc/button/link-attribute.");
    selenium.click("page:button-internal-link-with-slash");
    Assert.assertEquals(
        getHtmlSource(),
        "A simple page for the resource test (static).", selenium.getText("//html/body"));
  }

  @Test
  public void testInternalLinkWithoutSlash() {
    open("/tc/button/link-attribute.");
    selenium.click("page:button-internal-link-without-slash");
    Assert.assertEquals(
        getHtmlSource(),
        "A simple page for the resource test (static).", selenium.getText("//html/body"));
  }

  @Test
  public void testExternalLink() throws InterruptedException {
    open("/tc/button/link-attribute.");
    selenium.click("page:button-external-link");
    // XXX This sleep call is not nice...
    sleep();
    // go to the apache home page
    Assert.assertTrue(
        getHtmlSource(),
        selenium.getText("//html/head/title").contains("Apache Software Foundation"));
  }
}
