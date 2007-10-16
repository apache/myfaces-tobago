package org.apache.myfaces.tobago.facelets;

import com.sun.facelets.tag.AbstractTagLibrary;

/**
 * User: lofwyr
 * Date: 15.10.2007 15:08:23
 */
public class TobagoSandboxTagLibrary extends AbstractTagLibrary {

  public static final String NAMESPACE = "http://myfaces.apache.org/tobago/sandbox";

  public static final TobagoSandboxTagLibrary INSTANCE = new TobagoSandboxTagLibrary();

  public TobagoSandboxTagLibrary() {

    super(NAMESPACE);

    addComponent("tree", "org.apache.myfaces.tobago.Tree", "Tree", TobagoComponentHandler.class);

    addComponent("treeNode", "org.apache.myfaces.tobago.TreeNode", "TreeNode", TobagoComponentHandler.class);

    addComponent("treeData", "org.apache.myfaces.tobago.TreeData", "TreeData", TobagoComponentHandler.class);

  }
}
