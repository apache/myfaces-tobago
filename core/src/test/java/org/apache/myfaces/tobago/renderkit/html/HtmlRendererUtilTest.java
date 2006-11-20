package org.apache.myfaces.tobago.renderkit.html;

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

import junit.framework.TestCase;

/*
 * User: bommel
 * Date: Nov 19, 2006
 * Time: 7:07:31 PM
 */
public class HtmlRendererUtilTest extends TestCase {

  public void test() {
    assertEquals("bla bla bla bla bla", HtmlRendererUtil.removeTobagoClasses("bla bla bla tobago-test-inline bla bla", "test"));
    assertEquals("blablubber bla", HtmlRendererUtil.removeTobagoClasses("tobago-test-inline blablubber bla", "test"));
    assertEquals("bla bla bla tobago-2test-inline bla bla blubb",
        HtmlRendererUtil.removeTobagoClasses("bla bla bla tobago-2test-inline bla tobago-test-blubber bla blubb", "test"));
    assertEquals("bla bla bla ",
        HtmlRendererUtil.removeTobagoClasses("bla bla bla tobago-testXXX", "test"));
    assertEquals("", HtmlRendererUtil.removeTobagoClasses("tobago-test", "test"));
    //XXX
    assertEquals("  x x ", HtmlRendererUtil.removeTobagoClasses(" x x ", "test"));
    assertEquals("", HtmlRendererUtil.removeTobagoClasses("", "test"));
    assertEquals("hallo", HtmlRendererUtil.removeTobagoClasses("hallo", "test"));
  }
}
