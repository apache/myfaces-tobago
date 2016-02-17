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

import org.apache.myfaces.tobago.component.UIStyle;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Overflow;
import org.apache.myfaces.tobago.layout.Position;
import org.apache.myfaces.tobago.layout.TextAlign;

import java.io.Serializable;

/**
 * A subset of the CSS style used by Tobago. It's more or less the layout specific part.
 */
public class Style implements Serializable {

  private static final long serialVersionUID = 5L;

  private Measure width;
  private Measure height;

  private Measure minWidth;
  private Measure minHeight;
  private Measure maxWidth;
  private Measure maxHeight;

  private Measure left;
  private Measure right;
  private Measure top;
  private Measure bottom;

  private Measure paddingLeft;
  private Measure paddingRight;
  private Measure paddingTop;
  private Measure paddingBottom;

  private Measure marginLeft;
  private Measure marginRight;
  private Measure marginTop;
  private Measure marginBottom;

  private Overflow overflowX;
  private Overflow overflowY;
  private Display display;
  private Position position;

  private String backgroundImage;
  private String backgroundPosition;
  private Integer zIndex; // TBD
  private TextAlign textAlign;

  private Boolean empty;

  public Style() {
  }

  public Style(final UIStyle style) {

    width = style.getWidth();
    height = style.getHeight();

    minWidth = style.getMinWidth();
    minHeight = style.getMinHeight();
    maxWidth = style.getMaxWidth();
    maxHeight = style.getMaxHeight();

    left = style.getLeft();
    right = style.getRight();
    top = style.getTop();
    bottom = style.getBottom();

    paddingLeft = style.getPaddingLeft();
    paddingRight = style.getPaddingRight();
    paddingTop = style.getPaddingTop();
    paddingBottom = style.getPaddingBottom();

    marginLeft = style.getMarginLeft();
    marginRight = style.getMarginRight();
    marginTop = style.getMarginTop();
    marginBottom = style.getMarginBottom();

    overflowX = style.getOverflowX();
    overflowY = style.getOverflowY();
    display = style.getDisplay();
    position = style.getPosition();

    // tbd: backgroundImage from UIStyle?
    // tbd: backgroundPosition from UIStyle?
    // tbd: zIndex from UIStyle?
    textAlign = style.getTextAlign();

    checkEmptiness();
  }

  /**
   * Checks if the encode string holds free text, which must be escaped.
   * This is the case for image URLs.
   * For {@link Measure}, and enum types like {@link Display} no escaping is needed.
   */
  public boolean needsToBeEscaped() {
    return backgroundImage != null;
  }

  public boolean isEmpty() {
    if (empty == null) {
      checkEmptiness();
    }
    return empty;
  }

