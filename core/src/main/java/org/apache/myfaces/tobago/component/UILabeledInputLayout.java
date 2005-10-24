/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 06.12.2004 20:49:49.
 * $Id$
 */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.config.ThemeConfig;
import org.apache.myfaces.tobago.renderkit.LabeledLayoutRender;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.LayoutInfo;
import org.apache.myfaces.tobago.util.LayoutUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import static org.apache.myfaces.tobago.TobagoConstants.*;

public class UILabeledInputLayout extends UILayout {
  private static final Log LOG = LogFactory.getLog(UILabeledInputLayout.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.LabeledInputLayout";
  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.Layout";

  public void layoutBegin(FacesContext facesContext, UIComponent component) {

    if (! ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {

      // do layout calculation for label, component and picker
      UIComponent label = ComponentUtil.provideLabel(facesContext, component);
      UIComponent picker = component.getFacet(FACET_PICKER);

      RendererBase layoutRenderer = ComponentUtil.getRenderer(facesContext, this);

      String layoutOrder = ComponentUtil.getStringAttribute(this, ATTR_LAYOUT_ORDER);
      if (layoutOrder == null) {
        layoutOrder = ((LabeledLayoutRender)layoutRenderer).getDefaultLayoutOrder();
      }

      String layoutTokens = ComponentUtil.getStringAttribute(this, ATTR_COLUMNS);
      if (layoutTokens == null) {
        layoutTokens = createDefaultLayoutTokens(facesContext, component, layoutOrder);
      }

      Integer layoutWidth = LayoutUtil.getLayoutWidth(component);
      if (layoutWidth == null) {
        return;
      }
      int space = layoutWidth;
      if (label != null) {
        space -= ThemeConfig.getValue(facesContext, component, "labelSpace");
      }
      if (picker != null) {
        space -= ThemeConfig.getValue(facesContext, component, "pickerSpace");
      }
      LayoutInfo layoutInfo = new LayoutInfo(3, space, layoutTokens);
      layoutInfo.parseColumnLayout(space);


      for (int i = 0; i<layoutOrder.length(); i++) {
        switch (layoutOrder.toUpperCase().charAt(i)) {
          case 'L':
            if (label != null ) {
              int spaceForColumn = layoutInfo.getSpaceForColumn(i);
              label.getAttributes().put(ATTR_LAYOUT_WIDTH, spaceForColumn);
            }
            break;

          case 'C':
            {
              int spaceForColumn = layoutInfo.getSpaceForColumn(i);
              component.getAttributes().put(ATTR_LAYOUT_WIDTH, spaceForColumn);
              HtmlRendererUtil.layoutWidth(facesContext, component);
            }
            break;

          case 'P':
            if (picker != null) {
              int spaceForColumn = layoutInfo.getSpaceForColumn(i);
              picker.getAttributes().put(ATTR_LAYOUT_WIDTH, spaceForColumn);
              // prevent height layouting
              picker.getAttributes().put(ATTR_LAYOUT_HEIGHT, 0);
            }
            break;
          default:
            LOG.warn("Illegal layoutOrder token : " + layoutOrder.charAt(i));
        }
      }
    }

  }


  private String  createDefaultLayoutTokens(FacesContext facesContext, UIComponent component, String layoutOrder) {
    UIComponent label = component.getFacet(FACET_LABEL);
    int labelWidth = 0;
    if (label != null) {
      labelWidth = ThemeConfig.getValue(facesContext, label, "labelWidth");
    }

    UIComponent picker = component.getFacet(FACET_PICKER);
    int pickerWidth = 0;
    if (picker != null) {
      pickerWidth = ThemeConfig.getValue(facesContext, label, "pickerWidth");
    }

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i<layoutOrder.length(); i++) {
      switch (layoutOrder.toUpperCase().charAt(i)) {
        case 'L':
          if (label != null ) {
            sb.append(labelWidth + "px;");
          } else {
            sb.append("0px;");
          }
          break;

        case 'C':
          sb.append("1*;");
          break;

        case 'P':
          // set picker width
          if (picker != null) {
            sb.append(pickerWidth + "px;");
          } else {
            sb.append("0px;");
          }
          break;
        default:
          LOG.warn("Illegal layoutOrder token : " + layoutOrder.charAt(i));
      }
    }
    return sb.substring(0, sb.length() -1);
  }


  public String getFamily() {
    return COMPONENT_FAMILY;
  }

}
