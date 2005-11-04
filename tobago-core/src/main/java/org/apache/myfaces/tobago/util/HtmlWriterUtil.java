/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;

/**
 * User: weber
 * Date: Jun 28, 2005
 * Time: 2:07:29 PM
 */
public final class HtmlWriterUtil {

  private static final Log LOG = LogFactory.getLog(HtmlWriterUtil.class);

  private static final int BUFFER_SIZE = 1028;
  private final Writer out;
  private final boolean utf8;
  private final char[][] charsToEscape;
  private final char[] buff;

  private int bufferIndex;

  public HtmlWriterUtil(Writer out, String characterEncoding, boolean attribute) {
    this.out = out;
    utf8 = "utf-8".equalsIgnoreCase(characterEncoding);
    charsToEscape = attribute ? ATTRIBUTE_CHARS_TO_ESCAPE : TEXT_CHARS_TO_ESCAPE;
    buff = new char[BUFFER_SIZE];
//    LOG.info("utf8 = " + utf8);

  }


  public void writeAttributeValue(final String text)
      throws IOException {
    writeAttributeValue(text.toCharArray(), 0, text.length());
  }

  public void writeAttributeValue(final char[] text)
      throws IOException {
      writeEncodedValue(text, 0, text.length, true);
  }

  public void writeAttributeValue(
      final char[] text, final int start, final int length)
      throws IOException {
    writeEncodedValue(text, start, length, true);
  }





  public void writeText(final String text) throws IOException {
    writeText(text.toCharArray(), 0, text.length());
  }

  public void writeText(final char[] text) throws IOException {
    writeEncodedValue(text, 0, text.length, false);
  }

  public void writeText(final char[] text, final int start, final int length)
      throws IOException {
    writeEncodedValue(text, start, length, false);
  }

//  static public void writeText(Writer out, char[] buffer, char[] text)
//      throws IOException {
//    writeText(out, buffer, text, 0, text.length);
//  }
//
//  public static void writeText(
//      Writer out, char[] buff, char[] text, int start, int length)
//      throws IOException {
//    writeEncodedValue(out, buff, text, start, length, false);
//  }




  private void writeEncodedValue(final char[] text, final int start,
                                 final int length, final boolean isAttribute)
      throws IOException {

//    final char[][] charsToEscape;
//    if (isAttribute) {
//      charsToEscape = ATTRIBUTE_CHARS_TO_ESCAPE;
//    } else {
//      charsToEscape = TEXT_CHARS_TO_ESCAPE;
//    }


    int localIndex = -1;


    final int end = start + length;
    for (int i = start; i < end; i++) {
      char ch = text[i];
      if (ch >= charsToEscape.length - 1 || charsToEscape[ch] != null) {
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
        if (ch < charsToEscape.length - 1) {
          if (isAttribute && ch == '&' && (i + 1 < end) && text[i + 1] == '{') {
            // HTML 4.0, section B.7.1: ampersands followed by
            // an open brace don't get escaped
            addToBuffer('&');
          } else if (charsToEscape[ch] != null) {
            for (char cha : charsToEscape[ch]) {
              addToBuffer(cha);
            }
          } else {
            addToBuffer(ch);
          }
        } else if (utf8) {
          addToBuffer(ch);
        } else if (ch <= 0xff) {
          // ISO-8859-1 entities: encode as needed
          flushBuffer();

          out.write('&');
//          fixme? write(String) sets the startStillOpen=false
//          out.write(sISO8859_1_Entities[ch - 0xA0]);
          for (char c : sISO8859_1_Entities[ch - 0xA0].toCharArray()) {
            out.write(c);
          }
          out.write(';');
        } else {
          flushBuffer();

          // Double-byte characters to encode.
          // PENDING: when outputting to an encoding that
          // supports double-byte characters (UTF-8, for example),
          // we should not be encoding
          _writeDecRef(ch);
        }
      }

      flushBuffer();
    }
  }


