package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

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
  @TagAttribute @UIComponentTagAttribute()
  public void setNameReference(String name);
}
