package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIOutput;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

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
