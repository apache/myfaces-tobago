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
import com.atanion.tobago.taglib.decl.HasBorder;
import com.atanion.tobago.taglib.decl.HasDimension;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;

/**
 *  Renders a Image.
 */
@Tag(name="image", bodyContent="empty")
public class ImageTag extends TobagoTag
    implements HasIdBindingAndRendered, HasBorder, HasDimension, HasTip {

  private String value;
  private String alt;
  private String border;
  private String tip;

  public String getComponentType() {
    return UIGraphic.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_ALT, alt, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_BORDER, border, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_TIP, tip, getIterationHelper());
  }

  public void release() {
    super.release();
    this.alt = null;
    this.border = null;
    this.value = null;
    this.tip = null;
  }

  public String getValue() {
    return value;
  }

  /**
   *  <![CDATA[
   * Absolute url to an image or image name to lookup in tobago resource path
   *    ]]>
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute(type=String.class)
  public void setValue(String value) {
    this.value = value;
  }

  public String getAlt() {
    return alt;
  }

  /**
   *  <![CDATA[
   *  Alternate textual description of the image rendered by this component.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=String.class)
  public void setAlt(String alt) {
    this.alt = alt;
  }

  public String getBorder() {
    return border;
  }

  public void setBorder(String border) {
    this.border = border;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }
}
