package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasVar {
  /**
   *  <![CDATA[
   * Name of a request-scope attribute under which the model data for the row
   * selected by the current value of the "rowIndex" property
   * (i.e. also the current value of the "rowData" property) will be exposed.    
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setVar(String var);
}
