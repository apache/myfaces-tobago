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

public class XmlUtils {

  public static String escape(String s) {
    return escape(s, true);
  }

  public static String escape(String s, boolean isAttributeValue) {
    if (null == s) {
      return "";
    }
    int len = s.length();
    StringBuffer buffer = new StringBuffer(len);
    for (int i = 0; i < len; i++) {
      appendEntityRef(buffer, s.charAt(i), isAttributeValue);
    }
    return buffer.toString();
  }

  private static void appendEntityRef(StringBuffer buffer, char ch,
      boolean isAttributeValue) {
    // Encode special XML characters into the equivalent character references.
    // These five are defined by default for all XML documents.
    switch (ch) {
      case '<':
        buffer.append("&lt;");
        break;
      case '&':
        buffer.append("&amp;");
        break;
      case '"': // need inside attributes values
        if (isAttributeValue) {
          buffer.append("&quot;");
        } else {
          buffer.append(ch);
        }
        break;
      case '\'': // need inside attributes values
        if (isAttributeValue) {
          buffer.append("&apos;");
        } else {
          buffer.append(ch);
        }
        break;
      case '>': // optional
        buffer.append("&gt;");
        break;
      default:
        buffer.append(ch);
    }
  }
}
