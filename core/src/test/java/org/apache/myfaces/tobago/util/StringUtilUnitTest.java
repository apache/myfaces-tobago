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
/*
 * Created 17.03.2004 11:16:26.
 * $Id:StringUtilUnitTest.java 1300 2005-08-10 16:40:23 +0200 (Mi, 10 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.util;

import junit.framework.TestCase;

public class StringUtilUnitTest extends TestCase {

  public void test() {
    assertNull(StringUtil.firstToUpperCase(null));
    assertEquals("", StringUtil.firstToUpperCase(""));
    assertEquals("1", StringUtil.firstToUpperCase("1"));
    assertEquals("X", StringUtil.firstToUpperCase("x"));
    assertEquals("Xxx", StringUtil.firstToUpperCase("xxx"));
    assertEquals("XxxXxxx", StringUtil.firstToUpperCase("xxxXxxx"));
    assertEquals("Xxx", StringUtil.firstToUpperCase("Xxx"));
    assertEquals(" x ", StringUtil.firstToUpperCase(" x "));
  }
}
