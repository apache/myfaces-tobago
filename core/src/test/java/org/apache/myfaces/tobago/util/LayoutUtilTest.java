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

package org.apache.myfaces.tobago.util;

import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;

/**
 * Created 04.02.2009 10:48:34
 */
public class LayoutUtilTest extends TestCase {

  public void testCheckTokens() {
    assertTrue(LayoutUtil.checkTokens("1px;*;25%;fixed"));
    assertFalse(LayoutUtil.checkTokens("1px; *; fixed")); // XXX whitespace shouldn't matter
    assertTrue(LayoutUtil.checkTokens("0px"));
    assertTrue(LayoutUtil.checkTokens("1px"));
    assertTrue(LayoutUtil.checkTokens("99999px"));
    assertTrue(LayoutUtil.checkTokens("*"));
    assertTrue(LayoutUtil.checkTokens("1*"));
    assertTrue(LayoutUtil.checkTokens("99*"));
    assertTrue(LayoutUtil.checkTokens("0%"));
    assertTrue(LayoutUtil.checkTokens("1%"));
    assertTrue(LayoutUtil.checkTokens("200%")); // XXX percentile value over 100 doesn't make much sense, does it?
    assertTrue(LayoutUtil.checkTokens("fixed"));
  }

  public void testStripNonNumericChars() {
    assertEquals(null, LayoutUtil.stripNonNumericChars(null));
    assertEquals("", LayoutUtil.stripNonNumericChars(""));
    assertEquals("1", LayoutUtil.stripNonNumericChars("1px"));
    assertEquals("99", LayoutUtil.stripNonNumericChars("99px"));
    assertEquals("50", LayoutUtil.stripNonNumericChars("50%"));
    assertEquals("3", LayoutUtil.stripNonNumericChars("3*"));
    assertEquals("", LayoutUtil.stripNonNumericChars("*"));
  }

  public void testIsNumberAndSuffix() {
    assertTrue(LayoutUtil.isNumberAndSuffix("34cm", "cm"));
    assertFalse(LayoutUtil.isNumberAndSuffix("acm", "cm"));
    assertFalse(LayoutUtil.isNumberAndSuffix("cm", "cm"));
  }

  public void testSplit() {
    assertEquals("ab", StringUtils.split("ab, sd", " ,")[0]);
  }

}
