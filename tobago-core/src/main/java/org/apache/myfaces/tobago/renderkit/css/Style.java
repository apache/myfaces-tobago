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

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * A subset of the CSS style used by Tobago. It's more or less the layout specific part.
 *
 * @deprecated since 4.0.0. UIStyle now holds the data and the StyleRenderer renders.
 */
@Deprecated
public class Style implements Serializable {

  private static final long serialVersionUID = 6L;

  private Measure width;
  private ValueExpression widthVE;
  private Measure height;
  private ValueExpression heightVE;

  private Measure minWidth;
  private ValueExpression minWidthVE;
  private Measure minHeight;
  private ValueExpression minHeightVE;
  private Measure maxWidth;
  private ValueExpression maxWidthVE;
  private Measure maxHeight;
  private ValueExpression maxHeightVE;

  private Measure left;
  private ValueExpression leftVE;
  private Measure right;
  private ValueExpression rightVE;
  private Measure top;
  private ValueExpression topVE;
  private Measure bottom;
  private ValueExpression bottomVE;

  private Measure paddingLeft;
  private ValueExpression paddingLeftVE;
  private Measure paddingRight;
  private ValueExpression paddingRightVE;
  private Measure paddingTop;
  private ValueExpression paddingTopVE;
  private Measure paddingBottom;
  private ValueExpression paddingBottomVE;

  private Measure marginLeft;
  private ValueExpression marginLeftVE;
  private Measure marginRight;
  private ValueExpression marginRightVE;
  private Measure marginTop;
  private ValueExpression marginTopVE;
  private Measure marginBottom;
  private ValueExpression marginBottomVE;

  private Overflow overflowX;
  private ValueExpression overflowXVE;
  private Overflow overflowY;
  private ValueExpression overflowYVE;
  private Display display;
  private ValueExpression displayVE;
  private Position position;
  private ValueExpression positionVE;

  // tbd
  private String backgroundImage;
  // tbd
  private ValueExpression backgroundImageVE;
  // tbd
  private String backgroundPosition;
  // tbd
  private ValueExpression backgroundPositionVE;
  // tbd
  private Integer zIndex;
  // tbd
  private ValueExpression zIndexVE;
  private TextAlign textAlign;
  private ValueExpression textAlignVE;

  private Boolean empty;

  public Style() {
  }

  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
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
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ELContext elContext = facesContext.getELContext();
    final StringBuilder buf = new StringBuilder();