  /**
   * Writes a character as a decimal escape.  Hex escapes are smaller than
   * the decimal version, but Netscape didn't support hex escapes until
   * 4.7.4.
   */
  private void _writeDecRef(final char ch) throws IOException {
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

  //
  // Buffering scheme: we use a tremendously simple buffering
  // scheme that greatly reduces the number of calls into the
  // Writer/PrintWriter.  In practice this has produced significant
  // measured performance gains (at least in JDK 1.3.1).  We only
  // support adding single characters to the buffer, so anytime
  // multiple characters need to be written out, the entire buffer
  // gets flushed.  In practice, this is good enough, and keeps
  // the core simple.
  //

  /**
   * Add a character to the buffer, flushing the buffer if the buffer is
   * full, and returning the new buffer index
   */
  private void addToBuffer(final char ch) throws IOException {
      if (bufferIndex >= BUFFER_SIZE) {
          out.write(buff, 0, bufferIndex);
          bufferIndex = 0;
      }

      buff[bufferIndex] = ch;

      bufferIndex += 1;
  }


  /**
   * Flush the contents of the buffer to the output stream
   * and return the reset buffer index
   */
  private void flushBuffer() throws IOException {
      if (bufferIndex > 0) {
          out.write(buff, 0, bufferIndex);
      }
      bufferIndex = 0;
  }

  public static boolean attributeValueMustEscaped(final String name) {
    // this is 30% faster then the  .equals(name) version
    // tested with 100 loops over 19871 names
    //       (extracted from logfile over all demo pages)

    try {
      switch (name.charAt(0)) {
        case 'i' : // 'id'
          if (name.charAt(1) == 'd') {
            return false;
          }
          break;
        case 'n' : // 'name'
          if (name.charAt(1) == 'a' && name.charAt(2) == 'm'
              && name.charAt(3) == 'e') {
            return false;
          }
          break;
        case 'c' : // 'class'
          if (name.charAt(1) == 'l' && name.charAt(2) == 'a'
              && name.charAt(3) == 's' && name.charAt(4) == 's') {
            return false;
          }
          break;
      }
    } catch (Exception e) { /* ignore */ }
//    if ("id".equals(name) || "name".equals(name) || "class".equals(name)) {
//      return false;
//    }
    return true;
  }

  //
  // Entities from HTML 4.0, section 24.2.1; character codes 0xA0 to 0xFF
  //
  static private final String[] sISO8859_1_Entities = new String[]{
      "nbsp",
      "iexcl",
      "cent",
      "pound",
      "curren",
      "yen",
      "brvbar",
      "sect",
      "uml",
      "copy",
      "ordf",
      "laquo",
      "not",
      "shy",
      "reg",
      "macr",
      "deg",
      "plusmn",
      "sup2",
      "sup3",
      "acute",
      "micro",
      "para",
      "middot",
      "cedil",
      "sup1",
      "ordm",
      "raquo",
      "frac14",
      "frac12",
      "frac34",
      "iquest",
      "Agrave",
      "Aacute",
      "Acirc",
      "Atilde",
      "Auml",
      "Aring",
      "AElig",
      "Ccedil",
      "Egrave",
      "Eacute",
      "Ecirc",
      "Euml",
      "Igrave",
      "Iacute",
      "Icirc",
      "Iuml",
      "ETH",
      "Ntilde",
      "Ograve",
      "Oacute",
      "Ocirc",
      "Otilde",
      "Ouml",
      "times",
      "Oslash",
      "Ugrave",
      "Uacute",
      "Ucirc",
      "Uuml",
      "Yacute",
      "THORN",
      "szlig",
      "agrave",
      "aacute",
      "acirc",
      "atilde",
      "auml",
      "aring",
      "aelig",
      "ccedil",
      "egrave",
      "eacute",
      "ecirc",
      "euml",
      "igrave",
      "iacute",
      "icirc",
      "iuml",
      "eth",
      "ntilde",
      "ograve",
      "oacute",
      "ocirc",
      "otilde",
      "ouml",
      "divide",
      "oslash",
      "ugrave",
      "uacute",
      "ucirc",
      "uuml",
      "yacute",
      "thorn",
      "yuml"
  };

  private static char[][] ATTRIBUTE_CHARS_TO_ESCAPE;

  private static char[][] TEXT_CHARS_TO_ESCAPE;

  static {
    // init lookup arrays
    // initial values of char[][] are null  see java language spec
    // '<' is not escaped in attribute values, but in text
    ATTRIBUTE_CHARS_TO_ESCAPE = new char[0xA0 + 1][];
    ATTRIBUTE_CHARS_TO_ESCAPE[0x22] = "&quot;".toCharArray(); // 0x22  '"'
    ATTRIBUTE_CHARS_TO_ESCAPE[0x26] = "&amp;".toCharArray(); // 0x26  '&'
    ATTRIBUTE_CHARS_TO_ESCAPE[0x3E] = "&gt;".toCharArray(); // 0x3E  '>'

    TEXT_CHARS_TO_ESCAPE = new char[ATTRIBUTE_CHARS_TO_ESCAPE.length][];
    for (int i = 0 ; i < ATTRIBUTE_CHARS_TO_ESCAPE.length; i++) {
      TEXT_CHARS_TO_ESCAPE[i] = ATTRIBUTE_CHARS_TO_ESCAPE[i];
    }
    TEXT_CHARS_TO_ESCAPE[0x3C] = "&lt;".toCharArray(); // 0x  '<'

  }

}
