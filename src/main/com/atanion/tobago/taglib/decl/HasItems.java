package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import java.util.List;
import java.util.Map;

/**
 * $Id$
 */
public interface HasItems {
  /**
   * <![CDATA[ <code>java.util.List</code>,
   *   <code>java.util.Map</code> or <code>Object[]</code> of items to iterate over.
   *   This <strong>must</strong> be a <code>ValueBinding</code>.
   * ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type={List.class, Map.class, Object[].class})
  public void setItems(String items);
}
