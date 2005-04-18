package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasScriptFile {
  /**
   * Absolute url to script file or script name to lookup in tobago resource path
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setFile(String file);
}
