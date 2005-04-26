package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UICommand;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Apr 26, 2005
 * Time: 3:01:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectBooleanCommandTag extends CommandTag {
  public static final String COMMAND_TYPE = "commandSelectBoolean";
  private String label;
  private String accessKey;
  private String labelWithAccessKey;
  private String value;

  public String getComponentType() {
    return UICommand.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    component.setRendererType(RENDERER_TYPE_MENUCOMMAND);

    ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_COMMAND_TYPE, COMMAND_TYPE, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
  }

  public void release() {
    super.release();
    value = null;
    label = null;
    accessKey = null;
    labelWithAccessKey = null;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    this.labelWithAccessKey = labelWithAccessKey;
  }
}
