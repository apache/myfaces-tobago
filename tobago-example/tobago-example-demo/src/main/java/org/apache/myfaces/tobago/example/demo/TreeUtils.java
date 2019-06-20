package org.apache.myfaces.tobago.example.demo;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeUtils {

  private TreeUtils() {
  }

  public static void resetSelection(final DefaultMutableTreeNode node) {
    final Node userObject = (Node) node.getUserObject();
    userObject.setSelected(false);
    for (int i = 0; i < node.getChildCount(); i++) {
      final DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
      resetSelection(child);
    }
  }

  public static String getSelectedNodes(final DefaultMutableTreeNode treeNode) {
    final StringBuilder stringBuilder = new StringBuilder();
    buildSelectedNodesString(stringBuilder, treeNode);
    if (stringBuilder.length() > 2) {
      return stringBuilder.substring(2); // Remove ', '.
    } else {
      return "";
    }
  }

  private static void buildSelectedNodesString(final StringBuilder stringBuilder, final DefaultMutableTreeNode node) {
    final Node userObject = (Node) node.getUserObject();
    if (userObject.isSelected()) {
      stringBuilder.append(", ");
      stringBuilder.append(userObject.getName());
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      final DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
      buildSelectedNodesString(stringBuilder, child);
    }
  }

}
