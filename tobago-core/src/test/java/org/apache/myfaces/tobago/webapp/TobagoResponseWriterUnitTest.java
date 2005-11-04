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
package org.apache.myfaces.tobago.webapp;

import junit.framework.TestCase;

import java.io.StringWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;

public class TobagoResponseWriterUnitTest extends TestCase {

  private static final Log LOG = LogFactory.getLog(TobagoResponseWriterUnitTest.class);

  private StringWriter stringWriter;
  private TobagoResponseWriter writer;

  protected void setUp() throws Exception {
    super.setUp();
    stringWriter = new StringWriter();
    writer = new TobagoResponseWriter(stringWriter, "", "UTF-8");
  }

  public void testDocument() throws IOException {
    writer.startDocument();
    writer.endDocument();
    assertEquals("no content needed", "", stringWriter.toString());
  }

  public void testElement() throws IOException {
    writer.startElement("test", null);
    writer.endElement("test");
    assertEquals("simple tag", "<test\n></test>", stringWriter.toString());
  }

  public void testAttribute() throws IOException {
    writer.startElement("test", null);
    writer.endElement("test");
    assertEquals("simple tag", "<test\n></test>", stringWriter.toString());
  }

  public void testNonUtf8() throws IOException {
    writer = new TobagoResponseWriter(stringWriter, "", "ISO-8859-1");
    writer.startElement("input", null);
    writer.writeAttribute("value", "Gutschein über 100 Euro.", null);
    writer.writeAttribute("readonly", true);
    writer.endElement("input");
  }

}
