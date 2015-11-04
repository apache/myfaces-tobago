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

import org.apache.commons.fileupload.FileItem;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockServletInputStream;
import org.apache.myfaces.tobago.internal.webapp.TobagoMultipartFormdataRequest;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TobagoMultipartFormdataRequestUnitTest {

  private static final String SNIP = "--";

  private static final String BOUNDARY = "xxx";

  private static final String NEWLINE = "\r\n";

  private TobagoMultipartFormdataRequest request;

  public TobagoMultipartFormdataRequestUnitTest() throws UnsupportedEncodingException {
    final String body
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

    final MockHttpServletRequest mockRequest = new MultipartRequest("multipart/form-data; boundary=xxx", body.length());
    mockRequest.setMethod("post");
    ByteArrayInputStream source = new ByteArrayInputStream(body.getBytes("UTF-8"));
    mockRequest.setInputStream(new MockServletInputStream(source));

    request = new TobagoMultipartFormdataRequest(mockRequest, System.getProperty("java.io.tmpdir"), 1024 * 1024);
  }

  private String parameter(final String key, final String value) {
    return SNIP + BOUNDARY + NEWLINE
        + "Content-Disposition: form-data; name=\"" + key + "\"" + NEWLINE
        + NEWLINE
        + value + NEWLINE;
  }

  private String fileItem(final String key, final String filename, final String value) {
    return SNIP + BOUNDARY + NEWLINE
        + "Content-Disposition: form-data; name=\""
        + key + "\"; filename=\"" + filename + "\"" + NEWLINE
        + "Content-Type: text/plain" + NEWLINE
        + NEWLINE
        + value + NEWLINE;
  }

  @Test
  public void testGetFileItem() {

    final FileItem item = request.getFileItem("file");
    Assert.assertNotNull(item);
    Assert.assertEquals("filename", "hello.txt", item.getName());
    Assert.assertEquals("content", "Hello World!", item.getString());
  }

  @Test
  public void testGetParameter() {

    Assert.assertEquals("red", request.getParameter("color"));
    Assert.assertEquals("Amsterdam", request.getParameter("city"));
    Assert.assertEquals("Trinidad & Tobago", request.getParameter("country"));
    Assert.assertEquals(null, request.getParameter("empty"));
  }

  @Test
  public void testGetParameterValues() {

    Set<String> expectedSet;
    Set<String> actualSet;

    expectedSet = new HashSet<String>(
        Arrays.asList("red", "green", "blue", "yellow"));
    actualSet
        = new HashSet<String>(Arrays.asList(request.getParameterValues("color")));
    Assert.assertEquals("color", expectedSet, actualSet);

    expectedSet = new HashSet<String>(
        Arrays.asList("Amsterdam", "Bonn", "Pisa"));
    actualSet = new HashSet<String>(Arrays.asList(request.getParameterValues("city")));
    Assert.assertEquals("city", expectedSet, actualSet);

    expectedSet = new HashSet<String>(
        Arrays.asList("Trinidad & Tobago"));
    actualSet
        = new HashSet<String>(Arrays.asList(request.getParameterValues("country")));
    Assert.assertEquals("country", expectedSet, actualSet);

    Assert.assertEquals("empty", null, request.getParameterValues("empty"));
  }

  @Test
  public void testGetParameterNames() {

    final Set<Object> actual = new HashSet<Object>();
    final Enumeration e = request.getParameterNames();
    while (e.hasMoreElements()) {
      actual.add(e.nextElement());
    }

    final Set<String> expected = new HashSet<String>(
        Arrays.asList("color", "city", "country"));

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetParameterMap() {

    final Map actual = request.getParameterMap();

    final Map<String, String[]> expected = new HashMap<String, String[]>();
    expected.put("color", new String[]{"red", "green", "blue", "yellow"});
    expected.put("city", new String[]{"Amsterdam", "Bonn", "Pisa"});
    expected.put("country", new String[]{"Trinidad & Tobago"});

    Assert.assertEquals(expected.keySet(), actual.keySet());

    final Set keys = actual.keySet();
    for (final Object key1 : keys) {
      final String key = (String) key1;
      final String[] expectedStrings = expected.get(key);
      final String[] actualStrings = (String[]) actual.get(key);
      Assert.assertEquals(expectedStrings.length, actualStrings.length);
      final Set<String> expectedSet = new HashSet<String>(Arrays.asList(expectedStrings));
      final Set<String> actualSet = new HashSet<String>(Arrays.asList(actualStrings));
      Assert.assertEquals(expectedSet, actualSet);
    }
  }

  private static class MultipartRequest extends MockHttpServletRequest {

   private String contentType;
    private int contentLength;

    private MultipartRequest(String contentType, int contentLength) {
      this.contentType = contentType;
      this.contentLength = contentLength;
    }

    @Override
    public String getContentType() {
      return contentType;
    }

    @Override
    public int getContentLength() {
      return contentLength;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
      throw new UnsupportedOperationException();
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
      throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
      throw new UnsupportedOperationException();
    }

    @Override
    public void login(String s, String s2) throws ServletException {
      throw new UnsupportedOperationException();
    }

    @Override
    public void logout() throws ServletException {
      throw new UnsupportedOperationException();
    }

    @Override
    public AsyncContext getAsyncContext() {
      throw new UnsupportedOperationException();
    }

    @Override
    public DispatcherType getDispatcherType() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAsyncStarted() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAsyncSupported() {
      throw new UnsupportedOperationException();
    }

    @Override
    public AsyncContext startAsync() {
      throw new UnsupportedOperationException();
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) {
      throw new UnsupportedOperationException();
    }
  }
}
