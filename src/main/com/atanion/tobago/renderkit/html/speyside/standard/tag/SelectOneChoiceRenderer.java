/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.html.speyside.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.renderkit.SelectOneRendererBase;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectOneChoiceRenderer extends SelectOneRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(SelectOneChoiceRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code


  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UISelectOne component = (UISelectOne)uiComponent;

    boolean inline = ComponentUtil.getBooleanAttribute(component, ATTR_INLINE);
    List<SelectItem> items = ComponentUtil.getSelectItems(component);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    String image = ResourceManagerUtil.getImage(facesContext, "image/1x1.gif");

    if (LOG.isDebugEnabled()) {
      LOG.debug("items.size() = '" + items.size() + "'");
    }

    boolean disabled = items.size() == 0
        || ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)
        || ComponentUtil.getBooleanAttribute(component, ATTR_READONLY);

    UIComponent label = component.getFacet(FACET_LABEL);
    if (!inline) {
      writer.startElement("table", component);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("cellspacing", "0", null);
      writer.writeAttribute("cellpadding", "0", null);
      writer.writeAttribute("summary", "", null);
      writer.writeAttribute("title", null, ATTR_TIP);

      writer.startElement("tr", null);
      if (label != null) {
        writer.startElement("td", null);
        writer.writeAttribute("class", "tobago-label-td", null);
        writer.writeAttribute("valign", "top", null);
        writer.writeText("", null);

        HtmlRendererUtil.encodeHtml(facesContext, label);

        writer.endElement("td");
        writer.startElement("td", null);
        writer.writeAttribute("class", "tobago-textArea-spacer-custom", null);

        writer.startElement("img", null);
        writer.writeAttribute("src",image, null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("height", "1", null);
        writer.writeAttribute("width", "5", null);
        writer.endElement("img");

        writer.endElement("td");
      }
      writer.startElement("td", null);
      writer.writeAttribute("valign", "top", null);
      writer.writeAttribute("rowspan", "2", null);
  }

    writer.startElement("select", component);
    writer.writeAttribute("name", component.getClientId(facesContext), null);
    writer.writeAttribute("id", component.getClientId(facesContext), null);
    writer.writeAttribute("disabled", disabled);
    writer.writeAttribute("style", null, "style");
    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
    writer.writeAttribute("title", null, ATTR_TIP);
    String onchange = HtmlUtils.generateOnchange(component, facesContext);
    if (onchange != null) {
      writer.writeAttribute("onchange", onchange, null);
    }

    Object value = component.getValue();
    for (SelectItem item : items) {
      writer.startElement("option", null);
      String formattedValue
          = getFormattedValue(facesContext, component, item.getValue());
      writer.writeAttribute("value", formattedValue, null);
      if (item.getValue().equals(value)) {
        writer.writeAttribute("selected", "selected", null);
      }
      writer.writeText(item.getLabel(), null);
      writer.endElement("option");
    }
    writer.endElement("select");

    if (!inline) {
      writer.endElement("td");
      writer.endElement("tr");
      writer.startElement("tr", null);
      if (label != null) {
        writer.startElement("td", null);
        writer.writeAttribute("class", "tobago-label-td-underline-label", null);
        writer.startElement("img", null);
        writer.writeAttribute("src",image, null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("height", "1", null);
        writer.endElement("img");
        writer.endElement("td");
        writer.startElement("td", null);
        writer.writeAttribute("class", "tobago-label-td-underline-spacer", null);
        writer.startElement("img", null);
        writer.writeAttribute("src",image, null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("height", "1", null);
        writer.endElement("img");
        writer.endElement("td");
      }
      writer.endElement("tr");
      writer.endElement("table");
    }
  }


  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;

    if (component.getFacet(FACET_LABEL) != null) {
      int labelWidht = LayoutUtil.getLabelWidth(component);
      space += labelWidht != 0 ? labelWidht : getLabelWidth(facesContext, component);
      space += getConfiguredValue(facesContext, component, "labelSpace");
    }

    return space;
  }

// ///////////////////////////////////////////// bean getter + setter

}

