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

package org.apache.myfaces.tobago.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Properties;

public final class XmlUtils {

  public static String escape(final String s) {
    return escape(s, true);
  }

  public static String escape(final String s, final boolean isAttributeValue) {
    if (null == s) {
      return "";
    }
    int len = s.length();
    StringBuilder buffer = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      appendEntityRef(buffer, s.charAt(i), isAttributeValue);
    }
    return buffer.toString();
  }

  public static String escape(final char[] chars, final int offset, final int length, final boolean isAttributeValue) {
    if (null == chars) {
      return "";
    }
    StringBuilder buffer = new StringBuilder(length);
    for (int i = offset; i < length; i++) {
      appendEntityRef(buffer, chars[i], isAttributeValue);
    }
    return buffer.toString();
  }

  private static void appendEntityRef(final StringBuilder buffer, final char ch,
      final boolean isAttributeValue) {
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

  public static void load(final Properties properties, final InputStream stream)
      throws IOException {
    Document document;
    try {
      document = createDocument(stream);
    } catch (SAXException e) {
      throw new RuntimeException("Invalid properties format", e);
    }
    Element propertiesElement = (Element) document.getChildNodes().item(
        document.getChildNodes().getLength() - 1);
    importProperties(properties, propertiesElement);
  }

  private static Document createDocument(final InputStream stream)
      throws SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setIgnoringElementContentWhitespace(true);
    factory.setValidating(false);
    factory.setCoalescing(true);
    factory.setIgnoringComments(true);
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setEntityResolver(new Resolver());
      InputSource source = new InputSource(stream);
      return builder.parse(source);
    } catch (ParserConfigurationException e) {
      throw new Error(e);
    }
  }

  static void importProperties(final Properties properties, final Element propertiesElement) {
    NodeList entries = propertiesElement.getChildNodes();
    int numEntries = entries.getLength();
    int start = numEntries > 0
        && entries.item(0).getNodeName().equals("comment") ? 1 : 0;
    for (int i = start; i < numEntries; i++) {
      Node child = entries.item(i);
      if (child instanceof Element) {
        Element entry = (Element) child;
        if (entry.hasAttribute("key")) {
          Node node = entry.getFirstChild();
          String value = (node == null) ? "" : node.getNodeValue();
          properties.setProperty(entry.getAttribute("key"), value);
        }
      }
    }
  }

  private static class Resolver implements EntityResolver {

    public InputSource resolveEntity(final String publicId, final String systemId)
        throws SAXException {
      String dtd = "<!ELEMENT properties (comment?, entry*)>"
          + "<!ATTLIST properties version CDATA #FIXED '1.0'>"
          + "<!ELEMENT comment (#PCDATA)>"
          + "<!ELEMENT entry (#PCDATA)>"
          + "<!ATTLIST entry key CDATA #REQUIRED>";
      InputSource inputSource = new InputSource(new StringReader(dtd));
      inputSource.setSystemId("http://java.sun.com/dtd/properties.dtd");
      return inputSource;
    }
  }

}
