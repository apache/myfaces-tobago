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
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package org.apache.myfaces.tobago.renderkit.wml.standard.standard.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.collections.KeyValue;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ButtonRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(ButtonRenderer.class);

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {

    UICommand command = (UICommand) component;
    UIPage page = ComponentUtil.findPage(command);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    String type = (String) command.getAttributes().get(
        TobagoConstants.ATTR_TYPE);
    String action = (String) command.getAttributes().get(
        TobagoConstants.ATTR_ACTION_STRING);

    if ("submit".equals(type) && page != null) {
      ValueHolder labelComponent
          = (ValueHolder) command.getFacet(TobagoConstants.FACET_LABEL);
      String label = (String) labelComponent.getValue();
      page.getPostfields().add(
          new DefaultKeyValue(command.getClientId(facesContext), label));


      writer.startElement("anchor", command);
      writer.writeText(label, null);

      writer.startElement("go", command);
      writer.writeAttribute("href", action, null);

      for (KeyValue postField : page.getPostfields()) {
        writer.startElement("postfield", command);
        writer.writeAttribute("name", postField.getKey(), null);
        writer.writeAttribute("value", postField.getValue(), null);
        writer.endElement("postfield");
      }
      writer.endElement("go");
      writer.endElement("anchor");
    } else {
      LOG.error("button type \"" +
          type + "\" is not supported!");
    }
  }
}

