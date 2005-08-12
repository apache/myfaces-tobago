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
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import java.io.IOException;

public class SelectBooleanCheckboxRenderer extends RendererBase {

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UIInput uiInput = (UIInput) component;

    String newValue = ((ServletRequest)facesContext.getExternalContext().getRequest())
        .getParameter(uiInput.getClientId(facesContext));
//    LogUtils.logParameter(((ServletRequest)facesContext.getExternalContext().getRequest()));
//    LOG.debug("decode: key='" + uiInput.getClientId(facesContext)
//        + "' value='" + newValue + "'  valid = " + uiInput.isValid());
    if (newValue != null) {
      uiInput.setValue(new Boolean(newValue));
    } else {
      uiInput.setValue(Boolean.FALSE);
    }
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UISelectBoolean component = (UISelectBoolean) uiComponent;

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    UIComponent label = ComponentUtil.provideLabel(facesContext, component);

    boolean inline = ComponentUtil.getBooleanAttribute(component, ATTR_INLINE);

    if (label != null && ! inline) {

      writer.startElement("table", component);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("cellspacing", "0", null);
      writer.writeAttribute("cellpadding", "0", null);
      writer.writeAttribute("summary", "", null);
      writer.writeAttribute("title", null, ATTR_TIP);

      writer.startElement("tr", null);
      writer.startElement("td", null);
    }

    Boolean currentValue = (Boolean)component.getValue();
    boolean checked = currentValue != null ? currentValue.booleanValue() : false;

    writer.startElement("input", component);
    writer.writeAttribute("type", "checkbox", null);
    writer.writeAttribute("value", "true", null);
    writer.writeAttribute("checked", checked);
    writer.writeNameAttribute(component.getClientId(facesContext));
    writer.writeComponentClass();
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
    writer.writeAttribute("title", null, ATTR_TIP);
    writer.endElement("input");

    if (label != null && ! inline) {
      writer.endElement("td");
      writer.startElement("td", null);
      writer.writeText("", null);
    }

    if (label != null) {
      RenderUtil.encode(facesContext, label);
    }

    if (label != null && ! inline) {
      writer.endElement("td");
      writer.endElement("tr");
      writer.endElement("table");
    }


  }

// ///////////////////////////////////////////// bean getter + setter

}

