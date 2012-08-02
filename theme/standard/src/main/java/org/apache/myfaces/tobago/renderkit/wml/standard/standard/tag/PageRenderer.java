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

import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class PageRenderer extends PageRendererBase {

  private static final String DOCTYPE =
      "<?xml version='1.0'?>\n"
          + "<!DOCTYPE wml PUBLIC '-//WAPFORUM//DTD WML 1.1//EN'\n"
          + " 'http://www.wapforum.org/DTD/wml_1.1.xml'>";

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
  }

  @Override
  public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {

    UIPage page = (UIPage) component;

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.write(DOCTYPE);
    writer.write('\n');

    writer.startElement("wml", page);
    writer.startElement("card", page);

    writer.writeText("WML currently not supported!", null);
//    writer.write(content.toString());

    writer.endElement("card");
    writer.endElement("wml");

    // fixme, this is only a workaround for bug TOBAGO-296
    facesContext.responseComplete();
  }

  @Override
  public boolean getRendersChildren() {
    // fixme, this is only a workaround for bug TOBAGO-296
    return true;
  }
}
