package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasVar {
  /**
   *
   * Name of a request-scope attribute under which the model data for the row
   * selected by the current value of the "rowIndex" property
   * (i.e. also the current value of the "rowData" property) will be exposed.    
   *
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute()
  public void setVar(String var);
}
