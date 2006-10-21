package org.apache.myfaces.tobago.util;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Locale;

public class StringUtil {

  private StringUtil() {
  }

  public static String firstToUpperCase(String string) {
    if (string == null) {
      return null;
    }
    switch (string.length()) {
      case 0:
        return string;
      case 1:
        return string.toUpperCase(Locale.ENGLISH);
      default:
        return string.substring(0, 1).toUpperCase(Locale.ENGLISH) + string.substring(1);
    }
  }

  public static List<Integer> parseIntegerList(String integerList)
      throws NumberFormatException {
    List<Integer> list = new ArrayList<Integer>();

    StringTokenizer tokenizer = new StringTokenizer(integerList, ", ");
    while (tokenizer.hasMoreElements()) {
      String token = tokenizer.nextToken().trim();
      if (token.length() > 0) {
        list.add(new Integer(token));
      }
    }

    return list;
  }

  public static <T> String toString(List<T> list) {
    StringBuffer buffer = new StringBuffer(",");
    for (T t : list) {
      buffer.append(t);
      buffer.append(",");
    }
    return buffer.toString();
  }

  public static String escapeAccessKeyIndicator(String label) {
    return StringUtils.replace(label,
        String.valueOf(LabelWithAccessKey.INDICATOR), LabelWithAccessKey.ESCAPED_INDICATOR);
  }

}
