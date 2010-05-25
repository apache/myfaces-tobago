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
import org.apache.myfaces.tobago.context.ResourceManager;
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
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    UIToolBar toolBar = (UIToolBar) component;

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlConstants.DIV, toolBar);
    writer.writeIdAttribute(toolBar.getClientId(facesContext));
    HtmlRendererUtils.renderDojoDndItem(toolBar, writer, true);
    writer.writeClassAttribute();
    Style style = new Style(facesContext, toolBar);
    boolean right = toolBar instanceof UIToolBar && UIToolBar.ORIENTATION_RIGHT.equals(toolBar.getOrientation());
    if (right) {
      style.setTextAlign("right");
    }
    writer.writeStyleAttribute(style);
    super.encodeEnd(facesContext, toolBar);
    writer.endElement(HtmlConstants.DIV);
  }

  @Override
  public Measure getHeight(FacesContext facesContext, Configurable toolBar) {

    final ResourceManager rm = getResourceManager();

    final String iconSize = getIconSize((UIComponent) toolBar);
    final String labelPosition = getLabelPosition((UIComponent) toolBar);

    final boolean showIcon = !UIToolBar.ICON_OFF.equals(iconSize);
    final boolean iconBig = UIToolBar.ICON_BIG.equals(iconSize);
    final boolean iconSmall = UIToolBar.ICON_SMALL.equals(iconSize);
    final boolean showLabelBottom = UIToolBar.LABEL_BOTTOM.equals(labelPosition);
    final boolean showLabelRight = UIToolBar.LABEL_RIGHT.equals(labelPosition);
    final boolean showLabel = showLabelBottom || showLabelRight;

    final Measure paddingTop = rm.getThemeMeasure(facesContext, toolBar, "custom.padding-top");
    final Measure paddingMiddle = rm.getThemeMeasure(facesContext, toolBar, "custom.padding-middle");
    final Measure paddingBottom = rm.getThemeMeasure(facesContext, toolBar, "custom.padding-bottom");
    final Measure iconHeight = iconBig
        ? rm.getThemeMeasure(facesContext, toolBar, "custom.icon-big-height")
        : rm.getThemeMeasure(facesContext, toolBar, "custom.icon-small-height");
    final Measure labelHeight = rm.getThemeMeasure(facesContext, toolBar, "custom.label-height");

    Measure result = paddingTop;
    if (showIcon) {
      result = result.add(iconHeight);
      if (showLabel && showLabelBottom) {
        result = result.add(paddingMiddle);
        result = result.add(labelHeight);
      }
    } else {
      if (showLabel) {
        result = result.add(labelHeight);
      } else {
        // both off: use some reasonable defaults
        result = result.add(16);
      }
    }
    result = result.add(paddingBottom);

    result = result.add(rm.getThemeMeasure(facesContext, toolBar, "css.border-top-width"));
    result = result.add(rm.getThemeMeasure(facesContext, toolBar, "css.border-bottom-width"));

    return result;
  }
}
