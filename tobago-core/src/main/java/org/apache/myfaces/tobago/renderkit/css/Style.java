package org.apache.myfaces.tobago.renderkit.css;

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

import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.Measure;

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
  private Overflow overflow;
  private Measure marginTop;
  private Measure margin;
  private Measure paddingTop;
  private Measure paddingBottom;
  private Measure padding;
  private String backgroundImage;
  private Integer zIndex;
  private String textAlign;

  public Style() {
  }

  public Style(Style map) {
    this.width = map.width;
    this.height = map.height;
    this.left = map.left;
    this.top = map.top;
    this.display = map.display;
    this.position = map.position;
    this.overflow = map.overflow;
    this.marginTop = map.marginTop;
    this.margin = map.margin;
    this.paddingTop = map.paddingTop;
    this.paddingBottom = map.paddingBottom;
    this.padding = map.padding;
    this.backgroundImage = map.backgroundImage;
    this.zIndex = map.zIndex;
    this.textAlign = map.textAlign;
  }

  public Style(FacesContext facesContext, LayoutBase layout) {

    String rendererType = layout.getRendererType();
    
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
  }

  public String encode() {
    StringBuilder buf = new StringBuilder();
    if (width != null) {
      buf.append("width:");
      buf.append(width);
      buf.append(';');
    }
    if (height != null) {
      buf.append("height:");
      buf.append(height);
      buf.append(';');
    }
    if (top != null) {
      buf.append("top:");
      buf.append(top);
      buf.append(';');
    }
    if (left != null) {
      buf.append("left:");
      buf.append(left);
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
    if (overflow != null) {
      buf.append("overflow:");
      buf.append(overflow.getValue());
      buf.append(';');
    }
    if (marginTop != null) {
      buf.append("margin-top:");
      buf.append(marginTop);
      buf.append(';');
    }
    if (margin != null) {
      buf.append("margin:");
      buf.append(margin);
      buf.append(';');
    }
    if (paddingTop != null) {
      buf.append("padding-top:");
      buf.append(paddingTop);
      buf.append(';');
    }
    if (paddingBottom != null) {
      buf.append("padding-bottom:");
      buf.append(paddingBottom);
      buf.append(';');
    }
    if (padding != null) {
      buf.append("padding:");
      buf.append(padding);
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
      buf.append(textAlign);
      buf.append(';');
    }

    return buf.toString();
  }

  public Measure getWidth() {
    return width;
  }

  public void setWidth(Measure width) {
    this.width = width;
  }

  public Measure getHeight() {
    return height;
  }

  public void setHeight(Measure height) {
    this.height = height;
  }

  public Measure getLeft() {
    return left;
  }

  public void setLeft(Measure left) {
    this.left = left;
  }

  public Measure getTop() {
    return top;
  }

  public void setTop(Measure top) {
    this.top = top;
  }

  public Display getDisplay() {
    return display;
  }

  public void setDisplay(Display display) {
    this.display = display;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public Overflow getOverflow() {
    return overflow;
  }

  public void setOverflow(Overflow overflow) {
    this.overflow = overflow;
  }

  public Measure getMarginTop() {
    return marginTop;
  }

  public void setMarginTop(Measure marginTop) {
    this.marginTop = marginTop;
  }

  public Measure getMargin() {
    return margin;
  }

  public void setMargin(Measure margin) {
    this.margin = margin;
  }

  public Measure getPaddingTop() {
    return paddingTop;
  }

  public void setPaddingTop(Measure paddingTop) {
    this.paddingTop = paddingTop;
  }

  public Measure getPaddingBottom() {
    return paddingBottom;
  }

  public void setPaddingBottom(Measure paddingBottom) {
    this.paddingBottom = paddingBottom;
  }

  public Measure getPadding() {
    return padding;
  }

  public void setPadding(Measure padding) {
    this.padding = padding;
  }

  public String getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(String backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public Integer getZIndex() {
    return zIndex;
  }

  public void setZIndex(Integer zIndex) {
    this.zIndex = zIndex;
  }

  public String getTextAlign() {
    return textAlign;
  }

  public void setTextAlign(String textAlign) {
    this.textAlign = textAlign;
  }
}