    if (widthVE != null) {
      buf.append("width:");
      buf.append(Measure.valueOf(widthVE.getValue(elContext)).serialize());
      buf.append(';');
    } else if (width != null) {
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
      buf.append(overflowX.name());
      buf.append(';');
    }
    if (overflowY != null) {
      buf.append("overflow-y:");
      buf.append(overflowY.name());
      buf.append(';');
    }
    if (display != null) {
      buf.append("display:");
      buf.append(display.name());
      buf.append(';');
    }
    if (position != null) {
      buf.append("position:");
      buf.append(position.name());
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

    final Measure widthValue = getWidth();
    if (widthValue != null) {
      buf.append("\"width\":\"");
      buf.append(widthValue.serialize());
      buf.append("\",");
    }
    final Measure heightValue = getHeight();
    if (heightValue != null) {
      buf.append("\"height\":\"");
      buf.append(heightValue.serialize());
      buf.append("\",");
    }
    final Measure minWidthValue = getMinWidth();
    if (minWidthValue != null) {
      buf.append("\"minWidth\":\"");
      buf.append(minWidthValue.serialize());
      buf.append("\",");
    }
    final Measure minHeightValue = getMinHeight();
    if (minHeightValue != null) {
      buf.append("\"minHeight\":\"");
      buf.append(minHeightValue.serialize());
      buf.append("\",");
    }
    final Measure maxWidthValue = getMaxWidth();
    if (maxWidthValue != null) {
      buf.append("\"maxWidth\":\"");
      buf.append(maxWidthValue.serialize());
      buf.append("\",");
    }
    final Measure maxHeightValue = getMaxHeight();
    if (maxHeightValue != null) {
      buf.append("\"maxHeight\":\"");
      buf.append(maxHeightValue.serialize());
      buf.append("\",");
    }
    final Measure leftValue = getLeft();
    if (leftValue != null) {
      buf.append("\"left\":\"");
      buf.append(leftValue.serialize());
      buf.append("\",");
    }
    final Measure rightValue = getRight();
    if (rightValue != null) {
      buf.append("\"right\":\"");
      buf.append(rightValue.serialize());
      buf.append("\",");
    }
    final Measure topValue = getTop();
    if (topValue != null) {
      buf.append("\"top\":\"");
      buf.append(topValue.serialize());
      buf.append("\",");
    }
    final Measure bottomValue = getBottom();
    if (bottomValue != null) {
      buf.append("\"bottom\":\"");
      buf.append(bottomValue.serialize());
      buf.append("\",");
    }
    final Measure paddingLeftValue = getPaddingLeft();
    if (paddingLeftValue != null) {
      buf.append("\"paddingLeft\":\"");
      buf.append(paddingLeftValue.serialize());
      buf.append("\",");
    }
    final Measure paddingRightValue = getPaddingRight();
    if (paddingRightValue != null) {
      buf.append("\"paddingRight\":\"");
      buf.append(paddingRightValue.serialize());
      buf.append("\",");
    }
    final Measure paddingTopValue = getPaddingTop();
    if (paddingTopValue != null) {
      buf.append("\"paddingTop\":\"");
      buf.append(paddingTopValue.serialize());
      buf.append("\",");
    }
    final Measure paddingBottomValue = getPaddingBottom();
    if (paddingBottomValue != null) {
      buf.append("\"paddingBottom\":\"");
      buf.append(paddingBottomValue.serialize());
      buf.append("\",");
    }
    final Measure marginLeftValue = getMarginLeft();
    if (marginLeftValue != null) {
      buf.append("\"marginLeft\":\"");
      buf.append(marginLeftValue.serialize());
      buf.append("\",");
    }
    final Measure marginRightValue = getMarginRight();
    if (marginRightValue != null) {
      buf.append("\"marginRight\":\"");
      buf.append(marginRightValue.serialize());
      buf.append("\",");
    }
    final Measure marginTopValue = getMarginTop();
    if (marginTopValue != null) {
      buf.append("\"marginTop\":\"");
      buf.append(marginTopValue.serialize());
      buf.append("\",");
    }
    final Measure marginBottomValue = getMarginBottom();
    if (marginBottomValue != null) {
      buf.append("\"marginBottom\":\"");
      buf.append(marginBottomValue.serialize());
      buf.append("\",");
    }
    final Overflow overflowXValue = getOverflowX();
    if (overflowXValue != null) {
      buf.append("\"overflowX\":\"");
      buf.append(overflowXValue.name());
      buf.append("\",");
    }
    final Overflow overflowYValue = getOverflowY();
    if (overflowYValue != null) {
      buf.append("\"overflowY\":\"");
      buf.append(overflowYValue.name());
      buf.append("\",");
    }
    final Display displayValue = getDisplay();
    if (displayValue != null) {
      buf.append("\"display\":\"");
      buf.append(displayValue.name());
      buf.append("\",");
    }
    final Position positionValue = getPosition();
    if (positionValue != null) {
      buf.append("\"position\":\"");
      buf.append(positionValue.name());
      buf.append("\",");
    }
    // tbd
    final String backgroundImageValue = getBackgroundImage();
    if (backgroundImageValue != null) {
      buf.append("\"backgroundImage\":\"");
      buf.append(backgroundImageValue);
      buf.append("\",");
    }
    // tbd
    final String backgroundPositionValue = getBackgroundPosition();
    if (backgroundPositionValue != null) {
      buf.append("\"backgroundPosition\":\"");
      buf.append(backgroundPositionValue);
      buf.append("\",");
    }
    // tbd
    final Integer zIndexValue = getZIndex();
    if (zIndexValue != null) {
      buf.append("\"zIndex\":");
      buf.append(zIndexValue);
      buf.append(",");
    }
    final TextAlign textAlignValue = getTextAlign();
    if (textAlignValue != null) {
      buf.append("\"textAlign\":\"");
      buf.append(textAlignValue.name());
      buf.append("\",");
    }

    if (buf.length() > 1) {
      buf.deleteCharAt(buf.length() - 1);
    }

    buf.append('}');
    return buf.toString();
  }

