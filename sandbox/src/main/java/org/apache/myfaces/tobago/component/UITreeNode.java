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
import org.apache.myfaces.tobago.model.MixedTreeModel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.swing.tree.DefaultMutableTreeNode;

public class UITreeNode extends UICommand implements SupportsMarkup, TreeModelBuilder {

  private static final Log LOG = LogFactory.getLog(UITreeNode.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TreeNode";

  private String[] markup;

  private Boolean marked;
  private static final boolean DEFAULT_MARKED = false;

  private Boolean expanded;
  private static final boolean DEFAULT_EXPANDED = false;

  public String[] getMarkup() {
    if (markup != null) {
      return markup;
    }
    return ComponentUtil.getMarkupBinding(getFacesContext(), this);
  }

  public void setMarkup(String[] markup) {
    this.markup = markup;
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  public void buildBegin(MixedTreeModel model) {
    model.beginBuildNode(this);
  }

  public void buildChildren(MixedTreeModel model) {
    for (Object child : getChildren()) {
      if (child instanceof TreeModelBuilder) {
        TreeModelBuilder builder = (TreeModelBuilder) child;
        builder.buildBegin(model);
        builder.buildChildren(model);
        builder.buildEnd(model);
      }
    }
  }

  public void buildEnd(MixedTreeModel model) {
    model.endBuildNode(this);
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
    // this must do the same as nodeStateId() in tree.js
    String clientId = getClientId(facesContext);
    UITree tree = findTree(this);
    String treeId = tree.getClientId(facesContext);
    return clientId.substring(treeId.length() + 1);
  }

  public UITree findTree() {
    return findTree(this);
  }

  private UITree findTree(UIComponent component) {
    while (component != null) {
      if (component instanceof UITree) {
        return (UITree) component;
      }
      component = component.getParent();
    }
    return null;
  }

  @Override
  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    markup = (String[]) values[1];
    marked = (Boolean) values[2];
    expanded = (Boolean) values[3];
  }

  @Override
  public Object saveState(FacesContext context) {
    Object[] values = new Object[4];
    values[0] = super.saveState(context);
    values[1] = markup;
    values[2] = marked;
    values[3] = expanded;
    return values;
  }

  public void setMarked(boolean marked) {
    this.marked = marked;
  }

  public boolean isMarked() {
    if (marked != null) {
      return marked;
    }
    ValueBinding valueBinding = getValueBinding("marked");
    Boolean value = valueBinding != null ? (Boolean) valueBinding.getValue(getFacesContext()) : null;
    return value != null ? value : DEFAULT_MARKED;
  }

  public void setExpanded(boolean expanded) {
    this.expanded = expanded;
  }

  public boolean isExpanded() {
    if (expanded != null) {
      return expanded;
    }
    ValueBinding valueBinding = getValueBinding("expanded");
    Boolean value = valueBinding != null ? (Boolean) valueBinding.getValue(getFacesContext()) : null;
    return value != null ? value : DEFAULT_EXPANDED;
  }

}
