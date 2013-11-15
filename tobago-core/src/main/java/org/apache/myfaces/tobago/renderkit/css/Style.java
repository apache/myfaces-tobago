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

package org.apache.myfaces.tobago.renderkit.css;

import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.TextAlign;

import javax.faces.context.FacesContext;
import java.io.Serializable;

public class Style implements Serializable {
                                          
  private static final long serialVersionUID = 4L;

  private Measure width;
  private Measure height;
  private Measure left;
  private Measure top;
  private Display display;
  private Position position;
  private Overflow overflowX;
  private Overflow overflowY;
  private Measure marginLeft;
  private Measure marginRight;
  private Measure marginTop;
  private Measure marginBottom;
  private Measure margin;
  private Measure paddingLeft;
  private Measure paddingRight;
  private Measure paddingTop;
  private Measure paddingBottom;
  private Measure padding;
  private String backgroundImage;
  private Integer zIndex;
  private TextAlign textAlign;

  public Style() {
  }

  public Style(final Style map) {
    this.width = map.width;
    this.height = map.height;
    this.left = map.left;
    this.top = map.top;
    this.display = map.display;
    this.position = map.position;
    this.overflowX = map.overflowX;
    this.overflowY = map.overflowY;
    this.marginLeft = map.marginLeft;
    this.marginRight = map.marginRight;
    this.marginTop = map.marginTop;
    this.marginBottom = map.marginBottom;
    this.margin = map.margin;
    this.paddingLeft = map.paddingLeft;
    this.paddingRight = map.paddingRight;
    this.paddingTop = map.paddingTop;
    this.paddingBottom = map.paddingBottom;
    this.padding = map.padding;
    this.backgroundImage = map.backgroundImage;
    this.zIndex = map.zIndex;
    this.textAlign = map.textAlign;
  }

  public Style(final FacesContext facesContext, final LayoutBase layout) {

    final String rendererType = layout.getRendererType();
    
    width = layout.getCurrentWidth();
    if (width != null) {
      // TODO: Make configurable: this is needed if the box-sizing is border-box, not content-box (see CSS3)
      width = width.subtractNotNegative(
          ResourceManagerUtils.getThemeMeasure(facesContext, layout, "css.border-left-width"));
      width = width.subtractNotNegative(
          ResourceManagerUtils.getThemeMeasure(facesContext, layout, "css.padding-left"));
      width = width.subtractNotNegative(
          ResourceManagerUtils.getThemeMeasure(facesContext, layout, "css.padding-right"));
      width = width.subtractNotNegative(
          ResourceManagerUtils.getThemeMeasure(facesContext, layout, "css.border-right-width"));
    }
    height = layout.getCurrentHeight();
    if (height != null) {
      // TODO: Make configurable: this is needed if the box-sizing is border-box, not content-box (see CSS3)
      height = height.subtractNotNegative(
          ResourceManagerUtils.getThemeMeasure(facesContext, layout, "css.border-top-width"));
      height = height.subtractNotNegative(
          ResourceManagerUtils.getThemeMeasure(facesContext, layout, "css.padding-top"));
      height = height.subtractNotNegative(
          ResourceManagerUtils.getThemeMeasure(facesContext, layout, "css.padding-bottom"));
      height = height.subtractNotNegative(
          ResourceManagerUtils.getThemeMeasure(facesContext, layout, "css.border-bottom-width"));
    }
    this.left = layout.getLeft();
    this.top = layout.getTop();

    // if there are a position coordinates, activate absolute positioning
    // XXX String "Page" is not nice here
    if ((left != null || top != null) && !rendererType.contains("Page")) {
      position = Position.ABSOLUTE;
    }

    if (layout instanceof LayoutComponent) { // fixme
      display = ((LayoutComponent) layout).getDisplay();
    }

    if (layout instanceof LayoutContainer) {
      overflowX = ((LayoutContainer) layout).isOverflowX() ? Overflow.AUTO : null;
      overflowY = ((LayoutContainer) layout).isOverflowY() ? Overflow.AUTO : null;
    }
  }

  /**
   * Checks if the encode string holds free text, which must be escaped.
   * This is the case for image URLs.
   * For {@link Measure}, and enum types like {@link Display} no escaping is needed.
   */
  public boolean needsToBeEscaped() {
    return backgroundImage != null;
  }

