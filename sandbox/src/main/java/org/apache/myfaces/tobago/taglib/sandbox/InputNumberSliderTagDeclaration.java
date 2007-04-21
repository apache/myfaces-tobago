package org.apache.myfaces.tobago.taglib.sandbox;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.HasValueChangeListener;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;

@Tag(name = "numberSlider")
@UIComponentTag(rendererType = "InputNumberSlider",
    uiComponent = "org.apache.myfaces.tobago.component.UIInputNumberSlider")
public interface InputNumberSliderTagDeclaration extends
    HasIdBindingAndRendered, IsReadonly, IsDisabled,
    HasValue, HasValueChangeListener {

  /**
   * The minimum integer that can be entered and which represents the left
   * border of the slider.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "0")
  void setMin(String min);

  /**
   * The maximum integer that can be entered and which represents the rigth
   * border of the slider.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "100")
  void setMax(String max);
}
