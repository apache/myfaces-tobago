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

import javax.el.ValueExpression;

/**
 * Add GridLayoutConstraints to the parent UIComponent.
 */
@Tag(name = "gridLayoutConstraint")
@SimpleTag(faceletHandler = "org.apache.myfaces.tobago.facelets.GridLayoutConstraintHandler")
public interface GridLayoutConstraintTagDeclaration {

  /**
   * The number of horizontal cells this component should use.
   */
  @TagAttribute(name = "columnSpan", type = "java.lang.Integer")
  void setColumnSpan(final ValueExpression columnSpan);

  /**
   * The number of vertical cells this component should use.
   */
  @TagAttribute(name = "rowSpan", type = "java.lang.Integer")
  void setRowSpan(final ValueExpression rowSpan);

  /**
   * The width for this component.
   */
  @TagAttribute(name = "width", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setWidth(final ValueExpression width);

  /**
   * The height for this component.
   */
  @TagAttribute(name = "height", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setHeight(final ValueExpression height);

  /**
   * The minimum width for this component.
   */
  @TagAttribute(name = "minimumWidth", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setMinimumWidth(final ValueExpression minimumWidth);

  /**
   * The minimum height for this component.
   */
  @TagAttribute(name = "minimumHeight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setMinimumHeight(final ValueExpression minimumHeight);

  /**
   * The preferred width for this component.
   */
  @TagAttribute(name = "preferredWidth", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setPreferredWidth(final ValueExpression preferredWidth);

  /**
   * The preferred height for this component.
   */
  @TagAttribute(name = "preferredHeight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setPreferredHeight(final ValueExpression preferredHeight);

  /**
   * The maximum width for this component.
   */
  @TagAttribute(name = "maximumWidth", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setMaximumWidth(final ValueExpression maximumWidth);

  /**
   * The maximum height for this component.
   */
  @TagAttribute(name = "maximumHeight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setMaximumHeight(final ValueExpression maximumHeight);

  /**
   * The left margin for this component.
   */
  @TagAttribute(name = "marginLeft", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setMarginLeft(final ValueExpression marginLeft);

  /**
   * The right margin for this component.
   */
  @TagAttribute(name = "marginRight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setMarginRight(final ValueExpression marginRight);

  /**
   * The top margin for this component.
   */
  @TagAttribute(name = "marginTop", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setMarginTop(final ValueExpression marginTop);

  /**
   * The bottom margin for this component.
   */
  @TagAttribute(name = "marginBottom", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setMarginBottom(final ValueExpression marginBottom);

  /**
   * The left border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderLeft", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setBorderLeft(final ValueExpression borderLeft);

  /**
   * The right border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderRight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setBorderRight(final ValueExpression borderRight);

  /**
   * The top border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderTop", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setBorderTop(final ValueExpression borderTop);

  /**
   * The bottom border area for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "borderBottom", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setBorderBottom(final ValueExpression borderBottom);

  /**
   * The left padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingLeft", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setPaddingLeft(final ValueExpression paddingLeft);

  /**
   * The right padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingRight", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setPaddingRight(final ValueExpression paddingRight);

  /**
   * The top padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingTop", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setPaddingTop(final ValueExpression paddingTop);

  /**
   * The bottom padding for this component. Its only applicably for containers.
   */
  @TagAttribute(name = "paddingBottom", type = "java.lang.Object")
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setPaddingBottom(final ValueExpression paddingBottom);
}
