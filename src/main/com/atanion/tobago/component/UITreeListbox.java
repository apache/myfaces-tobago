package com.atanion.tobago.component;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.atanion.tobago.model.TreeState;
import com.atanion.tobago.TobagoConstants;

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

  public static final String BOXES_PREFIX ="boxes_";


  private List<UITreeNode> selectionPath;

  private boolean encodingChildren = false;

  private List<UITreeListboxBox> boxes;


  public void encodeBegin(FacesContext facesContext)
      throws IOException {
    getLayout().layoutBegin(facesContext, this);
    super.encodeBegin(facesContext);
    createSelectionPath();
    createUIBoxes(facesContext);
  }

  public void encodeChildren(FacesContext facesContext) throws IOException {
   if (isRendered() ) {
     encodingChildren = true;
     getLayout().encodeChildrenOfComponent(facesContext, this);
     encodingChildren = false;
   }
  }

  public void encodeEnd(FacesContext facesContext) throws IOException {
    super.encodeEnd(facesContext);
  }

  public List getChildren() {
    if (encodingChildren) {
      return boxes;
    } else {
      return super.getChildren();
    }
  }

  public int getChildCount() {
    if (encodingChildren) {
      return boxes != null ? boxes.size() : 0;
    } else {
      return super.getChildCount();
    }
  }

  private void createUIBoxes(FacesContext facesContext) {
    int depth = getRoot().getTreeNode().getDepth();
    boxes = new ArrayList<UITreeListboxBox>(depth);
    for (int i = 0; i < depth; i++) {
      UITreeListboxBox box = (UITreeListboxBox) ComponentUtil.createComponent(
          facesContext, UITreeListboxBox.COMPONENT_TYPE,
          UITreeListboxBox.RENDERER_TYPE);
      getFacets().put(BOXES_PREFIX + i, box);
      box.setLevel(i);
      box.setNodes(getNodes(i));
      boxes.add(box);
    }
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



  private List<UITreeNode> getNodes(int level) {
    List children;
    if (level == 0) {
      children = getRoot().getChildren();
    }
    else if (selectionPath.size() > level) {
      children = selectionPath.get(level).getChildren();
    }
    else {
      children = Collections.EMPTY_LIST;
    }
    List<UITreeNode> nodes = new ArrayList<UITreeNode>(children.size());
    for (Iterator iter = children.iterator(); iter.hasNext();) {
      Object node = iter.next();
      if (node instanceof UITreeNode) {
        nodes.add((UITreeNode) node);
      }
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



  private UILayout getLayout() {
    UILayout layout = (UILayout) getFacet(TobagoConstants.FACET_LAYOUT);
    if (layout == null) {
      layout = (UILayout) getFacet(TobagoConstants.FACET_LAYOUT_DEFAULT);
      if (layout == null) {
        layout = (UILayout) ComponentUtil.createComponent(
             UIGridLayout.COMPONENT_TYPE, TobagoConstants.RENDERER_TYPE_GRID_LAYOUT);
        layout.getAttributes().put(TobagoConstants.ATTR_COLUMNS, "1*;1*;1*;1*");
        layout.getAttributes().put(TobagoConstants.ATTR_ROWS, "1*;1*;1*;1*");
        getFacets().put(TobagoConstants.FACET_LAYOUT_DEFAULT, layout);
      }
    }
    if (layout instanceof UIGridLayout) {
      ((UIGridLayout)layout).setIgnoreFree(true);
    }
    return layout;
  }
}
