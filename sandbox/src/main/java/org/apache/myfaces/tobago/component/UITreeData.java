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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.model.MixedTreeModel;
import org.apache.myfaces.tobago.model.TreeModel;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UITreeData extends javax.faces.component.UIInput
    implements NamingContainer, TreeModelBuilder {

  private static final Log LOG = LogFactory.getLog(UITreeData.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TreeData";

  private String var;

  private TreeModel treeModel;

  private DefaultMutableTreeNode currentNode;

  private String currentNodeId;

  private String currentParentNodeId;

  private String pathIndex;

  // XXX hack: fix this if there is a Listener
  private Object marker;

//  @Override public void processDecodes(FacesContext facesContext) {
//    LOG.info("processDecodes for nodes");
//    LOG.warn("todo"); // todo
//    super.processDecodes(facesContext);
//  }

  // Holds for each node the states of the child components of this UITreeData.
  private Map<String, Object> pathStates = new HashMap<String, Object>();

  @Override
  public void processDecodes(FacesContext facesContext) {

    // todo: does treeModel should be stored in the state?
    // todo: what is, when the value has been changed since last rendering?
    treeModel = new TreeModel((DefaultMutableTreeNode) getValue());

    for (String pathIndex : treeModel.getPathIndexList()) {
      setPathIndex(pathIndex);
      UITreeNode node = getTemplateComponent();
      node.processDecodes(facesContext);

      // XXX hack: fix this if there is a Listener
      if (node.isMarked()) {
        marker = node.getValue();
      }

      setPathIndex(null);
    }
  }

  @Override
  public void decode(FacesContext facesContext) {

  }

  public String getPathIndex() {
    return pathIndex;
  }

  private void setPathIndex(String pathIndex) {

    if (StringUtils.equals(this.pathIndex, pathIndex)) {
      return; // nothing to do, if already set.
    }

    FacesContext facesContext = FacesContext.getCurrentInstance();

    UITreeNode template = getTemplateComponent();
    pathStates.put(this.pathIndex, template.saveState(facesContext));
    if (LOG.isDebugEnabled()) {
      LOG.debug("save   " + this.pathIndex + " ex=" + template.isExpanded());
    }

    // reset the client id (see spec 3.1.6)
    template.setId(template.getId());

    this.pathIndex = pathIndex;
    if (pathIndex != null) {
      currentNode = treeModel.getNode(pathIndex);
      facesContext.getExternalContext().getRequestMap().put(var, currentNode);
      // todo: remove currentNodeId
      currentNodeId = pathIndex;
      currentParentNodeId = treeModel.getParentPathIndex(pathIndex);
    } else {
      FacesContext.getCurrentInstance().getExternalContext().getRequestMap().remove(var);
      currentNode = null;
      currentNodeId = null;
      currentParentNodeId = null;
    }

    Object state = pathStates.get(this.pathIndex);
    if (state != null) {
      template.restoreState(facesContext, state);
      if (LOG.isDebugEnabled()) {
        LOG.debug("restore " + this.pathIndex + " ex=" + template.isExpanded());
      }
    }

  }

  public void buildBegin(MixedTreeModel model) {
    model.beginBuildNodeData(this);
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
    model.endBuildNodeData(this);
  }

  @Override
  public void encodeChildren(FacesContext context) throws IOException {
  }

  @Override
  public void encodeEnd(FacesContext facesContext) throws IOException {

    // todo: does treeModel should be stored in the state?
    treeModel = new TreeModel((DefaultMutableTreeNode) getValue());

    for (TreeModel.Tag pathIndex : treeModel.getDoublePathIndexList()) {
      setPathIndex(pathIndex.getName());
      if (pathIndex.isStart()) {
        getTemplateComponent().encodeBegin(facesContext);
      } else {
        getTemplateComponent().encodeEnd(facesContext);
      }
//      RenderUtil.encode(facesContext, getTemplateComponent());
      setPathIndex(null);
    }

    super.encodeEnd(facesContext);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
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
  public String getClientId(FacesContext context) {
    String clientId = super.getClientId(context);
    if (pathIndex == null) {
      return clientId;
    }
    return clientId + NamingContainer.SEPARATOR_CHAR + pathIndex;
  }

  public UIComponent findComponent(String searchId) {

    assert searchId.matches("^(_\\d+)+" + SEPARATOR_CHAR + ".*")
        : "The searchId '" + searchId + "' does not start with a tree structure";

    searchId = searchId.substring(searchId.indexOf(SEPARATOR_CHAR) + 1);
    return super.findComponent(searchId);
  }

  @Override
  public void queueEvent(FacesEvent event) {
    super.queueEvent(new FacesEventWrapper(event, getPathIndex(), this));
  }

  @Override
  public void broadcast(FacesEvent event) throws AbortProcessingException {
    if (event instanceof FacesEventWrapper) {
      FacesEvent originalEvent = ((FacesEventWrapper) event).getWrappedFacesEvent();
      String eventPathIndex = ((FacesEventWrapper) event).getPathIndex();
      String currentPathIndex = getPathIndex();
      setPathIndex(eventPathIndex);
      try {
        originalEvent.getComponent().broadcast(originalEvent);
      } finally {
        setPathIndex(currentPathIndex);
      }
    } else {
      super.broadcast(event);
    }
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

  private static class FacesEventWrapper extends FacesEvent {

    private static final long serialVersionUID = 1L;

    private FacesEvent wrappedFacesEvent;
    private String pathIndex;

    FacesEventWrapper(FacesEvent facesEvent, String pathIndex, UITreeData redirectComponent) {
      super(redirectComponent);
      wrappedFacesEvent = facesEvent;
      this.pathIndex = pathIndex;
    }

    @Override
    public PhaseId getPhaseId() {
      return wrappedFacesEvent.getPhaseId();
    }

    @Override
    public void setPhaseId(PhaseId phaseId) {
      wrappedFacesEvent.setPhaseId(phaseId);
    }

    @Override
    public void queue() {
      wrappedFacesEvent.queue();
    }

    @Override
    public String toString() {
      return wrappedFacesEvent.toString();
    }

    @Override
    public boolean isAppropriateListener(
        FacesListener faceslistener) {
      return wrappedFacesEvent.isAppropriateListener(faceslistener);
    }

    @Override
    public void processListener(FacesListener faceslistener) {
      wrappedFacesEvent.processListener(faceslistener);
    }

    public FacesEvent getWrappedFacesEvent() {
      return wrappedFacesEvent;
    }

    public String getPathIndex() {
      return pathIndex;
    }
  }

  // XXX hack: fix this if there is a Listener
  public Object getMarker() {
    return marker;
  }
}
