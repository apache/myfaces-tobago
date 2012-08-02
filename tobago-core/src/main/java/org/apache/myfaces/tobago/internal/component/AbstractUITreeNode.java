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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.TreeModelBuilder;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.apache.myfaces.tobago.event.TreeExpansionListener;
import org.apache.myfaces.tobago.event.TreeMarkedEvent;
import org.apache.myfaces.tobago.event.TreeMarkedListener;
import org.apache.myfaces.tobago.model.MixedTreeModel;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.List;

public abstract class AbstractUITreeNode
    extends UIOutput implements SupportsMarkup, TreeModelBuilder, Configurable {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUITreeNode.class);

  private int depth;
  private boolean folder;
  private TreePath path;
  private List<Boolean> junctions;
  private boolean hasNextSibling;

  public void buildTreeModelBegin(FacesContext facesContext, MixedTreeModel model) {
    model.beginBuildNode();
    setDepth(computeDepth(this));
    setFolder(computeFolder());
  }

  public void buildTreeModelChildren(FacesContext facesContext, MixedTreeModel model) {
    for (Object child : getChildren()) {
      if (child instanceof TreeModelBuilder) {
        TreeModelBuilder builder = (TreeModelBuilder) child;
        builder.buildTreeModelBegin(facesContext, model);
        builder.buildTreeModelChildren(facesContext, model);
        builder.buildTreeModelEnd(facesContext, model);
      }
    }
  }

  public void buildTreeModelEnd(FacesContext facesContext, MixedTreeModel model) {
    model.endBuildNode();
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    AbstractUITree tree = ComponentUtils.findAncestor(this, AbstractUITree.class);
    MixedTreeModel mixedModel = tree.getModel();
    mixedModel.onEncodeBegin();
    setPath(mixedModel.getPath());
    setHasNextSibling(computeHasNextSibling());
    setJunctions(mixedModel.getJunctions());
    super.encodeBegin(context);
  }

  private int computeDepth(UIComponent component) {
    int depth = 0;
    while (component != null) {
      depth++;
      if (component instanceof AbstractUITree) {
        return depth;
      }
      if (component instanceof AbstractUITreeData) {
        Object dataTree = ((AbstractUITreeData) component).getValue();
        // todo: make independent from impl.
        if (dataTree instanceof DefaultMutableTreeNode) {
          return ((DefaultMutableTreeNode) dataTree).getDepth(); // XXX expensive
        }
        LOG.warn("Tree type not supported");
      }
      component = component.getParent();
    }
    throw new RuntimeException("Not inside of a UITree");
  }

  private boolean computeFolder() {
    DefaultMutableTreeNode node = getDataNode();
    if (node != null) {
      return !node.isLeaf();
    } else {
      for (UIComponent child : (List<UIComponent>) getChildren()) {
        if ((child instanceof AbstractUITreeNode || child instanceof AbstractUITreeData) && child.isRendered()) {
          return true;
        }
      }
      return false;
    }
  }

  private boolean computeHasNextSibling() {
    DefaultMutableTreeNode node = getDataNode();
    if (node != null) {
      if (node.isRoot()) {
        return hasSiblingAfter(getParent().getParent(), getParent());
      } else {
        return node.getNextSibling() != null;
      }
    } else {
      return hasSiblingAfter(getParent(), this);
    }
  }

  private boolean hasSiblingAfter(UIComponent parent, UIComponent child) {
    boolean found = false;
    for (Object sibling : parent.getChildren()) {
      if (child.equals(sibling)) {
        found = true;
        continue;
      }
      if (found) {
        return true;
      }
    }
    return false;
  }

  /**
   * Finds the value of the current node via the var attribute of the tree data.
   * Returns null if it will be called not inside of {@link AbstractUITreeData}
   */
  // todo: make independent from impl.: DefaultMutableTreeNode
  private DefaultMutableTreeNode getDataNode() {
    UIComponent component = this;
    while (component != null) {
      if (component instanceof AbstractUITreeData) {
        final AbstractUITreeData data = (AbstractUITreeData) component;
        final Object currentNode
            = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(data.getVar());
        return (DefaultMutableTreeNode) currentNode;
      } else if (component instanceof AbstractUITree) {
        return null;
      }
      component = component.getParent();
    }
    return null;
  }

  @Override
  public Object getValue() {
    LOG.error("XXXXXXXXXXX should not be called!!!!!!!!!!!!");
    return super.getValue();
  }

  /**
   * Returns the level of the tree node inside of the virtual tree. The root node has level 0.
   * The children of the root note have level 1, and so on. 
   */
  public int getLevel() {
    return path.getLength() - 1;
  }

  public String nodeStateId(FacesContext facesContext) {
    String clientId = getClientId(facesContext);
    AbstractUITree tree = ComponentUtils.findAncestor(this, AbstractUITree.class);
    String treeId = tree.getClientId(facesContext);
    return clientId.substring(treeId.length() + 1);
  }

  @Override
  public void broadcast(FacesEvent event) throws AbortProcessingException {
    super.broadcast(event);
    if (event instanceof TreeExpansionEvent) {
      FacesUtils.invokeMethodBinding(getFacesContext(), getTreeExpansionListener(), event);
      boolean expanded = ((TreeExpansionEvent) event).isNewExpanded();

      if (FacesUtils.hasValueBindingOrValueExpression(this, Attributes.EXPANDED)) {
        try {
          FacesUtils.setValueOfBindingOrExpression(getFacesContext(), expanded, this, Attributes.EXPANDED);
        } catch (Exception e) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Can't set expanded.", e);
          }
        }
      } else {
        setExpanded(expanded);
      }
    }
    if (event instanceof TreeMarkedEvent) {
      FacesUtils.invokeMethodBinding(getFacesContext(), getTreeMarkedListener(), event);
      boolean marked = ((TreeMarkedEvent) event).isNewMarked();

      if (FacesUtils.hasValueBindingOrValueExpression(this, Attributes.MARKED)) {
        try {
          FacesUtils.setValueOfBindingOrExpression(getFacesContext(), marked, this, Attributes.MARKED);
        } catch (Exception e) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Can't set marked.", e);
          }
        }
      } else {
        setMarked(marked);
      }
    }
  }

  public void restoreState(FacesContext context, Object componentState) {
    Object[] values = (Object[]) componentState;
    super.restoreState(context, values[0]);
    path = (TreePath) values[1];
    folder = (Boolean) values[2];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[3];
    values[0] = super.saveState(context);
    values[1] = path;
    values[2] = folder;
    return values;
  }

  public int getDepth() {
    return depth;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public boolean isFolder() {
    return folder;
  }

  public void setFolder(boolean folder) {
    this.folder = folder;
  }

  public TreePath getPath() {
    return path;
  }

  public void setPath(TreePath path) {
    this.path = path;
  }

  public List<Boolean> getJunctions() {
    return junctions;
  }

  public void setJunctions(List<Boolean> junctions) {
    this.junctions = junctions;
  }

  public boolean isHasNextSibling() {
    return hasNextSibling;
  }

  public void setHasNextSibling(boolean hasNextSibling) {
    this.hasNextSibling = hasNextSibling;
  }

  public abstract MethodBinding getTreeExpansionListener();

  public abstract void setTreeExpansionListener(MethodBinding treeExpansionListener);

  public void addTreeExpansionListener(TreeExpansionListener listener) {
    addFacesListener(listener);
  }

  public TreeExpansionListener[] getTreeExpansionListeners() {
    return (TreeExpansionListener[]) getFacesListeners(TreeExpansionListener.class);
  }

  public void removeStateChangeListener(TreeExpansionListener listener) {
    removeFacesListener(listener);
  }

  public abstract MethodBinding getTreeMarkedListener();

  public abstract void setTreeMarkedListener(MethodBinding treeMarkedListener);

  public void addTreeMarkedListener(TreeMarkedListener listener) {
    addFacesListener(listener);
  }

  public TreeMarkedListener[] getTreeMarkedListeners() {
    return (TreeMarkedListener[]) getFacesListeners(TreeMarkedListener.class);
  }

  public void removeStateChangeListener(TreeMarkedListener listener) {
    removeFacesListener(listener);
  }

  public abstract boolean isMarked();

  public abstract void setMarked(boolean b);

  public abstract boolean isExpanded();

  public abstract void setExpanded(boolean expanded);

  public abstract boolean isSelected();

  public abstract void setSelected(boolean selected);
}
