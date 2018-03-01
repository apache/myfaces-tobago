package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectReference;

@Preliminary
public interface RenderRange {

  String getRenderRange();

  void setRenderRangeReference(AbstractUISelectReference reference);

  AbstractUISelectReference getRenderRangeReference();
}
