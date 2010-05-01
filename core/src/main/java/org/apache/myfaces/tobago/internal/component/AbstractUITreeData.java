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

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.TreeModelBuilder;
import org.apache.myfaces.tobago.model.MixedTreeModel;
import org.apache.myfaces.tobago.model.Node;
import org.apache.myfaces.tobago.model.TreePath;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractUITreeData extends javax.faces.component.UIInput
    implements NamingContainer, TreeModelBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUITreeData.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TreeData";

  private String var;

  private DefaultMutableTreeNode currentNode;

  private TreePath rowIndex;

  // XXX hack: fix this if there is a Listener
  private Object marker;

  // Holds for each node the states of the child components of this UITreeData.
  private Map<TreePath, Object> pathStates = new HashMap<TreePath, Object>();

  @Override
  public void processDecodes(FacesContext facesContext) {

    decodeNodes(facesContext, (DefaultMutableTreeNode) getValue(), new TreePath(0));
  }

  private void decodeNodes(FacesContext facesContext, DefaultMutableTreeNode node, TreePath position) {

    setRowIndex(facesContext, position);
    LOG.debug("path index (decode) = '" + position + "'");
    AbstractUITreeNode templateComponent = getTemplateComponent();
    templateComponent.processDecodes(facesContext);
    setRowIndex(facesContext, null);

    // XXX hack: fix this if there is a Listener
    if (templateComponent.isMarked()) {
      marker = templateComponent.getValue();
    }

    int index = 0;
    for (Enumeration e = node.children(); e.hasMoreElements();) {
      DefaultMutableTreeNode sub = (DefaultMutableTreeNode) e.nextElement();
      decodeNodes(facesContext, sub, new TreePath(position, index));
      index++;
    }
  }

  @Override
  public void decode(FacesContext facesContext) {

  }

  public TreePath getRowIndex() {
    return rowIndex;
  }

  private void setRowIndex(FacesContext facesContext, TreePath rowIndex) {

    if (ObjectUtils.equals(this.rowIndex, rowIndex)) {
      return; // nothing to do, if already set.
    }

    AbstractUITreeNode template = getTemplateComponent();
    pathStates.put(this.rowIndex, template.saveState(facesContext));
    if (LOG.isDebugEnabled()) {
      LOG.debug("save   " + this.rowIndex + " ex=" + template.isExpanded());
    }

    // reset the client id (see spec 3.1.6)
    template.setId(template.getId());

    this.rowIndex = rowIndex;
    if (rowIndex != null) {
      DefaultMutableTreeNode model = (DefaultMutableTreeNode) getValue();
      currentNode = rowIndex.getNode(model);
      facesContext.getExternalContext().getRequestMap().put(var, currentNode);
    } else {
      FacesContext.getCurrentInstance().getExternalContext().getRequestMap().remove(var);
      currentNode = null;
    }

    Object state = pathStates.get(this.rowIndex);
    if (state != null) {
      template.restoreState(facesContext, state);
      if (LOG.isDebugEnabled()) {
        LOG.debug("restore " + this.rowIndex + " ex=" + template.isExpanded());
      }
    }
  }
