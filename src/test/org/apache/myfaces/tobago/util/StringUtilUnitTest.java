/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 17.03.2004 11:16:26.
 * $Id$
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