  public void checkEmptiness() {
    empty
        = width == null
        && height == null

        && minWidth == null
        && minHeight == null
        && maxWidth == null
        && maxHeight == null

        && left == null
        && right == null
        && top == null
        && bottom == null

        && marginLeft == null
        && marginRight == null
        && marginTop == null
        && marginBottom == null

        && paddingLeft == null
        && paddingRight == null
        && paddingTop == null
        && paddingBottom == null

        && display == null
        && position == null
        && overflowX == null
        && overflowY == null

        && backgroundImage == null
        && backgroundPosition == null
        && zIndex == null
        && textAlign == null;
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
    if (minWidth != null) {
      buf.append("min-width:");
      buf.append(minWidth.serialize());
      buf.append(';');
    }
    if (minHeight != null) {
      buf.append("min-height:");
      buf.append(minHeight.serialize());
      buf.append(';');
    }
    if (maxWidth != null) {
      buf.append("max-width:");
      buf.append(maxWidth.serialize());
      buf.append(';');
    }
    if (maxHeight != null) {
      buf.append("max-height:");
      buf.append(maxHeight.serialize());
      buf.append(';');
    }
    if (left != null) {
      buf.append("left:");
      buf.append(left.serialize());
      buf.append(';');
    }
    if (right != null) {
      buf.append("right:");
      buf.append(right.serialize());
      buf.append(';');
    }
    if (top != null) {
      buf.append("top:");
      buf.append(top.serialize());
      buf.append(';');
    }
    if (bottom != null) {
      buf.append("bottom:");
      buf.append(bottom.serialize());
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
    if (display != null) {
      buf.append("display:");
      buf.append(display.name());
      buf.append(';');
    }
    if (position != null) {
      buf.append("position:");
      buf.append(position.getValue());
      buf.append(';');
    }
    if (backgroundImage != null) {
      buf.append("background-image:");
      buf.append(backgroundImage);
      buf.append(';');
    }
    if (backgroundPosition != null) {
      buf.append("background-position:");
      buf.append(backgroundPosition);
      buf.append(';');
    }
    if (zIndex != null) {
      buf.append("z-index:");
      buf.append(zIndex);
      buf.append(';');
    }
    if (textAlign != null) {
      buf.append("text-align:");
      buf.append(textAlign.name());
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
    if (minWidth != null) {
      buf.append("\"minWidth\":\"");
      buf.append(minWidth.serialize());
      buf.append("\",");
    }
    if (minHeight != null) {
      buf.append("\"minHeight\":\"");
      buf.append(minHeight.serialize());
      buf.append("\",");
    }
    if (maxWidth != null) {
      buf.append("\"maxWidth\":\"");
      buf.append(maxWidth.serialize());
      buf.append("\",");
    }
    if (maxHeight != null) {
      buf.append("\"maxHeight\":\"");
      buf.append(maxHeight.serialize());
      buf.append("\",");
    }
    if (left != null) {
      buf.append("\"left\":\"");
      buf.append(left.serialize());
      buf.append("\",");
    }
    if (right != null) {
      buf.append("\"right\":\"");
      buf.append(right.serialize());
      buf.append("\",");
    }
    if (top != null) {
      buf.append("\"top\":\"");
      buf.append(top.serialize());
      buf.append("\",");
    }
    if (bottom != null) {
      buf.append("\"bottom\":\"");
      buf.append(bottom.serialize());
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
    if (display != null) {
      buf.append("\"display\":\"");
      buf.append(display.name());
      buf.append("\",");
    }
    if (position != null) {
      buf.append("\"position\":\"");
      buf.append(position.getValue());
      buf.append("\",");
    }
    if (backgroundImage != null) {
      buf.append("\"backgroundImage\":\"");
      buf.append(backgroundImage);
      buf.append("\",");
    }
    if (backgroundPosition != null) {
      buf.append("\"backgroundPosition\":\"");
      buf.append(backgroundPosition);
      buf.append("\",");
    }
    if (zIndex != null) {
      buf.append("\"zIndex\":");
      buf.append(zIndex);
      buf.append(",");
    }
    if (textAlign != null) {
      buf.append("\"textAlign\":\"");
      buf.append(textAlign.name());
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
    empty = null;
    this.width = width;
  }

  public Measure getHeight() {
    return height;
  }

  public void setHeight(final Measure height) {
    empty = null;
    this.height = height;
  }

  public Measure getMinWidth() {
    return minWidth;
  }

  public void setMinWidth(Measure minWidth) {
    empty = null;
    this.minWidth = minWidth;
  }

  public Measure getMinHeight() {
    return minHeight;
  }

  public void setMinHeight(Measure minHeight) {
    empty = null;
    this.minHeight = minHeight;
  }

  public Measure getMaxWidth() {
    return maxWidth;
  }

  public void setMaxWidth(Measure maxWidth) {
    empty = null;
    this.maxWidth = maxWidth;
  }

  public Measure getMaxHeight() {
    return maxHeight;
  }

  public void setMaxHeight(Measure maxHeight) {
    empty = null;
    this.maxHeight = maxHeight;
  }

  public Measure getLeft() {
    return left;
  }

  public void setLeft(final Measure left) {
    empty = null;
    this.left = left;
  }

  public Measure getRight() {
    return right;
  }

  public void setRight(Measure right) {
    empty = null;
    this.right = right;
  }

  public Measure getTop() {
    return top;
  }

  public void setTop(final Measure top) {
    empty = null;
    this.top = top;
  }

  public Measure getBottom() {
    return bottom;
  }

  public void setBottom(Measure bottom) {
    empty = null;
    this.bottom = bottom;
  }

  public Display getDisplay() {
    return display;
  }

  public void setDisplay(final Display display) {
    empty = null;
    this.display = display;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(final Position position) {
    empty = null;
    this.position = position;
  }

  public Overflow getOverflowX() {
    return overflowX;
  }

  public void setOverflowX(final Overflow overflowX) {
    empty = null;
    this.overflowX = overflowX;
  }

  public Overflow getOverflowY() {
    return overflowY;
  }

  public void setOverflowY(final Overflow overflowY) {
    empty = null;
    this.overflowY = overflowY;
  }

  public Measure getMarginLeft() {
    return marginLeft;
  }

  public void setMarginLeft(final Measure marginLeft) {
    empty = null;
    this.marginLeft = marginLeft;
  }

  public Measure getMarginRight() {
    return marginRight;
  }

  public void setMarginRight(final Measure marginRight) {
    empty = null;
    this.marginRight = marginRight;
  }

  public Measure getMarginTop() {
    return marginTop;
  }

  public void setMarginTop(final Measure marginTop) {
    empty = null;
    this.marginTop = marginTop;
  }

  public Measure getMarginBottom() {
    return marginBottom;
  }

  public void setMarginBottom(final Measure marginBottom) {
    empty = null;
    this.marginBottom = marginBottom;
  }

  public Measure getPaddingLeft() {
    return paddingLeft;
  }

  public void setPaddingLeft(final Measure paddingLeft) {
    empty = null;
    this.paddingLeft = paddingLeft;
  }

  public Measure getPaddingRight() {
    return paddingRight;
  }

  public void setPaddingRight(final Measure paddingRight) {
    empty = null;
    this.paddingRight = paddingRight;
  }

  public Measure getPaddingTop() {
    return paddingTop;
  }

  public void setPaddingTop(final Measure paddingTop) {
    empty = null;
    this.paddingTop = paddingTop;
  }

  public Measure getPaddingBottom() {
    return paddingBottom;
  }

  public void setPaddingBottom(final Measure paddingBottom) {
    empty = null;
    this.paddingBottom = paddingBottom;
  }

  public String getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(String backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public String getBackgroundPosition() {
    return backgroundPosition;
  }

  public void setBackgroundPosition(String backgroundPosition) {
    this.backgroundPosition = backgroundPosition;
  }

  public Integer getZIndex() {
    return zIndex;
  }

  public void setZIndex(final Integer zIndex) {
    empty = null;
    this.zIndex = zIndex;
  }

  public TextAlign getTextAlign() {
    return textAlign;
  }

  public void setTextAlign(final TextAlign textAlign) {
    empty = null;
    this.textAlign = textAlign;
  }

  @Override
  public String toString() {
    return encode();
  }
}