/*
  public void buildTreeModel(MixedTreeModel model) {
    model.beginBuildNodeData(this);
    // childred are not needed to add here
    model.endBuildNodeData(this);
  }
  */

  public void buildTreeModelBegin(FacesContext facesContext, MixedTreeModel model) {
    Object data = getValue();
    if (data instanceof Node) {
      buildTreeModelNodes(facesContext, model, (Node) data, new TreePath(0));
    } else if (data instanceof DefaultMutableTreeNode) {
      buildTreeModelNodes(facesContext, model, (DefaultMutableTreeNode) data, new TreePath(0));
    }
  }

  public void buildTreeModelChildren(FacesContext facesContext, MixedTreeModel model) {
  }

  public void buildTreeModelEnd(FacesContext facesContext, MixedTreeModel model) {
  }

  public void buildTreeModelNodes(
      FacesContext facesContext, MixedTreeModel model, Node node, TreePath position) {

    setRowIndex(facesContext, position);
    LOG.debug("path index (build) node = '" + position + "'");

    getTemplateComponent().buildTreeModelBegin(facesContext, model);

    int index = 0;
    for (Node sub : node.getChildren()) {
      buildTreeModelNodes(facesContext, model, sub, new TreePath(position, index));
      index++;
    }

    getTemplateComponent().buildTreeModelEnd(facesContext, model);

    setRowIndex(facesContext, null);
  }

  public void buildTreeModelNodes(
      FacesContext facesContext, MixedTreeModel model, DefaultMutableTreeNode node, TreePath position) {

    setRowIndex(facesContext, position);
    LOG.debug("path index (build) dmtn = '" + position + "'");

    getTemplateComponent().buildTreeModelBegin(facesContext, model);

    int index = 0;
    for (Enumeration e = node.children(); e.hasMoreElements();) {
      DefaultMutableTreeNode sub = (DefaultMutableTreeNode) e.nextElement();
      buildTreeModelNodes(facesContext, model, sub, new TreePath(position, index));
      index++;
    }

    getTemplateComponent().buildTreeModelEnd(facesContext, model);

    setRowIndex(facesContext, null);
  }

  @Override
  public void encodeChildren(FacesContext context) throws IOException {
  }

  @Override
  public void encodeEnd(FacesContext facesContext) throws IOException {
    encodeNodes(facesContext, (DefaultMutableTreeNode) getValue(), new TreePath(0));
    super.encodeEnd(facesContext);
  }

  private void encodeNodes(FacesContext facesContext, DefaultMutableTreeNode node, TreePath position)
      throws IOException {

    setRowIndex(facesContext, position);
    LOG.debug("path index (begin)  = '" + position + "'");
    getTemplateComponent().encodeBegin(facesContext);
    setRowIndex(facesContext, null);

    int index = 0;
    for (Enumeration e = node.children(); e.hasMoreElements();) {
      DefaultMutableTreeNode sub = (DefaultMutableTreeNode) e.nextElement();
      encodeNodes(facesContext, sub, new TreePath(position, index));
      index++;
    }

    setRowIndex(facesContext, position);
    LOG.debug("path index (end)    = '" + position + "'");
    getTemplateComponent().encodeEnd(facesContext);
    setRowIndex(facesContext, null);
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

  public AbstractUITreeNode getTemplateComponent() {
    for (Object child : getChildren()) {
      if (child instanceof AbstractUITreeNode) {
        return (AbstractUITreeNode) child;
      }
    }
    return null;
  }

  @Override
  public String getClientId(FacesContext context) {
    String clientId = super.getClientId(context);
    if (rowIndex == null) {
      return clientId;
    }
    return clientId + NamingContainer.SEPARATOR_CHAR + rowIndex;
  }

  public UIComponent findComponent(String searchId) {

    assert searchId.matches("^(_\\d+)+" + SEPARATOR_CHAR + ".*")
        : "The searchId '" + searchId + "' does not start with a tree structure";

    searchId = searchId.substring(searchId.indexOf(SEPARATOR_CHAR) + 1);
    return super.findComponent(searchId);
  }

  @Override
  public void queueEvent(FacesEvent event) {
    super.queueEvent(new FacesEventWrapper(event, getRowIndex(), this));
  }

  @Override
  public void broadcast(FacesEvent event) throws AbortProcessingException {
    if (event instanceof FacesEventWrapper) {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      FacesEvent originalEvent = ((FacesEventWrapper) event).getWrappedFacesEvent();
      TreePath eventPathIndex = ((FacesEventWrapper) event).getRowIndex();
      TreePath currentPathIndex = getRowIndex();
      setRowIndex(facesContext, eventPathIndex);
      try {
        originalEvent.getComponent().broadcast(originalEvent);
      } finally {
        setRowIndex(facesContext, currentPathIndex);
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

  private static class FacesEventWrapper extends FacesEvent {

    private static final long serialVersionUID = 1L;

    private FacesEvent wrappedFacesEvent;
    private TreePath rowIndex;

    FacesEventWrapper(FacesEvent facesEvent, TreePath rowIndex, AbstractUITreeData redirectComponent) {
      super(redirectComponent);
      wrappedFacesEvent = facesEvent;
      this.rowIndex = rowIndex;
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

    public TreePath getRowIndex() {
      return rowIndex;
    }
  }

  // XXX hack: fix this if there is a Listener
  public Object getMarker() {
    return marker;
  }
}
