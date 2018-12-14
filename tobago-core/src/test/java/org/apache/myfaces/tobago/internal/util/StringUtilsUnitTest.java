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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilsUnitTest {

  @Test
  public void testEqualsIgnoreCaseAndWhitespace() {
    Assertions.assertTrue(StringUtils.equalsIgnoreCaseAndWhitespace(null, null));
    Assertions.assertTrue(StringUtils.equalsIgnoreCaseAndWhitespace("", ""));
    Assertions.assertTrue(StringUtils.equalsIgnoreCaseAndWhitespace("", " "));
    Assertions.assertTrue(StringUtils.equalsIgnoreCaseAndWhitespace(" ", " "));
    Assertions.assertTrue(StringUtils.equalsIgnoreCaseAndWhitespace(
        "text/html;charset=utf-8", "  text/HTML;  charset=UTF-8  "));

    Assertions.assertFalse(StringUtils.equalsIgnoreCaseAndWhitespace(";", ""));
    Assertions.assertFalse(StringUtils.equalsIgnoreCaseAndWhitespace(";", ";;"));
    Assertions.assertFalse(StringUtils.equalsIgnoreCaseAndWhitespace(" a ", " ä "));
  }

  @Test
  public void testIsUrl() {
    Assertions.assertTrue(StringUtils.isUrl("http://www.apache.org/"));
    Assertions.assertTrue(StringUtils.isUrl("http:"));
    Assertions.assertTrue(StringUtils.isUrl("ftp:"));
    Assertions.assertTrue(StringUtils.isUrl("abc:fjdskal:fdsa"));

    Assertions.assertFalse(StringUtils.isUrl(null));
    Assertions.assertFalse(StringUtils.isUrl("null"));
    Assertions.assertFalse(StringUtils.isUrl("/test"));
    Assertions.assertFalse(StringUtils.isUrl("test.xhtml?id=#page:input"));
    Assertions.assertFalse(StringUtils.isUrl(":test"));
  }

  @Test
  public void testGetIndices0() {

    int[] ints = {0, 5, 10};
    Assertions.assertArrayEquals(ints, StringUtils.getIndices("0,5,10"));
    Assertions.assertArrayEquals(ints, StringUtils.getIndices("0, 5, 10"));
    Assertions.assertArrayEquals(ints, StringUtils.getIndices(" 0 , 5 , 10 "));

  }

  @Test
  public void testGetIndices1() {

    int[] ints = new int[]{3, 4, 5, 6, 7, 15, 16, 17};
    Assertions.assertArrayEquals(ints, StringUtils.getIndices("3-7,15-17"));
    Assertions.assertArrayEquals(ints, StringUtils.getIndices("3-5,6,7,15,16-17"));
    Assertions.assertArrayEquals(ints, StringUtils.getIndices("3-5, 6, 7, 15, 16 - 17 "));
  }

  @Test
  public void testGetIndices2() {

    int[] ints = new int[]{3, 4, 5, 6, 7, 15, 14, 13};
    Assertions.assertArrayEquals(ints, StringUtils.getIndices("3-7,15-13"));
    Assertions.assertArrayEquals(ints, StringUtils.getIndices("3 - 7, 15 - 13"));
  }

  @Test
  public void testGetIndices3() {

    int[] ints = new int[]{};
    Assertions.assertArrayEquals(ints, StringUtils.getIndices(null));
    Assertions.assertArrayEquals(ints, StringUtils.getIndices(""));
    Assertions.assertArrayEquals(ints, StringUtils.getIndices(" "));
  }

}
