package org.apache.myfaces.tobago.taglib.sandbox;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIInputNumberSlider;
import org.apache.myfaces.tobago.taglib.component.TobagoTag;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;

public class InputNumberSliderTag extends TobagoTag
    implements InputNumberSliderTagDeclaration {

  private String max;
  private String min;
  private String value;
  private String valueChangeListener;

  public String getComponentType() {
    return UIInputNumberSlider.COMPONENT_TYPE;
  }

  public String getMax() {
    return max;
  }

  public void setMax(String max) {
    this.max = max;
  }

  public String getMin() {
    return min;
  }

  public void setMin(String min) {
    this.min = min;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setValueChangeListener(String valueChangeListener) {
    this.valueChangeListener = valueChangeListener;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setIntegerProperty(component, "min", min);
    ComponentUtil.setIntegerProperty(component, "max", max);
    ComponentUtil.setStringProperty(component, "value", value);
    if (component instanceof EditableValueHolder) {
      EditableValueHolder editableValueHolder = (EditableValueHolder) component;
      ComponentUtil.setValueChangeListener(editableValueHolder, valueChangeListener);
    }
  }

  public void release() {
    super.release();
    min = null;
    max = null;
    value = null;
    valueChangeListener = null;
  }
}
