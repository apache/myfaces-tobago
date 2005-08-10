package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

@Tag(name="object", bodyContent=BodyContent.EMPTY)
public class ObjectTag extends TobagoTag {

  private String src;



  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(
        component, ATTR_TARGET, src, getIterationHelper());
  }

  public String getSrc() {
    return src;
  }


  /**
   *  URI to object source
   *
   */
  @TagAttribute()
  @UIComponentTagAttribute()
  public void setSrc(String src) {
    this.src = src;
  }
}
