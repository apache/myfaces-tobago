package org.apache.myfaces.tobago.internal.taglib.declaration;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

public interface Select2 {

  /**
   * Flag indicating that this select provides support for clearable selections.
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false", generate = false)
  void setAllowClear(String allowed);

  /**
   * Flag indicating that this select enables free text responses.
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false", generate = false)
  void setAllowCustom(String allowed);

  /**
   * Javascript callback to handle custom search matching
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(generate = false)
  void setMatcher(String matcher);

  /**
   * Maximum number of characters that may be provided for a search term.
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "int", defaultValue = "0", generate = false)
  void setMaximumInputLength(String allowed);

  /**
   * Minimum number of characters required to start a search.
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "int", defaultValue = "0", generate = false)
  void setMinimumInputLength(String allowed);

  /**
   * The maximum number of items that may be selected in a multi-select control.
   * If the value of this option is less than 1, the number of selected items will not be limited.
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "int", defaultValue = "0", generate = false)
  void setMaximumSelectionLength(String allowed);

}
