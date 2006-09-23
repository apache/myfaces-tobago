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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.SelectOneRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectOneChoiceRenderer extends SelectOneRendererBase {

  private static final Log LOG = LogFactory.getLog(SelectOneChoiceRenderer.class);

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeEndTobago(FacesContext facesContext, UIComponent uiComponent) throws IOException {
    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();
    UISelectOne component = (UISelectOne) uiComponent;
    List<SelectItem> items = ComponentUtil.getSelectItems(component);

    if (LOG.isDebugEnabled()) {
      LOG.debug("items.size() = '" + items.size() + "'");
    }

    boolean disabled = items.size() == 0
        || ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)
        || ComponentUtil.getBooleanAttribute(component, ATTR_READONLY);

    writer.startElement(HtmlConstants.SELECT, component);
    writer.writeNameAttribute(component.getClientId(facesContext));
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);
    writer.writeComponentClass();
    writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);
    String onchange = HtmlUtils.generateOnchange(component, facesContext);
    if (onchange != null) {
      writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, null);
    }

    Object value = component.getValue();
    for (SelectItem item : items) {
      final Object itemValue = item.getValue();
      writer.startElement(HtmlConstants.OPTION, null);
      String formattedValue
          = getFormattedValue(facesContext, component, itemValue);
      writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, null);
      if (itemValue.equals(value)) {
        writer.writeAttribute(HtmlAttributes.SELECTED, "selected", null);
      }
      writer.writeText(item.getLabel(), null);
      writer.endElement(HtmlConstants.OPTION);
    }
    writer.endElement(HtmlConstants.SELECT);
    super.encodeEndTobago(facesContext, component);
    checkForCommandFacet(component, facesContext, writer);
  }

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    return 0;
  }
}

