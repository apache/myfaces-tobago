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

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class PartUtilsUnitTest {

  @Test
  public void testGetSubmittedFileName() throws Exception {

    final String dc0 = "form-data; name=\"page:file\"; filename=\"foo.jpg\"";
    Assert.assertEquals("foo.jpg", PartUtils.getSubmittedFileName(new PartMock(dc0)));

    final String dc1 = "form-data; name=\"page:file\"; filename=\"foo;bar=\\\"boo\\\"-bar.jpg\"";
    Assert.assertEquals("foo;bar=\"boo\"-bar.jpg", PartUtils.getSubmittedFileName(new PartMock(dc1)));

/* XXX will fail, because a / will be encoded as a :
    final String dc2
        = "form-data; name=\"page:file1\"; filename=\"semicolon;doublequote\\\"backslash\\"
        + "slash:doublebackslash\\\\quote'umlautsäöüumlautsäöüeuro€tilde~muµspace hiraganaぁ.jpg\"";
    Assert.assertEquals(
        "semicolon;doublequote\"backslash\\slash/doublebackslash\\\\quote'umlautsäöüumlautsäöüeuro€tilde~muµspace "
            + "hiraganaぁ.jpg", PartUtils.getSubmittedFileName(new PartMock(dc2)));
*/
  }

  private static final class PartMock implements Part {

    private String contentDisposition;

    public PartMock(String contentDisposition) {
      this.contentDisposition = contentDisposition;
    }

    @Override
    public void delete() throws IOException {

    }

    @Override
    public String getContentType() {
      return null;
    }

    @Override
    public String getHeader(String headerName) {
      if (headerName.equals("Content-Disposition")) {
        return contentDisposition;
      } else {
        return null;
      }
    }

    @Override
    public Collection<String> getHeaderNames() {
      return null;
    }

    @Override
    public Collection<String> getHeaders(String headerName) {
      return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
      return null;
    }

    @Override
    public String getName() {
      return null;
    }

    @Override
    public long getSize() {
      return 0;
    }

    @Override
    public void write(String fileName) throws IOException {

    }
  }
}
