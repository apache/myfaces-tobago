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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class LinkAlternativeLinksRenderer extends LinkRenderer {

  @Override
  protected void encodeBeginOuter(final FacesContext facesContext, final AbstractUICommand command) throws IOException {
    final String clientId = command.getClientId(facesContext);
    final boolean parentOfCommands = command.isParentOfCommands();

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.LI);
    if (parentOfCommands) {
      writer.writeIdAttribute(clientId);
    }

    writer.writeClassAttribute(
        BootstrapClass.NAV_ITEM,
        parentOfCommands ? BootstrapClass.DROPDOWN : null);
  }

  @Override
  protected void encodeEndOuter(final FacesContext facesContext, final AbstractUICommand command) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.LI);
  }

  @Override
  protected void addCssItems(
      final FacesContext facesContext, final AbstractUICommand command, final List<CssItem> collected) {
    collected.add(BootstrapClass.NAV_LINK);
  }
}
