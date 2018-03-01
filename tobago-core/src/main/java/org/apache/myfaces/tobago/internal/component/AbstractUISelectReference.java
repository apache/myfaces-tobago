package org.apache.myfaces.tobago.internal.component;

import javax.faces.component.UIOutput;

public abstract class AbstractUISelectReference extends UIOutput {
  public abstract String getFor();
  public abstract String getRenderRange();
}
