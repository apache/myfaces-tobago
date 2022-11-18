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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.Overflow;
import org.apache.myfaces.tobago.layout.Position;
import org.apache.myfaces.tobago.layout.TextAlign;

/**
 * <p>
 * Add a style tag with the given file name to the header (using file attribute) or add
 * some CSS styles to the parent component.
 * </p>
 */
@Tag(name = "style")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIStyle",
    componentFamily = "org.apache.myfaces.tobago.Style",
    rendererType = RendererTypes.STYLE,
    allowedChildComponenents = "NONE")
public interface StyleTagDeclaration extends HasIdBindingAndRendered {

  /**
   * Name of the stylesheet file to add to page. The name must be full qualified, or relative.
   * If using a complete path from root, you'll need to add the contextPath from the web application.
   * This can be done with the EL #{request.contextPath}.
   */
  @TagAttribute()
  @UIComponentTagAttribute()
  void setFile(String file);

  /**
   * @param customClass A custom CSS class for the parent component.
   *                    Note: The rendered attribute has no effect to the use of this customClass.
   *                    To switch the customClass, you may use an EL expression
   *                    like e.g. customClass="#{isError ? 'red' : null}".
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.renderkit.css.CustomClass")
  void setCustomClass(String customClass);

  /**
   * @param selector A selector to define the element for this style rules. Defaults to the ID of the parent component.
   * @since 4.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute()
  void setSelector(String selector);

  /**
   * @param width The width for the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setWidth(String width);

  /**
   * @param height The height for the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setHeight(String height);

  /**
   * @param minWidth The minimum width for the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setMinWidth(String minWidth);

  /**
   * @param minHeight The minimum height for the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setMinHeight(String minHeight);

  /**
   * @param maxWidth The maximum width for the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setMaxWidth(String maxWidth);

  /**
   * @param maxHeight The maximum height for the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setMaxHeight(String maxHeight);

  /**
   * @param left The left position value for the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setLeft(String left);

  /**
   * @param right The left position value for the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setRight(String right);

  /**
   * @param top The top position value for the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setTop(String top);

  /**
   * @param bottom The top position value for the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setBottom(String bottom);

  /**
   * @param paddingLeft The left padding.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setPaddingLeft(String paddingLeft);

  /**
   * @param paddingRight The right padding.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setPaddingRight(String paddingRight);

  /**
   * @param paddingTop The top padding.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setPaddingTop(String paddingTop);

  /**
   * @param paddingBottom The bottom padding.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setPaddingBottom(String paddingBottom);

  /**
   * @param marginLeft The margin at the left of the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setMarginLeft(String marginLeft);

  /**
   * @param marginRight The margin at the right of the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setMarginRight(String marginRight);

  /**
   * @param marginTop The margin at the top of the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setMarginTop(String marginTop);

  /**
   * @param marginBottom The margin at the bottom of the parent component.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setMarginBottom(String marginBottom);

  /**
   * @param overflowX Does the component need a horizontal scrollbar.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Overflow",
      allowedValues = {Overflow.AUTO, Overflow.SCROLL, Overflow.HIDDEN})
  void setOverflowX(String overflowX);

  /**
   * @param overflowY Does the component need a vertical scrollbar.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Overflow",
      allowedValues = {Overflow.AUTO, Overflow.SCROLL, Overflow.HIDDEN})
  void setOverflowY(String overflowY);

  /**
   * @param display Indicates the renderer to render the element as
   *                {@link Display#block}, {@link Display#inline} or {@link Display#none}.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Display",
      allowedValues = {Display.BLOCK, Display.INLINE, Display.GRID, Display.NONE})
  void setDisplay(String display);

  /**
   * @param position Values for the position used with CSS.
   * @since 3.0.0
   */
  @TagAttribute()
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Position",
      allowedValues = {Position.ABSOLUTE, Position.RELATIVE, Position.FIXED})
  void setPosition(String position);

  /**
   * The alignment of the elements inside of the container, possible values are:
   * {@link TextAlign#left},
   * {@link TextAlign#right},
   * {@link TextAlign#center} and
   * {@link TextAlign#justify}.
   *
   * @since 3.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"org.apache.myfaces.tobago.layout.TextAlign"},
      allowedValues = {
          TextAlign.LEFT, TextAlign.RIGHT, TextAlign.CENTER, TextAlign.JUSTIFY
      })
  void setTextAlign(String textAlign);

  /**
   * The background image of the element.
   *
   * @since 4.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute
  void setBackgroundImage(String backgroundImage);

  /**
   * Flex grow value of the element.
   *
   * @since 4.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Number")
  void setFlexGrow(String flexGrow);

  /**
   * Flex shrink value of the element.
   *
   * @since 4.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Number")
  void setFlexShrink(String flexShrink);

  /**
   * Flex basis value of the element.
   *
   * @since 4.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setFlexBasis(String flexBasis);

  /**
   * Grid template columns value of the element.
   *
   * @since 4.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute
//(type = "org.apache.myfaces.tobago.layout.TBD")
  void setGridTemplateColumns(String gridTemplateColumns);

  /**
   * Grid template rows value of the element.
   *
   * @since 4.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute
//(type = "org.apache.myfaces.tobago.layout.TBD")
  void setGridTemplateRows(String gridTemplateRows);

  /**
   * Grid column value of the element.
   *
   * @since 4.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.GridSpan")
  void setGridColumn(String gridColumn);

  /**
   * Grid row value of the element.
   *
   * @since 4.0.0
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.GridSpan")
  void setGridRow(String gridRow);

  /**
   * Note: The rendered attribute has no effect to the use of the customClass attribute.
   */
  @Override
  void setRendered(String rendered);
}
