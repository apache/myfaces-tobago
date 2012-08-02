/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_COLUMNS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT_DEFAULT;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_GRID_LAYOUT;
import org.apache.myfaces.tobago.config.ThemeConfig;

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

/*
 * User: weber
 * Date: Mar 16, 2005
 * Time: 12:33:08 PM
 */
public class UITreeListbox extends UITreeOld implements LayoutProvider {

  private static final Log LOG = LogFactory.getLog(UITreeListbox.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TreeListbox";

  public static final String BOXES_PREFIX = "boxes_";

  private List<UITreeOldNode> selectionPath = null;
  private List<UITreeOldNode> expandPath = null;

  private boolean encodingChildren = false;

  private List<UITreeListboxBox> boxes;


  protected String nodeStateId(FacesContext facesContext, UITreeOldNode node) {
    // this must do the same as nodeStateId() in tree.js
    String clientId = node.getClientId(facesContext);
    int last = clientId.lastIndexOf(':') + 1;
    return clientId.substring(last);
  }

  public void encodeBegin(FacesContext facesContext)
      throws IOException {
    // TODO change this should be renamed to DimensionUtils.prepare!!!
    UILayout.getLayout(this).layoutBegin(facesContext, this);
//    debugStates(facesContext);
    fixSelectionType();
    super.encodeBegin(facesContext);
    debugStates(facesContext);
    createUIBoxes(facesContext);
  }

  @SuppressWarnings(value = "unchecked")
  private void fixSelectionType() {
    final Map attributes = getAttributes();
    Object selectable = attributes.get(ATTR_SELECTABLE);
    if ("single".equals(selectable)
        || "singleLeafOnly".equals(selectable)
        || "siblingLeafOnly".equals(selectable)) {
    } else if (selectable == null) {
      attributes.put(ATTR_SELECTABLE, "single");
    } else {
      // fix to single
      LOG.warn("Illegal attributeValue selectable : " + selectable + " set to 'single'");
      attributes.put(ATTR_SELECTABLE, "single");
    }
  }

  private void debugStates(FacesContext facesContext) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("#####################################################");
      StringBuilder state = new StringBuilder("expandState : ;");
      for (DefaultMutableTreeNode treeNode : getState().getExpandState()) {
        state.append(nodeStateId(facesContext, findUITreeNode(getRoot(), treeNode)));
        state.append(";");
      }
      LOG.debug(state);

      state = new StringBuilder("selectState : ;");
      for (DefaultMutableTreeNode treeNode : getState().getSelection()) {
        state.append(nodeStateId(facesContext, findUITreeNode(getRoot(), treeNode)));
        state.append(";");
      }
      LOG.debug(state);

      state = new StringBuilder("selectionPath : ;");
      for (UITreeOldNode treeNode : getSelectionPath()) {
        state.append(nodeStateId(facesContext, treeNode));
        state.append(";");
      }
      LOG.debug(state);

      state = new StringBuilder("expandPath : ;");
      for (UITreeOldNode treeNode : getExpandPath()) {
        state.append(nodeStateId(facesContext, treeNode));
        state.append(";");
      }
      LOG.debug(state);

      LOG.debug("");
    }

  }

  public void createSelectionPath() {
    selectionPath = new ArrayList<UITreeOldNode>();
    expandPath = new ArrayList<UITreeOldNode>();
    if (isSelectableTree()) {
      Iterator iterator = getState().getSelection().iterator();
      if (iterator.hasNext()) {
        TreeNode treeNode = (TreeNode) iterator.next();
        UITreeOldNode selectedNode = findUITreeNode(getRoot(), treeNode);
        if (selectedNode != null) {
          UIComponent ancestor = selectedNode;
          while (ancestor != null && ancestor instanceof UITreeOldNode) {
            selectionPath.add(0, (UITreeOldNode) ancestor);
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
      for (UITreeOldNode node : selectionPath) {
        if (!node.getTreeNode().isLeaf()) {
          expandPath.add(node);
        }
      }
    }
    if (expandPath.isEmpty()) {
      expandPath.add(getRoot());
    }
    expandState.clear();
    for (UITreeOldNode uiTreeNode : expandPath) {
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

  private List<UITreeOldNode> getNodes(int level) {
    List children;
    if (level == 0) {
      children = getRoot().getChildren();
    } else if (selectionPath.size() > level) {
      children = selectionPath.get(level).getChildren();
    } else {
      children = Collections.EMPTY_LIST;
    }
    List<UITreeOldNode> nodes = new ArrayList<UITreeOldNode>(children.size());
    for (Object node : children) {
      if (node instanceof UITreeOldNode) {
        nodes.add((UITreeOldNode) node);
      }
    }
    return nodes;
  }

  public void encodeChildren(FacesContext facesContext) throws IOException {
    if (isRendered()) {
      encodingChildren = true;
      UILayout.getLayout(this).encodeChildrenOfComponent(facesContext, this);
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

  public UITreeOldNode getSelectedNode(int level) {
    UITreeOldNode selectedComponent = null;
    if (selectionPath.size() > level + 1) {
      selectedComponent = selectionPath.get(level + 1);
    }
    return selectedComponent;
  }

  public List<UITreeOldNode> getSelectionPath() {
    return selectionPath;
  }

  public List<UITreeOldNode> getExpandPath() {
    return expandPath;
  }

  public boolean isSelectedNode(DefaultMutableTreeNode treeNode) {
    return getState().getSelection().contains(treeNode);
  }

// --------------------------------------------------- Interface LayoutProvider

  public UILayout provideLayout() {
    UILayout layout = (UILayout) getFacet(FACET_LAYOUT_DEFAULT);
    if (layout == null) {
      layout = (UILayout) ComponentUtil.createComponent(
          UIGridLayout.COMPONENT_TYPE,
          RENDERER_TYPE_GRID_LAYOUT, null);

      int depth = ((DefaultMutableTreeNode) getValue()).getDepth();
      final int defaultColumnCount = ThemeConfig.getValue(
          FacesContext.getCurrentInstance(), this, "defaultColumnCount");

      if (defaultColumnCount < depth) {
        depth = defaultColumnCount;
      }

      StringBuilder columns = new StringBuilder("1*");
      for (int i = 1; i < depth; i++) {
        columns.append(";1*");
      }

      layout.getAttributes().put(ATTR_COLUMNS, columns.toString());
      getFacets().put(FACET_LAYOUT_DEFAULT, layout);
    }

    return layout;
  }

}

