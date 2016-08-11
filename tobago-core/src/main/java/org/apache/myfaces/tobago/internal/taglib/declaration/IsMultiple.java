package org.apache.myfaces.tobago.internal.taglib.declaration;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

public interface IsMultiple {

  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setMultiple(String multiple);
}
