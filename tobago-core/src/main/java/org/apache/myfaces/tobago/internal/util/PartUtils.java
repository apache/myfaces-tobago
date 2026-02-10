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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Part;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Only needed for Servlet 3.0. Not needed for Servlet 3.1 or higher.
 * <p>
 * Basically taken from Apache Tomcat 8
 */
public final class PartUtils {

  private static final Logger LOG = LoggerFactory.getLogger(PartUtils.class);

  private static final Method SUBMITTED_FILE_NAME_METHOD = findSubmittedFileNameMethod();

  private PartUtils() {
    // to prevent instantiation
  }

  private static Method findSubmittedFileNameMethod() {
    try { // try to call the Servlet 3.1 function
      for (final PropertyDescriptor pd : Introspector.getBeanInfo(Part.class).getPropertyDescriptors()) {
        if ("submittedFileName".equals(pd.getName())) {
          final Method readMethod = pd.getReadMethod();
          if (readMethod != null) {
            return readMethod;
          }
        }
      }
    } catch (final Exception e) {
      // ignore
    }
    return null;
  }

  /**
   * This is a helper method, to get the original file name of the upload.
   * If you have at least Servlet 3.1, you wouldn't need this function.
   *
   * @since Tobago 3.0.0
   */
  public static String getSubmittedFileName(final Part part) {

    try { // try to call the Servlet 3.1 function
      if (SUBMITTED_FILE_NAME_METHOD != null) {
        final String fileName = (String) SUBMITTED_FILE_NAME_METHOD.invoke(part);
        LOG.debug("Upload file name = '{}'", fileName);
        return fileName;
      }
    } catch (final Exception e) {
      // ignore
    }

    String fileName = null;
    final String cd = part.getHeader("Content-Disposition");
    if (cd != null) {
      final String cdl = cd.toLowerCase(Locale.ENGLISH);
      if (cdl.startsWith("form-data") || cdl.startsWith("attachment")) {
        final ParameterParser paramParser = new ParameterParser();
        paramParser.setLowerCaseNames(true);
        // Parameter parser can handle null input
        final Map<String, String> params =
            paramParser.parse(cd, ';');
        if (params.containsKey("filename")) {
          fileName = params.get("filename");
          if (fileName != null) {
            fileName = fileName.trim();
            // XXX seems to be wrong in the code?
            fileName = fileName.replaceAll("\\\\\\\"", "\""); // replaces \" with "
          } else {
            // Even if there is no value, the parameter is present,
            // so we return an empty file name rather than no file
            // name.
            fileName = "";
          }
        }
      }
    }
    LOG.debug("Upload file name = '{}'", fileName);
    return fileName;
  }

  private static class ParameterParser {

    /**
     * String to be parsed.
     */
    private char[] chars = null;

    /**
     * Current position in the string.
     */
    private int pos = 0;

    /**
     * Maximum position in the string.
     */
    private int len = 0;

    /**
     * Start of a token.
     */
    private int i1 = 0;

    /**
     * End of a token.
     */
    private int i2 = 0;

    /**
     * Whether names stored in the map should be converted to lower case.
     */
    private boolean lowerCaseNames = false;

    /**
     * Default ParameterParser constructor.
     */
    ParameterParser() {
      super();
    }

    /**
     * Are there any characters left to parse?
     *
     * @return {@code true} if there are unparsed characters,
     * {@code false} otherwise.
     */
    private boolean hasChar() {
      return this.pos < this.len;
    }

    /**
     * A helper method to process the parsed token. This method removes
     * leading and trailing blanks as well as enclosing quotation marks,
     * when necessary.
     *
     * @param quoted {@code true} if quotation marks are expected,
     *               {@code false} otherwise.
     * @return the token
     */
    private String getToken(final boolean quoted) {
      // Trim leading white spaces
      while (i1 < i2 && Character.isWhitespace(chars[i1])) {
        i1++;
      }
      // Trim trailing white spaces
      while (i2 > i1 && Character.isWhitespace(chars[i2 - 1])) {
        i2--;
      }
      // Strip away quotation marks if necessary
      if (quoted
          && i2 - i1 >= 2
          && chars[i1] == '"'
          && chars[i2 - 1] == '"') {
        i1++;
        i2--;
      }
      String result = null;
      if (i2 > i1) {
        result = String.valueOf(chars, i1, i2 - i1);
      }
      return result;
    }

