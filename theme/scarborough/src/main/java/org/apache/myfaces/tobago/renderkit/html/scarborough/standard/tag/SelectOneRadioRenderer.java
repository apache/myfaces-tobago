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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_REQUIRED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.SelectOneRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SelectOneRadioRenderer extends SelectOneRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectOneRadioRenderer.class);

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UISelectOne component = (UISelectOne) uiComponent;
    String clientId = component.getClientId(facesContext);

    ComponentUtil.findPage(component).getOnloadScripts().add("Tobago.selectOneRadioInit('" + clientId + "')");

    if (LOG.isDebugEnabled()) {
      for (Object o : component.getChildren()) {
        LOG.debug("ITEMS " + o);
        if (o instanceof UISelectItems) {
          UISelectItems uiitems = (UISelectItems) o;
          Object v = uiitems.getValue();
          LOG.debug("VALUE " + v);
          if (v != null) {
            LOG.debug("VALUE " + v.getClass().getName());
          }
        }
      }
    }

    List<SelectItem> items = ComponentUtil.getItemsToRender(component);

    boolean inline = ComponentUtil.getBooleanAttribute(component, ATTR_INLINE);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    if (!inline) {
      writer.startElement(HtmlConstants.TABLE, component);
      writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
      writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
      writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", null);
      writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);
      writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);
    }

    Object value = component.getValue();
    List clientIds = new ArrayList();
    for (SelectItem item : items) {

      if (!inline) {
        writer.startElement(HtmlConstants.TR, null);
        writer.startElement(HtmlConstants.TD, null);
      }

      String id = clientId + NamingContainer.SEPARATOR_CHAR
          + NamingContainer.SEPARATOR_CHAR + item.getValue().toString();
      clientIds.add(id);
      writer.startElement(HtmlConstants.INPUT, component);
      writer.writeAttribute(HtmlAttributes.TYPE, "radio", null);
      writer.writeComponentClass();
      if (item.getValue().equals(value)) {
        writer.writeAttribute(HtmlAttributes.CHECKED, "checked", null);
      }
      writer.writeNameAttribute(clientId);

      writer.writeIdAttribute(id);
      String formattedValue
          = RenderUtil.getFormattedValue(facesContext, component, item.getValue());
      writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, null);
      writer.writeAttribute(HtmlAttributes.DISABLED,
          ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
      writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);
      if (!ComponentUtil.getBooleanAttribute(component, ATTR_REQUIRED)) {
        writer.writeAttribute(HtmlAttributes.ONCLICK, "Tobago.selectOneRadioClick(this, '" + clientId + "')", null);
      }
      writer.endElement(HtmlConstants.INPUT);

      if (item.getLabel() != null) {


        if (!inline) {
          writer.endElement(HtmlConstants.TD);
          writer.startElement(HtmlConstants.TD, null);
        }

        // FIXME: use created UIOutput Label
        // FIXME: see outcommented part
        writer.startElement(HtmlConstants.LABEL, null);
        writer.writeClassAttribute("tobago-label-default");
        writer.writeAttribute(HtmlAttributes.FOR, id, null);
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
      if (!inline) {
        writer.endElement(HtmlConstants.TD);
        writer.endElement(HtmlConstants.TR);
      }
    }
    if (!inline) {
      writer.endElement(HtmlConstants.TABLE);
    }

    checkForCommandFacet(component, clientIds, facesContext, writer);

  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    List<SelectItem> items
        = ComponentUtil.getItemsToRender((UISelectOne) component);
    return items.size() * super.getFixedHeight(facesContext, component);
  }

}

