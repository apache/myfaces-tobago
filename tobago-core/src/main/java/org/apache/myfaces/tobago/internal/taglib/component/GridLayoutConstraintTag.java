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
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
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

    final boolean isLayoutContainer = component instanceof LayoutContainer;
    final boolean isLayoutComponent = component instanceof LayoutComponent;

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
      component.setValueExpression(Attributes.WIDTH, width);
    }

    if (height != null) {
      component.setValueExpression(Attributes.HEIGHT, height);
    }

    if (minimumWidth != null) {
      component.setValueExpression(Attributes.MINIMUM_WIDTH, minimumWidth);
    }

    if (minimumHeight != null) {
      component.setValueExpression(Attributes.MINIMUM_HEIGHT, minimumHeight);
    }

    if (preferredWidth != null) {
      component.setValueExpression(Attributes.PREFERRED_WIDTH, preferredWidth);
    }

    if (preferredHeight != null) {
      component.setValueExpression(Attributes.PREFERRED_HEIGHT, preferredHeight);
    }

    if (maximumWidth != null) {
      component.setValueExpression(Attributes.MAXIMUM_WIDTH, maximumWidth);
    }

    if (maximumHeight != null) {
      component.setValueExpression(Attributes.MAXIMUM_HEIGHT, maximumHeight);
    }

    if (marginLeft != null) {
      component.setValueExpression(Attributes.MARGIN_LEFT, marginLeft);
    }

    if (marginRight != null) {
      component.setValueExpression(Attributes.MARGIN_RIGHT, marginRight);
    }

    if (marginTop != null) {
      component.setValueExpression(Attributes.MARGIN_TOP, marginTop);
    }

    if (marginBottom != null) {
      component.setValueExpression(Attributes.MARGIN_BOTTOM, marginBottom);
    }

    if (borderLeft != null) {
      if (isLayoutContainer) {
        component.setValueExpression(Attributes.BORDER_LEFT, borderLeft);
      } else {
        LOG.warn("Ignoring '" + Attributes.BORDER_LEFT + "', because the parent is not a LayoutContainer!");
      }
    }

    if (borderRight != null) {
      if (isLayoutContainer) {
        component.setValueExpression(Attributes.BORDER_RIGHT, borderRight);
      } else {
        LOG.warn("Ignoring '" + Attributes.BORDER_RIGHT + "', because the parent is not a LayoutContainer!");
      }
    }
    if (borderTop != null) {
      if (isLayoutContainer) {
        component.setValueExpression(Attributes.BORDER_TOP, borderTop);
      } else {
        LOG.warn("Ignoring '" + Attributes.BORDER_TOP + "', because the parent is not a LayoutContainer!");
      }
    }

    if (borderBottom != null) {
      if (isLayoutContainer) {
        component.setValueExpression(Attributes.BORDER_BOTTOM, borderBottom);
      } else {
        LOG.warn("Ignoring '" + Attributes.BORDER_BOTTOM + "', because the parent is not a LayoutContainer!");
      }
    }

    if (paddingLeft != null) {
      if (isLayoutContainer) {
        component.setValueExpression(Attributes.PADDING_LEFT, paddingLeft);
      } else {
        LOG.warn("Ignoring '" + Attributes.PADDING_LEFT + "', because the parent is not a LayoutContainer!");
      }
    }

    if (paddingRight != null) {
      if (isLayoutContainer) {
        component.setValueExpression(Attributes.PADDING_RIGHT, paddingRight);
      } else {
        LOG.warn("Ignoring '" + Attributes.PADDING_RIGHT + "', because the parent is not a LayoutContainer!");
      }
    }
    if (paddingTop != null) {
      if (isLayoutContainer) {
        component.setValueExpression(Attributes.PADDING_TOP, paddingTop);
      } else {
        LOG.warn("Ignoring '" + Attributes.PADDING_TOP + "', because the parent is not a LayoutContainer!");
      }
    }

    if (paddingBottom != null) {
      if (isLayoutContainer) {
        component.setValueExpression(Attributes.PADDING_BOTTOM, paddingBottom);
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
  public void setColumnSpan(ValueExpression columnSpan) {
    this.columnSpan = columnSpan;
  }

  /**
   * The number of vertical cells this component should use.
   */
  @TagAttribute(name = "rowSpan", type = "java.lang.Integer")
  public void setRowSpan(ValueExpression rowSpan) {
    this.rowSpan = rowSpan;
  }

  /**
   * The width for this component.
   */
  @TagAttribute(name = "width", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setWidth(ValueExpression width) {
    this.width = width;
  }

  /**
   * The height for this component.
   */
  @TagAttribute(name = "height", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setHeight(ValueExpression height) {
    this.height = height;
  }

  /**
   * The minimum width for this component.
   */
  @TagAttribute(name = "minimumWidth", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setMinimumWidth(ValueExpression minimumWidth) {
    this.minimumWidth = minimumWidth;
  }

  /**
   * The minimum height for this component.
   */
  @TagAttribute(name = "minimumHeight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setMinimumHeight(ValueExpression minimumHeight) {
    this.minimumHeight = minimumHeight;
  }

  /**
   * The preferred width for this component.
   */
  @TagAttribute(name = "preferredWidth", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setPreferredWidth(ValueExpression preferredWidth) {
    this.preferredWidth = preferredWidth;
  }

  /**
   * The preferred height for this component.
   */
  @TagAttribute(name = "preferredHeight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setPreferredHeight(ValueExpression preferredHeight) {
    this.preferredHeight = preferredHeight;
  }

  /**
   * The maximum width for this component.
   */
  @TagAttribute(name = "maximumWidth", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setMaximumWidth(ValueExpression maximumWidth) {
    this.maximumWidth = maximumWidth;
  }

  /**
   * The maximum height for this component.
   */
  @TagAttribute(name = "maximumHeight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setMaximumHeight(ValueExpression maximumHeight) {
    this.maximumHeight = maximumHeight;
  }

  /**
   * The left margin for this component.
   */
  @TagAttribute(name = "marginLeft", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setMarginLeft(ValueExpression marginLeft) {
    this.marginLeft = marginLeft;
  }

  /**
   * The right margin for this component.
   */
  @TagAttribute(name = "marginRight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setMarginRight(ValueExpression marginRight) {
    this.marginRight = marginRight;
  }

  /**
   * The top margin for this component.
   */
  @TagAttribute(name = "marginTop", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setMarginTop(ValueExpression marginTop) {
    this.marginTop = marginTop;
  }

  /**
   * The bottom margin for this component.
   */
  @TagAttribute(name = "marginBottom", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setMarginBottom(ValueExpression marginBottom) {
    this.marginBottom = marginBottom;
  }

  /**
   * The left border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderLeft", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setBorderLeft(ValueExpression borderLeft) {
    this.borderLeft = borderLeft;
  }

  /**
   * The right border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderRight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setBorderRight(ValueExpression borderRight) {
    this.borderRight = borderRight;
  }

  /**
   * The top border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderTop", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setBorderTop(ValueExpression borderTop) {
    this.borderTop = borderTop;
  }

  /**
   * The bottom border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderBottom", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setBorderBottom(ValueExpression borderBottom) {
    this.borderBottom = borderBottom;
  }

  /**
   * The left padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingLeft", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setPaddingLeft(ValueExpression paddingLeft) {
    this.paddingLeft = paddingLeft;
  }

  /**
   * The right padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingRight", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setPaddingRight(ValueExpression paddingRight) {
    this.paddingRight = paddingRight;
  }

  /**
   * The top padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingTop", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setPaddingTop(ValueExpression paddingTop) {
    this.paddingTop = paddingTop;
  }

  /**
   * The bottom padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingBottom", type = "org.apache.myfaces.tobago.layout.Measure")
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  public void setPaddingBottom(ValueExpression paddingBottom) {
    this.paddingBottom = paddingBottom;
  }
}
