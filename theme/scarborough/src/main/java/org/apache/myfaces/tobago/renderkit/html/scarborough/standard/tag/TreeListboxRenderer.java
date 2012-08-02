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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UITreeListbox;
import org.apache.myfaces.tobago.component.UITreeOldNode;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class TreeListboxRenderer extends TreeOldRenderer{

  private static final Log LOG = LogFactory.getLog(TreeListboxRenderer.class);

  @Override
  public void encodeBegin(
      FacesContext facesContext, UIComponent component) throws IOException {


    UITreeListbox tree = (UITreeListbox) component;
    tree.createSelectionPath();

    String clientId = tree.getClientId(facesContext);
    UITreeOldNode root = tree.getRoot();


    UIPage page = ComponentUtil.findPage(facesContext, tree);
    if (LOG.isDebugEnabled()) {
      page.getOnloadScripts().add("tbgTreeStates('" + clientId + "')");
    }
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.DIV, tree);
    writer.writeClassAttribute();
    writer.writeStyleAttribute();

    StringBuilder value = new StringBuilder(";");
    List<UITreeOldNode> expandPath = tree.getExpandPath();
    for (UITreeOldNode node : expandPath) {
      value.append(nodeStateId(facesContext, node));
      value.append(";");
    }

    writer.startElement(HtmlConstants.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.VALUE, value.toString(), true);
    writer.endElement(HtmlConstants.INPUT);


    Set<DefaultMutableTreeNode> selection = tree.getState().getSelection();
    value = new StringBuilder(";");
    for (DefaultMutableTreeNode node : selection) {
      value.append(nodeStateId(facesContext, tree.findUITreeNode(root, node)));
      value.append(";");
    }
    writer.startElement(HtmlConstants.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + UITreeListbox.SELECT_STATE);
    writer.writeIdAttribute(clientId + UITreeListbox.SELECT_STATE);
    writer.writeAttribute(HtmlAttributes.VALUE, value.toString(), true);
    writer.endElement(HtmlConstants.INPUT);

    String scriptText = createJavascript(facesContext, clientId, tree, root);

    String[] scripts = {"script/tree.js"};
    List<String> scriptFiles = ComponentUtil.findPage(facesContext, tree).getScriptFiles();
    for (String script : scripts) {
      scriptFiles.add(script);
    }

    if (!TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      writer.writeJavascript(scriptText);
    } else {
      HtmlRendererUtil.writeScriptLoader(facesContext, scripts,
          new String[] {scriptText.replace('\n', ' ')});
    }

  }

  private String  createJavascript(FacesContext facesContext, String clientId,
                                    UITreeListbox tree, UITreeOldNode root) throws IOException {

    StringBuilder sb = new StringBuilder();
    sb.append("{\n");

    sb.append("  var treeResourcesHelp = new Object();\n");
    sb.append("  treeResourcesHelp.getImage = function (name) {\n");
    sb.append("    return \"");
    sb.append(ResourceManagerUtil.getImageWithPath(facesContext, "image/blank.gif"));
    sb.append("\";\n");
    sb.append("  };;\n");

    sb.append(getTreeNodeCommandVar(facesContext, tree));
    sb.append(getNodesAsJavascript(facesContext, root));

    sb.append("  var hidden =   document.getElementById('");
    sb.append(clientId);
    sb.append("'); \n");
    sb.append("  hidden.rootNode = ");
    String rootNode = createJavascriptVariable(root.getClientId(facesContext));
    sb.append(rootNode);
    sb.append(";\n");

    sb.append("}\n");

    return sb.toString();
  }

//  public void encodeChildrenTobago(
//      FacesContext facesContext, UIComponent component)
//      throws IOException {
//
//    UITreeListbox tree = (UITreeListbox) component;
//
//    ResponseWriter writer = facesContext.getResponseWriter();
//
//
//    // start of layouted list containers
//    // ///////////////////////////////////////////////////
//
//    int depth = tree.getRoot().getTreeNode().getDepth();
//    int cols = 4;
//
//
//    writer.startElement(HtmlConstants.TABLE, tree);
//    writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", null);
//    writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
//    writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
//    writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);
//    writer.writeComponentClass(ATTR_STYLE_CLASS);
//    writer.startElement(HtmlConstants.TR, null);
//
//
//    String clientId = tree.getClientId(facesContext);
//
//    for (int level = 0; level < depth; level++) {
//
//      writer.startElement(HtmlConstants.TD, null);
//
//
//
//      renderListbox(writer, clientId, level, tree);
//
//
//
//      writer.endElement(HtmlConstants.TD);
//      if ((level + 1) % cols == 0) {
//        writer.endElement(HtmlConstants.TR);
//        writer.startElement(HtmlConstants.TR, null);
//      }
//    }
//
//
//    writer.endElement(HtmlConstants.TR);
//    writer.endElement(HtmlConstants.TABLE);
//
//    // ///////////////////////////////////////////////////
//    // end of layouted list containers
//
//
//  }

  @Override
  public void encodeEnd(FacesContext facesContext,
                              UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    writer.endElement(HtmlConstants.DIV);
  }

//  private void renderListbox(ResponseWriter writer, String clientId, int level,
//                    UITreeListbox tree)
//      throws IOException {
//
//    List<UITreeNode> selectionPath = tree.getSelectionPath();
//    String className = "tobago-listbox-default";
//    if (selectionPath.size() > 0 &&  selectionPath.size() - 1 <= level
//        && selectionPath.get(selectionPath.size() - 1).getTreeNode().isLeaf()) {
//      className += " tobago-treeListbox-unused";
//    }
//
//    String listboxId = clientId + SUBCOMPONENT_SEP + "cont_" + level;
//    String onChange = "tobagoTreeListboxChange(this, '" + clientId + "')";
//    writer.startElement(HtmlConstants.SELECT, null);
//    writer.writeIdAttribute(listboxId);
//    writer.writeClassAttribute(className);
//    writer.writeAttribute(HtmlAttributes.STYLE , "width: 150px; height: 100px;", null);
//    writer.writeAttribute(HtmlAttributes.SIZE, "2", null);
//    writer.writeAttribute(HtmlAttributes.ONCHANGE, onChange, null);
//
//
//
//    List nodes = tree.getNodes(level);
//
//    for (int i = 0; i < nodes.size(); i++) {
//      if (nodes.get(i) instanceof UITreeNode) {
//        UITreeNode treeNode = (UITreeNode) nodes.get(i);
//        DefaultMutableTreeNode node = treeNode.getTreeNode();
//
//        writer.startElement(HtmlConstants.OPTION, null);
//        writer.writeAttribute(HtmlAttributes.VALUE, Integer.toString(i), null);
//        if (treeNode.equals(tree.getSelectedNode(level))) {
//          writer.writeAttribute(HtmlAttributes.SELECTED, "selected", null);
//        }
//
//        writer.writeText(treeNode.getAttributes().get(ATTR_NAME), null);
//        if (node.getChildCount() > 0) {
//          writer.writeText(" -->", null);
//        }
//
//        writer.endElement(HtmlConstants.OPTION);
//      }
//
//    }
//
//
//    writer.endElement(HtmlConstants.SELECT);
//  }

// ///////////////////////////////////////////// bean getter + setter

}
