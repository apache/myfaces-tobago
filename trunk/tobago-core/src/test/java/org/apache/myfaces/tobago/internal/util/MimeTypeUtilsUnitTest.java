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

package org.apache.myfaces.tobago.internal.util;

import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.Assert;
import org.junit.Test;

public class MimeTypeUtilsUnitTest extends AbstractTobagoTestBase {

  public static final String GIF = "http:///localhost:8080/demo/demo.gif";
  public static final String PNG = "http:///localhost:8080/demo/demo.png";
  public static final String JPG = "http:///localhost:8080/demo/demo.jpg";
  public static final String JS = "http:///localhost:8080/demo/demo.js";
  public static final String CSS = "http:///localhost:8080/demo/demo.css";
  public static final String ICO = "http:///localhost:8080/demo/demo.ico";
  public static final String HTML = "http:///localhost:8080/demo/demo.html";
  public static final String HTM = "http:///localhost:8080/demo/demo.htm";
  public static final String MAP = "http:///localhost:8080/demo/demo.map";
  public static final String WOFF = "http:///localhost:8080/demo/demo.woff";
  public static final String WOFF2 = "http:///localhost:8080/demo/demo.woff2";
  public static final String TTF = "http:///localhost:8080/demo/demo.ttf";
  public static final String EOT = "http:///localhost:8080/demo/demo.eot";
  public static final String SVG = "http:///localhost:8080/demo/demo.svg";
  public static final String ODT = "http:///localhost:8080/demo/demo.odt";

  public static final String UNKNOWN0 = "http:///localhost:8080/demo/demo.PNG";
  public static final String UNKNOWN1 = "http:///localhost:8080/demo/demos._png";
  public static final String UNKNOWN2 = "http:///localhost:8080/demo/demo.ggif";
  public static final String UNKNOWN3 = "http:///localhost:8080/demo/demos.ppg";
  public static final String UNKNOWN4 = "http:///localhost:8080/demo/demos.pngx";
  public static final String UNKNOWN5 = "http:///localhost:8080/demo/demos.png.";

  @Test
  public void testMimeTypes() {

    Assert.assertEquals("image/gif", MimeTypeUtils.getMimeTypeForFile(GIF));
    Assert.assertEquals("image/png", MimeTypeUtils.getMimeTypeForFile(PNG));
    Assert.assertEquals("image/jpeg", MimeTypeUtils.getMimeTypeForFile(JPG));
    Assert.assertEquals("text/javascript", MimeTypeUtils.getMimeTypeForFile(JS));
    Assert.assertEquals("text/css", MimeTypeUtils.getMimeTypeForFile(CSS));
    Assert.assertEquals("image/vnd.microsoft.icon", MimeTypeUtils.getMimeTypeForFile(ICO));
    Assert.assertEquals("text/html", MimeTypeUtils.getMimeTypeForFile(HTML));
    Assert.assertEquals("text/html", MimeTypeUtils.getMimeTypeForFile(HTM));
    Assert.assertEquals("application/json", MimeTypeUtils.getMimeTypeForFile(MAP));
    Assert.assertEquals("application/font-woff", MimeTypeUtils.getMimeTypeForFile(WOFF));
    Assert.assertEquals("application/font-woff2", MimeTypeUtils.getMimeTypeForFile(WOFF2));
    Assert.assertEquals("application/x-font-ttf", MimeTypeUtils.getMimeTypeForFile(TTF));
    Assert.assertEquals("application/vnd.ms-fontobject", MimeTypeUtils.getMimeTypeForFile(EOT));
    Assert.assertEquals("image/svg+xml", MimeTypeUtils.getMimeTypeForFile(SVG));
  }

  @Test
  public void testMimeTypesUnknown() {

    Assert.assertEquals(null, MimeTypeUtils.getMimeTypeForFile(UNKNOWN0));
    Assert.assertEquals(null, MimeTypeUtils.getMimeTypeForFile(UNKNOWN1));
    Assert.assertEquals(null, MimeTypeUtils.getMimeTypeForFile(UNKNOWN2));
    Assert.assertEquals(null, MimeTypeUtils.getMimeTypeForFile(UNKNOWN3));
    Assert.assertEquals(null, MimeTypeUtils.getMimeTypeForFile(UNKNOWN4));
    Assert.assertEquals(null, MimeTypeUtils.getMimeTypeForFile(UNKNOWN5));
  }

  @Test
  public void testMimeTypesConfigured() {
    // comes from tobago-config-for-unit-tests.xml
    Assert.assertEquals("application/vnd.oasis.opendocument.text", MimeTypeUtils.getMimeTypeForFile(ODT));
  }

}
