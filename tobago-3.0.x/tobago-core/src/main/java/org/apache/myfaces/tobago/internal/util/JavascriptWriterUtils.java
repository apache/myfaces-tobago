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

public final class JavascriptWriterUtils extends WriterUtils {

  private static final char[][] CHARS_TO_ESCAPE;

  static {
    // init lookup table
    CHARS_TO_ESCAPE = new char[0xA0][];

    for (int i = 0; i < 0x20; i++) {
      CHARS_TO_ESCAPE[i] = EMPTY; // Control characters
    }

    CHARS_TO_ESCAPE['\t'] = "\\n".toCharArray(); // Horizontal tabulator
    CHARS_TO_ESCAPE['\n'] = "\\n".toCharArray(); // Line feed
    CHARS_TO_ESCAPE['\r'] = "\\r".toCharArray(); // Carriage return

    CHARS_TO_ESCAPE['"'] = "\\\"".toCharArray();
    CHARS_TO_ESCAPE['\\'] = "\\\\".toCharArray();

    CHARS_TO_ESCAPE[0x7F] = EMPTY; // Delete

    for (int i = 0x80; i < 0xA0; i++) {
      CHARS_TO_ESCAPE[i] = EMPTY; // Control characters
    }

    // all "normal" character positions contains null
  }

  public JavascriptWriterUtils(final Writer out, final String characterEncoding) {
    super(out, characterEncoding);
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
          if (isAttribute && ch == '&' && (i + 1 < end) && text[i + 1] == '{') {
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
