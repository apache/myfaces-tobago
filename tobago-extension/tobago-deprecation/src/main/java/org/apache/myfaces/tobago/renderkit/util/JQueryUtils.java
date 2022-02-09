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

package org.apache.myfaces.tobago.renderkit.util;

/**
 * @deprecated since 2.0.0. JavaScript should not be used no server side (CSP)
 */
@Deprecated
public class JQueryUtils {

  private JQueryUtils() {
    // to prevent instantiation
  }

  /**
   * Puts two backslashes before : and . to escape them.
   * Two are needed, because of JavaScript string literals.
   */
  public static String escapeId(final String id) {
    return id.replaceAll("([\\:\\.])", "\\\\\\\\$1");
  }

  /**
   * Puts one backslashes before \ to escape it.
   * It is needed, because of JavaScript string literals.
   */
  public static String escapeValue(final String value) {
    return value.replaceAll("\\\\", "\\\\\\\\");
  }

  /**
   * Puts two backslashes before #;&,.+*~':"!^$[]()=>|/ to escape them.
   * Two are needed, because of JavaScript string literals.
   * Puts three backslashes before a \ itself, to escape it.
   */
  public static String escapeSelector(final String value) {
    final StringBuilder builder = new StringBuilder();
    for (final char c : value.toCharArray()) {
      switch (c) {
        case '\\':
          builder.append("\\\\\\\\");
          break;
        case '#':
        case ';':
        case '&':
        case ',':
        case '.':
        case '+':
        case '*':
        case '~':
        case '\'':
        case ':':
        case '"':
        case '!':
        case '^':
        case '$':
        case '[':
        case ']':
        case '(':
        case ')':
        case '=':
        case '>':
        case '|':
        case '/':
          builder.append("\\\\");
        default:
          builder.append(c);
          break;
      }
    }
    return builder.toString();
  }

  /**
   * Creates a selector for an id like jQuery('#id').
   * The id will be escaped if necessary.
   */
  public static String selectId(final String id) {
    final StringBuilder builder = new StringBuilder();
    builder.append("jQuery('#");
    builder.append(escapeId(id));
    builder.append("')");
    return builder.toString();
  }
}