    /**
     * Tests if the given character is present in the array of characters.
     *
     * @param ch      the character to test for presense in the array of characters
     * @param charray the array of characters to test against
     * @return {@code true} if the character is present in the array of
     * characters, {@code false} otherwise.
     */
    private boolean isOneOf(final char ch, final char[] charray) {
      boolean result = false;
      for (final char element : charray) {
        if (ch == element) {
          result = true;
          break;
        }
      }
      return result;
    }

    /**
     * Parses out a token until any of the given terminators
     * is encountered.
     *
     * @param terminators the array of terminating characters. Any of these
     *                    characters when encountered signify the end of the token
     * @return the token
     */
    private String parseToken(final char[] terminators) {
      char ch;
      i1 = pos;
      i2 = pos;
      while (hasChar()) {
        ch = chars[pos];
        if (isOneOf(ch, terminators)) {
          break;
        }
        i2++;
        pos++;
      }
      return getToken(false);
    }

    /**
     * Parses out a token until any of the given terminators
     * is encountered outside the quotation marks.
     *
     * @param terminators the array of terminating characters. Any of these
     *                    characters when encountered outside the quotation marks signify the end
     *                    of the token
     * @return the token
     */
    private String parseQuotedToken(final char[] terminators) {
      char ch;
      i1 = pos;
      i2 = pos;
      boolean quoted = false;
      boolean charEscaped = false;
      while (hasChar()) {
        ch = chars[pos];
        if (!quoted && isOneOf(ch, terminators)) {
          break;
        }
        if (!charEscaped && ch == '"') {
          quoted = !quoted;
        }
        charEscaped = !charEscaped && ch == '\\';
        i2++;
        pos++;

      }
      return getToken(true);
    }

    /**
     * Sets the flag if parameter names are to be converted to lower case when
     * name/value pairs are parsed.
     *
     * @param b {@code true} if parameter names are to be
     *          converted to lower case when name/value pairs are parsed.
     *          {@code false} otherwise.
     */
    public void setLowerCaseNames(final boolean b) {
      this.lowerCaseNames = b;
    }

    /**
     * Extracts a map of name/value pairs from the given string. Names are
     * expected to be unique.
     *
     * @param str       the string that contains a sequence of name/value pairs
     * @param separator the name/value pairs separator
     * @return a map of name/value pairs
     */
    public Map<String, String> parse(final String str, final char separator) {
      if (str == null) {
        return new HashMap<>();
      }
      return parse(str.toCharArray(), separator);
    }

    /**
     * Extracts a map of name/value pairs from the given array of
     * characters. Names are expected to be unique.
     *
     * @param charArray the array of characters that contains a sequence of
     *                  name/value pairs
     * @param separator the name/value pairs separator
     * @return a map of name/value pairs
     */
    public Map<String, String> parse(final char[] charArray, final char separator) {
      if (charArray == null) {
        return new HashMap<>();
      }
      return parse(charArray, 0, charArray.length, separator);
    }

    /**
     * Extracts a map of name/value pairs from the given array of
     * characters. Names are expected to be unique.
     *
     * @param charArray the array of characters that contains a sequence of
     *                  name/value pairs
     * @param offset    - the initial offset.
     * @param length    - the length.
     * @param separator the name/value pairs separator
     * @return a map of name/value pairs
     */
    public Map<String, String> parse(
        final char[] charArray,
        final int offset,
        final int length,
        final char separator) {

      if (charArray == null) {
        return new HashMap<>();
      }
      final HashMap<String, String> params = new HashMap<>();
      this.chars = charArray;
      this.pos = offset;
      this.len = length;

      String paramName;
      String paramValue;
      while (hasChar()) {
        paramName = parseToken(new char[]{
            '=', separator
        });
        paramValue = null;
        if (hasChar() && charArray[pos] == '=') {
          pos++; // skip '='
          paramValue = parseQuotedToken(new char[]{
              separator
          });
        }
        if (hasChar() && charArray[pos] == separator) {
          pos++; // skip separator
        }
        if (paramName != null && paramName.length() > 0) {
          if (this.lowerCaseNames) {
            paramName = paramName.toLowerCase(Locale.ENGLISH);
          }

          params.put(paramName, paramValue);
        }
      }
      return params;
    }
  }
}
