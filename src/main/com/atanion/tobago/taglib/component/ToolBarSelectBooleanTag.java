package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Mar 29, 2005
 * Time: 3:52:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToolBarSelectBooleanTag extends MenuSelectBooleanTag {
  
  private String image;


  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(component, ATTR_IMAGE, image, getIterationHelper());
  }

  public void release() {
    super.release();
    image = null;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
