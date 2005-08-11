package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:51:36 PM
 * User: bommel
 * $Id$
 */
public interface HasTreeNodeValue {


  /**
   * A javax.swing.tree.TreeNode object to use as rootNode in the tree.
   */
  @TagAttribute
  @UIComponentTagAttribute(type="javax.swing.tree.TreeNode")
  void setValue(String value);
}
