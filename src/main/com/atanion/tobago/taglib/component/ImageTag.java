/*
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jun 3, 2002
 * Time: 3:10:17 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;


public class ImageTag extends TobagoTag {

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  private String value;
  private String alt;
  private String border;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  public String getComponentType() {
    return UIGraphic.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_ALT, alt, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_BORDER, border, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
  }

  public void release() {
    super.release();
    this.alt = null;
    this.border = null;
    this.value = null;
  }

// /////////////////////////////////////////// setter + getter

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getAlt() {
    return alt;
  }

  public void setAlt(String alt) {
    this.alt = alt;
  }

  public String getBorder() {
    return border;
  }

  public void setBorder(String border) {
    this.border = border;
  }
}
