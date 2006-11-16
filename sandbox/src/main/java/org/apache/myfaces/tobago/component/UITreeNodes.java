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
import org.apache.myfaces.tobago.renderkit.RenderUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Iterator;
import java.util.Enumeration;

public class UITreeNodes extends javax.faces.component.UIInput {

  private static final Log LOG = LogFactory.getLog(UITreeNodes.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TreeNodes";

  private String var;

/*
  public void encodeBegin(FacesContext facesContext)
      throws IOException {
    recreateTreeNodes();
    super.encodeBegin(facesContext);
  }

  private void recreateTreeNodes() {
    UITreeNode root = getRoot();
    // Delete all UIComponent childs, because moving of childen will not work
    // in Mutable Tree.
    // They may have invalid modelReferences.
    try {
      if (root != null) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("removing root 1");
        }
        getChildren().remove(root);
        if (LOG.isDebugEnabled()) {
          LOG.debug("removing root 2");
        }
      }
    } catch (Exception e) {
      LOG.error("", e);
    }

    try {
      root = new UITreeNode(this, 0);
      root.createTreeNodes();
    } catch (Exception e) {
      LOG.error(e, e);
    }
  }
*/

  public UITreeNode getRoot() {
    // find the UITreeNode in the childen.
    for (Iterator i = getChildren().iterator(); i.hasNext();) {
      UIComponent child = (UIComponent) i.next();
      if (child instanceof UITreeNode) {
        return (UITreeNode) child;
      }
    }
    // in a new UITree isn't a root
    return null;
  }

  public void encodeChildren(FacesContext context)
      throws IOException {
  }


  public void encodeEnd(FacesContext facesContext) throws IOException {

    DefaultMutableTreeNode tree = (DefaultMutableTreeNode) getValue();

    encodeNodes(tree, "", 0, facesContext);

    super.encodeEnd(facesContext);
  }

  private void encodeNodes(DefaultMutableTreeNode node, String position,
      int index, FacesContext facesContext) throws IOException {

    if (node == null) { // XXX hotfix
      LOG.warn("node is null");
      return;
    }

    UITreeNode template = getTemplateComponent();
    template.setParentNodeId(position);
    position += ":" + index;
    template.setNodeId(position);

    facesContext.getExternalContext().getRequestMap().put(var, node);
    template.setCurrentNode(node);
    RenderUtil.encode(facesContext, template);
    facesContext.getExternalContext().getRequestMap().remove(var);
    template.setCurrentNode(null);

    index = 0;
    for (Enumeration e = node.children(); e.hasMoreElements();) {
      DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) e.nextElement();
      encodeNodes(subNode, position, index, facesContext);
      index++;
    }
  }

  public UITreeNode findUITreeNode(UITreeNode node, TreeNode treeNode) {
    UITreeNode found = null;
    if (node.getTreeNode().equals(treeNode)) {
      return node;
    } else {
      for (Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
        UITreeNode uiTreeNode = (UITreeNode) iter.next();
        found = findUITreeNode(uiTreeNode, treeNode);
        if (found != null) {
          break;
        }
      }
    }
    return found;
  }

  public boolean getRendersChildren() {
    return true;
  }

  public void processDecodes(FacesContext facesContext) {
    if (ComponentUtil.isOutputOnly(this)) {
      setValid(true);
    } else {
      // in tree first decode node and than decode children

      decode(facesContext);

      for (Iterator i = getFacetsAndChildren(); i.hasNext();) {
        UIComponent uiComponent = ((UIComponent) i.next());
        uiComponent.processDecodes(facesContext);
      }
    }
  }

  public void updateModel(FacesContext facesContext) {
    // nothig to update for tree's
    // TODO: updateing the model here and *NOT* in the decode phase
  }

  public UITreeNode getTemplateComponent() {
    for (Object child : getChildren()) {
      if (child instanceof UITreeNode) {
        return (UITreeNode) child;
      }
    }
    return null;
  }


  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }
}
