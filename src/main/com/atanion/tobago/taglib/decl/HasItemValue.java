package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.model.SelectItem;
import java.util.List;
import java.util.Map;

/**
 * $Id$
 */
public interface HasItemValue {
  /**
   *  Value binding expression pointing at a
   *  SelectItem instance containing the
   *  information for this option.
   */
  @TagAttribute @UIComponentTagAttribute(type=SelectItem.class)
  public void setItemValue(String itemValue);
}
