package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasNameReference {
  /**
   * Bean propperty reference to fetch the label for the treeNode's.<br>
   * Example:<br>
   *   a idRefrerence="userObject.name" try to invoke
   *   <code>&lt;UITreeNode>.getUserObject().getName()<code> to fetch the label.
   *
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setNameReference(String name);
}