  public Measure getWidth() {
    if (widthVE != null) {
      return Measure.valueOf(widthVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (width != null) {
      return width;
    } else {
      return null;
    }
  }

  public void setWidth(final Measure width) {
    empty = null;
    this.width = width;
  }

  public void setWidth(final ValueExpression width) {
    empty = null;
    this.widthVE = width;
  }

  public Measure getHeight() {
    if (heightVE != null) {
      return Measure.valueOf(heightVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (height != null) {
      return height;
    } else {
      return null;
    }
  }

  public void setHeight(final Measure height) {
    empty = null;
    this.height = height;
  }

  public void setHeight(final ValueExpression height) {
    empty = null;
    this.heightVE = height;
  }

  public Measure getMinWidth() {
    if (minWidthVE != null) {
      return Measure.valueOf(minWidthVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (minWidth != null) {
      return minWidth;
    } else {
      return null;
    }
  }

  public void setMinWidth(final Measure minWidth) {
    empty = null;
    this.minWidth = minWidth;
  }

  public void setMinWidth(final ValueExpression minWidth) {
    empty = null;
    this.minWidthVE = minWidth;
  }

  public Measure getMinHeight() {
    if (minHeightVE != null) {
      return Measure.valueOf(minHeightVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (minHeight != null) {
      return minHeight;
    } else {
      return null;
    }
  }

  public void setMinHeight(final Measure minHeight) {
    empty = null;
    this.minHeight = minHeight;
  }

  public void setMinHeight(final ValueExpression minHeight) {
    empty = null;
    this.minHeightVE = minHeight;
  }

  public Measure getMaxWidth() {
    if (maxWidthVE != null) {
      return Measure.valueOf(maxWidthVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (maxWidth != null) {
      return maxWidth;
    } else {
      return null;
    }
  }

  public void setMaxWidth(final Measure maxWidth) {
    empty = null;
    this.maxWidth = maxWidth;
  }

  public void setMaxWidth(final ValueExpression maxWidth) {
    empty = null;
    this.maxWidthVE = maxWidth;
  }

  public Measure getMaxHeight() {
    if (maxHeightVE != null) {
      return Measure.valueOf(maxHeightVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (maxHeight != null) {
      return maxHeight;
    } else {
      return null;
    }
  }

  public void setMaxHeight(final Measure maxHeight) {
    empty = null;
    this.maxHeight = maxHeight;
  }

  public void setMaxHeight(final ValueExpression maxHeight) {
    empty = null;
    this.maxHeightVE = maxHeight;
  }

  public Measure getLeft() {
    if (leftVE != null) {
      return Measure.valueOf(leftVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (left != null) {
      return left;
    } else {
      return null;
    }
  }

  public void setLeft(final Measure left) {
    empty = null;
    this.left = left;
  }

  public void setLeft(final ValueExpression left) {
    empty = null;
    this.leftVE = left;
  }

  public Measure getRight() {
    if (rightVE != null) {
      return Measure.valueOf(rightVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (right != null) {
      return right;
    } else {
      return null;
    }
  }

  public void setRight(final Measure right) {
    empty = null;
    this.right = right;
  }

  public void setRight(final ValueExpression right) {
    empty = null;
    this.rightVE = right;
  }

  public Measure getTop() {
    if (topVE != null) {
      return Measure.valueOf(topVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (top != null) {
      return top;
    } else {
      return null;
    }
  }

  public void setTop(final Measure top) {
    empty = null;
    this.top = top;
  }

  public void setTop(final ValueExpression top) {
    empty = null;
    this.topVE = top;
  }

  public Measure getBottom() {
    if (bottomVE != null) {
      return Measure.valueOf(bottomVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (bottom != null) {
      return bottom;
    } else {
      return null;
    }
  }

  public void setBottom(final Measure bottom) {
    empty = null;
    this.bottom = bottom;
  }

  public void setBottom(final ValueExpression bottom) {
    empty = null;
    this.bottomVE = bottom;
  }

  public Measure getPaddingLeft() {
    if (paddingLeftVE != null) {
      return Measure.valueOf(paddingLeftVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (paddingLeft != null) {
      return paddingLeft;
    } else {
      return null;
    }
  }

  public void setPaddingLeft(final Measure paddingLeft) {
    empty = null;
    this.paddingLeft = paddingLeft;
  }

  public void setPaddingLeft(final ValueExpression paddingLeft) {
    empty = null;
    this.paddingLeftVE = paddingLeft;
  }

  public Measure getPaddingRight() {
    if (paddingRightVE != null) {
      return Measure.valueOf(paddingRightVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (paddingRight != null) {
      return paddingRight;
    } else {
      return null;
    }
  }

  public void setPaddingRight(final Measure paddingRight) {
    empty = null;
    this.paddingRight = paddingRight;
  }

  public void setPaddingRight(final ValueExpression paddingRight) {
    empty = null;
    this.paddingRightVE = paddingRight;
  }

  public Measure getPaddingTop() {
    if (paddingTopVE != null) {
      return Measure.valueOf(paddingTopVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (paddingTop != null) {
      return paddingTop;
    } else {
      return null;
    }
  }

  public void setPaddingTop(final Measure paddingTop) {
    empty = null;
    this.paddingTop = paddingTop;
  }

  public void setPaddingTop(final ValueExpression paddingTop) {
    empty = null;
    this.paddingTopVE = paddingTop;
  }

  public Measure getPaddingBottom() {
    if (paddingBottomVE != null) {
      return Measure.valueOf(paddingBottomVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (paddingBottom != null) {
      return paddingBottom;
    } else {
      return null;
    }
  }

  public void setPaddingBottom(final Measure paddingBottom) {
    empty = null;
    this.paddingBottom = paddingBottom;
  }

  public void setPaddingBottom(final ValueExpression paddingBottom) {
    empty = null;
    this.paddingBottomVE = paddingBottom;
  }

  public Measure getMarginLeft() {
    if (marginLeftVE != null) {
      return Measure.valueOf(marginLeftVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (marginLeft != null) {
      return marginLeft;
    } else {
      return null;
    }
  }

  public void setMarginLeft(final Measure marginLeft) {
    empty = null;
    this.marginLeft = marginLeft;
  }

  public void setMarginLeft(final ValueExpression marginLeft) {
    empty = null;
    this.marginLeftVE = marginLeft;
  }

  public Measure getMarginRight() {
    if (marginRightVE != null) {
      return Measure.valueOf(marginRightVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (marginRight != null) {
      return marginRight;
    } else {
      return null;
    }
  }

  public void setMarginRight(final Measure marginRight) {
    empty = null;
    this.marginRight = marginRight;
  }

  public void setMarginRight(final ValueExpression marginRight) {
    empty = null;
    this.marginRightVE = marginRight;
  }

  public Measure getMarginTop() {
    if (marginTopVE != null) {
      return Measure.valueOf(marginTopVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (marginTop != null) {
      return marginTop;
    } else {
      return null;
    }
  }

  public void setMarginTop(final Measure marginTop) {
    empty = null;
    this.marginTop = marginTop;
  }

  public void setMarginTop(final ValueExpression marginTop) {
    empty = null;
    this.marginTopVE = marginTop;
  }

  public Measure getMarginBottom() {
    if (marginBottomVE != null) {
      return Measure.valueOf(marginBottomVE.getValue(FacesContext.getCurrentInstance().getELContext()));
    } else if (marginBottom != null) {
      return marginBottom;
    } else {
      return null;
    }
  }

  public void setMarginBottom(final Measure marginBottom) {
    empty = null;
    this.marginBottom = marginBottom;
  }

  public void setMarginBottom(final ValueExpression marginBottom) {
    empty = null;
    this.marginBottomVE = marginBottom;
  }

  public Overflow getOverflowX() {
    if (overflowXVE != null) {
      final Object value = overflowXVE.getValue(FacesContext.getCurrentInstance().getELContext());
      if (value instanceof Overflow) {
        return (Overflow) value;
      } else if (value != null) {
        return Overflow.valueOf(value.toString());
      } else {
        return null;
      }
    } else if (overflowX != null) {
      return overflowX;
    } else {
      return null;
    }
  }

  public void setOverflowX(final Overflow overflowX) {
    empty = null;
    this.overflowX = overflowX;
  }

  public void setOverflowX(final ValueExpression overflowX) {
    empty = null;
    this.overflowXVE = overflowX;
  }

  public Overflow getOverflowY() {
    if (overflowYVE != null) {
      final Object value = overflowYVE.getValue(FacesContext.getCurrentInstance().getELContext());
      if (value instanceof Overflow) {
        return (Overflow) value;
      } else if (value != null) {
        return Overflow.valueOf(value.toString());
      } else {
        return null;
      }
    } else if (overflowY != null) {
      return overflowY;
    } else {
      return null;
    }
  }

  public void setOverflowY(final Overflow overflowY) {
    empty = null;
    this.overflowY = overflowY;
  }

  public void setOverflowY(final ValueExpression overflowY) {
    empty = null;
    this.overflowYVE = overflowY;
  }

  public Display getDisplay() {
    if (displayVE != null) {
      final Object value = displayVE.getValue(FacesContext.getCurrentInstance().getELContext());
      if (value instanceof Display) {
        return (Display) value;
      } else if (value != null) {
        return Display.valueOf(value.toString());
      } else {
        return null;
      }
    } else if (display != null) {
      return display;
    } else {
      return null;
    }
  }

  public void setDisplay(final Display display) {
    empty = null;
    this.display = display;
  }

  public void setDisplay(final ValueExpression display) {
    empty = null;
    this.displayVE = display;
  }

  public Position getPosition() {
    if (positionVE != null) {
      final Object value = positionVE.getValue(FacesContext.getCurrentInstance().getELContext());
      if (value instanceof Position) {
        return (Position) value;
      } else if (value != null) {
        return Position.valueOf(value.toString());
      } else {
        return null;
      }
    } else if (position != null) {
      return position;
    } else {
      return null;
    }
  }

  public void setPosition(final Position position) {
    empty = null;
    this.position = position;
  }

  public void setPosition(final ValueExpression position) {
    empty = null;
    this.positionVE = position;
  }

  // tbd
  public String getBackgroundImage() {
    if (backgroundImageVE != null) {
      return (String) backgroundImageVE.getValue(FacesContext.getCurrentInstance().getELContext());
    } else if (backgroundImage != null) {
      return backgroundImage;
    } else {
      return null;
    }
  }

  // tbd
  public void setBackgroundImage(final String backgroundImage) {
    empty = null;
    this.backgroundImage = backgroundImage;
  }

  // tbd
  public void setBackgroundImage(final ValueExpression backgroundImage) {
    empty = null;
    this.backgroundImageVE = backgroundImage;
  }

  // tbd
  public String getBackgroundPosition() {
    if (backgroundPositionVE != null) {
      return (String) backgroundPositionVE.getValue(FacesContext.getCurrentInstance().getELContext());
    } else if (backgroundPosition != null) {
      return backgroundPosition;
    } else {
      return null;
    }
  }

  // tbd
  public void setBackgroundPosition(final String backgroundPosition) {
    empty = null;
    this.backgroundPosition = backgroundPosition;
  }

  // tbd
  public void setBackgroundPosition(final ValueExpression backgroundPosition) {
    empty = null;
    this.backgroundPositionVE = backgroundPosition;
  }

  // tbd
  public Integer getZIndex() {
    if (zIndexVE != null) {
      final Object value = zIndexVE.getValue(FacesContext.getCurrentInstance().getELContext());
      if (value instanceof Number) {
        return (Integer) value;
      } else if (value != null) {
        return Integer.parseInt(value.toString());
      } else {
        return null;
      }
    } else if (zIndex != null) {
      return zIndex;
    } else {
      return null;
    }
  }

  // tbd
  public void setZIndex(final Integer zIndexValue) {
    empty = null;
    this.zIndex = zIndexValue;
  }

  // tbd
  public void setZIndex(final ValueExpression zIndexValue) {
    empty = null;
    this.zIndexVE = zIndexValue;
  }

  public TextAlign getTextAlign() {
    if (textAlignVE != null) {
      final Object value = textAlignVE.getValue(FacesContext.getCurrentInstance().getELContext());
      if (value instanceof TextAlign) {
        return (TextAlign) value;
      } else if (value != null) {
        return TextAlign.valueOf(value.toString());
      } else {
        return null;
      }
    } else if (textAlign != null) {
      return textAlign;
    } else {
      return null;
    }
  }

  public void setTextAlign(final TextAlign textAlign) {
    empty = null;
    this.textAlign = textAlign;
  }

  public void setTextAlign(final ValueExpression textAlign) {
    empty = null;
    this.textAlignVE = textAlign;
  }

  @Override
  public String toString() {
    return encode();
  }
}
