/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 29.06.2004 11:07:07.
 * $Id$
 */
package com.atanion.tobago.webapp;

import junit.framework.TestCase;

import org.apache.commons.fileupload.FileItem;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TobagoMultipartFormdataRequestUnitTest extends TestCase {

// ///////////////////////////////////////////// constant

  private static final String SNIP = "--";

  private static final String BOUNDARY = "xxx";

  private static final String NEWLINE = "\r\n";


// ///////////////////////////////////////////// attribute

  TobagoMultipartFormdataRequest request;

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

// ///////////////////////////////////////////// mock class

  public static class MockServletInputStream extends ServletInputStream {

    private byte[] body;

    private int next;

    public MockServletInputStream(byte[] body) {
      this.body = body;
    }

    public int read() throws IOException {
      if (next < body.length) {
        return body[next++];
      } else {
        return -1;
      }
    }
  }

// ///////////////////////////////////////////// mock class

  public static class MockHttpServletRequest implements HttpServletRequest {

    private byte[] body;

    public MockHttpServletRequest(byte[] body) {
      this.body = body;
    }

    public String getContentType() {
      return "multipart/form-data; boundary=xxx";
    }

    public String getHeader(String reference) {
      if (reference.equals("Content-type")) {
        return "multipart/form-data; boundary=xxx";
      }
      return null;
    }

    public ServletInputStream getInputStream() throws IOException {
      return new MockServletInputStream(body);
    }

    public int getContentLength() {
      return body.length;
    }

    public String getAuthType() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public Cookie[] getCookies() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return new Cookie[0];
    }

    public long getDateHeader(String reference) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return 0;
    }

    public Enumeration getHeaders(String reference) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public Enumeration getHeaderNames() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public int getIntHeader(String reference) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return 0;
    }

    public String getMethod() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getPathInfo() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getPathTranslated() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getContextPath() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getQueryString() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getRemoteUser() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public boolean isUserInRole(String reference) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return false;
    }

    public Principal getUserPrincipal() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getRequestedSessionId() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getRequestURI() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public StringBuffer getRequestURL() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getServletPath() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public HttpSession getSession(boolean b) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public HttpSession getSession() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public boolean isRequestedSessionIdValid() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return false;
    }

    public boolean isRequestedSessionIdFromCookie() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return false;
    }

    public boolean isRequestedSessionIdFromURL() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return false;
    }

    public Object getAttribute(String reference) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public Enumeration getAttributeNames() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getCharacterEncoding() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public void setCharacterEncoding(String reference)
        throws UnsupportedEncodingException {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
    }

    public String getParameter(String reference) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public Enumeration getParameterNames() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String[] getParameterValues(String reference) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return new String[0];
    }

    public Map getParameterMap() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getProtocol() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getScheme() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getServerName() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public int getServerPort() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return 0;
    }

    public BufferedReader getReader() throws IOException {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getRemoteAddr() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getRemoteHost() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public void setAttribute(String reference, Object o) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
    }

    public void removeAttribute(String reference) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
    }

    public Locale getLocale() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public Enumeration getLocales() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public boolean isSecure() {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return false;
    }

    public RequestDispatcher getRequestDispatcher(String reference) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }

    public String getRealPath(String reference) {
      if (true) {
        throw new RuntimeException("Not implemented");
      }
      return null;
    }
  }

}
