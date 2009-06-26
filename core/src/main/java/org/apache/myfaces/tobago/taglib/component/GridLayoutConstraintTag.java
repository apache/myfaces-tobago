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
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.layout.Component;
import org.apache.myfaces.tobago.layout.Measure;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Register an PopupActionListener instance on the UIComponent
 * associated with the closest parent UIComponent.
 */
@Tag(name = "gridLayoutConstraint", bodyContent = BodyContent.EMPTY)
@TagGeneration(className = "org.apache.myfaces.tobago.internal.taglib.GridLayoutConstraintTag")
public abstract class GridLayoutConstraintTag extends TagSupport {

  private static final long serialVersionUID = 1L;

  /**
   * @param columnSpan The number of horizontal cells this component should use.
   */
  @TagAttribute
  public abstract void setColumnSpan(String columnSpan);

  /**
   * @param rowSpan The number of vertical cells this component should use.
   */
  @TagAttribute
  public abstract void setRowSpan(String rowSpan);

  /**
   * @param width The width for this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setWidth(String width);

  /**
   * @param height The height for this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setHeight(String height);

  /**
   * @param minimumWidth The minimum width for this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMinimumWidth(String minimumWidth);

  /**
   * @param minimumHeight The minimum height for this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMinimumHeight(String minimumHeight);

  /**
   * @param preferredWidth The preferred width for this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setPreferredWidth(String preferredWidth);

  /**
   * @param preferredHeight The preferred height for this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setPreferredHeight(String preferredHeight);

  /**
   * @param maximumWidth The maximum width for this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMaximumWidth(String maximumWidth);

  /**
   * @param maximumHeight The maximum height for this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMaximumHeight(String maximumHeight);

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

    if (!(component instanceof Component)) {
      // TODO Message resource i18n
      throw new JspException("Component Instance is not a LayoutComponent");
    }

    if (isColumnSpanSet()) {
      if (isColumnSpanLiteral()) {
        ((Component) component).setColumnSpan(Integer.valueOf(getColumnSpanExpression()));
      } else {
        component.setValueBinding(Attributes.COLUMN_SPAN, (ValueBinding) getColumnSpanAsBindingOrExpression());
      }
    }

    if (isRowSpanSet()) {
      if (isRowSpanLiteral()) {
        ((Component) component).setRowSpan(Integer.valueOf(getRowSpanExpression()));
      } else {
        component.setValueBinding(Attributes.ROW_SPAN, (ValueBinding) getRowSpanAsBindingOrExpression());
      }
    }

    if (isWidthSet()) {
      if (isWidthLiteral()) {
        ((Component) component).setWidth(Measure.parse(getWidthExpression()));
      } else {
        component.setValueBinding(Attributes.WIDTH, (ValueBinding) getWidthAsBindingOrExpression());
      }
    }

    if (isHeightSet()) {
      if (isHeightLiteral()) {
        ((Component) component).setHeight(Measure.parse(getHeightExpression()));
      } else {
        component.setValueBinding(Attributes.HEIGHT, (ValueBinding) getHeightAsBindingOrExpression());
      }
    }

    if (isMinimumWidthSet()) {
      if (isMinimumWidthLiteral()) {
        ((Component) component).setMinimumWidth(Measure.parse(getMinimumWidthExpression()));
      } else {
        component.setValueBinding(Attributes.MINIMUM_WIDTH, (ValueBinding) getMinimumWidthAsBindingOrExpression());
      }
    }

    if (isMinimumHeightSet()) {
      if (isMinimumHeightLiteral()) {
        ((Component) component).setMinimumHeight(Measure.parse(getMinimumHeightExpression()));
      } else {
        component.setValueBinding(Attributes.MINIMUM_WIDTH, (ValueBinding) getMinimumHeightAsBindingOrExpression());
      }
    }

    if (isPreferredWidthSet()) {
      if (isPreferredWidthLiteral()) {
        ((Component) component).setPreferredWidth(Measure.parse(getPreferredWidthExpression()));
      } else {
        component.setValueBinding(Attributes.PREFERRED_WIDTH, (ValueBinding) getPreferredWidthAsBindingOrExpression());
      }
    }

    if (isPreferredHeightSet()) {
      if (isPreferredHeightLiteral()) {
        ((Component) component).setPreferredHeight(Measure.parse(getPreferredHeightExpression()));
      } else {
        component.setValueBinding(Attributes.PREFERRED_WIDTH, (ValueBinding) getPreferredHeightAsBindingOrExpression());
      }
    }

    if (isMaximumWidthSet()) {
      if (isMaximumWidthLiteral()) {
        ((Component) component).setMaximumWidth(Measure.parse(getMaximumWidthExpression()));
      } else {
        component.setValueBinding(Attributes.MAXIMUM_WIDTH, (ValueBinding) getMaximumWidthAsBindingOrExpression());
      }
    }

    if (isMaximumHeightSet()) {
      if (isMaximumHeightLiteral()) {
        ((Component) component).setMaximumHeight(Measure.parse(getMaximumHeightExpression()));
      } else {
        component.setValueBinding(Attributes.MAXIMUM_WIDTH, (ValueBinding) getMaximumHeightAsBindingOrExpression());
      }
    }

    return (SKIP_BODY);
  }

  // column span

  public abstract boolean isColumnSpanSet();

  public abstract boolean isColumnSpanLiteral();

  public abstract Object getColumnSpanAsBindingOrExpression();

  public abstract String getColumnSpanExpression();

  // row span

  public abstract boolean isRowSpanSet();

  public abstract boolean isRowSpanLiteral();

  public abstract Object getRowSpanAsBindingOrExpression();

  public abstract String getRowSpanExpression();

  // width

  public abstract boolean isWidthSet();

  public abstract boolean isWidthLiteral();

  public abstract Object getWidthAsBindingOrExpression();

  public abstract String getWidthExpression();

  // height

  public abstract boolean isHeightSet();

  public abstract boolean isHeightLiteral();

  public abstract Object getHeightAsBindingOrExpression();

  public abstract String getHeightExpression();

  // minimum width

  public abstract boolean isMinimumWidthSet();

  public abstract boolean isMinimumWidthLiteral();

  public abstract Object getMinimumWidthAsBindingOrExpression();

  public abstract String getMinimumWidthExpression();

  // minimum height

  public abstract boolean isMinimumHeightSet();

  public abstract boolean isMinimumHeightLiteral();

  public abstract Object getMinimumHeightAsBindingOrExpression();

  public abstract String getMinimumHeightExpression();

  // preferred width

  public abstract boolean isPreferredWidthSet();

  public abstract boolean isPreferredWidthLiteral();

  public abstract Object getPreferredWidthAsBindingOrExpression();

  public abstract String getPreferredWidthExpression();

  // preferred height

  public abstract boolean isPreferredHeightSet();

  public abstract boolean isPreferredHeightLiteral();

  public abstract Object getPreferredHeightAsBindingOrExpression();

  public abstract String getPreferredHeightExpression();

  // maximum width

  public abstract boolean isMaximumWidthSet();

  public abstract boolean isMaximumWidthLiteral();

  public abstract Object getMaximumWidthAsBindingOrExpression();

  public abstract String getMaximumWidthExpression();

  // maximum height

  public abstract boolean isMaximumHeightSet();

  public abstract boolean isMaximumHeightLiteral();

  public abstract Object getMaximumHeightAsBindingOrExpression();

  public abstract String getMaximumHeightExpression();

}
