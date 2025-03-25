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

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UIImage;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UITextarea;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.internal.webapp.HtmlResponseWriter;
import org.apache.myfaces.tobago.internal.webapp.XmlResponseWriter;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.faces.render.Renderer;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class TobagoResponseWriterUnitTest extends AbstractTobagoTestBase {

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
  public void testComment() throws IOException {
    try (TobagoResponseWriter writer1
             = new HtmlResponseWriter(stringWriter, "", StandardCharsets.UTF_8)) {
      writer1.startElement(HtmlElements.P);
      writer1.writeAttribute(HtmlAttributes.VALUE, "Gutschein", true);
      writer1.writeAttribute(HtmlAttributes.READONLY, true);
      writer1.writeComment("Test");
      writer1.endElement(HtmlElements.P);
    }
    Assertions.assertEquals("\n<p value='Gutschein' readonly='readonly'><!--Test--></p>",
        stringWriter.toString());
  }

  @Test
  public void testCharArray() throws IOException {
    final TobagoResponseWriter xmlResponseWriter
        = new XmlResponseWriter(stringWriter, "text/xml", StandardCharsets.ISO_8859_1);
    xmlResponseWriter.writeText("123".toCharArray(), 0, 3);
    Assertions.assertEquals("123", stringWriter.toString());
  }

  @Test
  public void testPassthrough() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.getPassThroughAttributes().put("step", 1);
    c.getPassThroughAttributes().put("type", "number");
    writer.startElement(HtmlElements.INPUT, c);
    writer.writeAttribute(HtmlAttributes.VALUE, 100);
    writer.endElement(HtmlElements.INPUT);

    Assertions.assertTrue(stringWriter.toString().trim().matches(
        "<input value='100' (step='1' type='number'|type='number' step='1')\\s*>"
    ));
  }

  @Test
  public void testPassthroughChangeElementTag() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");
    c.getPassThroughAttributes().put("step", 1);
    c.getPassThroughAttributes().put(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY, HtmlElements.TEXTAREA);
    writer.startElement(HtmlElements.INPUT, c);
    writer.writeAttribute(HtmlAttributes.VALUE, 100);
    writer.endElement(HtmlElements.INPUT);

    Assertions.assertEquals("\n<textarea value='100' step='1'></textarea>",
        stringWriter.toString());
  }

  @Test
  public void testPassthroughInRenderer() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");

    c.getPassThroughAttributes().put("step", 1);
    c.getPassThroughAttributes().put("type", "number");
    c.encodeAll(facesContext);
    Assertions.assertTrue(getLastWritten().trim().matches("<tobago-in id='id' class='tobago-auto-spacing'>\\s*"
        + "<input name='id' id='id::field' class='form-control' (step='1' type='number'|type='number' step='1')\\s*>"
        + "\\s*</tobago-in>"));
  }

  @Test
  public void testPassthroughChangeElementTagInRender() throws IOException {
    final UIIn c = (UIIn) ComponentUtils.createComponent(
        facesContext, Tags.in.componentType(), RendererTypes.In, "id");

    c.getPassThroughAttributes().put("test", 1);
    c.getPassThroughAttributes().put(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY, HtmlElements.TEXTAREA);
    c.encodeAll(facesContext);
    Assertions.assertEquals("\n<tobago-in id='id' class='tobago-auto-spacing'>\n"
        + "<textarea type='text' name='id' id='id::field' class='form-control' test='1'></textarea>"
        + "</tobago-in>", getLastWritten());

  }

  @Test
  public void testPassthroughWithTextareaRenderer() throws IOException {
    final UITextarea c = (UITextarea) ComponentUtils.createComponent(
        facesContext, Tags.textarea.componentType(), RendererTypes.Textarea, "id");

    c.getPassThroughAttributes().put("spellcheck", true);
    c.encodeAll(facesContext);
    Assertions.assertEquals("\n<tobago-textarea id='id' class='tobago-auto-spacing'>\n"
        + "<textarea name='id' id='id::field' class='form-control' spellcheck></textarea>"
        + "</tobago-textarea>", getLastWritten());
  }

  @Test
  public void testPassthroughWithImgRenderer() throws IOException {
    final UIImage c = (UIImage) ComponentUtils.createComponent(
        facesContext, Tags.image.componentType(), RendererTypes.Image, "id");

    c.getPassThroughAttributes().put("spellcheck", true);
    c.encodeAll(facesContext);
    Assertions.assertEquals("\n<tobago-image id='id'>\n"
        + "<img alt='' spellcheck>"
        + "</tobago-image>", getLastWritten());
  }

  @Test
  public void testPassthroughWithOutRenderer() throws IOException {
    final UIOut c = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "id");

    c.getPassThroughAttributes().put("spellcheck", true);
    c.encodeAll(facesContext);
    Assertions.assertEquals("\n<tobago-out id='id' class='tobago-auto-spacing'>"
        + "<span class='form-control-plaintext' spellcheck></span>"
        + "</tobago-out>", getLastWritten());
  }

  @Test
  public void testPassthroughWithButtonRenderer() throws IOException {
    final UIButton b = (UIButton) ComponentUtils.createComponent(
        facesContext, Tags.button.componentType(), RendererTypes.Button, "id");

    b.setLabel("button");
    b.getPassThroughAttributes().put("aria-label", "label");
    b.encodeAll(facesContext);
    Assertions.assertEquals("\n<button type='button' id='id' name='id' "
        + "class='tobago-button btn btn-secondary tobago-auto-spacing' aria-label='label'>"
        + "<tobago-behavior event='click' client-id='id'></tobago-behavior>"
        + "<span>button</span></button>", getLastWritten());
  }

  @Test
  public void testPassthroughWithLinkRenderer() throws IOException {
    final UILink l = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "id");

    l.setLabel("link");
    l.getPassThroughAttributes().put("aria-label", "label");
    l.encodeAll(facesContext);
    Assertions.assertEquals("\n<button type='button' id='id' name='id' "
        + "class='tobago-link btn btn-link tobago-auto-spacing' aria-label='label'>"
        + "<tobago-behavior event='click' client-id='id'></tobago-behavior>"
        + "<span>link</span></button>", getLastWritten());
  }
}
