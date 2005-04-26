package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.swing.tree.TreeNode;

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
  @UIComponentTagAttribute(type=TreeNode.class)
  void setValue(String value);
}
