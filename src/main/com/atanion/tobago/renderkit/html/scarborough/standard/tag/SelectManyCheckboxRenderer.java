/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.SelectManyRendererBase;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class SelectManyCheckboxRenderer extends SelectManyRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(SelectManyCheckboxRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UISelectMany component = (UISelectMany)uiComponent;

    List items = SelectReferenceRenderer.getItemsToRender(component);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    Object[] values = component.getSelectedValues();
    if (LOG.isDebugEnabled()) {
      LOG.debug("values = '" + values + "'");
    }
    String id = component.getClientId(facesContext);

    writer.startElement("table", null);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("summary", "", null);

    for (Iterator i = items.iterator(); i.hasNext(); ) {

      writer.startElement("tr", null);
      writer.startElement("td", null);


      SelectItem item = (SelectItem) i.next();
      String itemId = id
          + NamingContainer.SEPARATOR_CHAR + NamingContainer.SEPARATOR_CHAR
          + item.getValue().toString();

      writer.startElement("input", component);
      writer.writeAttribute("type", "checkbox", null);

      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
      writer.writeAttribute("checked",
          RenderUtil.contains(values, item.getValue()));
      writer.writeAttribute("name", id, null);
      writer.writeAttribute("id", itemId, null);
      writer.writeAttribute("value", item.getValue(), null);
      writer.writeAttribute("disabled",
          ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
      writer.endElement("input");

      if (LOG.isDebugEnabled()) {
        LOG.debug("item.getLabel() = " + item.getLabel());
      }
      if (item.getLabel() != null) {

        writer.endElement("td");
        writer.startElement("td", null);



        // fixme: use created UIOutput Label
        // fixme: see outcommented part
        writer.startElement("label", null);
        writer.writeAttribute("class", "tobago-label-default", null);
        writer.writeAttribute("for", itemId, null);
        writer.writeText(item.getLabel(), null);
        writer.endElement("label");
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

      writer.endElement("td");
      writer.endElement("tr");

    }
    writer.endElement("table");
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    List items = SelectReferenceRenderer.getItemsToRender((UISelectMany) component);
    return items.size() * super.getFixedHeight(facesContext, component);
  }

// ///////////////////////////////////////////// bean getter + setter

}

