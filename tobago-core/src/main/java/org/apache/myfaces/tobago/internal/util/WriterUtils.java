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

public abstract class WriterUtils {
  protected static final char[] EMPTY = new char[0];
  //
  // Entities from HTML 4.0, section 24.2.1; character codes 0xA0 to 0xFF
  //
  protected static final char[][] ISO8859_1_ENTITIES = new char[][]{
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
  private final Writer out;
  private final ResponseWriterBuffer buffer;
  private final boolean utf8;

  public WriterUtils(final Writer out, final String characterEncoding) {
    this.out = out;
    buffer = new ResponseWriterBuffer(out);
    utf8 = "utf-8".equalsIgnoreCase(characterEncoding);
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

  protected abstract void writeEncodedValue(char[] text, int start,
                                            int length, boolean isAttribute)
      throws IOException;

  /**
   * Writes a character as a decimal escape.  Hex escapes are smaller than
   * the decimal version, but Netscape didn't support hex escapes until
   * 4.7.4.
   */
  protected void writeDecRef(final char ch) throws IOException {
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

  protected final Writer getOut() {
    return out;
  }

  protected final ResponseWriterBuffer getBuffer() {
    return buffer;
  }

  protected final boolean isUtf8() {
    return utf8;
  }
}
