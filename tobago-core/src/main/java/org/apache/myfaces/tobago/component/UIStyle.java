package org.apache.myfaces.tobago.component;

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

import org.apache.myfaces.tobago.layout.Measure;

import javax.faces.component.UIComponentBase;

/**
 * <p>
 * Add a style tag with the given file name to the header (using file attribute) or add
 * some CSS styles to the parent component.
 * </p>
 * UIComponent class, generated from template {@code component.stg} with class
 * {@link org.apache.myfaces.tobago.internal.taglib.component.StyleTagDeclaration}.
 *
 * @deprecated Since Tobago 4.0.0. The tag &lt;tc:style&gt; is using a handler
 * now: {@link org.apache.myfaces.tobago.facelets.StyleHandler}.
 */
@Deprecated
public class UIStyle extends UIComponentBase {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Style";

  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.Style";

  enum PropertyKeys {
    paddingRight,
    minHeight,
    marginRight,
    file,
    paddingBottom,
    top,
    maxHeight,
    paddingTop,
    height,
    maxWidth,
    textAlign,
    bottom,
    display,
    minWidth,
    right,
    marginLeft,
    overflowX,
    overflowY,
    left,
    width,
    customClass,
    marginBottom,
    position,
    paddingLeft,
    marginTop,
  }

  public String getFamily() {
    return COMPONENT_FAMILY;
  }

