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

import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Enumeration;

public class UITreeNodes extends javax.faces.component.UIInput {

  private static final Log LOG = LogFactory.getLog(UITreeNodes.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TreeNodes";

  private String var;

  private DefaultMutableTreeNode currentNode;

  private String currentNodeId;

  private String currentParentNodeId;


  @Override
  public void decode(FacesContext context) {
    super.decode(context);
  }

  @Override
  public void encodeChildren(FacesContext context)
      throws IOException {
  }

  @Override
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

    currentParentNodeId = position;
    position += ":" + index;
    currentNodeId = position;

    currentNode = node;
    if (var == null) {
      LOG.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      LOG.error("var not set");
      LOG.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      var = "node";
    }
    facesContext.getExternalContext().getRequestMap().put(var, currentNode);
    RenderUtil.encode(facesContext, getTemplateComponent());
    facesContext.getExternalContext().getRequestMap().remove(var);
    currentNode = null;

    index = 0;
    for (Enumeration e = node.children(); e.hasMoreElements();) {
      DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) e.nextElement();
      encodeNodes(subNode, position, index, facesContext);
      index++;
    }
  }

  public boolean getRendersChildren() {
    return true;
  }

  public void processDecodes(FacesContext facesContext) {
    LOG.info("processDecodes for nodes");
    LOG.warn("todo"); // todo
    super.processDecodes(facesContext);
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

  @Override
  public Object saveState(FacesContext context) {
    Object[] state = new Object[2];
    state[0] = super.saveState(context);
    state[1] = var;
    return state;
  }

  @Override
  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    var = (String) values[1];
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public DefaultMutableTreeNode getCurrentNode() {
    return currentNode;
  }

  public void setCurrentNode(DefaultMutableTreeNode currentNode) {
    this.currentNode = currentNode;
  }

  public String getCurrentNodeId() {
    return currentNodeId;
  }

  public void setCurrentNodeId(String currentNodeId) {
    this.currentNodeId = currentNodeId;
  }

  public String getCurrentParentNodeId() {
    return currentParentNodeId;
  }

  public void setCurrentParentNodeId(String currentParentNodeId) {
    this.currentParentNodeId = currentParentNodeId;
  }
}
