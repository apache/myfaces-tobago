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

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.Measure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Add GridLayoutConstraints to the parent UIComponent.
 */
@Tag(name = "gridLayoutConstraint", bodyContent = BodyContent.EMPTY)
@TagGeneration(className = "org.apache.myfaces.tobago.internal.taglib.GridLayoutConstraintTag")
public abstract class GridLayoutConstraintTag extends TagSupport {

  private static final long serialVersionUID = 3L;

  private static final Logger LOG = LoggerFactory.getLogger(GridLayoutConstraintTag.class);


  @Override
  public int doStartTag() throws JspException {

    // Locate our parent UIComponentTag
    UIComponentClassicTagBase tag =
        UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
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

    // XXX need a better solution
    if (component.getParent() != null && component.getParent().getClass().getName().endsWith("UIExtensionPanel")) {
        component = component.getParent();
    }

    if (!(component instanceof LayoutBase)) {
      // TODO Message resource i18n
      throw new JspException("Component Instance is not a LayoutBase");
    }


    if (component instanceof LayoutComponent && isColumnSpanSet()) {
      if (isColumnSpanLiteral()) {
        ((LayoutComponent) component).setColumnSpan(getColumnSpanValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.COLUMN_SPAN, getColumnSpanAsBindingOrExpression());
      }
    }

    if (component instanceof LayoutComponent && isRowSpanSet()) {
      if (isRowSpanLiteral()) {
        ((LayoutComponent) component).setRowSpan(getRowSpanValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.ROW_SPAN, getRowSpanAsBindingOrExpression());
      }
    }

    if (isWidthSet()) {
      if (isWidthLiteral()) {
        ((LayoutBase) component).setWidth(getWidthValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.WIDTH, getWidthAsBindingOrExpression());
      }
    }

    if (isHeightSet()) {
      if (isHeightLiteral()) {
        ((LayoutBase) component).setHeight(getHeightValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.HEIGHT, getHeightAsBindingOrExpression());
      }
    }

    if (isMinimumWidthSet()) {
      if (isMinimumWidthLiteral()) {
        ((LayoutBase) component).setMinimumWidth(getMinimumWidthValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MINIMUM_WIDTH, getMinimumWidthAsBindingOrExpression());
      }
    }

    if (isMinimumHeightSet()) {
      if (isMinimumHeightLiteral()) {
        ((LayoutBase) component).setMinimumHeight(getMinimumHeightValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MINIMUM_WIDTH, getMinimumHeightAsBindingOrExpression());
      }
    }

    if (isPreferredWidthSet()) {
      if (isPreferredWidthLiteral()) {
        ((LayoutBase) component).setPreferredWidth(getPreferredWidthValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.PREFERRED_WIDTH,
            getPreferredWidthAsBindingOrExpression());
      }
    }

    if (isPreferredHeightSet()) {
      if (isPreferredHeightLiteral()) {
        ((LayoutBase) component).setPreferredHeight(getPreferredHeightValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.PREFERRED_WIDTH,
            getPreferredHeightAsBindingOrExpression());
      }
    }

    if (isMaximumWidthSet()) {
      if (isMaximumWidthLiteral()) {
        ((LayoutBase) component).setMaximumWidth(getMaximumWidthValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MAXIMUM_WIDTH, getMaximumWidthAsBindingOrExpression());
      }
    }

    if (isMaximumHeightSet()) {
      if (isMaximumHeightLiteral()) {
        ((LayoutBase) component).setMaximumHeight(getMaximumHeightValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MAXIMUM_WIDTH, getMaximumHeightAsBindingOrExpression());
      }
    }

    if (isMarginLeftSet()) {
      if (isMarginLeftLiteral()) {
        ((LayoutBase) component).setMarginLeft(getMarginLeftValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MARGIN_LEFT, getMarginLeftAsBindingOrExpression());
      }
    }

    if (isMarginRightSet()) {
      if (isMarginRightLiteral()) {
        ((LayoutBase) component).setMarginRight(getMarginRightValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MARGIN_RIGHT, getMarginRightAsBindingOrExpression());
      }
    }
    if (isMarginTopSet()) {
      if (isMarginTopLiteral()) {
        ((LayoutBase) component).setMarginTop(getMarginTopValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MARGIN_TOP, getMarginTopAsBindingOrExpression());
      }
    }

    if (isMarginBottomSet()) {
      if (isMarginBottomLiteral()) {
        ((LayoutBase) component).setMarginBottom(getMarginBottomValue());
      } else {
        FacesUtils.setBindingOrExpression(component, Attributes.MARGIN_BOTTOM, getMarginBottomAsBindingOrExpression());
      }
    }

    if (isBorderLeftSet()) {
      if (component instanceof LayoutContainer) {
        if (isBorderLeftLiteral()) {
          ((LayoutContainer) component).setBorderLeft(getBorderLeftValue());
        } else {
          FacesUtils.setBindingOrExpression(component, Attributes.BORDER_LEFT, getBorderLeftAsBindingOrExpression());
        }
      } else {
        LOG.warn("Ignoring border left, because the parent is not a LayoutContainer!");
      }
    }

    if (isBorderRightSet()) {
      if (component instanceof LayoutContainer) {
        if (isBorderRightLiteral()) {
          ((LayoutContainer) component).setBorderRight(getBorderRightValue());
        } else {
          FacesUtils.setBindingOrExpression(component, Attributes.BORDER_RIGHT, getBorderRightAsBindingOrExpression());
        }
      } else {
        LOG.warn("Ignoring border left, because the parent is not a LayoutContainer!");
      }
    }
    if (isBorderTopSet()) {
      if (component instanceof LayoutContainer) {
        if (isBorderTopLiteral()) {
          ((LayoutContainer) component).setBorderTop(getBorderTopValue());
        } else {
          FacesUtils.setBindingOrExpression(component, Attributes.BORDER_TOP, getBorderTopAsBindingOrExpression());
        }
      } else {
        LOG.warn("Ignoring border left, because the parent is not a LayoutContainer!");
      }
    }

    if (isBorderBottomSet()) {
      if (component instanceof LayoutContainer) {
        if (isBorderBottomLiteral()) {
          ((LayoutContainer) component).setBorderBottom(getBorderBottomValue());
        } else {
          FacesUtils
              .setBindingOrExpression(component, Attributes.BORDER_BOTTOM, getBorderBottomAsBindingOrExpression());
        }
      } else {
        LOG.warn("Ignoring border left, because the parent is not a LayoutContainer!");
      }
    }

    if (isPaddingLeftSet()) {
      if (component instanceof LayoutContainer) {
        if (isPaddingLeftLiteral()) {
          ((LayoutContainer) component).setPaddingLeft(getPaddingLeftValue());
        } else {
          FacesUtils.setBindingOrExpression(component, Attributes.PADDING_LEFT, getPaddingLeftAsBindingOrExpression());
        }
      } else {
        LOG.warn("Ignoring padding left, because the parent is not a LayoutContainer!");
      }
    }

    if (isPaddingRightSet()) {
      if (component instanceof LayoutContainer) {
        if (isPaddingRightLiteral()) {
          ((LayoutContainer) component).setPaddingRight(getPaddingRightValue());
        } else {
          FacesUtils.setBindingOrExpression(
              component, Attributes.PADDING_RIGHT, getPaddingRightAsBindingOrExpression());
        }
      } else {
        LOG.warn("Ignoring padding left, because the parent is not a LayoutContainer!");
      }
    }
    if (isPaddingTopSet()) {
      if (component instanceof LayoutContainer) {
        if (isPaddingTopLiteral()) {
          ((LayoutContainer) component).setPaddingTop(getPaddingTopValue());
        } else {
          FacesUtils.setBindingOrExpression(component, Attributes.PADDING_TOP, getPaddingTopAsBindingOrExpression());
        }
      } else {
        LOG.warn("Ignoring padding left, because the parent is not a LayoutContainer!");
      }
    }

    if (isPaddingBottomSet()) {
      if (component instanceof LayoutContainer) {
        if (isPaddingBottomLiteral()) {
          ((LayoutContainer) component).setPaddingBottom(getPaddingBottomValue());
        } else {
          FacesUtils
              .setBindingOrExpression(component, Attributes.PADDING_BOTTOM, getPaddingBottomAsBindingOrExpression());
        }
      } else {
        LOG.warn("Ignoring padding left, because the parent is not a LayoutContainer!");
      }
    }

    return (SKIP_BODY);
  }

