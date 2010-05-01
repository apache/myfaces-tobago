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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Locale;

public class LabelRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(LabelRenderer.class);

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    UILabel output = null;
    // todo: remove test after 1.5.0, then UILabel is required
    if (component instanceof UILabel) {
      output = (UILabel) component;
    } else {
      Deprecation.LOG.warn("LabelRenderer should only render UILabel but got " + component.getClass().getName()
          + " id=" + component.getClientId(facesContext));
    }
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    LabelWithAccessKey label = new LabelWithAccessKey(component);

    String forValue = ComponentUtils.findClientIdFor(component, facesContext);

    createClassAttribute(component);
    
    String clientId = component.getClientId(facesContext);
    writer.startElement(HtmlConstants.DIV, component);
    HtmlRendererUtils.renderDojoDndItem(component, writer, true);
    writer.writeClassAttribute();
    // todo: remove after 1.5.0 (see begin of method)
    if (output != null) {
      Style style = new Style(facesContext, output);
      writer.writeStyleAttribute(style);
    }
    writer.startElement(HtmlConstants.A, component);
    writer.writeClassAttribute();
    writer.startElement(HtmlConstants.LABEL, component);
    writer.writeIdAttribute(clientId);
    if (forValue != null) {
      writer.writeAttribute(HtmlAttributes.FOR, forValue, false);
    }
    writer.writeClassAttribute();

    HtmlRendererUtils.renderTip(component, writer);

    if (label.getText() != null) {
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement(HtmlConstants.LABEL);
    writer.endElement(HtmlConstants.A);

    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("dublicated accessKey : " + label.getAccessKey());
      }      
      HtmlRendererUtils.addClickAcceleratorKey(facesContext, clientId, label.getAccessKey());
    }
    writer.endElement(HtmlConstants.DIV);
  }

  private void createClassAttribute(UIComponent component) {

    String rendererType = component.getRendererType().toLowerCase(Locale.ENGLISH);
    String name = getRendererName(rendererType);

    UIComponent parent = findParent(component);
    StyleClasses styleClasses = StyleClasses.ensureStyleClasses(component);
    styleClasses.updateClassAttribute(parent, name);
    styleClasses.addMarkupClass(component, name);

  }

  private UIComponent findParent(UIComponent component) {
    UIComponent parent = component.getParent();
    if (component != parent.getFacet(Facets.LABEL)) {

      // try to find belonging component
      // this can only success if the component was rendered (created) before this label
      parent = ComponentUtils.findFor(component);
    }
    if (parent == null) {
      parent = component;
    }
    return parent;
  }

}

