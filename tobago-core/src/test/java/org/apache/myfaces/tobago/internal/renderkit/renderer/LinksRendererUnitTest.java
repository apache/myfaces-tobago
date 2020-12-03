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
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UILinks;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LinksRendererUnitTest extends RendererTestBase {

  @Test
  public void linkInsideLinks() throws IOException {
    final UILinks l = (UILinks) ComponentUtils.createComponent(
        facesContext, Tags.links.componentType(), RendererTypes.Links, "list");
    final UILink c = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "id");
    c.setLabel("apache");
    c.setLink("https://www.apache.org/");
    l.getChildren().add(c);
    l.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/links/link-inside-links.html"), formattedResult());
  }

  @Test
  public void linkInsideLinksSub() throws IOException {
    final UILinks l = (UILinks) ComponentUtils.createComponent(
        facesContext, Tags.links.componentType(), RendererTypes.Links, "list");
    final UILink c = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "id");
    c.setLabel("apache");

    final UILink s = (UILink) ComponentUtils.createComponent(
        facesContext, Tags.link.componentType(), RendererTypes.Link, "sub");
    s.setLabel("sub");
    s.setLink("https://www.apache.org/");
    c.getChildren().add(s);

    l.getChildren().add(c);
    l.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/links/link-inside-links-sub.html"), formattedResult());
  }

}
