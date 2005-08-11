/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.SelectOneRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
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


  public boolean getRendersChildren() {
    return true;
  }

  protected void renderMain(FacesContext facesContext, UIComponent uiComponent,
                            TobagoResponseWriter writer) throws IOException {
    UISelectOne component = (UISelectOne)uiComponent;
    List<SelectItem> items = ComponentUtil.getSelectItems(component);

    if (LOG.isDebugEnabled()) {
      LOG.debug("items.size() = '" + items.size() + "'");
    }

    boolean disabled = items.size() == 0
        || ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)
        || ComponentUtil.getBooleanAttribute(component, ATTR_READONLY);



    writer.startElement("select", component);
    writer.writeNameAttribute(component.getClientId(facesContext));
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeAttribute("disabled", disabled);
    writer.writeAttribute("style", null, ATTR_STYLE);
    writer.writeComponentClass();
    writer.writeAttribute("title", null, ATTR_TIP);
    String onchange = HtmlUtils.generateOnchange(component, facesContext);
    if (onchange != null) {
      writer.writeAttribute("onchange", onchange, null);
    }

    Object value = component.getValue();
    for (SelectItem item : items) {
      final Object itemValue = item.getValue();
      writer.startElement("option", null);
      String formattedValue
          = getFormattedValue(facesContext, component, itemValue);
      writer.writeAttribute("value", formattedValue, null);
      if (itemValue.equals(value)) {
        writer.writeAttribute("selected", "selected", null);
      }
      writer.writeText(item.getLabel(), null);
      writer.endElement("option");
    }
    writer.endElement("select");



  }

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;

//    if (component.getFacet(FACET_LABEL) != null) {
//      int labelWidht = LayoutUtil.getLabelWidth(component);
//      space += labelWidht != 0 ? labelWidht : getLabelWidth(facesContext, component);
//      space += getConfiguredValue(facesContext, component, "labelSpace");
//    }

    return space;
  }
// ///////////////////////////////////////////// bean getter + setter

}

