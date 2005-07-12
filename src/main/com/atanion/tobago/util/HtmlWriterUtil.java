package com.atanion.tobago.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.converters.CharacterArrayConverter;

import java.io.Writer;
import java.io.IOException;
import java.io.StringWriter;
import java.io.CharArrayWriter;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jun 28, 2005
 * Time: 2:07:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class HtmlWriterUtil {

  static public void writeAttributeValue(Writer out, String text)
      throws IOException {
    writeAttributeValue(out, text.toCharArray(), 0, text.length());
  }
  static public void writeAttributeValue(Writer out, char[] buffer, String text)
      throws IOException {
    writeAttributeValue(out, buffer, text.toCharArray(), 0, text.length());
  }

  public static void writeAttributeValue(Writer out, char[] text)
      throws IOException {
      writeEncodedValue(out, text, 0, text.length, true);
  }

  public static void writeAttributeValue(Writer out, char[] buffer, char[] text)
      throws IOException {
      writeEncodedValue(out, buffer, text, 0, text.length, true);
  }

  public static void writeAttributeValue(
      Writer out, char[] text, int start, int length)
      throws IOException {
    writeEncodedValue(out, text, start, length, true);
  }

  public static void writeAttributeValue(
      Writer out, char[] buffer, char[] text, int start, int length)
      throws IOException {
    writeEncodedValue(out, buffer, text, start, length, true);
  }





  static public void writeText(Writer out, String text) throws IOException {
    writeText(out, text.toCharArray(), 0, text.length());
  }

  static public void writeText(Writer out, char[] buffer, String text)
      throws IOException {
    writeText(out, buffer, text.toCharArray(), 0, text.length());
  }

  static public void writeText(Writer out, char[] text) throws IOException {
    writeEncodedValue(out, text, 0, text.length, false);
  }

  static public void writeText(Writer out, char[] buffer, char[] text)
      throws IOException {
    writeEncodedValue(out, buffer, text, 0, text.length, false);
  }

  static public void writeText(Writer out, char[] text, int start, int length)
      throws IOException {
    writeEncodedValue(out, text, start, length, false);
  }

  static public void writeText(
      Writer out, char[] buffer, char[] text, int start, int length)
      throws IOException {
    writeEncodedValue(out, buffer, text, start, length, false);
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



  static public void writeEncodedValue(
      Writer out, char[] text, int start, int length, boolean isAttribute)
      throws IOException {
    char[] buff = new char[1028];
    writeEncodedValue(out, buff, text, start, length, isAttribute);
    buff = null;
  }

  private static void writeEncodedValue(Writer out,
      char[] buff, char[] text, int start, int length, boolean isAttribute)
      throws IOException {

    final char[][] charsToEscape;
    if (isAttribute) {
      charsToEscape = ATTRIBUTE_CHARS_TO_ESCAPE;
    } else {
      charsToEscape = TEXT_CHARS_TO_ESCAPE;
    }


    int localIndex = -1;


    int end = start + length;
    for (int i = start; i < end; i++) {
      char ch = text[i];
      if (ch >= 0xA0 || charsToEscape[ch] != null) {
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

      int buffLength = buff.length;
      int buffIndex = 0;

      for (int i = localIndex; i < end; i++) {
        char ch = text[i];

    // Tilde or less...
        if (ch < 0xA0) {
          if (isAttribute && ch == '&' && (i + 1 < end) && text[i + 1] == '{') {
            // HTML 4.0, section B.7.1: ampersands followed by
            // an open brace don't get escaped
            buffIndex = addToBuffer(out, buff, buffIndex, buffLength, '&');
          } else if (charsToEscape[ch] != null) {
            for (char cha : charsToEscape[ch]) {
              buffIndex = addToBuffer(out, buff, buffIndex, buffLength, cha);
            }
          } else {
            buffIndex = addToBuffer(out, buff, buffIndex, buffLength, ch);
          }
        } else if (ch <= 0xff) {
          // ISO-8859-1 entities: encode as needed
          buffIndex = flushBuffer(out, buff, buffIndex);

          out.write('&');
          out.write(sISO8859_1_Entities[ch - 0xA0]);
          out.write(';');
        } else {
          buffIndex = flushBuffer(out, buff, buffIndex);

          // Double-byte characters to encode.
          // PENDING: when outputting to an encoding that
          // supports double-byte characters (UTF-8, for example),
          // we should not be encoding
          _writeDecRef(out, ch);
        }
      }

      flushBuffer(out, buff, buffIndex);
    }
  }


  /**
   * Writes a character as a decimal escape.  Hex escapes are smaller than
   * the decimal version, but Netscape didn't support hex escapes until
   * 4.7.4.
   */
  static private void _writeDecRef(Writer out, char ch) throws IOException {
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
  private static int addToBuffer(Writer out,
                                 char[] buffer,
                                 int bufferIndex,
                                 int bufferLength,
                                 char ch) throws IOException {
      if (bufferIndex >= bufferLength) {
          out.write(buffer, 0, bufferIndex);
          bufferIndex = 0;
      }

      buffer[bufferIndex] = ch;

      return bufferIndex + 1;
  }


  /**
   * Flush the contents of the buffer to the output stream
   * and return the reset buffer index
   */
  private static int flushBuffer(Writer out,
                                 char[] buffer,
                                 int bufferIndex) throws IOException {
      if (bufferIndex > 0)
          out.write(buffer, 0, bufferIndex);

      return 0;
  }


  public static boolean attributeValueMustEscaped(String name) {
    if ("id".equals(name) || "name".equals(name) || "class".equals(name)) {
      return false;
    }
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
