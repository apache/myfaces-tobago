/*
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jun 3, 2002
 * Time: 3:10:17 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;


public class ImageTag extends TobagoTag {

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  private String value;
  private String alt;
  private String border;
  private String width;
  private String height;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  public String getComponentType() {
    return UIGraphic.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_ALT, alt);
    setProperty(component, TobagoConstants.ATTR_BORDER, border);
    setProperty(component, TobagoConstants.ATTR_WIDTH, width);
    setProperty(component, TobagoConstants.ATTR_HEIGHT, height);
    UIGraphic graphic = (UIGraphic) component;
    if (isValueReference(value)) {
      Application application = FacesContext.getCurrentInstance().getApplication();
      component.setValueBinding("value", application.createValueBinding(value));
    }
    else if (value != null) {
      graphic.setValue(value);
    }
  }

  public void setAlt(String alt) {
    this.alt = alt;
  }

  public void setBorder(String border) {
    this.border = border;
  }

  /**
   * @deprecated use setValue instead
   */
  public void setSrc(String src) {
    this.value = src;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void release() {
    super.release();
    this.alt = null;
    this.border = null;
    this.width = null;
    this.height = null;
    this.value = null;
  }
}
