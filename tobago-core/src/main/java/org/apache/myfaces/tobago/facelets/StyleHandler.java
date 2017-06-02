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

package org.apache.myfaces.tobago.facelets;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Overflow;
import org.apache.myfaces.tobago.layout.Position;
import org.apache.myfaces.tobago.layout.TextAlign;
import org.apache.myfaces.tobago.renderkit.css.CustomClass;
import org.apache.myfaces.tobago.renderkit.css.Style;

import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

public final class StyleHandler extends TagHandler {

  private final TagAttribute file;

  private final TagAttribute customClass;

  private final TagAttribute width;
  private final TagAttribute height;
  private final TagAttribute minWidth;
  private final TagAttribute minHeight;
  private final TagAttribute maxWidth;
  private final TagAttribute maxHeight;
  private final TagAttribute left;
  private final TagAttribute right;
  private final TagAttribute top;
  private final TagAttribute bottom;
  private final TagAttribute paddingLeft;
  private final TagAttribute paddingRight;
  private final TagAttribute paddingTop;
  private final TagAttribute paddingBottom;
  private final TagAttribute marginLeft;
  private final TagAttribute marginRight;
  private final TagAttribute marginTop;
  private final TagAttribute marginBottom;
  private final TagAttribute overflowX;
  private final TagAttribute overflowY;
  private final TagAttribute display;
  private final TagAttribute position;
  private final TagAttribute textAlign;


  public StyleHandler(final TagConfig config) {
    super(config);

    this.file = getAttribute(Attributes.file.getName());

    this.customClass = getAttribute(Attributes.customClass.getName());

    this.width = getAttribute(Attributes.width.getName());
    this.height = getAttribute(Attributes.height.getName());
    this.minWidth = getAttribute(Attributes.minWidth.getName());
    this.minHeight = getAttribute(Attributes.minHeight.getName());
    this.maxWidth = getAttribute(Attributes.maxWidth.getName());
    this.maxHeight = getAttribute(Attributes.maxHeight.getName());
    this.left = getAttribute(Attributes.left.getName());
    this.right = getAttribute(Attributes.right.getName());
    this.top = getAttribute(Attributes.top.getName());
    this.bottom = getAttribute(Attributes.bottom.getName());
    this.paddingLeft = getAttribute(Attributes.paddingLeft.getName());
    this.paddingRight = getAttribute(Attributes.paddingRight.getName());
    this.paddingTop = getAttribute(Attributes.paddingTop.getName());
    this.paddingBottom = getAttribute(Attributes.paddingBottom.getName());
    this.marginLeft = getAttribute(Attributes.marginLeft.getName());
    this.marginRight = getAttribute(Attributes.marginRight.getName());
    this.marginTop = getAttribute(Attributes.marginTop.getName());
    this.marginBottom = getAttribute(Attributes.marginBottom.getName());
    this.overflowX = getAttribute(Attributes.overflowX.getName());
    this.overflowY = getAttribute(Attributes.overflowY.getName());
    this.display = getAttribute(Attributes.display.getName());
    this.position = getAttribute(Attributes.position.getName());
    this.textAlign = getAttribute(Attributes.textAlign.getName());
  }

