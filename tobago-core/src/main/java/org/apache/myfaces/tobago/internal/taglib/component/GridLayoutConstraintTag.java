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

import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.Measure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Add GridLayoutConstraints to the parent UIComponent.
 */
@Tag(name = "gridLayoutConstraint")
@SimpleTag(
    faceletHandler = "org.apache.myfaces.tobago.facelets.GridLayoutConstraintHandler")
public abstract class GridLayoutConstraintTag extends TagSupport {

  private static final long serialVersionUID = 4L;

  private static final Logger LOG = LoggerFactory.getLogger(GridLayoutConstraintTag.class);

  private javax.el.ValueExpression borderBottom;
  private javax.el.ValueExpression borderLeft;
  private javax.el.ValueExpression borderRight;
  private javax.el.ValueExpression borderTop;
  private javax.el.ValueExpression columnSpan;
  private javax.el.ValueExpression height;
  private javax.el.ValueExpression marginBottom;
  private javax.el.ValueExpression marginLeft;
  private javax.el.ValueExpression marginRight;
  private javax.el.ValueExpression marginTop;
  private javax.el.ValueExpression maximumHeight;
  private javax.el.ValueExpression maximumWidth;
  private javax.el.ValueExpression minimumHeight;
  private javax.el.ValueExpression minimumWidth;
  private javax.el.ValueExpression paddingBottom;
  private javax.el.ValueExpression paddingLeft;
  private javax.el.ValueExpression paddingRight;
  private javax.el.ValueExpression paddingTop;
  private javax.el.ValueExpression preferredHeight;
  private javax.el.ValueExpression preferredWidth;
  private javax.el.ValueExpression rowSpan;
  private javax.el.ValueExpression width;

