package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import javax.faces.component.UIComponent;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Dec 1, 2004 4:26:05 PM
 * User: bommel
 * $Id$
 */
public class Layout {
  public static final int NONE_ORIENTATION = 0;
  public static final int LEFT_ORIENTATION = 1;
  public static final int TOP_ORIENTATION = 2;
  private Layout parent = null;
  private int x;
  private int y;
  private int width;
  private int height;
  private int orientation = NONE_ORIENTATION;
  public Layout(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public Layout(int width, int height) {
    this(0, 0, width, height);
  }

  public int getOrientation() {
    return orientation;
  }

  public void setOrientation(int orientation) {
    this.orientation = orientation;
  }

  public Layout getParent() {
    return parent;
  }
  public static String getMM(int value) {
    return DecimalFormat.getNumberInstance(Locale.US).format(value/10.0)+"mm";
  }
  public void setParent(Layout parent) {
    this.parent = parent;
  }

  public int getX() {
    return x;
  }
  public String getXMM() {
    return getMM(getX());
  }
  public String getYMM() {
    return getMM(getX());
  }
  public String getHeightMM() {
    return getMM(getHeight());
  }
  public String getWidthMM() {
    return getMM(getWidth());
  }
  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }
  public void addMargin(int top, int bottom, int left, int right) {
    if (!(orientation==TOP_ORIENTATION)) {
      x +=left;
      width -=(left+right);
    }
    if (!(orientation==LEFT_ORIENTATION)) {
      height -=(top+bottom);
      y +=top;
    }

  }
  public boolean hasParent() {
    return parent != null;
  }

  public Layout createWithMargin(int top, int bottom, int left, int right) {
    return new Layout(getX()+left, getY()+top, getWidth()-(left+right), getHeight()-(top+bottom));
  }

  public String toInnerString() {
    return
    "[x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "]";
  }
  public StringBuilder append(StringBuilder builder) {
    if (hasParent()) {
      getParent().append(builder);
    }
    builder.append("[x=").append(x).append(",y=").append(y).append(",width=")
        .append(width).append(",height=").append(height).append("]\n");

    return builder;
  }
  public String toString() {
    return append(new StringBuilder()).toString();
  }
  public static void putLayout(UIComponent component, Layout layout) {
    component.getAttributes().put("fo:layout", layout);
  }
  public static Layout getLayout(UIComponent component) {
    return (Layout) component.getAttributes().get("fo:layout");
  }

}
