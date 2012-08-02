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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
  * Created 28.04.2003 at 15:29:36.
  * $Id$
  */

import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ToolBarRenderer extends ToolBarRendererBase {

  public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
    UIPanel toolbar = (UIPanel) uiComponent;

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    setToolBarHeight(facesContext, uiComponent);

    writer.startElement(HtmlConstants.DIV, toolbar);
    writer.writeIdAttribute(toolbar.getClientId(facesContext));
    writer.writeClassAttribute();
    writer.writeStyleAttribute();
    writer.startElement(HtmlConstants.DIV, toolbar);
    boolean right = false;
    if (toolbar instanceof UIToolBar) {
      right = UIToolBar.ORIENTATION_RIGHT.equals(((UIToolBar) toolbar).getOrientation());
    }
    // TODO use StyleClasses
    writer.writeClassAttribute("tobago-toolbar-div-inner" + (right ? " tobago-toolbar-orientation-right" : ""));

    super.encodeEnd(facesContext, uiComponent);

    writer.endElement(HtmlConstants.DIV);
    writer.endElement(HtmlConstants.DIV);
  }

  private void setToolBarHeight(FacesContext facesContext, UIComponent component) {
    final int height = getFixedHeight(facesContext, component);
    HtmlRendererUtil.replaceStyleAttribute(component, "height", height);
  }

  protected String getHoverClasses(boolean first, boolean last) {
    return "tobago-toolBar-button-hover" + (first ? " tobago-toolBar-button-hover-first" : "");
  }

  protected String getTableClasses(boolean selected, boolean disabled) {
    return "tobago-toolbar-button-table tobago-toolbar-button-table-"
        + (selected ? "selected-" : "") + (disabled ? "disabled" : "enabled");
  }

  protected String getDivClasses(boolean selected, boolean disabled) {
    return "tobago-toolbar-button tobago-toolbar-button-"
        + (selected ? "selected-" : "") + (disabled ? "disabled" : "enabled");
  }

}
