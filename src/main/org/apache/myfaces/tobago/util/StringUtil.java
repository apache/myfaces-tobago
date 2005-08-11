/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 17.03.2004 11:12:44.
 * $Id$
 */
package org.apache.myfaces.tobago.util;

public class StringUtil {

  public static String firstToUpperCase(String string) {
    if (string == null) {
      return null;
    }
    switch (string.length()) {
      case 0:
        return string;
      case 1:
        return string.toUpperCase();
      default:
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }
  }

}
