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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public final class StringUtils {

  private StringUtils() {
    // utils class
  }

  public static List<Integer> parseIntegerList(String integerList) throws NumberFormatException {
    return parseIntegerList(integerList, ", ");
  }

  public static List<Integer> parseIntegerList(String integerList, String delimiters) throws NumberFormatException {
    List<Integer> list = new ArrayList<Integer>();

    StringTokenizer tokenizer = new StringTokenizer(integerList, delimiters);
    while (tokenizer.hasMoreElements()) {
      String token = tokenizer.nextToken().trim();
      if (token.length() > 0) {
        list.add(new Integer(token));
      }
    }

    return list;
  }

  public static <T> String joinWithSurroundingSeparator(List<T> list) {
    StringBuilder buffer = new StringBuilder(",");
    for (T t : list) {
      buffer.append(t);
      buffer.append(",");
    }
    return buffer.toString();
  }

  public static int[] getIndices(String list) {
    List<String> indexList = new ArrayList<String>();
    StringTokenizer st = new StringTokenizer(list, ",");
    while (st.hasMoreTokens()) {
      String token = st.nextToken().trim();
      int idx = token.indexOf('-');
      if (idx == -1) {
        indexList.add(token);
      } else {
        int start = Integer.parseInt(token.substring(0, idx).trim());
        int end = Integer.parseInt(token.substring(idx + 1).trim());
        if (start < end) {
          for (int i = start; i < end + 1; i++) {
            indexList.add(Integer.toString(i));
          }
        } else {
          for (int i = start; i > end - 1; i--) {
            indexList.add(Integer.toString(i));
          }
        }
      }
    }

    int[] indices = new int[indexList.size()];
    for (int i = 0; i < indices.length; i++) {
      indices[i] = Integer.parseInt(indexList.get(i));
    }
    return indices;
  }

  public static String constantToCamelCase(String constant) {
    final StringBuilder builder = new StringBuilder(constant.length());
    final char[] chars = constant.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      if (i == 0) {
        builder.append(chars[i]);
      } else if (chars[i] == '_') {
        builder.append(chars[++i]);
      } else {
        builder.append((((Character) chars[i]).toString().toLowerCase(Locale.ENGLISH)));
      }
    }
    return builder.toString();
  }

  /**
   * Is the same string, by ignoring differences that are only whitespaces.
   * (null and "" are not equal)
   */
  @SuppressWarnings("StringEquality")
  public static boolean equalsIgnoreCaseAndWhitespace(final String type1, final String type2) {

    // StringEquality
    if (type1 == type2) {
      return true;
    }

    if (type1 == null || type2 == null) {
      return false;
    }

    final char[] chars1 = type1.toCharArray();
    final char[] chars2 = type2.toCharArray();
    final int length1 = chars1.length;
    final int length = chars2.length;

    int i = 0;
    int j = 0;

    while (i < length1 && j < length) {
      if (chars1[i] == chars2[j] || Character.toUpperCase(chars1[i]) == Character.toUpperCase(chars2[j])) {
        i++;
        j++;
        // okay
      } else if (Character.isWhitespace(chars1[i])) {
        i++;
        // okay, ignore space
      } else if (Character.isWhitespace(chars2[j])) {
        j++;
        // try again
      } else {
        return false;
      }
    }

    while (i < length1) {
      if (Character.isWhitespace(chars1[i])) {
        i++;
        // okay, ignore space
      } else {
        return false;
      }
    }

    while (j < length) {
      if (Character.isWhitespace(chars2[j])) {
        j++;
        // okay, ignore space
      } else {
        return false;
      }
    }

    return true;
  }

  /**
   * Checks if the String starts like a url, e.g. http: or xyz:
   */
  public static boolean isUrl(final String link) {
    if (link == null) {
      return false;
    }
    int colon = link.indexOf(':');
    if (colon < 0) {
      return false;
    }
    for (int i = 0; i < colon; i++) {
      if (!Character.isLetter(link.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
