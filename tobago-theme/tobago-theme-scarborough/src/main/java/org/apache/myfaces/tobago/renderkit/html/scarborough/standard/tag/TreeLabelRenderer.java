package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

import org.apache.myfaces.tobago.component.UITreeLabel;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TreeLabelRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TreeLabelRenderer.class);

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final UITreeLabel label = (UITreeLabel) component;
    final UITreeNode node = ComponentUtils.findAncestor(label, UITreeNode.class);
    final boolean folder = node.isFolder();

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.LABEL, label);
    writer.writeClassAttribute(Classes.create(label));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, label);
    writer.writeStyleAttribute(createStyle(facesContext, label));
    HtmlRendererUtils.renderTip(label, writer);

    if (label.getValue() != null) {
      writer.writeText((String) label.getValue());
    }
    writer.endElement(HtmlElements.LABEL);
  }

  protected Style createStyle(FacesContext facesContext, UITreeLabel link) {
    return new Style(facesContext, link);
  }

}
