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

import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ToolBarRenderer extends ToolBarRendererBase {

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

    UIToolBar toolBar = (UIToolBar) uiComponent;

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlConstants.DIV, toolBar);
    writer.writeIdAttribute(toolBar.getClientId(facesContext));
    HtmlRendererUtils.renderDojoDndItem(toolBar, writer, true);
    writer.writeClassAttribute();
    Style style = new Style(facesContext, toolBar);
    writer.writeStyleAttribute(style);
    writer.startElement(HtmlConstants.DIV, toolBar);
    boolean right = toolBar instanceof UIToolBar && UIToolBar.ORIENTATION_RIGHT.equals(toolBar.getOrientation());

    // TODO use StyleClasses
    writer.writeClassAttribute("tobago-toolBar-div-inner" + (right ? " tobago-toolBar-orientation-right" : ""));

    super.encodeEnd(facesContext, uiComponent);

    writer.endElement(HtmlConstants.DIV);
    writer.endElement(HtmlConstants.DIV);
  }

  protected String getHoverClasses(boolean first, boolean last) {
    return "tobago-toolBar-button-hover" + (first ? " tobago-toolBar-button-hover-first" : "");
  }

  protected String getTableClasses(boolean selected, boolean disabled) {
    return "tobago-toolBar-button-table tobago-toolBar-button-table-"
        + (selected ? "selected-" : "") + (disabled ? "disabled" : "enabled");
  }

  protected String getDivClasses(boolean selected, boolean disabled) {
    return "tobago-toolBar-button tobago-toolBar-button-"
        + (selected ? "selected-" : "") + (disabled ? "disabled" : "enabled");
  }

  @Override
  public Measure getHeight(FacesContext facesContext, Configurable component) {
    UIToolBar toolBar = (UIToolBar) component;
    String labelPosition = getLabelPosition(toolBar);
    String iconSize = getIconSize(toolBar);
    String key = iconSize + "_" + labelPosition + "_Height";
    return getResourceManager().getThemeMeasure(facesContext, component, key);
  }
}
