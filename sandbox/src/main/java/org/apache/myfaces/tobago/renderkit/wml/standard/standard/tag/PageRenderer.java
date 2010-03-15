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

import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;

public class PageRenderer extends PageRendererBase {

  private static final String DOCTYPE =
      "<?xml version='1.0'?>\n"
          + "<!DOCTYPE wml PUBLIC '-//WAPFORUM//DTD WML 1.1//EN'\n"
          + " 'http://www.wapforum.org/DTD/wml_1.1.xml'>";

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {

    AbstractUIPage page = (AbstractUIPage) component;

    ResponseWriter writer = facesContext.getResponseWriter();

    // replace responseWriter and render page content
    StringWriter content = new StringWriter();
    ResponseWriter contentWriter = writer.cloneWithWriter(writer);
    facesContext.setResponseWriter(contentWriter);

    RenderUtil.encodeChildren(facesContext, page);

    // reset responseWriter and render page
    facesContext.setResponseWriter(writer);

    writer.writeText(DOCTYPE, null);
    writer.writeText('\n', null);

    writer.startElement("wml", page);
    writer.startElement("card", page);

    writer.writeText(content, null);

    writer.endElement("card");
    writer.endElement("wml");
  }
}

