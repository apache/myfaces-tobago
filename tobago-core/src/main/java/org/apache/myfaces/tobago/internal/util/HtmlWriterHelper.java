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

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public final class HtmlWriterHelper extends WriterHelper {

  private static final char[][] CHARS_TO_ESCAPE;

  static {
    // init lookup table
    CHARS_TO_ESCAPE = new char[0xA0][];

    // all "normal" character positions contains null

    // control characters are dropped
    for (int i = 0; i < 0x20; i++) {
      CHARS_TO_ESCAPE[i] = EMPTY;
    }
    for (int i = 0x7F; i < 0xA0; i++) {
      CHARS_TO_ESCAPE[i] = EMPTY;
    }

    CHARS_TO_ESCAPE['\t'] = "&#x09;".toCharArray(); // Horizontal tabulator
    CHARS_TO_ESCAPE['\n'] = "&#x0a;".toCharArray(); // Line feed
    CHARS_TO_ESCAPE['\r'] = "&#x0d;".toCharArray(); // Carriage return

    // See also https://www.owasp.org/index.php/XSS_%28Cross_Site_Scripting%29_Prevention_Cheat_Sheet
    CHARS_TO_ESCAPE['\''] = "&#x27;".toCharArray();
    CHARS_TO_ESCAPE['&'] = "&amp;".toCharArray();
    CHARS_TO_ESCAPE['<'] = "&lt;".toCharArray();
    CHARS_TO_ESCAPE['>'] = "&gt;".toCharArray();
    // We are not escaping quot " and slash / here, because we not really need that in our case.
    // It makes the HTML code better readable and shorter. There are many occurrences of quot, because of JSON.
    //    CHARS_TO_ESCAPE['\"'] = "&quot;".toCharArray();
    //    CHARS_TO_ESCAPE['/'] = "&#x2F;".toCharArray();

  }

  /**
   * @deprecated since 4.3.0
   */
  @Deprecated
  public HtmlWriterHelper(final Writer out, final String characterEncoding) {
    this(out, Charset.forName(characterEncoding));
  }

  public HtmlWriterHelper(final Writer out, final Charset charset) {
    super(out, charset);
  }

  @Override
  protected void writeEncodedValue(final char[] text, final int start,
      final int length, final boolean isAttribute) throws IOException {

    int localIndex = -1;

    final int end = start + length;
    for (int i = start; i < end; i++) {
      final char ch = text[i];
      if (ch >= CHARS_TO_ESCAPE.length || CHARS_TO_ESCAPE[ch] != null) {
        localIndex = i;
        break;
      }
    }
    final Writer out = getOut();

    if (localIndex == -1) {
      // no need to escape
      out.write(text, start, length);
    } else {
      // write until localIndex and then encode the remainder
      out.write(text, start, localIndex);

      final ResponseWriterBuffer buffer = getBuffer();

      for (int i = localIndex; i < end; i++) {
        final char ch = text[i];

        // Tilde or less...
        if (ch < CHARS_TO_ESCAPE.length) {
          if (isAttribute && ch == '&' && i + 1 < end && text[i + 1] == '{') {
            // HTML 4.0, section B.7.1: ampersands followed by
            // an open brace don't get escaped
            buffer.addToBuffer('&');
          } else if (CHARS_TO_ESCAPE[ch] != null) {
            buffer.addToBuffer(CHARS_TO_ESCAPE[ch]);
          } else {
            buffer.addToBuffer(ch);
          }
        } else if (isUtf8()) {
          buffer.addToBuffer(ch);
        } else if (ch <= 0xff) {
          // ISO-8859-1 entities: encode as needed
          buffer.flushBuffer();

          out.write('&');
          final char[] chars = ISO8859_1_ENTITIES[ch - 0xA0];
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
}