  public String encode() {
    final StringBuilder buf = new StringBuilder();
    if (width != null) {
      buf.append("width:");
      buf.append(width.serialize());
      buf.append(';');
    }
    if (height != null) {
      buf.append("height:");
      buf.append(height.serialize());
      buf.append(';');
    }
    if (top != null) {
      buf.append("top:");
      buf.append(top.serialize());
      buf.append(';');
    }
    if (left != null) {
      buf.append("left:");
      buf.append(left.serialize());
      buf.append(';');
    }
    if (display != null) {
      buf.append("display:");
      buf.append(display.getValue());
      buf.append(';');
    }
    if (position != null) {
      buf.append("position:");
      buf.append(position.getValue());
      buf.append(';');
    }
    if (overflowX != null) {
      buf.append("overflow-x:");
      buf.append(overflowX.getValue());
      buf.append(';');
    }
    if (overflowY != null) {
      buf.append("overflow-y:");
      buf.append(overflowY.getValue());
      buf.append(';');
    }
    if (marginLeft != null) {
      buf.append("margin-left:");
      buf.append(marginLeft.serialize());
      buf.append(';');
    }
    if (marginRight != null) {
      buf.append("margin-right:");
      buf.append(marginRight.serialize());
      buf.append(';');
    }
    if (marginTop != null) {
      buf.append("margin-top:");
      buf.append(marginTop.serialize());
      buf.append(';');
    }
    if (marginBottom != null) {
      buf.append("margin-bottom:");
      buf.append(marginBottom.serialize());
      buf.append(';');
    }
    if (margin != null) {
      buf.append("margin:");
      buf.append(margin.serialize());
      buf.append(';');
    }
    if (paddingLeft != null) {
      buf.append("padding-left:");
      buf.append(paddingLeft.serialize());
      buf.append(';');
    }
    if (paddingRight != null) {
      buf.append("padding-right:");
      buf.append(paddingRight.serialize());
      buf.append(';');
    }
    if (paddingTop != null) {
      buf.append("padding-top:");
      buf.append(paddingTop.serialize());
      buf.append(';');
    }
    if (paddingBottom != null) {
      buf.append("padding-bottom:");
      buf.append(paddingBottom.serialize());
      buf.append(';');
    }
    if (padding != null) {
      buf.append("padding:");
      buf.append(padding.serialize());
      buf.append(';');
    }
    if (backgroundImage != null) {
      buf.append("background-image:");
      buf.append(backgroundImage);
      buf.append(';');
    }
    if (zIndex != null) {
      buf.append("z-index:");
      buf.append(zIndex);
      buf.append(';');
    }
    if (textAlign != null) {
      buf.append("text-align:");
      buf.append(textAlign.getValue());
      buf.append(';');
    }

    return buf.toString();
  }

  public String encodeJson() {
    final StringBuilder buf = new StringBuilder("{");
    if (width != null) {
      buf.append("\"width\":\"");
      buf.append(width.serialize());
      buf.append("\",");
    }
    if (height != null) {
      buf.append("\"height\":\"");
      buf.append(height.serialize());
      buf.append("\",");
    }
    if (top != null) {
      buf.append("\"top\":\"");
      buf.append(top.serialize());
      buf.append("\",");
    }
    if (left != null) {
      buf.append("\"left\":\"");
      buf.append(left.serialize());
      buf.append("\",");
    }
    if (display != null) {
      buf.append("\"display\":\"");
      buf.append(display.getValue());
      buf.append("\",");
    }
    if (position != null) {
      buf.append("\"position\":\"");
      buf.append(position.getValue());
      buf.append("\",");
    }
    if (overflowX != null) {
      buf.append("\"overflowX\":\"");
      buf.append(overflowX.getValue());
      buf.append("\",");
    }
    if (overflowY != null) {
      buf.append("\"overflowY\":\"");
      buf.append(overflowY.getValue());
      buf.append("\",");
    }
    if (marginLeft != null) {
      buf.append("\"marginLeft\":\"");
      buf.append(marginLeft.serialize());
      buf.append("\",");
    }
    if (marginRight != null) {
      buf.append("\"marginRight\":\"");
      buf.append(marginRight.serialize());
      buf.append("\",");
    }
    if (marginTop != null) {
      buf.append("\"marginTop\":\"");
      buf.append(marginTop.serialize());
      buf.append("\",");
    }
    if (marginBottom != null) {
      buf.append("\"marginBottom\":\"");
      buf.append(marginBottom.serialize());
      buf.append("\",");
    }
    if (margin != null) {
      buf.append("\"margin\":\"");
      buf.append(margin.serialize());
      buf.append("\",");
    }
    if (paddingLeft != null) {
      buf.append("\"paddingLeft\":\"");
      buf.append(paddingLeft.serialize());
      buf.append("\",");
    }
    if (paddingRight != null) {
      buf.append("\"paddingRight\":\"");
      buf.append(paddingRight.serialize());
      buf.append("\",");
    }
    if (paddingTop != null) {
      buf.append("\"paddingTop\":\"");
      buf.append(paddingTop.serialize());
      buf.append("\",");
    }
    if (paddingBottom != null) {
      buf.append("\"paddingBottom\":\"");
      buf.append(paddingBottom.serialize());
      buf.append("\",");
    }
    if (padding != null) {
      buf.append("\"padding\":\"");
      buf.append(padding.serialize());
      buf.append("\",");
    }
    if (backgroundImage != null) {
      buf.append("\"backgroundImage\":\"");
      buf.append(backgroundImage);
      buf.append("\",");
    }
    if (zIndex != null) {
      buf.append("\"zIndex\":");
      buf.append(zIndex);
      buf.append(";");
    }
    if (textAlign != null) {
      buf.append("\"textAlign\":\"");
      buf.append(textAlign.getValue());
      buf.append("\",");
    }

    if (buf.length() > 1) {
      buf.deleteCharAt(buf.length() - 1);
    }

    buf.append('}');
    return buf.toString();
  }