  /**
   * The number of horizontal cells this component should use.
   */
  @TagAttribute(name = "columnSpan", type = "java.lang.Integer")
  public abstract void setColumnSpan(ValueExpression columnSpan);

  public abstract Integer getColumnSpanValue();

  public abstract boolean isColumnSpanSet();

  public abstract boolean isColumnSpanLiteral();

  public abstract Object getColumnSpanAsBindingOrExpression();

  /**
   * The number of vertical cells this component should use.
   */
  @TagAttribute(name = "rowSpan", type = "java.lang.Integer")
  public abstract void setRowSpan(ValueExpression rowSpan);

  public abstract Integer getRowSpanValue();

  public abstract boolean isRowSpanSet();

  public abstract boolean isRowSpanLiteral();

  public abstract Object getRowSpanAsBindingOrExpression();

  /**
   * The width for this component.
   */
  @TagAttribute(name = "width", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setWidth(ValueExpression width);

  public abstract Measure getWidthValue();

  public abstract boolean isWidthSet();

  public abstract boolean isWidthLiteral();

  public abstract Object getWidthAsBindingOrExpression();

  /**
   * The height for this component.
   */
  @TagAttribute(name = "height", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setHeight(ValueExpression height);

  public abstract Measure getHeightValue();

  public abstract boolean isHeightSet();

  public abstract boolean isHeightLiteral();

  public abstract Object getHeightAsBindingOrExpression();

  /**
   * The minimum width for this component.
   */
  @TagAttribute(name = "minimumWidth", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMinimumWidth(ValueExpression minimumWidth);

  public abstract Measure getMinimumWidthValue();

  public abstract boolean isMinimumWidthSet();

  public abstract boolean isMinimumWidthLiteral();

  public abstract Object getMinimumWidthAsBindingOrExpression();

  /**
   * The minimum height for this component.
   */
  @TagAttribute(name = "minimumHeight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMinimumHeight(ValueExpression minimumHeight);

  public abstract Measure getMinimumHeightValue();

  public abstract boolean isMinimumHeightSet();

  public abstract boolean isMinimumHeightLiteral();

  public abstract Object getMinimumHeightAsBindingOrExpression();

  /**
   * The preferred width for this component.
   */
  @TagAttribute(name = "preferredWidth", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setPreferredWidth(ValueExpression preferredWidth);

  public abstract Measure getPreferredWidthValue();

  public abstract boolean isPreferredWidthSet();

  public abstract boolean isPreferredWidthLiteral();

  public abstract Object getPreferredWidthAsBindingOrExpression();

  /**
   * The preferred height for this component.
   */
  @TagAttribute(name = "preferredHeight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setPreferredHeight(ValueExpression preferredHeight);

  public abstract Measure getPreferredHeightValue();

  public abstract boolean isPreferredHeightSet();

  public abstract boolean isPreferredHeightLiteral();

  public abstract Object getPreferredHeightAsBindingOrExpression();

  /**
   * The maximum width for this component.
   */
  @TagAttribute(name = "maximumWidth", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMaximumWidth(ValueExpression maximumWidth);

  public abstract Measure getMaximumWidthValue();

  public abstract boolean isMaximumWidthSet();

  public abstract boolean isMaximumWidthLiteral();

  public abstract Object getMaximumWidthAsBindingOrExpression();

  /**
   * The maximum height for this component.
   */
  @TagAttribute(name = "maximumHeight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMaximumHeight(ValueExpression maximumHeight);

  public abstract Measure getMaximumHeightValue();

  public abstract boolean isMaximumHeightSet();

  public abstract boolean isMaximumHeightLiteral();

  public abstract Object getMaximumHeightAsBindingOrExpression();

  /**
   * The left margin for this component.
   */
  @TagAttribute(name = "marginLeft", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMarginLeft(ValueExpression marginLeft);

  public abstract Measure getMarginLeftValue();

  public abstract boolean isMarginLeftSet();

  public abstract boolean isMarginLeftLiteral();

  public abstract Object getMarginLeftAsBindingOrExpression();

  /**
   * The right margin for this component.
   */
  @TagAttribute(name = "marginRight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMarginRight(ValueExpression marginLeft);

  public abstract Measure getMarginRightValue();

  public abstract boolean isMarginRightSet();

  public abstract boolean isMarginRightLiteral();

  public abstract Object getMarginRightAsBindingOrExpression();

  /**
   * The top margin for this component.
   */
  @TagAttribute(name = "marginTop", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMarginTop(ValueExpression marginTop);

  public abstract Measure getMarginTopValue();

  public abstract boolean isMarginTopSet();

  public abstract boolean isMarginTopLiteral();

  public abstract Object getMarginTopAsBindingOrExpression();

  /**
   * The bottom margin for this component.
   */
  @TagAttribute(name = "marginBottom", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setMarginBottom(ValueExpression marginBottom);

  public abstract Measure getMarginBottomValue();

  public abstract boolean isMarginBottomSet();

  public abstract boolean isMarginBottomLiteral();

  public abstract Object getMarginBottomAsBindingOrExpression();

  /**
   * The left border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderLeft", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setBorderLeft(ValueExpression borderLeft);

  public abstract Measure getBorderLeftValue();

  public abstract boolean isBorderLeftSet();

  public abstract boolean isBorderLeftLiteral();

  public abstract Object getBorderLeftAsBindingOrExpression();

  /**
   * The right border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderRight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setBorderRight(ValueExpression borderRight);

  public abstract Measure getBorderRightValue();

  public abstract boolean isBorderRightSet();

  public abstract boolean isBorderRightLiteral();

  public abstract Object getBorderRightAsBindingOrExpression();

  /**
   * The top border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderTop", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setBorderTop(ValueExpression borderTop);

  public abstract Measure getBorderTopValue();

  public abstract boolean isBorderTopSet();

  public abstract boolean isBorderTopLiteral();

  public abstract Object getBorderTopAsBindingOrExpression();

  /**
   * The bottom border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderBottom", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setBorderBottom(ValueExpression borderBottom);

  public abstract Measure getBorderBottomValue();

  public abstract boolean isBorderBottomSet();

  public abstract boolean isBorderBottomLiteral();

  public abstract Object getBorderBottomAsBindingOrExpression();

  /**
   * The left padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingLeft", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setPaddingLeft(ValueExpression paddingLeft);

  public abstract Measure getPaddingLeftValue();

  public abstract boolean isPaddingLeftSet();

  public abstract boolean isPaddingLeftLiteral();

  public abstract Object getPaddingLeftAsBindingOrExpression();

  /**
   * The right padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingRight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setPaddingRight(ValueExpression paddingRight);

  public abstract Measure getPaddingRightValue();

  public abstract boolean isPaddingRightSet();

  public abstract boolean isPaddingRightLiteral();

  public abstract Object getPaddingRightAsBindingOrExpression();

  /**
   * The top padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingTop", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setPaddingTop(ValueExpression paddingTop);

  public abstract Measure getPaddingTopValue();

  public abstract boolean isPaddingTopSet();

  public abstract boolean isPaddingTopLiteral();

  public abstract Object getPaddingTopAsBindingOrExpression();

  /**
   * The bottom padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingBottom", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public abstract void setPaddingBottom(ValueExpression paddingBottom);

  public abstract Measure getPaddingBottomValue();

  public abstract boolean isPaddingBottomSet();

  public abstract boolean isPaddingBottomLiteral();

  public abstract Object getPaddingBottomAsBindingOrExpression();
}
