/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.SelectOneRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;
import static org.apache.myfaces.tobago.TobagoConstants.*;

public class SelectOneListboxRenderer extends SelectOneRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(SelectOneListboxRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code


  public boolean getRendersChildren() {
    return true;
  }

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;

//    if (component.getFacet(FACET_LABEL) != null) {
//      int labelWidth = LayoutUtil.getLabelWidth(component);
//      space += labelWidth != 0 ? labelWidth : getLabelWidth(facesContext, component);
//      space += getConfiguredValue(facesContext, component, "labelSpace");
//    }

    return space;
  }
  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    int fixedHeight = -1;
    String height = (String) component.getAttributes().get(ATTR_HEIGHT);
    if (height != null) {
      try {
        fixedHeight = Integer.parseInt(height.replaceAll("\\D", ""));
      } catch (NumberFormatException e) {
        LOG.warn("Can't parse " + height + " to int");
      }
    }

    if (fixedHeight == -1) {
      fixedHeight = super.getFixedHeight(facesContext, component);
    }
    return fixedHeight;
  }

  protected void renderMain(FacesContext facesContext, UIComponent input,
                            TobagoResponseWriter writer) throws IOException {

    UISelectOne component = (UISelectOne) input;
    List<SelectItem> items = ComponentUtil.getSelectItems(component);

    writer.startElement("select", component);
    String clientId = component.getClientId(facesContext);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
    writer.writeAttribute("style", null, "style");
    writer.writeComponentClass();
    writer.writeAttribute("title", null, ATTR_TIP);
    writer.writeAttribute("size", 2, null); // should be greater 1
    if (!ComponentUtil.getBooleanAttribute(component, ATTR_REQUIRED)) {
      writer.writeAttribute("onchange", "tobagoSelectOneListboxChange(this)", null);
      writer.writeAttribute("onclick", "tobagoSelectOneListboxClick(this)", null);
    }

    Object value = component.getValue();
    if (LOG.isDebugEnabled()) {
      LOG.debug("value = '" + value + "'");
    }
    for (SelectItem item : items) {

      writer.startElement("option", null);
      final Object itemValue = item.getValue();
      String formattedValue
          = getFormattedValue(facesContext, component, itemValue);
      writer.writeAttribute("value", formattedValue, null);
      if (itemValue.equals(value)) {
        writer.writeAttribute("selected", "selected", null);
      }
      writer.writeText(item.getLabel(), null);
      writer.endElement("option");
//    LOG.debug("item-value" + itemValue);
    }


    writer.endElement("select");
  }

// ///////////////////////////////////////////// bean getter + setter

}

