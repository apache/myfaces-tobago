/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.wml.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.util.KeyValuePair;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectOneChoiceRenderer extends RendererBase {

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {

    UISelectOne selectOne = (UISelectOne) component;
    UIPage page = ComponentUtil.findPage(selectOne);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    String clientId = selectOne.getClientId(facesContext);

    if (page != null) {
      page.getPostfields().add(new KeyValuePair(clientId, clientId));
    }

    ValueHolder label
        = (ValueHolder) selectOne.getFacet(TobagoConstants.FACET_LABEL);
    if (label != null) {
      writer.writeText(label, null);
    }
    List<SelectItem> items = ComponentUtil.getSelectItems(selectOne);
    String value = ComponentUtil.currentValue(selectOne);

    writer.startElement("select", selectOne);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("value", value, null);
    writer.writeAttribute("multiple", false, null);

    for (SelectItem item : items) {
      writer.startElement("option", selectOne);
      writer.writeAttribute("value", item.getValue(), null);
      writer.writeText(item.getLabel(), null);
      writer.endElement("option");
    }
    writer.endElement("select");
  }

}

