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

import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class LabelRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(LabelRenderer.class);

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);

    ComponentUtils.evaluateAutoFor(component, UIInput.class);

    // adding the markups from the corresponding input component
    final UILabel label = (UILabel) component;
    final UIComponent corresponding = ComponentUtils.findFor(label);
    if (corresponding != null) {
      Markup markup = label.getCurrentMarkup();
      markup = ComponentUtils.updateMarkup(corresponding, markup);
      label.setCurrentMarkup(markup);
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    UILabel label = (UILabel) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    String forValue = ComponentUtils.findClientIdFor(label, facesContext);

    String clientId = label.getClientId(facesContext);
    writer.startElement(HtmlElements.LABEL, label);
    HtmlRendererUtils.renderDojoDndItem(label, writer, true);
    final Classes classes = Classes.create(label);
    writer.writeClassAttribute(classes);
    writer.writeStyleAttribute(new Style(facesContext, label));
    writer.writeIdAttribute(clientId);
    if (forValue != null) {
      writer.writeAttribute(HtmlAttributes.FOR, forValue, false);
    }
    HtmlRendererUtils.renderTip(label, writer);

    encodeTextContent(facesContext, writer, label);

    writer.endElement(HtmlElements.LABEL);
  }

  /** Encodes the text inside of the label. 
   * Can be overwritten in other themes.
   */
  protected void encodeTextContent(FacesContext facesContext, TobagoResponseWriter writer, UILabel label) 
      throws IOException {
    String clientId = label.getClientId(facesContext);
    LabelWithAccessKey key = new LabelWithAccessKey(label);
    if (key.getText() != null) {
      HtmlRendererUtils.writeLabelWithAccessKey(writer, key);
    }
    if (key.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, key.getAccessKey())) {
        LOG.info("Duplicated accessKey : " + key.getAccessKey());
      }
      HtmlRendererUtils.addClickAcceleratorKey(facesContext, clientId, key.getAccessKey());
    }
  }
}
