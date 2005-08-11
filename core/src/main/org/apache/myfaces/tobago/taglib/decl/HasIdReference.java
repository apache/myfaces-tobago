package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

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
  @TagAttribute @UIComponentTagAttribute()
  public void setIdReference(String id);
}
