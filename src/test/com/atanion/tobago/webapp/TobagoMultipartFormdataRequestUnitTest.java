/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 29.06.2004 11:07:07.
 * $Id$
 */
package com.atanion.tobago.webapp;

import com.atanion.mock.servlet.MockHttpServletRequest;
import junit.framework.TestCase;
import org.apache.commons.fileupload.FileItem;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TobagoMultipartFormdataRequestUnitTest extends TestCase {

// ///////////////////////////////////////////// constant

  private static final String SNIP = "--";

  private static final String BOUNDARY = "xxx";

  private static final String NEWLINE = "\r\n";


// ///////////////////////////////////////////// attribute

  private TobagoMultipartFormdataRequest request;

// ///////////////////////////////////////////// constructor

  public TobagoMultipartFormdataRequestUnitTest(String reference)
      throws UnsupportedEncodingException {
    super(reference);
    String body
        = SNIP + BOUNDARY + NEWLINE
        + parameter("color", "red")
        + parameter("city", "Amsterdam")
        + parameter("city", "Bonn")
        + parameter("city", "Pisa")
        + parameter("color", "green")
        + fileItem("file", "hello.txt", "Hello World!")
        + parameter("color", "blue")
        + parameter("color", "yellow")
        + parameter("country", "Trinidad & Tobago")
        + SNIP + BOUNDARY + SNIP + NEWLINE;

    MockHttpServletRequest mockRequest
        = new MockHttpServletRequest(body.getBytes("UTF-8"));

    request = new TobagoMultipartFormdataRequest(mockRequest);
  }

// ///////////////////////////////////////////// code

  private String parameter(String key, String value) {
    return SNIP + BOUNDARY + NEWLINE
        + "Content-Disposition: form-data; name=\"" + key + "\"" + NEWLINE
        + NEWLINE
        + value + NEWLINE;
  }

  private String fileItem(String key, String filename, String value) {
    return SNIP + BOUNDARY + NEWLINE
        + "Content-Disposition: form-data; name=\""
        + key + "\"; filename=\"" + filename + "\"" + NEWLINE
        + "Content-Type: text/plain" + NEWLINE
        + NEWLINE
        + value + NEWLINE;
  }

  public void testGetFileItem() {

    FileItem item = request.getFileItem("file");
    assertNotNull(item);
    assertEquals("filename", "hello.txt", item.getName());
    assertEquals("content", "Hello World!", item.getString());
  }

  public void testGetParameter() {

    assertEquals("red", request.getParameter("color"));
    assertEquals("Amsterdam", request.getParameter("city"));
    assertEquals("Trinidad & Tobago", request.getParameter("country"));
    assertEquals(null, request.getParameter("empty"));
  }

  public void testGetParameterValues() {

    Set expectedSet;
    Set actualSet;

    expectedSet = new HashSet(
        Arrays.asList(
            new String[]{
              "red", "green", "blue", "yellow"}));
    actualSet
        = new HashSet(Arrays.asList(request.getParameterValues("color")));
    assertEquals("color", expectedSet, actualSet);

    expectedSet = new HashSet(
        Arrays.asList(
            new String[]{
              "Amsterdam", "Bonn", "Pisa"}));
    actualSet = new HashSet(Arrays.asList(request.getParameterValues("city")));
    assertEquals("city", expectedSet, actualSet);

    expectedSet = new HashSet(
        Arrays.asList(
            new String[]{
              "Trinidad & Tobago"}));
    actualSet
        = new HashSet(Arrays.asList(request.getParameterValues("country")));
    assertEquals("country", expectedSet, actualSet);

    assertEquals("empty", null, request.getParameterValues("empty"));
  }

  public void testGetParameterNames() {

    Set actual = new HashSet();
    Enumeration e = request.getParameterNames();
    while (e.hasMoreElements()) {
      actual.add(e.nextElement());
    }

    Set expected = new HashSet(
        Arrays.asList(
            new String[]{
              "color", "city", "country"}));

    assertEquals(expected, actual);
  }

  public void testGetParameterMap() {

    Map actual = request.getParameterMap();

    Map expected = new HashMap();
    expected.put("color", new String[]{"red", "green", "blue", "yellow"});
    expected.put("city", new String[]{"Amsterdam", "Bonn", "Pisa"});
    expected.put("country", new String[]{"Trinidad & Tobago"});

    assertEquals(expected.keySet(), actual.keySet());

    Set keys = actual.keySet();
    for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
      String key = (String) iterator.next();
      String[] expectedStrings = (String[]) expected.get(key);
      String[] actualStrings = (String[]) actual.get(key);
      assertEquals(expectedStrings.length, actualStrings.length);
      Set expectedSet = new HashSet(Arrays.asList(expectedStrings));
      Set actualSet = new HashSet(Arrays.asList(actualStrings));
      assertEquals(expectedSet, actualSet);
    }
  }
}
