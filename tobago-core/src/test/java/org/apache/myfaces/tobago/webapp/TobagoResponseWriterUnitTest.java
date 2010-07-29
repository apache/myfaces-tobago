package org.apache.myfaces.tobago.webapp;

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

import org.apache.myfaces.tobago.internal.webapp.TobagoResponseWriterImpl;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseXmlWriterImpl;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class TobagoResponseWriterUnitTest {

  private StringWriter stringWriter;
  private TobagoResponseWriter writer;

  @Before
  public void setUp() throws Exception {
    stringWriter = new StringWriter();
    writer = new TobagoResponseWriterImpl(stringWriter, "", "UTF-8");
  }

  @Test
  public void testDocument() throws IOException {
    writer.startDocument();
    writer.endDocument();
    Assert.assertEquals("content expected",
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n" 
        + "<html\n"
        + "></html>", stringWriter.toString());
  }

  @Test
  public void testEmptyTag() throws IOException {
    writer.startElement(HtmlElements.INPUT, null);
    writer.endElement(HtmlElements.INPUT);
    Assert.assertEquals("empty tag", "<input\n>", stringWriter.toString());
  }

  @Test
  public void testNormalTag() throws IOException {
    writer.startElement(HtmlElements.SELECT, null);
    writer.endElement(HtmlElements.SELECT);
    Assert.assertEquals("normal tag", "<select\n></select>", stringWriter.toString());
  }

  @Test
  public void testAttribute() throws IOException {
    writer.startElement(HtmlElements.SELECT, null);
    writer.writeAttribute(HtmlAttributes.VALUE, "0", null);
    writer.endElement(HtmlElements.SELECT);
    Assert.assertEquals("attr tag", "<select value=\"0\"\n></select>", stringWriter.toString());
  }

  @Test
  public void testAttributeQuoting() throws IOException {
    writer.startElement(HtmlElements.SELECT, null);
    writer.writeAttribute(HtmlAttributes.VALUE, "-<->-ü-€-", null);
    writer.endElement(HtmlElements.SELECT);
    Assert.assertEquals("attr tag", "<select value=\"-&lt;-&gt;-ü-€-\"\n></select>", stringWriter.toString());
  }

  @Test
  public void testTextQuoting() throws IOException {
    writer.startElement(HtmlElements.TEXTAREA, null);
    writer.writeText("-<->-ü-€-", null);
    writer.endElement("textarea");
    Assert.assertEquals("attr tag", "<textarea\n>-&lt;-&gt;-ü-€-</textarea>", stringWriter.toString());
  }

  @Test
  public void testStringWriter() throws IOException {
    stringWriter.write("-ü-€-");
    Assert.assertEquals("-ü-€-", stringWriter.toString());
  }

  @Test
  public void testManyChars() throws IOException {
    writer.startElement(HtmlElements.SELECT, null);
    StringBuffer buffer = new StringBuffer();
    for (char c = 0x20; c < 0x1ff; c++) {
      buffer.append(c);
    }
    writer.writeAttribute(HtmlAttributes.VALUE, buffer, null);
    writer.writeText(buffer, null);
    writer.endElement(HtmlElements.SELECT);

    String result = buffer.toString(); // all the same but this 4 items
    result = result.replace("&", "&amp;");
    result = result.replace("\"", "&quot;");
    result = result.replace("<", "&lt;");
    result = result.replace(">", "&gt;");
    Assert.assertEquals("all chars", "<select value=\"" + result + "\"\n>" + result + "</select>",
        stringWriter.toString());
  }

  @Test
  public void testNonUtf8() throws IOException {
    TobagoResponseWriter writer1 = new TobagoResponseWriterImpl(stringWriter, "", "ISO-8859-1");
    writer1.startElement(HtmlElements.INPUT, null);
    writer1.writeAttribute(HtmlAttributes.VALUE, "Gutschein über 100 €.", null);
    writer1.writeAttribute(HtmlAttributes.READONLY, true);
    writer1.endElement(HtmlElements.INPUT);
    writer1.close();
    Assert.assertEquals("<input value=\"Gutschein &uuml;ber 100 &euro;.\" readonly=\"readonly\"\n>",
        stringWriter.toString());
  }

  @Test
  public void testCharArray() throws IOException {
    TobagoResponseWriter writer = new TobagoResponseXmlWriterImpl(stringWriter, "text/xml", "ISO-8859-1");
    writer.writeText("123".toCharArray(), 0, 3);
    Assert.assertEquals("123", stringWriter.toString());
  }
}
