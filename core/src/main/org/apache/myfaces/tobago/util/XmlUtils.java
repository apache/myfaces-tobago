package org.apache.myfaces.tobago.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XmlUtils {

  private static final Log LOG = LogFactory.getLog(XmlUtils.class);

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
