package com.atanion.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: weber
 * Date: Mar 16, 2005
 * Time: 12:33:08 PM
 */
public class UITreeListbox extends UITree {

// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(UITreeListbox.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.TreeListbox";

  public static final String BOXES_PREFIX = "boxes_";

// ----------------------------------------------------------------- attributes

  private List<UITreeNode> selectionPath;

  private boolean encodingChildren = false;

  private List<UITreeListboxBox> boxes;

// ----------------------------------------------------------- business methods

  public void encodeBegin(FacesContext facesContext)
      throws IOException {
    getLayout().layoutBegin(facesContext, this);
    super.encodeBegin(facesContext);
    createSelectionPath();
    createUIBoxes(facesContext);
  }

  public void createSelectionPath() {
    selectionPath = new ArrayList<UITreeNode>();
    if (isSelectableTree()) {
      Iterator iterator = getState().getSelection().iterator();
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

  private List<UITreeNode> getNodes(int level) {
    List children;
    if (level == 0) {
      children = getRoot().getChildren();
    } else if (selectionPath.size() > level) {
      children = selectionPath.get(level).getChildren();
    } else {
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

  public void encodeChildren(FacesContext facesContext) throws IOException {
    if (isRendered()) {
      encodingChildren = true;
      getLayout().encodeChildrenOfComponent(facesContext, this);
      encodingChildren = false;
    }
  }

  public void encodeEnd(FacesContext facesContext) throws IOException {
    super.encodeEnd(facesContext);
  }

  public int getChildCount() {
    if (encodingChildren) {
      return boxes != null ? boxes.size() : 0;
    } else {
      return super.getChildCount();
    }
  }

  public List getChildren() {
    if (encodingChildren) {
      return boxes;
    } else {
      return super.getChildren();
    }
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

// ------------------------------------------------------------ getter + setter

  private UILayout getLayout() {
    UILayout layout = (UILayout) getFacet(TobagoConstants.FACET_LAYOUT);
    if (layout == null) {
      layout = (UILayout) getFacet(TobagoConstants.FACET_LAYOUT_DEFAULT);
      if (layout == null) {
        layout =
            (UILayout) ComponentUtil.createComponent(
                UIGridLayout.COMPONENT_TYPE,
                TobagoConstants.RENDERER_TYPE_GRID_LAYOUT);
        layout.getAttributes().put(TobagoConstants.ATTR_COLUMNS, "1*;1*;1*;1*");
        layout.getAttributes().put(TobagoConstants.ATTR_ROWS, "1*;1*;1*;1*");
        getFacets().put(TobagoConstants.FACET_LAYOUT_DEFAULT, layout);
      }
    }
    if (layout instanceof UIGridLayout) {
      ((UIGridLayout) layout).setIgnoreFree(true);
    }
    return layout;
  }

  public List<UITreeNode> getSelectionPath() {
    return selectionPath;
  }
}

