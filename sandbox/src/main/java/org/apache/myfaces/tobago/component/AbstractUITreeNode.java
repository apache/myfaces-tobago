package org.apache.myfaces.tobago.component;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.model.MixedTreeModel;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;

public abstract class AbstractUITreeNode extends AbstractUICommand implements SupportsMarkup, TreeModelBuilder {

  private static final Log LOG = LogFactory.getLog(AbstractUITreeNode.class);


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
      LOG.info("Created temporary Node: " + value.getUserObject());
    }
    return value;
  }

  public String nodeStateId(FacesContext facesContext) {
    // this must do the same as nodeStateId() in tree.js
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


  public abstract void setExpanded(boolean expanded);

  public abstract void setMarked(boolean b);

  public abstract boolean isMarked();

  public abstract boolean isExpanded();
}
