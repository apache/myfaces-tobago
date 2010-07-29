package org.apache.myfaces.tobago.renderkit.wml.standard.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
 * : $
 */

import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectOneChoiceRenderer extends LayoutComponentRendererBase {

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {

    UISelectOne selectOne = (UISelectOne) component;
    AbstractUIPage page = ComponentUtils.findPage(facesContext, selectOne);

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    String clientId = selectOne.getClientId(facesContext);

    if (page != null) {
      page.getPostfields().add(new DefaultKeyValue(clientId, clientId));
    }

    ValueHolder label
        = (ValueHolder) selectOne.getFacet(Facets.LABEL);
    if (label != null) {
      writer.writeText(label.toString());
    }
    List<SelectItem> items = RenderUtils.getSelectItems(selectOne);
    String value = RenderUtils.currentValue(selectOne);

    writer.startElement("select", selectOne);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.VALUE, value, true);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, Boolean.FALSE.toString(), false);

    for (SelectItem item : items) {
      writer.startElement("option", selectOne);
      String formattedValue
          = RenderUtils.getFormattedValue(facesContext, component, item.getValue());
      writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);
      writer.writeText(item.getLabel());
      writer.endElement("option");
    }
    writer.endElement("select");
  }

}
