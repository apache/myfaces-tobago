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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class CommandRenderer extends CommandRendererBase {

  @Override
  protected void encodeBeginElement(final FacesContext facesContext, final AbstractUICommand command)
      throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlElements.A);
  }

  @Override
  protected void encodeEndElement(final FacesContext facesContext, final AbstractUICommand command)
      throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.A);
  }

  @Override
  protected void encodeBeginOuter(final FacesContext facesContext, final AbstractUICommand command)
      throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

//    writer.startElement(HtmlElements.SPAN);
    // XXX this class fixes the problem, that the submenu are not opened at correct position, but
    // the name doesn't suggest, that it is correct.
//    writer.writeClassAttribute(BootstrapClass.BTN_GROUP);
  }

  @Override
  protected void encodeEndOuter(final FacesContext facesContext, final AbstractUICommand command)
      throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

//    writer.endElement(HtmlElements.SPAN);
  }
}
