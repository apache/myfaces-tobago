package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_LINK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MUTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.component.UITreeNodeData;
import org.apache.myfaces.tobago.model.TreeState;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Map;

public class TreeNodeRenderer extends CommandRendererBase {

  private static final Log LOG = LogFactory.getLog(TreeNodeRenderer.class);

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {

    super.decode(facesContext, component);

    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UITreeNode node = (UITreeNode) component;
    UITree tree = node.findTree();
    TreeState state = tree.getState();
    String treeId = tree.getClientId(facesContext);
    String nodeStateId = node.nodeStateId(facesContext);
    Map requestParameterMap
        = facesContext.getExternalContext().getRequestParameterMap();

    // expand state
    String expandState = (String) requestParameterMap.get(treeId);
    String searchString = ";" + nodeStateId + ";";
    if (expandState.indexOf(searchString) > -1) {
      state.addExpandState((DefaultMutableTreeNode) node.getValue());
    }


    if (TreeRenderer.isSelectable(tree)) { // selection
      String selected = (String) requestParameterMap.get(treeId + UITree.SELECT_STATE);
      searchString = ";" + nodeStateId + ";";
      if (selected.indexOf(searchString) > -1) {
        state.addSelection((DefaultMutableTreeNode) node.getValue());
      }
    }

    // marker
    String marked = (String) requestParameterMap.get(treeId + UITree.MARKER);
    if (marked != null) {
      searchString = treeId + NamingContainer.SEPARATOR_CHAR + nodeStateId;

      if (marked.equals(searchString)) {
        state.setMarker((DefaultMutableTreeNode) node.getValue());
      }
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component)
      throws IOException {

    UITreeNode treeNode = (UITreeNode) component;

    UIComponent parent = treeNode.getParent();

    boolean isFolder = treeNode.getChildCount() > 0;

    String parentClientId = null;
    if (parent != null && parent instanceof UITreeNode) { // if not the root node
      parentClientId = treeNode.getParent().getClientId(facesContext);
    } else if (parent != null && parent instanceof UITreeNodeData) {
      String pci = parent.getClientId(facesContext);
      if (pci.endsWith(":_0")) {
        UIComponent superParent = parent.getParent();
        parentClientId = superParent.getClientId(facesContext);
      } else {
        parentClientId = pci.substring(0, pci.length() - 2) // fixme 2 is not correct for bitter trees
             + NamingContainer.SEPARATOR_CHAR + treeNode.getId();
      }
      DefaultMutableTreeNode currentNode =
          ((UITreeNodeData) parent).getCurrentNode();
      if (currentNode != null) {
        isFolder = currentNode.getChildCount() > 0;
      }
    }

    UITree root = treeNode.findTree();
    String rootId = root.getClientId(facesContext);

    String clientId = treeNode.getClientId(facesContext);
//    clientId += pos != null ? pos : "";

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

      DefaultMutableTreeNode modelNode = (DefaultMutableTreeNode) treeNode.getValue();

      ResponseWriter writer = facesContext.getResponseWriter();

      String debuging = "";

      writer.writeText("  var ", null);
      writer.writeText(jsClientId, null);
      writer.writeText(" = new TreeNode('", null);
      // label
      Object label = treeNode.getAttributes().get(ATTR_LABEL);
      if (LOG.isDebugEnabled()) {
        debuging += label + " : ";
      }
      if (label != null) {
        writer.writeText(StringEscapeUtils.escapeJavaScript(label.toString()), null);
      } else {
        LOG.warn("label = null");
      }
      writer.writeText("',", null);

      // tip
      String tip = (String) treeNode.getAttributes().get(ATTR_TIP);
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

      // mode
      writer.writeText(root.getMode(), null);
      writer.writeText("',", null);

      // is folder
      writer.writeText(isFolder, null);
      writer.writeText(",", null);

      // show icons
      writer.writeText(Boolean.toString(!root.isShowIcons()), null);
      writer.writeText(",", null);

      // show junctions
      writer.writeText(Boolean.toString(!root.isShowJunctions()), null);
      writer.writeText(",", null);

      // show root junction
      writer.writeText(Boolean.toString(!root.isShowRootJunction()), null);
      writer.writeText(",", null);

      // show root
      writer.writeText(Boolean.toString(!root.isShowRoot()), null);
      writer.writeText(",'", null);

      // tree id
      writer.writeText(rootId, null);
      writer.writeText("',", null);

      //
      String selectable = ComponentUtil.getStringAttribute(root, ATTR_SELECTABLE);
      if (selectable != null
          && !("multi".equals(selectable) || "multiLeafOnly".equals(selectable)
          || "single".equals(selectable) || "singleLeafOnly".equals(selectable)
          || "sibling".equals(selectable) || "siblingLeafOnly".equals(selectable))) {
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
      writer.writeText(ComponentUtil.findPage(treeNode).getFormId(facesContext), null);
      writer.writeText("',", null);
      if (treeNode.getChildCount() == 0
          || (selectable != null && !selectable.endsWith("LeafOnly"))) {
        boolean selected = treeState.isSelected(modelNode);
        writer.writeText(Boolean.toString(selected), null);
        if (LOG.isDebugEnabled()) {
          debuging += selected ? "S" : "-";
        }
      } else {
        writer.writeText("false", null);
        if (LOG.isDebugEnabled()) {
          debuging += "-";
        }
        if (treeState.isSelected(modelNode)) {
          LOG.warn("Ignore selected FolderNode in LeafOnly selection tree!");
        }
      }
      writer.writeText(",", null);

      // marked
      boolean marked = treeState.isMarked(modelNode);
      writer.writeText(Boolean.toString(marked), null);
      writer.writeText(",", null);

      // expanded
      boolean expanded = treeState.isExpanded(modelNode);
      writer.writeText(Boolean.toString(expanded), null);
      if (LOG.isDebugEnabled()) {
        debuging += expanded ? "E" : "-";
      }

      writer.writeText(",", null);

      // required
      writer.writeText(Boolean.toString(root.isRequired()), null);
      writer.writeText(",", null);

      // disabled
      writer.writeText(ComponentUtil.getBooleanAttribute(treeNode, ATTR_DISABLED), null);
      writer.writeText(",", null);

      // resources
      writer.writeText("treeResourcesHelp,", null);

      // action link
      String actionLink =
          (String) treeNode.getAttributes().get(ATTR_ACTION_LINK);
      if (actionLink != null) {
        writer.writeText("'", null);
        writer.writeText(actionLink, null);
        writer.writeText("',", null);
      } else {
        writer.writeText("null,", null);
      }

      // onclick
      String onclick = (String) treeNode.getAttributes().get(ATTR_ONCLICK);
      if (onclick != null) {
        writer.writeText("'", null);
        onclick = onclick.replaceAll("\\'", "\\\\'");
        writer.writeText(onclick, null);
        writer.writeText("',", null);
      } else {
        writer.writeText("null,", null);
      }

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
      writer.writeText(", ", null);

      // width
      writer.writeText("'", null);
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
      writer.writeText("', ", null);

      // css class
      writer.writeText("'", null);
      if ("menu".equals(root.getMode())) { // todo: clean up: think about composition of the style-class names
        HtmlRendererUtil.addCssClass(treeNode, "tobago-treeNode-menu");
        if (marked) {
          HtmlRendererUtil.addCssClass(treeNode, "tobago-treeNode-marker");
        }
      }
      StringBuilder treeNodeClass = new StringBuilder((String) treeNode.getAttributes().get(ATTR_STYLE_CLASS));
      if (LOG.isDebugEnabled()) {
        LOG.debug("styleClass='" + treeNodeClass + "'");
      }
      writer.writeText(treeNodeClass, null);
      writer.writeText("', ", null);

      // css class label
      writer.writeText("'", null);
      if (marked) {
        writer.writeText("tobago-treeNode-marker", null);
      }
      writer.writeText("'", null);

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
