/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UITree;
import com.atanion.tobago.component.UITreeNode;
import com.atanion.tobago.model.TreeState;
import com.atanion.tobago.renderkit.RendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletRequest;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Iterator;

public class TreeNodeRenderer extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TreeNodeRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UITreeNode node = (UITreeNode) component;
    UITree tree = node.findTreeRoot();
    TreeState state = (TreeState) tree.getValue();
    String treeId = tree.getClientId(facesContext);
    String nodeId = node.getId();
    ServletRequest request
        = (ServletRequest) facesContext.getExternalContext().getRequest();

    { // expand state
      String expandState = request.getParameter(treeId);
      String searchString = ";" + nodeId + ";";
      if (expandState.indexOf(searchString) > -1) {
        state.addExpandState((DefaultMutableTreeNode) node.getValue());
      }
    }

    if (TreeRenderer.isSelectable(tree)) { // selection
      String selected = request.getParameter(treeId + UITree.SELECT_STATE);
      String searchString = ";" + nodeId + ";";
      if (selected.indexOf(searchString) > -1) {
        state.addSelection((DefaultMutableTreeNode) node.getValue());
      }
    }

    { // marker
      String marked = request.getParameter(treeId + UITree.MARKER);
      if (marked != null) {
        String searchString = treeId + NamingContainer.SEPARATOR_CHAR + nodeId;

        if (marked.equals(searchString)) {
          state.setMarker((DefaultMutableTreeNode) node.getValue());
        }
      }
    }

    // link
    // fixme: this is equal to the CommandRendererBase, why not use that code?
    String actionId = ComponentUtil.findPage(component).getActionId();
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + actionId + "'");
      LOG.debug("nodeId = '" + treeId + NamingContainer.SEPARATOR_CHAR +
          nodeId +
          "'");
    }
    if (actionId != null &&
        actionId.equals(treeId + NamingContainer.SEPARATOR_CHAR + nodeId)) {
      UICommand treeNodeCommand
          = (UICommand) tree.getFacet(UITree.FACET_TREE_NODE_COMMAND);
      if (treeNodeCommand != null) {
        UIParameter parameter = ensureTreeNodeParameter(treeNodeCommand);
        parameter.setValue(node.getId());
//        LOG.error("no longer supported: treeNodeCommand.fireActionEvent(facesContext));");
//        treeNodeCommand.fireActionEvent(facesContext); // fixme jsfbeta
//        component.queueEvent(new ActionEvent(component));
        treeNodeCommand.queueEvent(new ActionEvent(treeNodeCommand));
      }

      UIForm form = ComponentUtil.findForm(component);
      if (form != null) {
        form.setSubmitted(true);
        if (LOG.isDebugEnabled()) {
          LOG.debug("setting Form Active: " + form.getClientId(facesContext));
        }
      }

      if (LOG.isDebugEnabled()) {
        LOG.debug("actionId: " + actionId);
        LOG.debug("nodeId: " + nodeId);
      }
    }

    node.setValid(true);
  }

  private static UIParameter ensureTreeNodeParameter(UICommand command) {
    UIParameter treeNodeParameter = null;
    for (Iterator i = command.getChildren().iterator(); i.hasNext();) {
      UIComponent component = (UIComponent) i.next();
      if (component instanceof UIParameter) {
        UIParameter parameter = (UIParameter) component;
        if (parameter.getName().equals(UITree.PARAMETER_TREE_NODE_ID)) {
          treeNodeParameter = parameter;
        }
      }
    }
    if (treeNodeParameter == null) {
      treeNodeParameter = new UIParameter();
      treeNodeParameter.setName(UITree.PARAMETER_TREE_NODE_ID);
    }
    return treeNodeParameter;
  }

  public void encodeBeginTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    UITreeNode treeNode = (UITreeNode) component;

    String clientId = treeNode.getClientId(facesContext);
    UIComponent parent = treeNode.getParent();

    String parentClientId = null;
    if (parent != null && parent instanceof UITreeNode) { // if not the root node
      parentClientId = treeNode.getParent().getClientId(facesContext);
    }

    UITree root = treeNode.findTreeRoot();
    String rootId = root.getClientId(facesContext);

    String jsClientId = TreeRenderer.createJavascriptVariable(clientId);
    String jsParentClientId = TreeRenderer.createJavascriptVariable(
        parentClientId);
//  rootId = HtmlUtils.createJavascriptVariable(rootId);

    TreeState treeState = (TreeState) root.getValue();
    if (treeState == null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("No treeState found. clientId=" + clientId);
      }
    } else {

      DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNode.getValue();

      ResponseWriter writer = facesContext.getResponseWriter();

      writer.writeText("var ", null);
      writer.writeText(jsClientId, null);
      writer.writeText(" = new ", null);
      if (component.getChildCount() == 0) {
        writer.writeText("TreeNode", null);
      } else {
        writer.writeText("TreeFolder", null);
      }
      writer.writeText("('", null);
      writer.writeText(treeNode.getAttributes().get(ATTR_NAME),
          null);
      writer.writeText("','", null);
      writer.writeText(clientId, null);
      writer.writeText("',", null);
      writer.writeText(Boolean.toString(!root.isShowIcons()), null);
      writer.writeText(",", null);
      writer.writeText(Boolean.toString(!root.isShowJunctions()), null);
      writer.writeText(",", null);
      writer.writeText(Boolean.toString(!root.isShowRootJunction()), null);
      writer.writeText(",", null);
      writer.writeText(Boolean.toString(!root.isShowRoot()), null);
      writer.writeText(",'", null);
      writer.writeText(rootId, null);
      writer.writeText("',", null);
      Object selectable = ComponentUtil.getAttribute(root, ATTR_SELECTABLE);
      if (selectable != null
          && (!(selectable.equals("multi") || selectable.equals("single")))) {
        selectable = null;
      }
      if (selectable != null) {
        writer.writeText("'", null);
        writer.writeText(selectable, null);
        writer.writeText("'", null);
      } else {
        writer.writeText("false", null);
      }
      writer.writeText(",", null);
      writer.writeText(Boolean.toString(ComponentUtil.getBooleanAttribute(root,
          ATTR_MUTABLE)), null);
      writer.writeText(",'", null);
      writer.writeText(
          ComponentUtil.findPage(component).getFormId(facesContext), null);
      writer.writeText("',", null);
      writer.writeText(Boolean.toString(treeState.isSelected(node)), null);
      writer.writeText(",", null);
      writer.writeText(Boolean.toString(treeState.isMarked(node)), null);
      if (component.getChildCount() > 0) {
        writer.writeText(",", null);
        writer.writeText(Boolean.toString(treeState.isExpanded(node)), null);
      }
      writer.writeText(",treeResourcesHelp);\n", null);

      if (jsParentClientId != null) { // if not the root node
        writer.writeText(jsParentClientId, null);
        writer.writeText(".add(", null);
        writer.writeText(jsClientId, null);
        writer.writeText(");\n", null);
      }
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
