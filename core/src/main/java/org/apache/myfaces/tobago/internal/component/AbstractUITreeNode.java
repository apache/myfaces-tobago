package org.apache.myfaces.tobago.internal.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.TreeModelBuilder;
import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.apache.myfaces.tobago.event.TreeExpansionListener;
import org.apache.myfaces.tobago.model.MixedTreeModel;
import org.apache.myfaces.tobago.model.TreePath;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.List;

public abstract class AbstractUITreeNode extends UICommandBase implements SupportsMarkup, TreeModelBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUITreeNode.class);

  private int depth;
  private boolean folder;
  private TreePath path;
  private List<Boolean> junctions;
  private boolean hasNextSibling;

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  public void buildTreeModelBegin(FacesContext facesContext, MixedTreeModel model) {
    model.beginBuildNode(this);
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
    model.endBuildNode(this);
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    AbstractUITree tree = findTree();
    MixedTreeModel mixedModel = tree.getModel();
    mixedModel.onEncodeBegin();
    setPath(mixedModel.getPath());
    setHasNextSibling(computeHasNextSibling());
    setJunctions(mixedModel.getJunctions());
    super.encodeBegin(context);
  }

  @Override
  public void encodeEnd(FacesContext context) throws IOException {
    super.encodeEnd(context);
    AbstractUITree tree = findTree();
    MixedTreeModel mixedModel = tree.getModel();
    mixedModel.onEncodeEnd();
  }

  private int computeDepth(UIComponent node) {
    int depth = 0;
    while (node != null) {
      depth++;
      if (node instanceof AbstractUITree) {
        return depth;
      }
      if (node instanceof AbstractUITreeData) {
        Object dataTree = ((AbstractUITreeData) node).getValue();
        // todo: make independent from impl.
        if (dataTree instanceof DefaultMutableTreeNode) {
          return ((DefaultMutableTreeNode) dataTree).getDepth();
        }
        LOG.warn("Tree type not supported");
      }
      node = node.getParent();
    }
    throw new RuntimeException("Not inside of a UITree");
  }

  private boolean computeFolder() {
    if (isInData()) {
      Object value = getValue();
      // todo: make independent from impl.
      if (value instanceof DefaultMutableTreeNode) {
        return !((DefaultMutableTreeNode) value).isLeaf();
      }
      LOG.warn("Tree type not supported");
      return true;
    } else {
      return getChildCount() != 0;
    }
  }

  private boolean computeHasNextSibling() {
    if (isInData()) {
      Object value = getValue();
      // todo: make independent from impl.
      if (value instanceof DefaultMutableTreeNode) {

        DefaultMutableTreeNode tree = (DefaultMutableTreeNode) value;
        if (tree.isRoot()) {
          return hasSiblingAfter(getParent().getParent(), getParent());
        } else {
          return tree.getNextSibling() != null;
        }
      }
      LOG.warn("Tree type not supported");
      return false;
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

  private boolean isInData() {
    UIComponent component = this;
    while (component != null) {
      if (component instanceof AbstractUITreeData) {
        return true;
      } else if (component instanceof AbstractUITree) {
        return false;
      }
      component = component.getParent();
    }
    return false;
  }

  @Override
  public Object getValue() {
    DefaultMutableTreeNode value = (DefaultMutableTreeNode) super.getValue();
    if (value == null) { // XXX: hack!
      value = new DefaultMutableTreeNode();
      value.setUserObject(System.identityHashCode(value));
      setValue(value);
      if (LOG.isInfoEnabled()) {
        LOG.info("Created temporary Node: " + value.getUserObject());
      }
    }
    return value;
  }

  public String nodeStateId(FacesContext facesContext) {
    String clientId = getClientId(facesContext);
    AbstractUITree tree = findTree(this);
    String treeId = tree.getClientId(facesContext);
    return clientId.substring(treeId.length() + 1);
  }

  public AbstractUITree findTree() {
    return findTree(this);
  }

  private AbstractUITree findTree(UIComponent component) {
    while (component != null) {
      if (component instanceof AbstractUITree) {
        return (AbstractUITree) component;
      }
      component = component.getParent();
    }
    return null;
  }

  @Override
  public void broadcast(FacesEvent event) throws AbortProcessingException {
    super.broadcast(event);
    if (event instanceof TreeExpansionEvent) {
      invokeMethodBinding(getTreeExpansionListener(), event);
    }
  }

  private void invokeMethodBinding(MethodBinding methodBinding, FacesEvent event) {
    if (methodBinding != null && event != null) {
      try {
        methodBinding.invoke(getFacesContext(), new Object[]{event});
      } catch (EvaluationException e) {
        Throwable cause = e.getCause();
        if (cause instanceof AbortProcessingException) {
          throw (AbortProcessingException) cause;
        } else {
          throw e;
        }
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


  public abstract void setExpanded(boolean expanded);

  public abstract void setMarked(boolean b);

  public abstract boolean isMarked();

  public abstract boolean isExpanded();
}
