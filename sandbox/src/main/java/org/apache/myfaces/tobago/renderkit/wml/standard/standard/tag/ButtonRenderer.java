/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.renderkit.wml.standard.standard.tag;

/*
 * Created 07.02.2003 16:00:00.
 * : $
 */

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_LINK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ButtonRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(ButtonRenderer.class);

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {

    UICommand command = (UICommand) component;
    UIPage page = ComponentUtil.findPage(facesContext, command);

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    // TODO
    //String action = (String) command.getAttributes().get(ATTR_ACTION);

    if (command.getAttributes().get(ATTR_ACTION_LINK) == null
        && command.getAttributes().get(ATTR_ACTION_ONCLICK) == null) {
      ValueHolder labelComponent
          = (ValueHolder) command.getFacet(FACET_LABEL);
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
