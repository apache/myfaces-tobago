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

  public static List<Integer> parseIntegerList(final String integerList) throws NumberFormatException {
    return parseIntegerList(integerList, ", ");
  }

  public static List<Integer> parseIntegerList(final String integerList, final String delimiters)
      throws NumberFormatException {
    final List<Integer> list = new ArrayList<Integer>();

    final StringTokenizer tokenizer = new StringTokenizer(integerList, delimiters);
    while (tokenizer.hasMoreElements()) {
      final String token = tokenizer.nextToken().trim();
      if (token.length() > 0) {
        list.add(new Integer(token));
      }
    }

    return list;
  }

  public static <T> String joinWithSurroundingSeparator(final List<T> list) {
    final StringBuilder buffer = new StringBuilder(",");
    if (list != null) {
      for (final T t : list) {
        buffer.append(t);
        buffer.append(",");
      }
    }
    return buffer.toString();
  }

  public static int[] getIndices(final String list) {
    final List<String> indexList = new ArrayList<String>();
    final StringTokenizer st = new StringTokenizer(list, ",");
    while (st.hasMoreTokens()) {
      final String token = st.nextToken().trim();
      final int idx = token.indexOf('-');
      if (idx == -1) {
        indexList.add(token);
      } else {
        final int start = Integer.parseInt(token.substring(0, idx).trim());
        final int end = Integer.parseInt(token.substring(idx + 1).trim());
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

    final int[] indices = new int[indexList.size()];
    for (int i = 0; i < indices.length; i++) {
      indices[i] = Integer.parseInt(indexList.get(i));
    }
    return indices;
  }

  public static String constantToLowerCamelCase(final String constant) {
    final StringBuilder builder = new StringBuilder(constantToUpperCamelCase(constant));
    if (builder.length() > 0) {
      builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));
    }
    return builder.toString();
  }

  public static String constantToUpperCamelCase(final String constant) {
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
   * Basically taken from commons-lang
   */
  public static boolean endsWith(final String string, final String suffix) {
    if (string == null || suffix == null) {
      return (string == null && suffix == null);
    }
    if (suffix.length() > string.length()) {
      return false;
    }
    final int strOffset = string.length() - suffix.length();
    return string.regionMatches(false, strOffset, suffix, 0, suffix.length());
  }

  /**
   * Basically taken from commons-lang
   */
  public static String[] split(final String string, final char separator) {
    // Performance tuned for 2.0 (JDK1.4)

    if (string == null) {
      return null;
    }
    final int len = string.length();
    if (len == 0) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    final List<String> list = new ArrayList<String>();
    int i = 0;
    int start = 0;
    boolean match = false;
    while (i < len) {
      if (string.charAt(i) == separator) {
        if (match) {
          list.add(string.substring(start, i));
          match = false;
        }
        start = ++i;
        continue;
      }
      match = true;
      i++;
    }
    if (match) {
      list.add(string.substring(start, i));
    }
    return list.toArray(new String[list.size()]);
  }

  /**
   * Basically taken from commons-lang
   */
  public static String[] split(final String string, final String separator) {
    final int max = -1;
    // Performance tuned for 2.0 (JDK1.4)
    // Direct code is quicker than StringTokenizer.
    // Also, StringTokenizer uses isSpace() not isWhitespace()

    if (string == null) {
      return null;
    }
    final int len = string.length();
    if (len == 0) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    final List<String> list = new ArrayList<String>();
    int sizePlus1 = 1;
    int i = 0;
    int start = 0;
    boolean match = false;
    if (separator == null) {
      // Null separator means use whitespace
      while (i < len) {
        if (Character.isWhitespace(string.charAt(i))) {
          if (match) {
            if (sizePlus1++ == max) {
              i = len;
            }
            list.add(string.substring(start, i));
            match = false;
          }
          start = ++i;
          continue;
        }
        match = true;
        i++;
      }
    } else if (separator.length() == 1) {
      // Optimise 1 character case
      final char sep = separator.charAt(0);
      while (i < len) {
        if (string.charAt(i) == sep) {
          if (match) {
            if (sizePlus1++ == max) {
              i = len;
            }
            list.add(string.substring(start, i));
            match = false;
          }
          start = ++i;
          continue;
        }
        match = true;
        i++;
      }
    } else {
      // standard case
      while (i < len) {
        if (separator.indexOf(string.charAt(i)) >= 0) {
          if (match) {
            if (sizePlus1++ == max) {
              i = len;
            }
            list.add(string.substring(start, i));
            match = false;
          }
          start = ++i;
          continue;
        }
        match = true;
        i++;
      }
    }
    if (match) {
      list.add(string.substring(start, i));
    }
    return list.toArray(new String[list.size()]);
  }

  /**
   * Basically taken from commons-lang
   */
  public static boolean isAlpha(final String string) {
    if (string == null) {
      return false;
    }
    final int sz = string.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isLetter(string.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Basically taken from commons-lang
   */
  public static boolean isEmpty(final String value) {
    return value == null || value.length() == 0;
  }

  /**
   * Basically taken from commons-lang
   */
  public static boolean isNotEmpty(final String value) {
    return !isEmpty(value);
  }

  /**
   * Basically taken from commons-lang
   */
  public static boolean isBlank(final String string) {
    if (string == null) {
      return true;
    }
    final int strLen = string.length();
    if (strLen == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if ((!Character.isWhitespace(string.charAt(i)))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Basically taken from commons-lang
   */
  public static boolean isNotBlank(final String str) {
    return !isBlank(str);
  }

  /**
   * Basically taken from commons-lang
   */
  public static String replace(final String text, final String searchString, final String replacement) {
    int max = -1;
    if (isEmpty(text) || isEmpty(searchString) || replacement == null) {
      return text;
    }
    int start = 0;
    int end = text.indexOf(searchString, start);
    if (end == -1) {
      return text;
    }
    final int replLength = searchString.length();
    int increase = replacement.length() - replLength;
    increase = (increase < 0 ? 0 : increase);
    increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
    final StringBuilder buf = new StringBuilder(text.length() + increase);
    while (end != -1) {
      buf.append(text.substring(start, end)).append(replacement);
      start = end + replLength;
      if (--max == 0) {
        break;
      }
      end = text.indexOf(searchString, start);
    }
    buf.append(text.substring(start));
    return buf.toString();
  }

  /**
   * Basically taken from commons-lang
   */
  public static String repeat(final String str, final int repeat) {
    final int outputLength = str.length() * repeat;
    final StringBuilder buf = new StringBuilder(outputLength);
    for (int i = 0; i < repeat; i++) {
      buf.append(str);
    }
    return buf.toString();
  }

  /**
   * Returns a string of the same length to hide confidential passwords from log files etc.
   */
  public static String toConfidentialString(final String string, final boolean confidential) {
    if (string == null) {
      return "<null>";
    } else if (confidential) {
      return repeat("*", string.length()) + " (confidential)";
    } else {
      return string;
    }
  }

  /**
   * Basically taken from commons-lang
   */
  public static String uncapitalize(final String str) {
    return String.valueOf(Character.toLowerCase(str.charAt(0))) + str.substring(1);
  }

  /**
   * Basically taken from commons-lang
   */
  public static boolean isAlphanumeric(final String str) {
    final int sz = str.length();
    for (int i = 0; i < sz; i++) {
      if (!Character.isLetterOrDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Basically taken from commons-lang
   */
  public static String join(final List<String> list, final char separator) {
    final int size = list.size();
    if (size <= 0) {
      return "";
    }

    final int bufSize = size * list.get(0).length() + 1;
    final StringBuilder builder = new StringBuilder(bufSize);

    for (int i = 0; i < size; i++) {
      if (i > 0) {
        builder.append(separator);
      }
      final String string = list.get(i);
      if (string != null) {
        builder.append(string);
      }
    }
    return builder.toString();
  }

  /**
   * Basically taken from commons-lang
   */
  public static String defaultString(final String string) {
    return string == null ? "" : string;
  }

  /**
   * Basically taken from commons-lang
   */
  public static boolean notEquals(String a, String b) {
    return a == null ? b != null : !a.equals(b);
  }

  /**
   * Checks if the String starts like a url, e.g. http: or xyz:
   */
  public static boolean isUrl(final String link) {
    if (link == null) {
      return false;
    }
    int colon = link.indexOf(':');
    if (colon < 1) {
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
