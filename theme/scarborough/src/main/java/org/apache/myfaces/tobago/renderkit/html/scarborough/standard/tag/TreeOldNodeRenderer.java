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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITreeOld;
import org.apache.myfaces.tobago.component.UITreeOldNode;
import org.apache.myfaces.tobago.model.TreeState;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;

import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Map;

@Deprecated
public class TreeOldNodeRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(TreeOldNodeRenderer.class);

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UITreeOldNode node = (UITreeOldNode) component;
    UITreeOld tree = node.findTreeRoot();
    TreeState state = tree.getState();
    String treeId = tree.getClientId(facesContext);
    String nodeId = node.getId();
    final Map requestParameterMap
        = facesContext.getExternalContext().getRequestParameterMap();

    // expand state
    String expandState = (String) requestParameterMap.get(treeId);
    String searchString = ";" + nodeId + ";";
    if (StringUtils.contains(expandState, searchString)) {
      state.addExpandState((DefaultMutableTreeNode) node.getValue());
    }


    if (TreeOldRenderer.isSelectable(tree)) { // selection
      String selected = (String) requestParameterMap.get(treeId + UITreeOld.SELECT_STATE);
      searchString = ";" + nodeId + ";";
      if (StringUtils.contains(selected, searchString)) {
        state.addSelection((DefaultMutableTreeNode) node.getValue());
      }
    }

    // marker
    String marked = (String) requestParameterMap.get(treeId + UITreeOld.MARKER);
    if (marked != null) {
      searchString = treeId + NamingContainer.SEPARATOR_CHAR + nodeId;

      if (marked.equals(searchString)) {
        state.setMarker((DefaultMutableTreeNode) node.getValue());
      }
    }


    // link
    // FIXME: this is equal to the CommandRendererBase, why not use that code?
    String actionId = ComponentUtil.findPage(facesContext, component).getActionId();
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + actionId + "'");
      LOG.debug("nodeId = '" + treeId + NamingContainer.SEPARATOR_CHAR
          + nodeId + "'");
    }
    if (actionId != null
        && actionId.equals(treeId + NamingContainer.SEPARATOR_CHAR + nodeId)) {
      UICommand treeNodeCommand
          = (UICommand) tree.getFacet(UITreeOld.FACET_TREE_NODE_COMMAND);
      if (treeNodeCommand != null) {
        UIParameter parameter = ensureTreeNodeParameter(treeNodeCommand);
        parameter.setValue(node.getId());
//        LOG.error("no longer supported: treeNodeCommand.fireActionEvent(facesContext));");
//        treeNodeCommand.fireActionEvent(facesContext); // FIXME jsfbeta
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

  private UIParameter ensureTreeNodeParameter(UICommand command) {
    UIParameter treeNodeParameter = null;
    for (Object o : command.getChildren()) {
      UIComponent component = (UIComponent) o;
      if (component instanceof UIParameter) {
        UIParameter parameter = (UIParameter) component;
        if (parameter.getName().equals(UITreeOld.PARAMETER_TREE_NODE_ID)) {
          treeNodeParameter = parameter;
        }
      }
    }
    if (treeNodeParameter == null) {
      treeNodeParameter = new UIParameter();
      treeNodeParameter.setName(UITreeOld.PARAMETER_TREE_NODE_ID);
    }
    return treeNodeParameter;
  }

  public void encodeBegin(FacesContext facesContext,
                                UIComponent component) throws IOException {

    UITreeOldNode treeNode = (UITreeOldNode) component;

    String clientId = treeNode.getClientId(facesContext);
    UIComponent parent = treeNode.getParent();

    String parentClientId = null;
    if (parent != null && parent instanceof UITreeOldNode) { // if not the root node
      parentClientId = treeNode.getParent().getClientId(facesContext);
    }

    UITreeOld root = treeNode.findTreeRoot();
    String rootId = root.getClientId(facesContext);

    String jsClientId = TreeOldRenderer.createJavascriptVariable(clientId);
    String jsParentClientId = TreeOldRenderer.createJavascriptVariable(
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

      writer.writeText("  var ", null);
      writer.writeText(jsClientId, null);
      writer.writeText(" = new TreeOldNode('", null);
      // label
      Object name = treeNode.getAttributes().get(TobagoConstants.ATTR_NAME);
      if (LOG.isDebugEnabled()) {
        debuging += name + " : ";
      }
      if (name != null) {
        writer.writeText(StringEscapeUtils.escapeJavaScript(name.toString()), null);
      } else {
        LOG.warn("name = null");
      }
      writer.writeText("',", null);

      // tip
      Object tip = treeNode.getAttributes().get(TobagoConstants.ATTR_TIP);
      if (tip != null) {
        tip = StringEscapeUtils.escapeJavaScript(tip.toString());
        writer.writeText("'", null);
        writer.writeText((String) tip, null);
        writer.writeText("','", null);
      } else {
        writer.writeText("null,'", null);
      }

      // id
      writer.writeText(clientId, null);
      writer.writeText("','", null);

      writer.writeText(root.getMode(), null);
      writer.writeText("',", null);

      // is folder
      writer.writeText(component.getChildCount() > 0, null);
      writer.writeText(",", null);
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
      String selectable = ComponentUtil.getStringAttribute(root,
          TobagoConstants.ATTR_SELECTABLE);
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
          TobagoConstants.ATTR_MUTABLE)), null);
      writer.writeText(",'", null);
      writer.writeText(
          ComponentUtil.findPage(facesContext, component).getFormId(facesContext), null);
      writer.writeText("',", null);
      if (component.getChildCount() == 0
          || (selectable != null && !selectable.endsWith("LeafOnly"))) {
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
      writer.writeText(",", null);
      // expanded
      boolean expanded = treeState.isExpanded(node);
      writer.writeText(Boolean.toString(expanded), null);
      if (LOG.isDebugEnabled()) {
        debuging += expanded ? "E" : "-";
      }
      writer.writeText(",", null);

      // required
      writer.writeText(Boolean.toString(root.isRequired()), null);
      writer.writeText(",", null);

      // disabled
      writer.writeText(ComponentUtil.getBooleanAttribute(treeNode,
          TobagoConstants.ATTR_DISABLED), null);

      // resources
      writer.writeText(",treeResourcesHelp", null);
      writer.writeText(",", null);

      // action (obsolete)
      writer.writeText("null", null);
      writer.writeText(",", null);

      // onclick (treeNodeCommand)
      String treeNodeCommandClientId = root.getClientId(facesContext);
      String treeNodeCommandJsClientId = TreeOldRenderer.createJavascriptVariable(treeNodeCommandClientId);

      writer.writeText(treeNodeCommandJsClientId + "_treeNodeCommand", null);
      writer.writeText(",", null);

      // parent
      if (jsParentClientId != null) {
        writer.writeText(jsParentClientId, null);
      } else {
        writer.writeText("null", null);
      }
      writer.writeText(",", null);

      // icon (not implemented)
      writer.writeText("null", null);
      writer.writeText(",", null);

      // open folder icon (not implemented)
      writer.writeText("null", null);
      writer.writeText(",'", null);

      // width
      Integer width = null;
      HtmlStyleMap style = (HtmlStyleMap) root.getAttributes().get(ATTR_STYLE);
      if (style != null) {
        width = style.getInt("width");
      }
      if (width != null) {
        writer.writeText(width - 4, null); // fixme: 4
      } else {
        writer.writeText("100%", null);
      }
      writer.writeText("',", null);

      // tabIndex
      if (root.getTabIndex() != null) {
        writer.writeText(root.getTabIndex(), null);
      } else {
        writer.writeText("null", null);
      }

      writer.writeText(");\n", null);

/*
      if (jsParentClientId != null) { // if not the root node
        writer.writeText("  ", null);
        writer.writeText(jsParentClientId, null);
        writer.writeText(".add(", null);
        writer.writeText(jsClientId, null);
        writer.writeText(");\n", null);
      }
*/
      if (LOG.isDebugEnabled()) {
        LOG.debug(debuging);
      }
    }
  }
}
