package org.apache.myfaces.tobago.internal.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.Writer;

/**
 * User: weber
 * Date: Jun 28, 2005
 * Time: 2:07:29 PM
 */
public final class HtmlWriterUtils {

  private static final char[][] CHARS_TO_ESCAPE;

  static {
    // init lookup table
    CHARS_TO_ESCAPE = new char[0xA0][];
    CHARS_TO_ESCAPE['"'] = "&quot;".toCharArray();
    CHARS_TO_ESCAPE['&'] = "&amp;".toCharArray();
    CHARS_TO_ESCAPE['<'] = "&lt;".toCharArray();
    CHARS_TO_ESCAPE['>'] = "&gt;".toCharArray();
    CHARS_TO_ESCAPE['\n'] = "&#13;".toCharArray();
    CHARS_TO_ESCAPE['\r'] = "&#10;".toCharArray();
  }

  private final Writer out;

  private final ResponseWriterBuffer buffer;

  private final boolean utf8;

  public HtmlWriterUtils(final Writer out, final String characterEncoding) {
    this.out = out;
    utf8 = "utf-8".equalsIgnoreCase(characterEncoding);
    buffer = new ResponseWriterBuffer(out);
  }

  public void writeAttributeValue(final String text)
      throws IOException {
    writeEncodedValue(text.toCharArray(), 0, text.length(), true);
  }

  public void writeText(final String text) throws IOException {
    writeEncodedValue(text.toCharArray(), 0, text.length(), false);
  }

  public void writeText(final char[] text, final int start, final int length)
      throws IOException {
    writeEncodedValue(text, start, length, false);
  }

  private void writeEncodedValue(final char[] text, final int start,
      final int length, final boolean isAttribute)
      throws IOException {

    int localIndex = -1;

    final int end = start + length;
    for (int i = start; i < end; i++) {
      char ch = text[i];
      if (ch >= CHARS_TO_ESCAPE.length || CHARS_TO_ESCAPE[ch] != null) {
        localIndex = i;
        break;
      }
    }

    if (localIndex == -1) {
      // no need to escape
      out.write(text, start, length);
    } else {
      // write until localIndex and then encode the remainder
      out.write(text, start, localIndex);

      for (int i = localIndex; i < end; i++) {
        final char ch = text[i];

        // Tilde or less...
        if (ch < CHARS_TO_ESCAPE.length) {
          if (isAttribute && ch == '&' && (i + 1 < end) && text[i + 1] == '{') {
            // HTML 4.0, section B.7.1: ampersands followed by
            // an open brace don't get escaped
            buffer.addToBuffer('&');
          } else if (CHARS_TO_ESCAPE[ch] != null) {
            buffer.addToBuffer(CHARS_TO_ESCAPE[ch]);
          } else {
            buffer.addToBuffer(ch);
          }
        } else if (utf8) {
          buffer.addToBuffer(ch);
        } else if (ch <= 0xff) {
          // ISO-8859-1 entities: encode as needed
          buffer.flushBuffer();

          out.write('&');
          char[] chars = ISO8859_1_ENTITIES[ch - 0xA0];
          out.write(chars, 0, chars.length);
          out.write(';');
        } else {
          buffer.flushBuffer();

          // Double-byte characters to encode.
          // PENDING: when outputting to an encoding that
          // supports double-byte characters (UTF-8, for example),
          // we should not be encoding
          writeDecRef(ch);
        }
      }

      buffer.flushBuffer();
    }
  }


  /**
   * Writes a character as a decimal escape.  Hex escapes are smaller than
   * the decimal version, but Netscape didn't support hex escapes until
   * 4.7.4.
   */
  private void writeDecRef(final char ch) throws IOException {
    if (ch == '\u20ac') {
      out.write("&euro;");
      return;
    }
    out.write("&#");
    // Formerly used String.valueOf().  This version tests out
    // about 40% faster in a microbenchmark (and on systems where GC is
    // going gonzo, it should be even better)
    int i = (int) ch;
    if (i > 10000) {
      out.write('0' + (i / 10000));
      i = i % 10000;
      out.write('0' + (i / 1000));
      i = i % 1000;
      out.write('0' + (i / 100));
      i = i % 100;
      out.write('0' + (i / 10));
      i = i % 10;
      out.write('0' + i);
    } else if (i > 1000) {
      out.write('0' + (i / 1000));
      i = i % 1000;
      out.write('0' + (i / 100));
      i = i % 100;
      out.write('0' + (i / 10));
      i = i % 10;
      out.write('0' + i);
    } else {
      out.write('0' + (i / 100));
      i = i % 100;
      out.write('0' + (i / 10));
      i = i % 10;
      out.write('0' + i);
    }

    out.write(';');
  }

