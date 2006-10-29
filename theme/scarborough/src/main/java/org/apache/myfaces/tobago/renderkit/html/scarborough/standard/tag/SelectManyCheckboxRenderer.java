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
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.SelectManyRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SelectManyCheckboxRenderer extends SelectManyRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectManyCheckboxRenderer.class);

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UISelectMany component = (UISelectMany) uiComponent;

    List<SelectItem> items = ComponentUtil.getItemsToRender(component);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    Object[] values = component.getSelectedValues();
    if (LOG.isDebugEnabled()) {
      LOG.debug("values = '" + values + "'");
    }
    String id = component.getClientId(facesContext);

    writer.startElement(HtmlConstants.TABLE, null);
    writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", null);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);
    List clientIds = new ArrayList();
    for (SelectItem item : items) {

      writer.startElement(HtmlConstants.TR, null);
      writer.startElement(HtmlConstants.TD, null);

      String itemId = id
          + NamingContainer.SEPARATOR_CHAR + NamingContainer.SEPARATOR_CHAR
          + item.getValue().toString();
      clientIds.add(itemId);
      writer.startElement(HtmlConstants.INPUT, component);
      writer.writeAttribute(HtmlAttributes.TYPE, "checkbox", null);

      writer.writeComponentClass();
      writer.writeAttribute(HtmlAttributes.CHECKED,
          RenderUtil.contains(values, item.getValue()));
      writer.writeNameAttribute(id);
      writer.writeIdAttribute(itemId);
      String formattedValue
          = RenderUtil.getFormattedValue(facesContext, component, item.getValue());
      writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, null);
      writer.writeAttribute(HtmlAttributes.DISABLED,
          ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
      writer.endElement(HtmlConstants.INPUT);

      if (LOG.isDebugEnabled()) {
        LOG.debug("item.getLabel() = " + item.getLabel());
      }
      if (item.getLabel() != null) {

        writer.endElement(HtmlConstants.TD);
        writer.startElement(HtmlConstants.TD, null);

        // FIXME: use created UIOutput Label
        // FIXME: see outcommented part
        writer.startElement(HtmlConstants.LABEL, null);
        writer.writeClassAttribute("tobago-label-default");
        writer.writeAttribute(HtmlAttributes.FOR, itemId, null);
        writer.writeText(item.getLabel(), null);
        writer.endElement(HtmlConstants.LABEL);
//        Application application = tobagoContext.getApplication();
//        UIOutput label = (UIOutput)
//            application.createComponent(TobagoConstants.COMPONENT_TYPE_OUTPUT);
//        label.getAttributes().put(TobagoConstants.ATTR_FOR, itemId);
//        label.setValue( item.getLabel() );
//        label.setRendererType("Label");
//        label.setRendered(true);
//
//        RenderUtil.encode(label);
      }

      writer.endElement(HtmlConstants.TD);
      writer.endElement(HtmlConstants.TR);

    }
    writer.endElement(HtmlConstants.TABLE);
    checkForCommandFacet(component, clientIds, facesContext, writer);
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    List<SelectItem> items
        = ComponentUtil.getItemsToRender((UISelectMany) component);
    return items.size() * super.getFixedHeight(facesContext, component);
  }

}

