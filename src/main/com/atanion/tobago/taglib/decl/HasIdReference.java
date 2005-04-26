package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasIdReference {
  /**
   * Bean propperty reference to fetch the id for the treeNode's.<br>
   * Example:<br>
   *   a idRefrerence="userObject.id" try to invoke
   *   <code>&lt;UITreeNode>.getUserObject().getId()<code> to fetch the id.
   *
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setIdReference(String id);
}
