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

import org.apache.myfaces.tobago.component.UITreeLabel;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeListbox;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TreeLabelRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TreeLabelRenderer.class);

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIData data = ComponentUtils.findAncestor(component, AbstractUIData.class);
    final boolean listbox = data instanceof AbstractUITreeListbox;

    final UITreeLabel label = (UITreeLabel) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String text = StringUtils.defaultString((String) label.getValue());

    if (listbox) {
      writer.writeText(text);
    } else {
      writer.startElement(HtmlElements.LABEL);
      writer.writeClassAttribute(Classes.create(label));
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, label);
      writer.writeStyleAttribute(label.getStyle());
      final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, label);
      if (title != null) {
        writer.writeAttribute(HtmlAttributes.TITLE, title, true);
      }

      writer.writeText(text);

      writer.endElement(HtmlElements.LABEL);
    }
  }
}
