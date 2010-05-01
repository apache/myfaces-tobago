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

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ButtonRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ButtonRenderer.class);

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {

    UICommand command = (UICommand) component;
    AbstractUIPage page = ComponentUtils.findPage(facesContext, command);

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    // TODO
    //String action = (String) command.getAttributes().get(ACTION);

    if (command.getAttributes().get(Attributes.LINK) == null
        && command.getAttributes().get(Attributes.ONCLICK) == null) {
      ValueHolder labelComponent
          = (ValueHolder) command.getFacet(Facets.LABEL);
      String label = (String) labelComponent.getValue();
      page.getPostfields().add(
          new DefaultKeyValue(command.getClientId(facesContext), label));


      writer.startElement("anchor", command);
      writer.writeText(label);

      writer.startElement("go", command);
      //writer.writeAttribute("href", action, null);

      for (KeyValue postField : page.getPostfields()) {
        writer.startElement("postfield", command);
        writer.writeAttribute("name", "" + postField.getKey(), false);
        writer.writeAttribute("value", "" + postField.getValue(), true);
        writer.endElement("postfield");
      }
      writer.endElement("go");
      writer.endElement("anchor");
    } else {
      LOG.error("button type navigate or script is not supported!");
    }
  }
}
