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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Locale;

public class LabelRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(LabelRenderer.class);

  private void createClassAttribute(UIComponent component) {

    String rendererType = component.getRendererType().toLowerCase(Locale.ENGLISH);
    String name = getRendererName(rendererType);

    UIComponent parent = findParent(component);
    StyleClasses.ensureStyleClasses(component).updateClassAttribute(name, parent);
  }

  private UIComponent findParent(UIComponent component) {
    UIComponent parent = component.getParent();
    if (component != parent.getFacet(FACET_LABEL)) {

      // try to find belonging component
      // this can only success if the component was rendered (created) before this label
      parent = ComponentUtil.findFor(component);
    }
    if (parent == null) {
      parent = component;
    }
    return parent;
  }

  public void encodeEnd(
      FacesContext facesContext, UIComponent component) throws IOException {

    UIOutput output = (UIOutput) component;

    Integer width = LayoutUtil.getLayoutWidth(output);
    if (width == null
        && !(ComponentUtil.getBooleanAttribute(findParent(component), ATTR_INLINE)
             || ComponentUtil.getBooleanAttribute(component, ATTR_INLINE))) {
      width = Integer.valueOf(getConfiguredValue(facesContext, component, "labelWidth"));      
    }

    LabelWithAccessKey label = new LabelWithAccessKey(component);

    String forValue = ComponentUtil.findClientIdFor(output, facesContext);

    createClassAttribute(component);
    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement(HtmlConstants.DIV, output);
    writer.writeComponentClass();    
    writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);
    writer.startElement(HtmlConstants.A, output);
    writer.writeComponentClass();
    writer.startElement(HtmlConstants.LABEL, output);
    String clientId = output.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    if (forValue != null) {
      writer.writeAttribute(HtmlAttributes.FOR, forValue, null);
    }
    writer.writeComponentClass();
    if (width != null) {
      writer.writeAttribute(HtmlAttributes.STYLE, "width: " + width + "px;", null);
    }
    writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);
    
    if (label.getText() != null) {
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement(HtmlConstants.LABEL);
    writer.endElement(HtmlConstants.A);

    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("dublicated accessKey : " + label.getAccessKey());
      }      
      HtmlRendererUtil.addClickAcceleratorKey(
          facesContext, clientId, label.getAccessKey());
    }
    writer.endElement(HtmlConstants.DIV);
  }

}

