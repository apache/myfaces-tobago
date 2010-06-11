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

import org.apache.commons.fileupload.FileItem;
import org.apache.myfaces.tobago.internal.mock.servlet.MockHttpServletRequest;
import org.apache.myfaces.tobago.internal.webapp.TobagoMultipartFormdataRequest;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
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
    mockRequest.setMethod("post");

    request = new TobagoMultipartFormdataRequest(mockRequest, System.getProperty("java.io.tmpdir"), 1024 * 1024);
  }

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

  @Test
  public void testGetFileItem() {

    FileItem item = request.getFileItem("file");
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

    Set<Object> actual = new HashSet<Object>();
    Enumeration e = request.getParameterNames();
    while (e.hasMoreElements()) {
      actual.add(e.nextElement());
    }

    Set<String> expected = new HashSet<String>(
        Arrays.asList("color", "city", "country"));

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetParameterMap() {

    Map actual = request.getParameterMap();

    Map<String, String[]> expected = new HashMap<String, String[]>();
    expected.put("color", new String[]{"red", "green", "blue", "yellow"});
    expected.put("city", new String[]{"Amsterdam", "Bonn", "Pisa"});
    expected.put("country", new String[]{"Trinidad & Tobago"});

    Assert.assertEquals(expected.keySet(), actual.keySet());

    Set keys = actual.keySet();
    for (Object key1 : keys) {
      String key = (String) key1;
      String[] expectedStrings = expected.get(key);
      String[] actualStrings = (String[]) actual.get(key);
      Assert.assertEquals(expectedStrings.length, actualStrings.length);
      Set<String> expectedSet = new HashSet<String>(Arrays.asList(expectedStrings));
      Set<String> actualSet = new HashSet<String>(Arrays.asList(actualStrings));
      Assert.assertEquals(expectedSet, actualSet);
    }
  }
}
