package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICommand;

import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Apr 26, 2005
 * Time: 3:03:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectOneCommandTag extends CommandTag {
  public static final String COMMAND_TYPE = "commandSelectOne";
  private String value;

  public String getComponentType() {
    return UICommand.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    value = null;

  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    component.setRendererType(RENDERER_TYPE_MENUCOMMAND);

    ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_COMMAND_TYPE, COMMAND_TYPE, getIterationHelper());
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
