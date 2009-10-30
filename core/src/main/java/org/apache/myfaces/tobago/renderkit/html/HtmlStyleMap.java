package org.apache.myfaces.tobago.renderkit.html;

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

import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.Measure;

import java.io.Serializable;

public class HtmlStyleMap implements Serializable {

  private static final long serialVersionUID = 2L;

  private Measure width;
  private Measure height;
  private Measure left;
  private Measure top;
  private Display display;
  // todo: class or enum instead of String
  private String position;

  // todo: class or enum instead of String
  private String overflow;
  private Measure paddingTop;
  private Measure paddingBottom;

  public HtmlStyleMap() {
  }

  public HtmlStyleMap(HtmlStyleMap map) {
    this.width = map.width;
    this.height = map.height;
    this.left = map.left;
    this.top = map.top;
    this.display = map.display;
    this.position = map.position;
    this.overflow = map.overflow;
    this.paddingTop = map.paddingTop;
    this.paddingBottom = map.paddingBottom;
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
      buf.append(position);
      buf.append(';');
    }
    if (overflow != null) {
      buf.append("overflow:");
      buf.append(overflow);
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

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getOverflow() {
    return overflow;
  }

  public void setOverflow(String overflow) {
    this.overflow = overflow;
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
}