  public org.apache.myfaces.tobago.layout.Measure getPaddingRight() {
    Object object = getStateHelper().eval(PropertyKeys.paddingRight);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setPaddingRight(org.apache.myfaces.tobago.layout.Measure paddingRight) {
    getStateHelper().put(PropertyKeys.paddingRight, paddingRight);
  }

  public org.apache.myfaces.tobago.layout.Measure getMinHeight() {
    Object object = getStateHelper().eval(PropertyKeys.minHeight);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setMinHeight(org.apache.myfaces.tobago.layout.Measure minHeight) {
    getStateHelper().put(PropertyKeys.minHeight, minHeight);
  }

  public org.apache.myfaces.tobago.layout.Measure getMarginRight() {
    Object object = getStateHelper().eval(PropertyKeys.marginRight);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setMarginRight(org.apache.myfaces.tobago.layout.Measure marginRight) {
    getStateHelper().put(PropertyKeys.marginRight, marginRight);
  }

  /**
   * Name of the stylesheet file to add to page. The name must be full qualified, or relative.
   * If using a complete path from root, you'll need to add the contextPath from the web application.
   * This can be done with the EL #{request.contextPath}.
   */
  public java.lang.String getFile() {
    return (java.lang.String) getStateHelper().eval(PropertyKeys.file);
  }

  public void setFile(java.lang.String file) {
    getStateHelper().put(PropertyKeys.file, file);
  }

  public org.apache.myfaces.tobago.layout.Measure getPaddingBottom() {
    Object object = getStateHelper().eval(PropertyKeys.paddingBottom);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setPaddingBottom(org.apache.myfaces.tobago.layout.Measure paddingBottom) {
    getStateHelper().put(PropertyKeys.paddingBottom, paddingBottom);
  }

  public org.apache.myfaces.tobago.layout.Measure getTop() {
    Object object = getStateHelper().eval(PropertyKeys.top);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setTop(org.apache.myfaces.tobago.layout.Measure top) {
    getStateHelper().put(PropertyKeys.top, top);
  }

  public org.apache.myfaces.tobago.layout.Measure getMaxHeight() {
    Object object = getStateHelper().eval(PropertyKeys.maxHeight);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setMaxHeight(org.apache.myfaces.tobago.layout.Measure maxHeight) {
    getStateHelper().put(PropertyKeys.maxHeight, maxHeight);
  }

  public org.apache.myfaces.tobago.layout.Measure getPaddingTop() {
    Object object = getStateHelper().eval(PropertyKeys.paddingTop);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setPaddingTop(org.apache.myfaces.tobago.layout.Measure paddingTop) {
    getStateHelper().put(PropertyKeys.paddingTop, paddingTop);
  }

  public org.apache.myfaces.tobago.layout.Measure getHeight() {
    Object object = getStateHelper().eval(PropertyKeys.height);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setHeight(org.apache.myfaces.tobago.layout.Measure height) {
    getStateHelper().put(PropertyKeys.height, height);
  }

  public org.apache.myfaces.tobago.layout.Measure getMaxWidth() {
    Object object = getStateHelper().eval(PropertyKeys.maxWidth);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setMaxWidth(org.apache.myfaces.tobago.layout.Measure maxWidth) {
    getStateHelper().put(PropertyKeys.maxWidth, maxWidth);
  }

  /**
   * The alignment of the elements inside of the container, possible values are:
   * {
   * <br>Allowed Values: <code>left,right,center,justify</code>
   */
  public org.apache.myfaces.tobago.layout.TextAlign getTextAlign() {
    return (org.apache.myfaces.tobago.layout.TextAlign) getStateHelper().eval(PropertyKeys.textAlign);
  }

  public void setTextAlign(org.apache.myfaces.tobago.layout.TextAlign textAlign) {
    getStateHelper().put(PropertyKeys.textAlign, textAlign);
  }

  public org.apache.myfaces.tobago.layout.Measure getBottom() {
    Object object = getStateHelper().eval(PropertyKeys.bottom);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setBottom(org.apache.myfaces.tobago.layout.Measure bottom) {
    getStateHelper().put(PropertyKeys.bottom, bottom);
  }

  public org.apache.myfaces.tobago.layout.Display getDisplay() {
    return (org.apache.myfaces.tobago.layout.Display) getStateHelper().eval(PropertyKeys.display);
  }

  public void setDisplay(org.apache.myfaces.tobago.layout.Display display) {
    getStateHelper().put(PropertyKeys.display, display);
  }

  public org.apache.myfaces.tobago.layout.Measure getMinWidth() {
    Object object = getStateHelper().eval(PropertyKeys.minWidth);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setMinWidth(org.apache.myfaces.tobago.layout.Measure minWidth) {
    getStateHelper().put(PropertyKeys.minWidth, minWidth);
  }

  public org.apache.myfaces.tobago.layout.Measure getRight() {
    Object object = getStateHelper().eval(PropertyKeys.right);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setRight(org.apache.myfaces.tobago.layout.Measure right) {
    getStateHelper().put(PropertyKeys.right, right);
  }

  public org.apache.myfaces.tobago.layout.Measure getMarginLeft() {
    Object object = getStateHelper().eval(PropertyKeys.marginLeft);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setMarginLeft(org.apache.myfaces.tobago.layout.Measure marginLeft) {
    getStateHelper().put(PropertyKeys.marginLeft, marginLeft);
  }

  public org.apache.myfaces.tobago.layout.Overflow getOverflowX() {
    return (org.apache.myfaces.tobago.layout.Overflow) getStateHelper().eval(PropertyKeys.overflowX);
  }

  public void setOverflowX(org.apache.myfaces.tobago.layout.Overflow overflowX) {
    getStateHelper().put(PropertyKeys.overflowX, overflowX);
  }

  public org.apache.myfaces.tobago.layout.Overflow getOverflowY() {
    return (org.apache.myfaces.tobago.layout.Overflow) getStateHelper().eval(PropertyKeys.overflowY);
  }

  public void setOverflowY(org.apache.myfaces.tobago.layout.Overflow overflowY) {
    getStateHelper().put(PropertyKeys.overflowY, overflowY);
  }

  public org.apache.myfaces.tobago.layout.Measure getLeft() {
    Object object = getStateHelper().eval(PropertyKeys.left);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setLeft(org.apache.myfaces.tobago.layout.Measure left) {
    getStateHelper().put(PropertyKeys.left, left);
  }

  public org.apache.myfaces.tobago.layout.Measure getWidth() {
    Object object = getStateHelper().eval(PropertyKeys.width);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setWidth(org.apache.myfaces.tobago.layout.Measure width) {
    getStateHelper().put(PropertyKeys.width, width);
  }

  public org.apache.myfaces.tobago.renderkit.css.CustomClass getCustomClass() {
    return (org.apache.myfaces.tobago.renderkit.css.CustomClass) getStateHelper().eval(PropertyKeys.customClass);
  }

  public void setCustomClass(org.apache.myfaces.tobago.renderkit.css.CustomClass customClass) {
    getStateHelper().put(PropertyKeys.customClass, customClass);
  }

  public org.apache.myfaces.tobago.layout.Measure getMarginBottom() {
    Object object = getStateHelper().eval(PropertyKeys.marginBottom);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setMarginBottom(org.apache.myfaces.tobago.layout.Measure marginBottom) {
    getStateHelper().put(PropertyKeys.marginBottom, marginBottom);
  }

  public org.apache.myfaces.tobago.layout.Position getPosition() {
    return (org.apache.myfaces.tobago.layout.Position) getStateHelper().eval(PropertyKeys.position);
  }

  public void setPosition(org.apache.myfaces.tobago.layout.Position position) {
    getStateHelper().put(PropertyKeys.position, position);
  }

  public org.apache.myfaces.tobago.layout.Measure getPaddingLeft() {
    Object object = getStateHelper().eval(PropertyKeys.paddingLeft);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setPaddingLeft(org.apache.myfaces.tobago.layout.Measure paddingLeft) {
    getStateHelper().put(PropertyKeys.paddingLeft, paddingLeft);
  }

  public org.apache.myfaces.tobago.layout.Measure getMarginTop() {
    Object object = getStateHelper().eval(PropertyKeys.marginTop);
    if (object != null) {
      return Measure.valueOf(object);
    }
    return null;
  }

  public void setMarginTop(org.apache.myfaces.tobago.layout.Measure marginTop) {
    getStateHelper().put(PropertyKeys.marginTop, marginTop);
  }
}
