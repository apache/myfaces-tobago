/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * User: weber
 * Date: Mar 16, 2005
 * Time: 12:33:08 PM
 */
public class UITreeListbox extends UITree {

// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(UITreeListbox.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TreeListbox";

  public static final String BOXES_PREFIX = "boxes_";

// ----------------------------------------------------------------- attributes

  private List<UITreeNode> selectionPath;
  private List<UITreeNode> expandPath;

  private boolean encodingChildren = false;

  private List<UITreeListboxBox> boxes;

  public UITreeListbox() {
    super();
//    getAttributes().put(TobagoConstants.ATTR_SELECTABLE, "single");
  }
// ----------------------------------------------------------- business methods


  protected String nodeStateId(FacesContext facesContext, UITreeNode node) {
    // this must do the same as nodeStateId() in tree.js
    String clientId = node.getClientId(facesContext);
    int last = clientId.lastIndexOf(':') + 1;
    return clientId.substring(last);
  }

  public void encodeBegin(FacesContext facesContext)
      throws IOException {
    getLayout().layoutBegin(facesContext, this);
//    debugStates(facesContext);
    fixSelectionType();
    super.encodeBegin(facesContext);
    debugStates(facesContext);
    createUIBoxes(facesContext);
  }

  @SuppressWarnings(value = "unchecked")
  private void fixSelectionType() {
    final Map  attributes = getAttributes();
    Object selectable = attributes.get(TobagoConstants.ATTR_SELECTABLE);
    if ("single".equals(selectable)
        || "singleLeafOnly".equals(selectable)
        || "siblingLeafOnly".equals(selectable)) {
      return;
    } else {
      // fix to single
      LOG.warn("Illegal attributeValue selectable : " + selectable + " set to 'single'");
      attributes.put(TobagoConstants.ATTR_SELECTABLE, "single");
    }
  }

  private void debugStates(FacesContext facesContext) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("#####################################################");
      String state = "expandState : ;";
      for (DefaultMutableTreeNode treeNode : getState().getExpandState()) {
        state += nodeStateId(facesContext, findUITreeNode(getRoot(), treeNode)) + ";";
      }
      LOG.debug(state);

      state = "selectState : ;";
      for (DefaultMutableTreeNode treeNode : getState().getSelection()) {
        state += nodeStateId(facesContext, findUITreeNode(getRoot(), treeNode)) + ";";
      }
      LOG.debug(state);

      state = "selectionPath : ;";
      for (UITreeNode treeNode : getSelectionPath()) {
        state += nodeStateId(facesContext, treeNode) + ";";
      }
      LOG.debug(state);

      state = "expandPath : ;";
      for (UITreeNode treeNode : getExpandPath()) {
        state += nodeStateId(facesContext, treeNode) + ";";
      }
      LOG.info(state);

      LOG.debug("");
    }

  }

  public void createSelectionPath() {
    selectionPath = new ArrayList<UITreeNode>();
    expandPath = new ArrayList<UITreeNode>();
    if (isSelectableTree()) {
      Iterator iterator = getState().getSelection().iterator();
      if (iterator.hasNext()) {
        TreeNode treeNode = (TreeNode) iterator.next();
        UITreeNode selectedNode = findUITreeNode(getRoot(), treeNode);
        if (selectedNode != null) {
          UIComponent ancestor = selectedNode;
          while (ancestor != null && ancestor instanceof UITreeNode) {
            selectionPath.add(0, (UITreeNode) ancestor);
            ancestor = ancestor.getParent();
          }
        }
      }
    }
    Set<DefaultMutableTreeNode> expandState = getState().getExpandState();
    if (selectionPath.isEmpty()) {
      DefaultMutableTreeNode treeNode = getRoot().getTreeNode();
      createExpandPath(treeNode, expandState);
      selectionPath.addAll(expandPath);
    } else {
      for (UITreeNode node : selectionPath) {
        if (! node.getTreeNode().isLeaf()) {
          expandPath.add(node);
        }
      }
    }
    if (expandPath.isEmpty()) {
      expandPath.add(getRoot());
    }
    expandState.clear();
    for (UITreeNode uiTreeNode : expandPath) {
      expandState.add((DefaultMutableTreeNode) uiTreeNode.getValue());
    }

  }

  private boolean createExpandPath(DefaultMutableTreeNode node,
                                   Set<DefaultMutableTreeNode> expandState) {
    if (expandState.contains(node)) {
      expandPath.add(findUITreeNode(getRoot(), node));
      for (int i = 0; i < node.getChildCount(); i++) {
        if (createExpandPath((DefaultMutableTreeNode) node.getChildAt(i), expandState)) {
          break;
        }
      }
      return true;
    }
    return false;
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

  public List<UITreeNode> getExpandPath() {
    return expandPath;
  }

  public boolean isSelectedNode(DefaultMutableTreeNode treeNode) {
    return getState().getSelection().contains(treeNode);
  }
}

