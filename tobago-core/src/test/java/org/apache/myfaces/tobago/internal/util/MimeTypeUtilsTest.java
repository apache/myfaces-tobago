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

import org.junit.Assert;
import org.junit.Test;

public class MimeTypeUtilsTest {

  @Test
  public void testGetMimeTypeForFile() throws Exception {
    MimeTypeUtils.init(null);
    Assert.assertEquals("image/gif", MimeTypeUtils.getMimeTypeForFile("image.gif"));
    Assert.assertEquals("image/png", MimeTypeUtils.getMimeTypeForFile("images/red.png"));
    Assert.assertEquals("image/jpeg", MimeTypeUtils.getMimeTypeForFile("images/button.jpg"));
    Assert.assertEquals("text/javascript", MimeTypeUtils.getMimeTypeForFile("path/tobago.js"));
    Assert.assertEquals("text/css", MimeTypeUtils.getMimeTypeForFile("tobago.css"));
    Assert.assertEquals("image/vnd.microsoft.icon", MimeTypeUtils.getMimeTypeForFile("tobago.ico"));
    Assert.assertEquals("text/html", MimeTypeUtils.getMimeTypeForFile("test.html"));
    Assert.assertEquals("text/html", MimeTypeUtils.getMimeTypeForFile("test.htm"));
    Assert.assertEquals("application/json", MimeTypeUtils.getMimeTypeForFile("object.map"));
    Assert.assertNull(MimeTypeUtils.getMimeTypeForFile("notValid.extension"));
  }
}
