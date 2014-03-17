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

package org.apache.myfaces.tobago.internal.util;

import junit.framework.Assert;
import org.junit.Test;

public class StringUtilsUnitTest {

  @Test
  public void testEqualsIgnoreCaseAndWhitespace() {
    Assert.assertTrue(StringUtils.equalsIgnoreCaseAndWhitespace(null, null));
    Assert.assertTrue(StringUtils.equalsIgnoreCaseAndWhitespace("", ""));
    Assert.assertTrue(StringUtils.equalsIgnoreCaseAndWhitespace("", " "));
    Assert.assertTrue(StringUtils.equalsIgnoreCaseAndWhitespace(" ", " "));
    Assert.assertTrue(StringUtils.equalsIgnoreCaseAndWhitespace(
        "text/html;charset=utf-8", "  text/HTML;  charset=UTF-8  "));

    Assert.assertFalse(StringUtils.equalsIgnoreCaseAndWhitespace(";", ""));
    Assert.assertFalse(StringUtils.equalsIgnoreCaseAndWhitespace(";", ";;"));
    Assert.assertFalse(StringUtils.equalsIgnoreCaseAndWhitespace(" a ", " ä "));
  }

  @Test
  public void testIsUrl() {
    Assert.assertTrue(StringUtils.isUrl("http://www.apache.org/"));
    Assert.assertTrue(StringUtils.isUrl("http:"));
    Assert.assertTrue(StringUtils.isUrl("ftp:"));
    Assert.assertTrue(StringUtils.isUrl("abc:fjdskal:fdsa"));

    Assert.assertFalse(StringUtils.isUrl(null));
    Assert.assertFalse(StringUtils.isUrl("null"));
    Assert.assertFalse(StringUtils.isUrl("/test"));
    Assert.assertFalse(StringUtils.isUrl("test.xhtml?id=#page:input"));
    Assert.assertFalse(StringUtils.isUrl(":test"));
  }
}
