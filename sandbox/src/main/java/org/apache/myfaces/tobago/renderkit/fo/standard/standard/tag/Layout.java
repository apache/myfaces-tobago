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

package org.apache.myfaces.tobago.renderkit.fo.standard.standard.tag;

import javax.faces.component.UIComponent;
import java.text.DecimalFormat;
import java.util.Locale;

/*
 * Created: Dec 1, 2004 4:26:05 PM
 * User: bommel
 * $Id:Layout.java 472227 2006-11-07 21:05:00 +0100 (Tue, 07 Nov 2006) bommel $
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
    return DecimalFormat.getNumberInstance(Locale.US).format(value / 10.0) + "mm";
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
    if (!(orientation == TOP_ORIENTATION)) {
      x += left;
      width -= (left + right);
    }
    if (!(orientation == LEFT_ORIENTATION)) {
      height -= (top + bottom);
      y += top;
    }

  }

  public boolean hasParent() {
    return parent != null;
  }

  public Layout createWithMargin(int top, int bottom, int left, int right) {
    return new Layout(getX() + left, getY() + top, getWidth() - (left + right), getHeight() - (top + bottom));
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

  public static void setInLayout(UIComponent component, boolean layout) {
    component.getAttributes().put("fo:layoutMode", "LayoutMode");
  }

  public static boolean isInLayout(UIComponent component) {
    return component.getAttributes().containsKey("fo:layoutMode");
  }

  public static Layout getLayout(UIComponent component) {
    return (Layout) component.getAttributes().get("fo:layout");
  }

}
