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
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.junit.Assert;
import org.junit.Test;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class StyleRenderUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testEncodeSelector() {
    Assert.assertEquals("", StyleRenderUtils.encodeSelector());

    Assert.assertEquals("", StyleRenderUtils.encodeSelector(""));

    Assert.assertEquals("tag", StyleRenderUtils.encodeSelector("tag"));

    Assert.assertEquals(".class", StyleRenderUtils.encodeSelector(".class"));

    Assert.assertEquals("parent>child", StyleRenderUtils.encodeSelector("parent>child"));

    Assert.assertEquals("#id\\:sub", StyleRenderUtils.encodeSelector("#id:sub"));

    Assert.assertEquals("#id\\:sub\\:sub2", StyleRenderUtils.encodeSelector("#id:sub:sub2"));

    Assert.assertEquals("#id\\:sub\\:sub2\\:sub3", StyleRenderUtils.encodeSelector("#id:sub:sub2:sub3"));
  }

  @Test
  public void writeIdSelector() throws IOException {

    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    StyleRenderUtils.writeIdSelector(writer, "id");
    Assert.assertEquals("#id", getLastWritten());

    StyleRenderUtils.writeIdSelector(writer, "id:sub");
    Assert.assertEquals("#id\\:sub", getLastWritten());

    StyleRenderUtils.writeIdSelector(writer, "id:sub:sub2");
    Assert.assertEquals("#id\\:sub\\:sub2", getLastWritten());

    StyleRenderUtils.writeIdSelector(writer, "id:sub:sub2:sub3");
    Assert.assertEquals("#id\\:sub\\:sub2\\:sub3", getLastWritten());

    StyleRenderUtils.writeIdSelector(writer, "id::sub");
    Assert.assertEquals("#id\\:\\:sub", getLastWritten());
  }

  @Test
  public void writeSelector() throws IOException {

    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    StyleRenderUtils.writeSelector(writer, "parent>child");
    Assert.assertEquals("parent>child", getLastWritten());

    StyleRenderUtils.writeSelector(writer, "parent<child");
    Assert.assertEquals("parent&lt;child", getLastWritten());

    StyleRenderUtils.writeSelector(writer, "#id");
    Assert.assertEquals("#id", getLastWritten());

    StyleRenderUtils.writeSelector(writer, "#id\\:sub");
    Assert.assertEquals("#id\\:sub", getLastWritten());
  }
}
