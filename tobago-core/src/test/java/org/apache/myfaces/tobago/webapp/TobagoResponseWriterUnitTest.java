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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class TobagoResponseWriterUnitTest extends AbstractJsfTestCase {

  private StringWriter stringWriter;
  private TobagoResponseWriter writer;

  @Before
  public void setUp() throws Exception {
    super.setUp();
    stringWriter = new StringWriter();
    writer = new HtmlResponseWriter(stringWriter, "", "UTF-8");
  }

  @Test
  public void testDocument() throws IOException {
    writer.startDocument();
    writer.endDocument();
    Assert.assertEquals("content expected","<!DOCTYPE html>\n", stringWriter.toString());
  }

  @Test
  public void testEmptyTag() throws IOException {
    writer.startElement(HtmlElements.INPUT);
    writer.endElement(HtmlElements.INPUT);
    Assert.assertEquals("empty tag", "\n<input>", stringWriter.toString());
  }

  @Test
  public void testNormalTag() throws IOException {
    writer.startElement(HtmlElements.SELECT);
    writer.endElement(HtmlElements.SELECT);
    Assert.assertEquals("normal tag", "\n<select></select>", stringWriter.toString());
  }

  @Test
  public void testAttribute() throws IOException {
    writer.startElement(HtmlElements.SELECT);
    writer.writeAttribute(HtmlAttributes.VALUE, 0);
    writer.endElement(HtmlElements.SELECT);
    Assert.assertEquals("attr tag", "\n<select value='0'></select>", stringWriter.toString());
  }

  @Test
  public void testURIAttribute() throws IOException {
    writer.startElement(HtmlElements.A);
    writer.writeURIAttribute(HtmlAttributes.HREF, "http://example.org/web?text=äöüß");
    writer.endElement(HtmlElements.A);
    Assert.assertEquals(
        "uri attr tag",
        "\n<a href='http:&#x2F;&#x2F;example.org&#x2F;web?text=%C3%A4%C3%B6%C3%BC%C3%9F'></a>",
        stringWriter.toString());
  }

  @Test
  public void testAttributeQuoting() throws IOException {
    writer.startElement(HtmlElements.SELECT);
    writer.writeAttribute(HtmlAttributes.VALUE, "-<->-ü-€-", true);
    writer.endElement(HtmlElements.SELECT);
    Assert.assertEquals("attr tag", "\n<select value='-&lt;-&gt;-ü-€-'></select>", stringWriter.toString());
  }

  @Test
  public void testTextQuoting() throws IOException {
    writer.startElement(HtmlElements.TEXTAREA);
    writer.writeText("-<->-ü-€-");
    writer.endElement(HtmlElements.TEXTAREA);
    Assert.assertEquals("attr tag", "\n<textarea>-&lt;-&gt;-ü-€-</textarea>", stringWriter.toString());
  }

  @Test
  public void testStringWriter() throws IOException {
    stringWriter.write("-ü-€-");
    Assert.assertEquals("-ü-€-", stringWriter.toString());
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
    result = result.replace("\"", "&quot;");
    result = result.replace("/", "&#x2F;");
    result = result.replace("<", "&lt;");
    result = result.replace(">", "&gt;");
    Assert.assertEquals("all chars", "\n<select value='" + result + "'>" + result + "</select>",
        stringWriter.toString());
  }

  @Test
  public void testNonUtf8() throws IOException {
    final TobagoResponseWriter writer1 = new HtmlResponseWriter(stringWriter, "", "ISO-8859-1");
    writer1.startElement(HtmlElements.INPUT);
    writer1.writeAttribute(HtmlAttributes.VALUE, "Gutschein über 100 €.", true);
    writer1.writeAttribute(HtmlAttributes.READONLY, true);
    writer1.endElement(HtmlElements.INPUT);
    writer1.close();
    Assert.assertEquals("\n<input value='Gutschein &uuml;ber 100 &euro;.' readonly='readonly'>",
        stringWriter.toString());
  }

  @Test
  public void testCharArray() throws IOException {
    final TobagoResponseWriter writer = new XmlResponseWriter(stringWriter, "text/xml", "ISO-8859-1");
    writer.writeText("123".toCharArray(), 0, 3);
    Assert.assertEquals("123", stringWriter.toString());
  }
}