  @Override
  public int doStartTag() throws JspException {

    // Locate our parent UIComponentTag
    final UIComponentClassicTagBase tag =
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

    final LayoutBase layoutBase = (LayoutBase) component;
    final boolean isLayoutContainer = component instanceof LayoutContainer;
    final boolean isLayoutComponent = component instanceof LayoutComponent;
    final LayoutContainer layoutContainer = isLayoutContainer ? (LayoutContainer) component : null;
    final ELContext elContext = FacesContext.getCurrentInstance().getELContext();

    if (columnSpan != null) {
      if (isLayoutComponent) {
        component.setValueExpression(Attributes.COLUMN_SPAN, columnSpan);
      } else {
        LOG.warn("Ignoring '" + Attributes.COLUMN_SPAN + "', because the parent is not a LayoutComponent!");
      }
    }

    if (rowSpan != null) {
      if (isLayoutComponent) {
        component.setValueExpression(Attributes.ROW_SPAN, rowSpan);
      } else {
        LOG.warn("Ignoring '" + Attributes.ROW_SPAN + "', because the parent is not a LayoutComponent!");
      }
    }

    if (width != null) {
      if (width.isLiteralText()) {
        layoutBase.setWidth(Measure.valueOf(width.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.WIDTH, width);
      }
    }

    if (height != null) {
      if (height.isLiteralText()) {
        layoutBase.setHeight(Measure.valueOf(height.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.HEIGHT, height);
      }
    }

    if (minimumWidth != null) {
      if (minimumWidth.isLiteralText()) {
        layoutBase.setMinimumWidth(Measure.valueOf(minimumWidth.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.MINIMUM_WIDTH, minimumWidth);
      }
    }

    if (minimumHeight != null) {
      if (minimumHeight.isLiteralText()) {
        layoutBase.setMinimumHeight(Measure.valueOf(minimumHeight.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.MINIMUM_HEIGHT, minimumHeight);
      }
    }

    if (preferredWidth != null) {
      if (preferredWidth.isLiteralText()) {
        layoutBase.setPreferredWidth(Measure.valueOf(preferredWidth.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.PREFERRED_WIDTH, preferredWidth);
      }
    }

    if (preferredHeight != null) {
      if (preferredHeight.isLiteralText()) {
        layoutBase.setPreferredHeight(Measure.valueOf(preferredHeight.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.PREFERRED_HEIGHT, preferredHeight);
      }
    }

    if (maximumWidth != null) {
      if (maximumWidth.isLiteralText()) {
        layoutBase.setMaximumWidth(Measure.valueOf(maximumWidth.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.MAXIMUM_WIDTH, maximumWidth);
      }
    }

    if (maximumHeight != null) {
      if (maximumHeight.isLiteralText()) {
        layoutBase.setMaximumHeight(Measure.valueOf(maximumHeight.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.MAXIMUM_HEIGHT, maximumHeight);
      }
    }

    if (marginLeft != null) {
      if (marginLeft.isLiteralText()) {
        layoutBase.setMarginLeft(Measure.valueOf(marginLeft.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.MARGIN_LEFT, marginLeft);
      }
    }

    if (marginRight != null) {
      if (marginRight.isLiteralText()) {
        layoutBase.setMarginRight(Measure.valueOf(marginRight.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.MARGIN_RIGHT, marginRight);
      }
    }

    if (marginTop != null) {
      if (marginTop.isLiteralText()) {
        layoutBase.setMarginTop(Measure.valueOf(marginTop.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.MARGIN_TOP, marginTop);
      }
    }

    if (marginBottom != null) {
      if (marginBottom.isLiteralText()) {
        layoutBase.setMarginBottom(Measure.valueOf(marginBottom.getValue(elContext)));
      } else {
        component.setValueExpression(Attributes.MARGIN_BOTTOM, marginBottom);
      }
    }

    if (borderLeft != null) {
      if (isLayoutContainer) {
        if (borderLeft.isLiteralText()) {
          layoutContainer.setBorderLeft(Measure.valueOf(borderLeft.getValue(elContext)));
        } else {
          component.setValueExpression(Attributes.BORDER_LEFT, borderLeft);
        }
      } else {
        LOG.warn("Ignoring '" + Attributes.BORDER_LEFT + "', because the parent is not a LayoutContainer!");
      }
    }

    if (borderRight != null) {
      if (isLayoutContainer) {
        if (borderRight.isLiteralText()) {
          layoutContainer.setBorderRight(Measure.valueOf(borderRight.getValue(elContext)));
        } else {
          component.setValueExpression(Attributes.BORDER_RIGHT, borderRight);
        }
      } else {
        LOG.warn("Ignoring '" + Attributes.BORDER_RIGHT + "', because the parent is not a LayoutContainer!");
      }
    }

    if (borderTop != null) {
      if (isLayoutContainer) {
        if (borderTop.isLiteralText()) {
          layoutContainer.setBorderTop(Measure.valueOf(borderTop.getValue(elContext)));
        } else {
          component.setValueExpression(Attributes.BORDER_TOP, borderTop);
        }
      } else {
        LOG.warn("Ignoring '" + Attributes.BORDER_TOP + "', because the parent is not a LayoutContainer!");
      }
    }

    if (borderBottom != null) {
      if (isLayoutContainer) {
        if (borderBottom.isLiteralText()) {
          layoutContainer.setBorderBottom(Measure.valueOf(borderBottom.getValue(elContext)));
        } else {
          component.setValueExpression(Attributes.BORDER_BOTTOM, borderBottom);
        }
      } else {
        LOG.warn("Ignoring '" + Attributes.BORDER_BOTTOM + "', because the parent is not a LayoutContainer!");
      }
    }

    if (paddingLeft != null) {
      if (isLayoutContainer) {
        if (paddingLeft.isLiteralText()) {
          layoutContainer.setPaddingLeft(Measure.valueOf(paddingLeft.getValue(elContext)));
        } else {
          component.setValueExpression(Attributes.PADDING_LEFT, paddingLeft);
        }
      } else {
        LOG.warn("Ignoring '" + Attributes.PADDING_LEFT + "', because the parent is not a LayoutContainer!");
      }
    }

    if (paddingRight != null) {
      if (isLayoutContainer) {
        if (paddingRight.isLiteralText()) {
          layoutContainer.setPaddingRight(Measure.valueOf(paddingRight.getValue(elContext)));
        } else {
          component.setValueExpression(Attributes.PADDING_RIGHT, paddingRight);
        }
      } else {
        LOG.warn("Ignoring '" + Attributes.PADDING_RIGHT + "', because the parent is not a LayoutContainer!");
      }
    }
    if (paddingTop != null) {
      if (isLayoutContainer) {
        if (paddingTop.isLiteralText()) {
          layoutContainer.setPaddingTop(Measure.valueOf(paddingTop.getValue(elContext)));
        } else {
          component.setValueExpression(Attributes.PADDING_TOP, paddingTop);
        }
      } else {
        LOG.warn("Ignoring '" + Attributes.PADDING_TOP + "', because the parent is not a LayoutContainer!");
      }
    }

    if (paddingBottom != null) {
      if (isLayoutContainer) {
        if (paddingBottom.isLiteralText()) {
          layoutContainer.setPaddingBottom(Measure.valueOf(paddingBottom.getValue(elContext)));
        } else {
          component.setValueExpression(Attributes.PADDING_BOTTOM, paddingBottom);
        }
      } else {
        LOG.warn("Ignoring '" + Attributes.PADDING_BOTTOM + "', because the parent is not a LayoutContainer!");
      }
    }

    return (SKIP_BODY);
  }

  @Override
  public void release() {
    super.release();
    borderBottom = null;
    borderLeft = null;
    borderRight = null;
    borderTop = null;
    columnSpan = null;
    height = null;
    marginBottom = null;
    marginLeft = null;
    marginRight = null;
    marginTop = null;
    maximumHeight = null;
    maximumWidth = null;
    minimumHeight = null;
    minimumWidth = null;
    paddingBottom = null;
    paddingLeft = null;
    paddingRight = null;
    paddingTop = null;
    preferredHeight = null;
    preferredWidth = null;
    rowSpan = null;
    width = null;
  }

  /**
   * The number of horizontal cells this component should use.
   */
  @TagAttribute(name = "columnSpan", type = "java.lang.Integer")
  public void setColumnSpan(final ValueExpression columnSpan) {
    this.columnSpan = columnSpan;
  }

  /**
   * The number of vertical cells this component should use.
   */
  @TagAttribute(name = "rowSpan", type = "java.lang.Integer")
  public void setRowSpan(final ValueExpression rowSpan) {
    this.rowSpan = rowSpan;
  }

  /**
   * The width for this component.
   */
  @TagAttribute(name = "width", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setWidth(final ValueExpression width) {
    this.width = width;
  }

  /**
   * The height for this component.
   */
  @TagAttribute(name = "height", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setHeight(final ValueExpression height) {
    this.height = height;
  }

  /**
   * The minimum width for this component.
   */
  @TagAttribute(name = "minimumWidth", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setMinimumWidth(final ValueExpression minimumWidth) {
    this.minimumWidth = minimumWidth;
  }

  /**
   * The minimum height for this component.
   */
  @TagAttribute(name = "minimumHeight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setMinimumHeight(final ValueExpression minimumHeight) {
    this.minimumHeight = minimumHeight;
  }

  /**
   * The preferred width for this component.
   */
  @TagAttribute(name = "preferredWidth", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setPreferredWidth(final ValueExpression preferredWidth) {
    this.preferredWidth = preferredWidth;
  }

  /**
   * The preferred height for this component.
   */
  @TagAttribute(name = "preferredHeight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setPreferredHeight(final ValueExpression preferredHeight) {
    this.preferredHeight = preferredHeight;
  }

  /**
   * The maximum width for this component.
   */
  @TagAttribute(name = "maximumWidth", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setMaximumWidth(final ValueExpression maximumWidth) {
    this.maximumWidth = maximumWidth;
  }

  /**
   * The maximum height for this component.
   */
  @TagAttribute(name = "maximumHeight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setMaximumHeight(final ValueExpression maximumHeight) {
    this.maximumHeight = maximumHeight;
  }

  /**
   * The left margin for this component.
   */
  @TagAttribute(name = "marginLeft", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setMarginLeft(final ValueExpression marginLeft) {
    this.marginLeft = marginLeft;
  }

  /**
   * The right margin for this component.
   */
  @TagAttribute(name = "marginRight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setMarginRight(final ValueExpression marginRight) {
    this.marginRight = marginRight;
  }

  /**
   * The top margin for this component.
   */
  @TagAttribute(name = "marginTop", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setMarginTop(final ValueExpression marginTop) {
    this.marginTop = marginTop;
  }

  /**
   * The bottom margin for this component.
   */
  @TagAttribute(name = "marginBottom", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setMarginBottom(final ValueExpression marginBottom) {
    this.marginBottom = marginBottom;
  }

  /**
   * The left border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderLeft", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setBorderLeft(final ValueExpression borderLeft) {
    this.borderLeft = borderLeft;
  }

  /**
   * The right border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderRight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setBorderRight(final ValueExpression borderRight) {
    this.borderRight = borderRight;
  }

  /**
   * The top border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderTop", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setBorderTop(final ValueExpression borderTop) {
    this.borderTop = borderTop;
  }

  /**
   * The bottom border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderBottom", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setBorderBottom(final ValueExpression borderBottom) {
    this.borderBottom = borderBottom;
  }

  /**
   * The left padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingLeft", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setPaddingLeft(final ValueExpression paddingLeft) {
    this.paddingLeft = paddingLeft;
  }

  /**
   * The right padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingRight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setPaddingRight(final ValueExpression paddingRight) {
    this.paddingRight = paddingRight;
  }

  /**
   * The top padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingTop", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setPaddingTop(final ValueExpression paddingTop) {
    this.paddingTop = paddingTop;
  }

  /**
   * The bottom padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingBottom", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setPaddingBottom(final ValueExpression paddingBottom) {
    this.paddingBottom = paddingBottom;
  }
}
