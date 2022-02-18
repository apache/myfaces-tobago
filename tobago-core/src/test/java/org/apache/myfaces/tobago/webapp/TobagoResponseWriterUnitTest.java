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

package org.apache.myfaces.tobago.webapp;

import org.apache.myfaces.test.base.junit4.AbstractJsfTestCase;
import org.apache.myfaces.tobago.internal.webapp.HtmlResponseWriter;
import org.apache.myfaces.tobago.internal.webapp.XmlResponseWriter;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class TobagoResponseWriterUnitTest extends AbstractJsfTestCase {

  private StringWriter stringWriter;
  private TobagoResponseWriter writer;

  @Override
  @BeforeEach
  public void setUp() throws Exception {
    super.setUp();
    stringWriter = new StringWriter();
    writer = new HtmlResponseWriter(stringWriter, "", StandardCharsets.UTF_8);
  }

  @Test
  public void testDocument() throws IOException {
    writer.startDocument();
    writer.endDocument();
    Assertions.assertEquals("<!DOCTYPE html>\n", stringWriter.toString(), "content expected");
  }

  @Test
  public void testEmptyTag() throws IOException {
    writer.startElement(HtmlElements.INPUT);
    writer.endElement(HtmlElements.INPUT);
    Assertions.assertEquals("\n<input>", stringWriter.toString(), "empty tag");
  }

  @Test
  public void testNormalTag() throws IOException {
    writer.startElement(HtmlElements.SELECT);
    writer.endElement(HtmlElements.SELECT);
    Assertions.assertEquals("\n<select></select>", stringWriter.toString(), "normal tag");
  }

  @Test
  public void testAttribute() throws IOException {
    writer.startElement(HtmlElements.SELECT);
    writer.writeAttribute(HtmlAttributes.VALUE, 0);
    writer.endElement(HtmlElements.SELECT);
    Assertions.assertEquals("\n<select value='0'></select>", stringWriter.toString(), "attr tag");
  }

  @Test
  public void testURIAttribute() throws IOException {
    writer.startElement(HtmlElements.A);
    writer.writeURIAttribute(HtmlAttributes.HREF, "http://example.org/web?text=äöüß");
    writer.endElement(HtmlElements.A);
    Assertions.assertEquals(
        "\n<a href='http://example.org/web?text=%C3%A4%C3%B6%C3%BC%C3%9F'></a>",
        stringWriter.toString(),
        "uri attr tag");
  }

  @Test
  public void testAttributeQuoting() throws IOException {
    writer.startElement(HtmlElements.SELECT);
    writer.writeAttribute(HtmlAttributes.VALUE, "-<->-ü-€-", true);
    writer.endElement(HtmlElements.SELECT);
    Assertions.assertEquals("\n<select value='-&lt;-&gt;-ü-€-'></select>", stringWriter.toString(), "attr tag");
  }

  @Test
  public void testTextQuoting() throws IOException {
    writer.startElement(HtmlElements.TEXTAREA);
    writer.writeText("-<->-ü-€-");
    writer.endElement(HtmlElements.TEXTAREA);
    Assertions.assertEquals("\n<textarea>-&lt;-&gt;-ü-€-</textarea>", stringWriter.toString(), "attr tag");
  }

  @Test
  public void testStringWriter() throws IOException {
    stringWriter.write("-ü-€-");
    Assertions.assertEquals("-ü-€-", stringWriter.toString());
  }

  @Test
  public void testManyChars() throws IOException {
    writer.startElement(HtmlElements.SELECT);
    final StringBuilder buffer = new StringBuilder();
    for (char c = 0x20; c < 0x7F; c++) {
      buffer.append(c);
    }
    for (char c = 0xA0; c < 0x1ff; c++) {
      buffer.append(c);
    }
    writer.writeAttribute(HtmlAttributes.VALUE, buffer.toString(), true);
    writer.writeText(buffer.toString());
    writer.endElement(HtmlElements.SELECT);

    String result = buffer.toString(); // all the same but this 4 items
    result = result.replace("&", "&amp;");
    result = result.replace("'", "&#x27;");
//    result = result.replace("\"", "&quot;");
//    result = result.replace("/", "&#x2F;");
    result = result.replace("<", "&lt;");
    result = result.replace(">", "&gt;");
    Assertions.assertEquals(
        "\n<select value='" + result + "'>" + result + "</select>", stringWriter.toString(), "all chars");
  }

  @Test
  public void testNonUtf8() throws IOException {
    try (TobagoResponseWriter writer1
             = new HtmlResponseWriter(stringWriter, "", StandardCharsets.ISO_8859_1)) {
      writer1.startElement(HtmlElements.INPUT);
      writer1.writeAttribute(HtmlAttributes.VALUE, "Gutschein über 100 €.", true);
      writer1.writeAttribute(HtmlAttributes.READONLY, true);
      writer1.endElement(HtmlElements.INPUT);
    }
    Assertions.assertEquals("\n<input value='Gutschein &uuml;ber 100 &euro;.' readonly='readonly'>",
        stringWriter.toString());
  }

  @Test
  public void testCharArray() throws IOException {
    final TobagoResponseWriter xmlResponseWriter
        = new XmlResponseWriter(stringWriter, "text/xml", StandardCharsets.ISO_8859_1);
    xmlResponseWriter.writeText("123".toCharArray(), 0, 3);
    Assertions.assertEquals("123", stringWriter.toString());
  }
}
