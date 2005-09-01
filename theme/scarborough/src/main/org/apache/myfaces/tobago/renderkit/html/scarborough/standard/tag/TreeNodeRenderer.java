/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.model.TreeState;
import org.apache.myfaces.tobago.renderkit.RendererBase;

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
import java.util.Map;

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
    TreeState state = tree.getState();
    String treeId = tree.getClientId(facesContext);
    String nodeId = node.getId();
    final Map requestParameterMap
        = facesContext.getExternalContext().getRequestParameterMap();

    { // expand state
      String expandState = (String) requestParameterMap.get(treeId);
      String searchString = ";" + nodeId + ";";
      if (expandState.indexOf(searchString) > -1) {
        state.addExpandState((DefaultMutableTreeNode) node.getValue());
      }
    }

    if (TreeRenderer.isSelectable(tree)) { // selection
      String selected = (String) requestParameterMap.get(treeId + UITree.SELECT_STATE);
      String searchString = ";" + nodeId + ";";
      if (selected.indexOf(searchString) > -1) {
        state.addSelection((DefaultMutableTreeNode) node.getValue());
      }
    }

    { // marker
      String marked = (String) requestParameterMap.get(treeId + UITree.MARKER);
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

    TreeState treeState = root.getState();
    if (treeState == null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("No treeState found. clientId=" + clientId);
      }
    } else {

      DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNode.getValue();

      ResponseWriter writer = facesContext.getResponseWriter();

      String debuging = null;

      writer.writeText("var ", null);
      writer.writeText(jsClientId, null);
      writer.writeText(" = new ", null);
      if (component.getChildCount() == 0) {
        writer.writeText("TreeNode", null);
      } else {
        writer.writeText("TreeFolder", null);
      }
      writer.writeText("('", null);
      Object name = treeNode.getAttributes().get(ATTR_NAME);
      if (LOG.isDebugEnabled()) {
        debuging += name + " : ";
      }
      if (name != null) {
        writer.writeText(StringEscapeUtils.escapeJavaScript(name.toString()), null);
      } else {
        LOG.warn("name = null");
      }
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
      String selectable = ComponentUtil.getStringAttribute(root, ATTR_SELECTABLE) ;
      if (selectable != null
          && (!(selectable.equals("multi") || selectable.equals("multiLeafOnly")
          || selectable.equals("single") || selectable.equals("singleLeafOnly")
          || selectable.equals("sibling") || selectable.equals("siblingLeafOnly")))) {
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
      if (component.getChildCount() == 0
          || (selectable != null && ! selectable.endsWith("LeafOnly"))) {
        boolean selected = treeState.isSelected(node);
        writer.writeText(Boolean.toString(selected), null);
        if (LOG.isDebugEnabled()) {
          debuging += selected ? "S" : "-";
        }
      } else {
        writer.writeText("false", null);
        if (LOG.isDebugEnabled()) {
          debuging += "-";
        }
        if (treeState.isSelected(node)) {
          LOG.warn("Ignore selected FolderNode in LeafOnly selection tree!");
        }
      }
      writer.writeText(",", null);
      writer.writeText(Boolean.toString(treeState.isMarked(node)), null);
      if (component.getChildCount() > 0) {
        writer.writeText(",", null);
        boolean expanded = treeState.isExpanded(node);
        writer.writeText(Boolean.toString(expanded), null);
        if (LOG.isDebugEnabled()) {
          debuging += expanded ? "E" : "-";
        }
      }
      writer.writeText(",", null);
      writer.writeText(Boolean.toString(root.isRequired()), null);
      writer.writeText(",treeResourcesHelp);\n", null);

      if (jsParentClientId != null) { // if not the root node
        writer.writeText(jsParentClientId, null);
        writer.writeText(".add(", null);
        writer.writeText(jsClientId, null);
        writer.writeText(");\n", null);
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug(debuging);
      }
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
