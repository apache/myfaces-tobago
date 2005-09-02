/*
 * Copyright (c) 2005 Atanion GmbH, Germany
 * All rights reserved. Created 01.09.2005 12:17:03.
 * $Id: $
 */
package org.apache.myfaces.tobago.webapp;

import junit.framework.TestCase;

import java.io.StringWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

}
