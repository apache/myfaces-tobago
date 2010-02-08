package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UIMenuBar;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class MenuBarRenderer extends LayoutComponentRendererBase {

  private static final Log LOG = LogFactory.getLog(MenuBarRenderer.class);

  public static final String SEARCH_ID_POSTFIX = ComponentUtils.SUB_SEPARATOR + "popup";
  private static final String MENU_ACCELERATOR_KEYS = "menuAcceleratorKeys";

  // XXX check the ajax case
  @Override
  public void onComponentCreated(FacesContext facesContext, UIComponent component) {
    // XXX move to tobago.js or ...
    if (facesContext instanceof TobagoFacesContext) {
      TobagoFacesContext pageFacesContext = (TobagoFacesContext) facesContext;

      // todo: move to PageRenderer or ...
//      pageFacesContext.getScriptFiles().add("script/jquery-1.3.2.min.js");
      pageFacesContext.getScriptFiles().add("script/tobago-menu.js");
    }    
  }
  
  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    UIMenuBar menuBar = (UIMenuBar) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    
    writer.startElement(HtmlConstants.OL, menuBar);
    writer.writeClassAttribute();
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlConstants.OL);
  }
}
