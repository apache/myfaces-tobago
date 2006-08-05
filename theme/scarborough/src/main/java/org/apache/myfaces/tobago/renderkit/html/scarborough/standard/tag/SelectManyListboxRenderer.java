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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.SelectManyRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectManyListboxRenderer extends SelectManyRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectManyListboxRenderer.class);

  public boolean getRendersChildren() {
    return true;
  }

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    return 0;
  }

  public int getLabelWidth(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "labelWidth");
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

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UISelectMany component = (UISelectMany) uiComponent;

    List<SelectItem> items = ComponentUtil.getSelectItems(component);

    if (LOG.isDebugEnabled()) {
      LOG.debug("items.size() = '" + items.size() + "'");
    }

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("select", component);
    String clientId = component.getClientId(facesContext);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
    writer.writeAttribute("style", null, "style");
    writer.writeComponentClass();
    writer.writeAttribute("multiple", "multiple", null);
    writer.writeAttribute("title", null, ATTR_TIP);

    Object[] values = component.getSelectedValues();
    if (LOG.isDebugEnabled()) {
      LOG.debug("values = '" + values + "'");
    }
    for (SelectItem item : items) {

      writer.startElement("option", null);
      String formattedValue
          = getFormattedValue(facesContext, component, item.getValue());
      writer.writeAttribute("value", formattedValue, null);
      if (RenderUtil.contains(values, item.getValue())) {
        writer.writeAttribute("selected", "selected", null);
      }
      writer.writeText(item.getLabel(), null);
      writer.endElement("option");
//    LOG.debug("item-value" + item.getValue());
    }


    writer.endElement("select");
    checkForCommandFacet(component, facesContext, writer);
  }

// ///////////////////////////////////////////// bean getter + setter

}

