/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 06.12.2004 20:49:49.
 * $Id$
 */
package com.atanion.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.util.LayoutInfo;
import com.atanion.tobago.config.ThemeConfig;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.LabeledLayoutRender;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.faces.el.ValueBinding;
import java.util.Map;

public class UILabeledInputLayout extends UILayout
    implements TobagoConstants {
  private static final Log LOG = LogFactory.getLog(UILabeledInputLayout.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.LabeledInputLayout";
  public static final String COMPONENT_FAMILY = "com.atanion.tobago.Layout";

  public void layoutBegin(FacesContext facesContext, UIComponent component) {

    if (! ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {

      // do layout calculation for label, component and picker
      UIComponent label = provideLabel(component);
      UIComponent picker = component.getFacet(FACET_PICKER);

      RendererBase layoutRenderer = ComponentUtil.getRenderer(facesContext, this);

      String layoutOrder = ComponentUtil.getStringAttribute(this, ATTR_LAYOUT_ORDER);
      if (layoutOrder == null) {
        layoutOrder = ((LabeledLayoutRender)layoutRenderer).getDefaultLayoutOrder();
      }

      String layoutTokens = ComponentUtil.getStringAttribute(this, ATTR_COLUMNS);
      LOG.info("tokens = " + layoutTokens);
      if (layoutTokens == null) {
        layoutTokens = createDefaultLayoutTokens(facesContext, component, layoutOrder);
      }
      LOG.info("tokens = " + layoutTokens);

      int space = LayoutUtil.getLayoutWidth(component);
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
              LOG.info("spaceForColumn L = " + spaceForColumn);
              label.getAttributes().put(ATTR_LAYOUT_WIDTH, spaceForColumn);
            }
            break;

          case 'C':
            {
              int spaceForColumn = layoutInfo.getSpaceForColumn(i);
              LOG.info("spaceForColumn = C" + spaceForColumn);
              component.getAttributes().put(ATTR_LAYOUT_WIDTH, spaceForColumn);
              HtmlRendererUtil.layoutWidth(facesContext, component);
            }
            break;

          case 'P':
            if (picker != null) {
              int spaceForColumn = layoutInfo.getSpaceForColumn(i);
              LOG.info("spaceForColumn = P" + spaceForColumn);
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

  private UIComponent provideLabel(UIComponent component) {
    UIComponent label = component.getFacet(FACET_LABEL);


    if (label == null) {
      final Map attributes = component.getAttributes();
      Object labelText = component.getValueBinding(ATTR_LABEL);
      if (labelText ==null) {
        labelText = attributes.get(ATTR_LABEL);
      }

      Object labelWithAccessKey = component.getValueBinding(ATTR_LABEL_WITH_ACCESS_KEY);
      if (labelWithAccessKey == null) {
        labelWithAccessKey = attributes.get(ATTR_LABEL_WITH_ACCESS_KEY);
      }

      Object accessKey = component.getValueBinding(ATTR_ACCESS_KEY);
      if (accessKey == null) {
        accessKey = attributes.get(ATTR_ACCESS_KEY);
      }

      if (labelText != null || labelWithAccessKey != null || accessKey != null) {
        Application application = getFacesContext().getApplication();
        label = (UIOutput) application.createComponent(UIOutput.COMPONENT_TYPE);
        label.setRendererType("Label");
        label.setRendered(true);

        if (labelText instanceof ValueBinding) {
          label.setValueBinding(ATTR_VALUE, (ValueBinding) labelText);
        } else if (labelText != null) {
          label.getAttributes().put(ATTR_VALUE, labelText);
        }
        if (labelWithAccessKey instanceof ValueBinding) {
          label.setValueBinding(ATTR_LABEL_WITH_ACCESS_KEY, (ValueBinding) labelWithAccessKey);
        } else if (labelWithAccessKey != null) {
          label.getAttributes().put(ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey);
        }
        if (accessKey instanceof ValueBinding) {
          label.setValueBinding(ATTR_ACCESS_KEY, (ValueBinding) accessKey);
        } else if (accessKey != null) {
          label.getAttributes().put(ATTR_ACCESS_KEY, accessKey);
        }

        component.getFacets().put(FACET_LABEL, label);
      }
    }
    return label;
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
