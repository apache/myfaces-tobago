package com.atanion.tobago.component;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.atanion.tobago.model.TreeState;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Mar 16, 2005
 * Time: 12:33:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class UITreeListbox extends UITree {

  private static final Log LOG = LogFactory.getLog(UITreeListbox.class);

  public static final String COMPONENT_TYPE="com.atanion.tobago.TreeListbox";


  private List<UITreeNode> selectionPath;

  public void encodeBegin(FacesContext facesContext)
      throws IOException {
    super.encodeBegin(facesContext);
    createSelectionPath();
  }

  public void createSelectionPath() {
    selectionPath = new ArrayList<UITreeNode>();
    if (isSelectableTree()) {
      TreeState treeState = (TreeState) getValue();
      Iterator iterator = treeState.getSelection().iterator();
      if (iterator.hasNext()) {
        TreeNode treeNode = (TreeNode) iterator.next();
        UITreeNode selectedNode = findSelectedComponent(getRoot(), treeNode);
        if (selectedNode != null) {
          UIComponent ancestor = selectedNode;
          while (ancestor != null && ancestor instanceof UITreeNode) {
            selectionPath.add(0, (UITreeNode) ancestor);
            ancestor = ancestor.getParent();
          }
        }
      }
    }
  }



  public List getNodes(int level) {    
    List nodes;
    if (level == 0) {
      nodes = getRoot().getChildren();
    }
    else if (selectionPath.size() > level) {
      nodes = selectionPath.get(level).getChildren();
    }
    else {
      nodes = Collections.EMPTY_LIST;
    }
    return nodes;
  }

  public UITreeNode getSelectedNode(int level) {
    UITreeNode selectedComponent;
    if (selectionPath.size() > level + 1) {
      selectedComponent = selectionPath.get(level + 1);
    } else {
      selectedComponent = null;
    }
    return selectedComponent;
  }

  public List<UITreeNode> getSelectionPath() {
    return selectionPath;
  }
}