  public static boolean attributeValueMustEscaped(final String name) {
    // this is 30% faster then the  .equals(name) version
    // tested with 100 loops over 19871 names
    //       (extracted from logfile over all demo pages)

    try {
      switch (name.charAt(0)) {
        case 'i': // 'id'
          if (name.length() == 2 && name.charAt(1) == 'd') {
            return false;
          }
          break;
        case 'n': // 'name'
          if (name.length() == 4 && name.charAt(1) == 'a' && name.charAt(2) == 'm'
              && name.charAt(3) == 'e') {
            return false;
          }
          break;
        case 'c': // 'class'
          if (name.length() == 5 && name.charAt(1) == 'l' && name.charAt(2) == 'a'
              && name.charAt(3) == 's' && name.charAt(4) == 's') {
            return false;
          }
          break;
        default:
          return true;
      }
    } catch (NullPointerException e) {
      // ignore
    } catch (StringIndexOutOfBoundsException e) {
      // ignore
    }
    return true;
  }

  //
  // Entities from HTML 4.0, section 24.2.1; character codes 0xA0 to 0xFF
  //
  private static final char [][] ISO8859_1_ENTITIES = new char [][]{
      "nbsp".toCharArray(),
      "iexcl".toCharArray(),
      "cent".toCharArray(),
      "pound".toCharArray(),
      "curren".toCharArray(),
      "yen".toCharArray(),
      "brvbar".toCharArray(),
      "sect".toCharArray(),
      "uml".toCharArray(),
      "copy".toCharArray(),
      "ordf".toCharArray(),
      "laquo".toCharArray(),
      "not".toCharArray(),
      "shy".toCharArray(),
      "reg".toCharArray(),
      "macr".toCharArray(),
      "deg".toCharArray(),
      "plusmn".toCharArray(),
      "sup2".toCharArray(),
      "sup3".toCharArray(),
      "acute".toCharArray(),
      "micro".toCharArray(),
      "para".toCharArray(),
      "middot".toCharArray(),
      "cedil".toCharArray(),
      "sup1".toCharArray(),
      "ordm".toCharArray(),
      "raquo".toCharArray(),
      "frac14".toCharArray(),
      "frac12".toCharArray(),
      "frac34".toCharArray(),
      "iquest".toCharArray(),
      "Agrave".toCharArray(),
      "Aacute".toCharArray(),
      "Acirc".toCharArray(),
      "Atilde".toCharArray(),
      "Auml".toCharArray(),
      "Aring".toCharArray(),
      "AElig".toCharArray(),
      "Ccedil".toCharArray(),
      "Egrave".toCharArray(),
      "Eacute".toCharArray(),
      "Ecirc".toCharArray(),
      "Euml".toCharArray(),
      "Igrave".toCharArray(),
      "Iacute".toCharArray(),
      "Icirc".toCharArray(),
      "Iuml".toCharArray(),
      "ETH".toCharArray(),
      "Ntilde".toCharArray(),
      "Ograve".toCharArray(),
      "Oacute".toCharArray(),
      "Ocirc".toCharArray(),
      "Otilde".toCharArray(),
      "Ouml".toCharArray(),
      "times".toCharArray(),
      "Oslash".toCharArray(),
      "Ugrave".toCharArray(),
      "Uacute".toCharArray(),
      "Ucirc".toCharArray(),
      "Uuml".toCharArray(),
      "Yacute".toCharArray(),
      "THORN".toCharArray(),
      "szlig".toCharArray(),
      "agrave".toCharArray(),
      "aacute".toCharArray(),
      "acirc".toCharArray(),
      "atilde".toCharArray(),
      "auml".toCharArray(),
      "aring".toCharArray(),
      "aelig".toCharArray(),
      "ccedil".toCharArray(),
      "egrave".toCharArray(),
      "eacute".toCharArray(),
      "ecirc".toCharArray(),
      "euml".toCharArray(),
      "igrave".toCharArray(),
      "iacute".toCharArray(),
      "icirc".toCharArray(),
      "iuml".toCharArray(),
      "eth".toCharArray(),
      "ntilde".toCharArray(),
      "ograve".toCharArray(),
      "oacute".toCharArray(),
      "ocirc".toCharArray(),
      "otilde".toCharArray(),
      "ouml".toCharArray(),
      "divide".toCharArray(),
      "oslash".toCharArray(),
      "ugrave".toCharArray(),
      "uacute".toCharArray(),
      "ucirc".toCharArray(),
      "uuml".toCharArray(),
      "yacute".toCharArray(),
      "thorn".toCharArray(),
      "yuml".toCharArray()
  };
}