  @Override
  public void apply(final FaceletContext faceletContext, final UIComponent parent) throws ELException {

    boolean renderedSet = getAttribute(Attributes.rendered.getName()) != null;
    if (renderedSet) {
      Deprecation.LOG.warn("Attribute 'rendered' is deprecated for tc:style.");
    }
    boolean rendered = !renderedSet || getAttribute(Attributes.rendered.getName()).getBoolean(faceletContext);

    if (ComponentHandler.isNew(parent) && rendered) {

      // file
      if (file != null) {
        final String value = file.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          FacesContextUtils.addStyleFile(faceletContext.getFacesContext(), value);
        }
      }

      // custom class
      if (customClass != null) {
        final CustomClass customClassAttribute;
        if (customClass.isLiteral()) {
          final String value = customClass.getValue(faceletContext);
          if (StringUtils.isNotBlank(value)) {
            customClassAttribute = new CustomClass(value);
          } else {
            customClassAttribute = null;
          }
        } else {
          customClassAttribute = new CustomClass(customClass.getValueExpression(faceletContext, Object.class));
        }
        ((Visual) parent).setCustomClass(customClassAttribute);
      }

      // style
      Style style = null;
      style = applyAttributes1(faceletContext, style);
      style = applyAttributes2(faceletContext, style);

      // XXX the applyAttributes1() and applyAttributes2() methods are only, because the checkstyle check requires
      // XXX methods smaller than 250 lines. A better resolution might be possible with Java 8 lambdas.

      if (style != null) {
        if (parent instanceof Visual) {
          ((Visual) parent).setStyle(style);
        } else {
          throw new TagException(tag, "Parent component is not of type Visual, so no style can be set!");
        }
      }
    }
  }

  private Style applyAttributes1(final FaceletContext faceletContext, final Style parameter) {

    Style style = parameter;

    if (width != null) {
      if (width.isLiteral()) {
        final String value = width.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setWidth(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setWidth(width.getValueExpression(faceletContext, Object.class));
      }
    }

    if (height != null) {
      if (height.isLiteral()) {
        final String value = height.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setHeight(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setHeight(height.getValueExpression(faceletContext, Object.class));
      }
    }

    if (minWidth != null) {
      if (minWidth.isLiteral()) {
        final String value = minWidth.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setMinWidth(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setMinWidth(minWidth.getValueExpression(faceletContext, Object.class));
      }
    }

    if (minHeight != null) {
      if (minHeight.isLiteral()) {
        final String value = minHeight.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setMinHeight(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setMinHeight(minHeight.getValueExpression(faceletContext, Object.class));
      }
    }

    if (maxWidth != null) {
      if (maxWidth.isLiteral()) {
        final String value = maxWidth.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setMaxWidth(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setMaxWidth(maxWidth.getValueExpression(faceletContext, Object.class));
      }
    }

    if (maxHeight != null) {
      if (maxHeight.isLiteral()) {
        final String value = maxHeight.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setMaxHeight(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setMaxHeight(maxHeight.getValueExpression(faceletContext, Object.class));
      }
    }

    if (left != null) {
      if (left.isLiteral()) {
        final String value = left.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setLeft(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setLeft(left.getValueExpression(faceletContext, Object.class));
      }
    }

    if (right != null) {
      if (right.isLiteral()) {
        final String value = right.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setRight(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setRight(right.getValueExpression(faceletContext, Object.class));
      }
    }

    if (top != null) {
      if (top.isLiteral()) {
        final String value = top.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setTop(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setTop(top.getValueExpression(faceletContext, Object.class));
      }
    }

    if (bottom != null) {
      if (bottom.isLiteral()) {
        final String value = bottom.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setBottom(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setBottom(bottom.getValueExpression(faceletContext, Object.class));
      }
    }

    if (paddingLeft != null) {
      if (paddingLeft.isLiteral()) {
        final String value = paddingLeft.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setPaddingLeft(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setPaddingLeft(paddingLeft.getValueExpression(faceletContext, Object.class));
      }
    }

    if (paddingRight != null) {
      if (paddingRight.isLiteral()) {
        final String value = paddingRight.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setPaddingRight(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setPaddingRight(paddingRight.getValueExpression(faceletContext, Object.class));
      }
    }

    if (paddingTop != null) {
      if (paddingTop.isLiteral()) {
        final String value = paddingTop.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setPaddingTop(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setPaddingTop(paddingTop.getValueExpression(faceletContext, Object.class));
      }
    }

    if (paddingBottom != null) {
      if (paddingBottom.isLiteral()) {
        final String value = paddingBottom.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setPaddingBottom(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setPaddingBottom(paddingBottom.getValueExpression(faceletContext, Object.class));
      }
    }
    return style;
  }

  private Style applyAttributes2(FaceletContext faceletContext, Style parameter) {

    Style style = parameter;

    if (marginLeft != null) {
      if (marginLeft.isLiteral()) {
        final String value = marginLeft.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setMarginLeft(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setMarginLeft(marginLeft.getValueExpression(faceletContext, Object.class));
      }
    }

    if (marginRight != null) {
      if (marginRight.isLiteral()) {
        final String value = marginRight.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setMarginRight(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setMarginRight(marginRight.getValueExpression(faceletContext, Object.class));
      }
    }

    if (marginTop != null) {
      if (marginTop.isLiteral()) {
        final String value = marginTop.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setMarginTop(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setMarginTop(marginTop.getValueExpression(faceletContext, Object.class));
      }
    }

    if (marginBottom != null) {
      if (marginBottom.isLiteral()) {
        final String value = marginBottom.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setMarginBottom(Measure.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setMarginBottom(marginBottom.getValueExpression(faceletContext, Object.class));
      }
    }

    if (overflowX != null) {
      if (overflowX.isLiteral()) {
        final String value = overflowX.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setOverflowX(Overflow.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setOverflowX(overflowX.getValueExpression(faceletContext, Object.class));
      }
    }

    if (overflowY != null) {
      if (overflowY.isLiteral()) {
        final String value = overflowY.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setOverflowY(Overflow.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setOverflowY(overflowY.getValueExpression(faceletContext, Object.class));
      }
    }

    if (display != null) {
      if (display.isLiteral()) {
        final String value = display.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setDisplay(Display.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setDisplay(display.getValueExpression(faceletContext, Object.class));
      }
    }

    if (position != null) {
      if (position.isLiteral()) {
        final String value = position.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setPosition(Position.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setPosition(position.getValueExpression(faceletContext, Object.class));
      }
    }

    if (textAlign != null) {
      if (textAlign.isLiteral()) {
        final String value = textAlign.getValue(faceletContext);
        if (StringUtils.isNotBlank(value)) {
          if (style == null) {
            style = new Style();
          }
          style.setTextAlign(TextAlign.valueOf(value));
        }
      } else {
        if (style == null) {
          style = new Style();
        }
        style.setTextAlign(textAlign.getValueExpression(faceletContext, Object.class));
      }
    }
    return style;
  }
}
