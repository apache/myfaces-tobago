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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class BoxRendererUnitTest extends RendererTestBase {

  @Test
  public void boxLabel() throws IOException {
    final UIBox c = (UIBox) ComponentUtils.createComponent(
        facesContext, Tags.box.componentType(), RendererTypes.Box, "id");
    c.setLabel("label");
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/box/box-label.html"), formattedResult());
  }

  @Test
  public void boxLabelFacet() throws IOException {
    final UIBox c = (UIBox) ComponentUtils.createComponent(
        facesContext, Tags.box.componentType(), RendererTypes.Box, "id");
    final UIOut o = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    o.setValue("label");
    o.setPlain(true);
    c.getFacets().put("label", o);
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/box/box-label-facet.html"), formattedResult());
  }

  @Test
  public void simple() throws IOException {
    final UIBox c = (UIBox) ComponentUtils.createComponent(
        facesContext, Tags.box.componentType(), RendererTypes.Box, "id");
    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/box/simple.html"), formattedResult());
  }

}
