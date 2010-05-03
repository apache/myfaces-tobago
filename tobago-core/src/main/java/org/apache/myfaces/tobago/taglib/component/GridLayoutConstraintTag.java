package org.apache.myfaces.tobago.taglib.component;

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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.Measure;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Add GridLayoutConstraints to the parent UIComponent.
 */
@Tag(name = "gridLayoutConstraint", bodyContent = BodyContent.EMPTY)
@TagGeneration(className = "org.apache.myfaces.tobago.internal.taglib.GridLayoutConstraintTag")
public abstract class GridLayoutConstraintTag extends TagSupport {

  private static final long serialVersionUID = 1L;

  @Override
  public int doStartTag() throws JspException {

    // Locate our parent UIComponentTag
    UIComponentTag tag =
        UIComponentTag.getParentUIComponentTag(pageContext);
    if (tag == null) {
      // TODO Message resource i18n
      throw new JspException("Not nested in faces tag");
    }

    if (!tag.getCreated()) {
      return (SKIP_BODY);
    }

    UIComponent component = tag.getComponentInstance();
    if (component == null) {
      // TODO Message resource i18n
      throw new JspException("Component Instance is null");
    }

    if (!(component instanceof LayoutBase)) {
      // TODO Message resource i18n
      throw new JspException("Component Instance is not a LayoutBase");
    }

    if (component instanceof LayoutComponent && isColumnSpanSet()) {
      if (isColumnSpanLiteral()) {
        ((LayoutComponent) component).setColumnSpan(Integer.valueOf(getColumnSpanValue()));
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.COLUMN_SPAN, getColumnSpanAsBindingOrExpression());
      }
    }

    if (component instanceof LayoutComponent && isRowSpanSet()) {
      if (isRowSpanLiteral()) {
        ((LayoutComponent) component).setRowSpan(Integer.valueOf(getRowSpanValue()));
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.ROW_SPAN, getRowSpanAsBindingOrExpression());
      }
    }

    if (isWidthSet()) {
      if (isWidthLiteral()) {
        ((LayoutBase) component).setWidth(Measure.parse(getWidthValue()));
//        ((LayoutBase) component).setWidth(Measure.parse(getWidthExpression()));
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.WIDTH, getWidthAsBindingOrExpression());
      }
    }

    if (isHeightSet()) {
      if (isHeightLiteral()) {
        ((LayoutBase) component).setHeight(Measure.parse(getHeightValue()));
//        ((LayoutBase) component).setHeight(Measure.parse(getHeightExpression()));
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.HEIGHT, getHeightAsBindingOrExpression());
      }
    }

    if (isMinimumWidthSet()) {
      if (isMinimumWidthLiteral()) {
        ((LayoutBase) component).setMinimumWidth(Measure.parse(getMinimumWidthValue()));
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MINIMUM_WIDTH, getMinimumWidthAsBindingOrExpression());
      }
    }

    if (isMinimumHeightSet()) {
      if (isMinimumHeightLiteral()) {
        ((LayoutBase) component).setMinimumHeight(Measure.parse(getMinimumHeightValue()));
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MINIMUM_WIDTH, getMinimumHeightAsBindingOrExpression());
      }
    }

    if (isPreferredWidthSet()) {
      if (isPreferredWidthLiteral()) {
        ((LayoutBase) component).setPreferredWidth(Measure.parse(getPreferredWidthValue()));
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.PREFERRED_WIDTH,
            getPreferredWidthAsBindingOrExpression());
      }
    }

    if (isPreferredHeightSet()) {
      if (isPreferredHeightLiteral()) {
        ((LayoutBase) component).setPreferredHeight(Measure.parse(getPreferredHeightValue()));
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.PREFERRED_WIDTH,
            getPreferredHeightAsBindingOrExpression());
      }
    }

    if (isMaximumWidthSet()) {
      if (isMaximumWidthLiteral()) {
        ((LayoutBase) component).setMaximumWidth(Measure.parse(getMaximumWidthValue()));
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MAXIMUM_WIDTH, getMaximumWidthAsBindingOrExpression());
      }
    }

    if (isMaximumHeightSet()) {
      if (isMaximumHeightLiteral()) {
        ((LayoutBase) component).setMaximumHeight(Measure.parse(getMaximumHeightValue()));
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MAXIMUM_WIDTH, getMaximumHeightAsBindingOrExpression());
      }
    }

    return (SKIP_BODY);
  }

  // column span

  public abstract boolean isColumnSpanSet();

  public abstract boolean isColumnSpanLiteral();

  public abstract Object getColumnSpanAsBindingOrExpression();

  /**
   * The number of horizontal cells this component should use.
   */
  @TagAttribute(name = "columnSpan")
  public abstract String getColumnSpanValue();

  // row span

  public abstract boolean isRowSpanSet();

  public abstract boolean isRowSpanLiteral();

  public abstract Object getRowSpanAsBindingOrExpression();

  /**
   * The number of vertical cells this component should use.
   */
  @TagAttribute(name = "rowSpan")
  public abstract String getRowSpanValue();

  // width

  public abstract boolean isWidthSet();

  public abstract boolean isWidthLiteral();

  public abstract Object getWidthAsBindingOrExpression();

  /**
   * The width for this component.
   */
  @TagAttribute(name = "width")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract String getWidthValue();

//  public abstract String getWidthExpression();
  
  // height

  public abstract boolean isHeightSet();

  public abstract boolean isHeightLiteral();

  public abstract Object getHeightAsBindingOrExpression();
  /**
   * The height for this component.
   */
  @TagAttribute(name = "height")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract String getHeightValue();

//  public abstract String getHeightExpression();
  
  // minimum width

  public abstract boolean isMinimumWidthSet();

  public abstract boolean isMinimumWidthLiteral();

  public abstract Object getMinimumWidthAsBindingOrExpression();

  /**
   * The minimum width for this component.
   */
  @TagAttribute(name = "minimumWidth")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract String getMinimumWidthValue();

  // minimum height

  public abstract boolean isMinimumHeightSet();

  public abstract boolean isMinimumHeightLiteral();

  public abstract Object getMinimumHeightAsBindingOrExpression();

  /**
   * The minimum height for this component.
   */
  @TagAttribute(name = "minimumHeight")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract String getMinimumHeightValue();

  // preferred width

  public abstract boolean isPreferredWidthSet();

  public abstract boolean isPreferredWidthLiteral();

  public abstract Object getPreferredWidthAsBindingOrExpression();

  /**
   * The preferred width for this component.
   */
  @TagAttribute(name = "preferredWidth")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract String getPreferredWidthValue();

  // preferred height

  public abstract boolean isPreferredHeightSet();

  public abstract boolean isPreferredHeightLiteral();

  public abstract Object getPreferredHeightAsBindingOrExpression();

  /**
   * The preferred height for this component.
   */
  @TagAttribute(name = "preferredHeight")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract String getPreferredHeightValue();

  // maximum width

  public abstract boolean isMaximumWidthSet();

  public abstract boolean isMaximumWidthLiteral();

  public abstract Object getMaximumWidthAsBindingOrExpression();

  /**
   * The maximum width for this component.
   */
  @TagAttribute(name = "maximumWidth")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract String getMaximumWidthValue();

  // maximum height

  public abstract boolean isMaximumHeightSet();

  public abstract boolean isMaximumHeightLiteral();

  public abstract Object getMaximumHeightAsBindingOrExpression();

  /**
   * The maximum height for this component.
   */
  @TagAttribute(name = "maximumHeight")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract String getMaximumHeightValue();

}
