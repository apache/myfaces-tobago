/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.LabelWithAccessKey;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.util.AccessKeyMap;

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

    String styleClass = (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE_CLASS);
    styleClass = HtmlRendererUtil.updateClassAttribute(styleClass, name, parent);
    component.getAttributes().put(TobagoConstants.ATTR_STYLE_CLASS, styleClass);
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

    // todo move into labelLayout ?
    createClassAttribute(component);
    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("a", output);
    writer.writeComponentClass();
    writer.startElement("label", output);
    if (forValue != null) {
      writer.writeAttribute("for", forValue, null);
    }
    writer.writeComponentClass();
    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && ! AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
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
  }

}

