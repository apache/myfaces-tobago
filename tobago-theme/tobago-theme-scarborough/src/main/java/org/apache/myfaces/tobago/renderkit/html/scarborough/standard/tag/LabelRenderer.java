package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class LabelRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(LabelRenderer.class);

  private void createClassAttribute(UIComponent component) {

    String rendererType = component.getRendererType().toLowerCase();
    String name = getRendererName(rendererType);

    UIComponent parent = findParent(component);

    String styleClass = (String) component.getAttributes().get(ATTR_STYLE_CLASS);
    styleClass = HtmlRendererUtil.updateClassAttribute(styleClass, name, parent);
    component.getAttributes().put(ATTR_STYLE_CLASS, styleClass);
  }

  private UIComponent findParent(UIComponent component) {
    UIComponent parent = component.getParent();
    if (component != parent.getFacet("label")) {
      // try to find belonging component
      // this can only success if the component was rendered (created) before this label
      parent = ComponentUtil.findFor(component);
    }
    if (parent == null) {
      parent = component;
    }
    return parent;
  }

  public void encodeEndTobago(
      FacesContext facesContext, UIComponent component) throws IOException {

    UIOutput output = (UIOutput) component;

    Integer width = LayoutUtil.getLayoutWidth(output);
    if (width == null
        && !(ComponentUtil.getBooleanAttribute(findParent(component), ATTR_INLINE)
             || ComponentUtil.getBooleanAttribute(component, ATTR_INLINE))) {
      width = new
          Integer(getConfiguredValue(facesContext, component, "labelWidth"));      
    }

    LabelWithAccessKey label = new LabelWithAccessKey(component);

    String forValue = ComponentUtil.findClientIdFor(output, facesContext);

    // TODO move into labelLayout ?
    createClassAttribute(component);
    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("div", output);
    writer.writeComponentClass();    
    writer.writeAttribute("style", null, ATTR_STYLE);
    writer.startElement("a", output);
    writer.writeComponentClass();
    writer.startElement("label", output);
    if (forValue != null) {
      writer.writeAttribute("for", forValue, null);
    }
    writer.writeComponentClass();
    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("dublicated accessKey : " + label.getAccessKey());
      }
      writer.writeAttribute("accesskey", label.getAccessKey(), null);
    }
    if (width != null) {
      writer.writeAttribute("style", "width: " + width + "px;", null);
    }
    writer.writeAttribute("title", null, ATTR_TIP);
    
    if (label.getText() != null) {
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement("label");
    writer.endElement("a");
    writer.endElement("div");
  }

}

