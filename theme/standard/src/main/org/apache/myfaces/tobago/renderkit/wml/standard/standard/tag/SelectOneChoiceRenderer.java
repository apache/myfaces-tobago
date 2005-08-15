/*
 * Copyright 2002-2005 atanion GmbH.
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
 * : $
 */
package org.apache.myfaces.tobago.renderkit.wml.standard.standard.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;

public class SelectOneChoiceRenderer extends RendererBase {

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {

    UISelectOne selectOne = (UISelectOne) component;
    UIPage page = ComponentUtil.findPage(selectOne);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    String clientId = selectOne.getClientId(facesContext);

    if (page != null) {
      page.getPostfields().add(new DefaultKeyValue(clientId, clientId));
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
      String formattedValue
          = getFormattedValue(facesContext, component, item.getValue());
      writer.writeAttribute("value", formattedValue, null);
      writer.writeText(item.getLabel(), null);
      writer.endElement("option");
    }
    writer.endElement("select");
  }

}