  public Measure getWidth() {
    return width;
  }

  public void setWidth(final Measure width) {
    this.width = width;
  }

  public Measure getHeight() {
    return height;
  }

  public void setHeight(final Measure height) {
    this.height = height;
  }

  public Measure getLeft() {
    return left;
  }

  public void setLeft(final Measure left) {
    this.left = left;
  }

  public Measure getTop() {
    return top;
  }

  public void setTop(final Measure top) {
    this.top = top;
  }

  public Display getDisplay() {
    return display;
  }

  public void setDisplay(final Display display) {
    this.display = display;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(final Position position) {
    this.position = position;
  }

  public Overflow getOverflowX() {
    return overflowX;
  }

  public void setOverflowX(final Overflow overflowX) {
    this.overflowX = overflowX;
  }

  public Overflow getOverflowY() {
    return overflowY;
  }

  public void setOverflowY(final Overflow overflowY) {
    this.overflowY = overflowY;
  }

  public Measure getMarginLeft() {
    return marginLeft;
  }

  public void setMarginLeft(final Measure marginLeft) {
    this.marginLeft = marginLeft;
  }

  public Measure getMarginRight() {
    return marginRight;
  }

  public void setMarginRight(final Measure marginRight) {
    this.marginRight = marginRight;
  }

  public Measure getMarginTop() {
    return marginTop;
  }

  public void setMarginTop(final Measure marginTop) {
    this.marginTop = marginTop;
  }

  public Measure getMarginBottom() {
    return marginBottom;
  }

  public void setMarginBottom(final Measure marginBottom) {
    this.marginBottom = marginBottom;
  }

  public Measure getMargin() {
    return margin;
  }

  public void setMargin(final Measure margin) {
    this.margin = margin;
  }

  public Measure getPaddingLeft() {
    return paddingLeft;
  }

  public void setPaddingLeft(final Measure paddingLeft) {
    this.paddingLeft = paddingLeft;
  }

  public Measure getPaddingRight() {
    return paddingRight;
  }

  public void setPaddingRight(final Measure paddingRight) {
    this.paddingRight = paddingRight;
  }

  public Measure getPaddingTop() {
    return paddingTop;
  }

  public void setPaddingTop(final Measure paddingTop) {
    this.paddingTop = paddingTop;
  }

  public Measure getPaddingBottom() {
    return paddingBottom;
  }

  public void setPaddingBottom(final Measure paddingBottom) {
    this.paddingBottom = paddingBottom;
  }

  public Measure getPadding() {
    return padding;
  }

  public void setPadding(final Measure padding) {
    this.padding = padding;
  }

  public String getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(final String backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public Integer getZIndex() {
    return zIndex;
  }

  public void setZIndex(final Integer zIndex) {
    this.zIndex = zIndex;
  }

  public TextAlign getTextAlign() {
    return textAlign;
  }

  public void setTextAlign(final TextAlign textAlign) {
    this.textAlign = textAlign;
  }
}
