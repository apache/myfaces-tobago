package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UITreeListbox;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class TreeListboxRenderer extends TreeRenderer{

  private static final Log LOG = LogFactory.getLog(TreeListboxRenderer.class);

  public void encodeBeginTobago(
      FacesContext facesContext, UIComponent component) throws IOException {


    UITreeListbox tree = (UITreeListbox) component;
    tree.createSelectionPath();

    String clientId = tree.getClientId(facesContext);
    UITreeNode root = tree.getRoot();


    UIPage page = ComponentUtil.findPage(tree);
    if (LOG.isDebugEnabled()) {
      page.getOnloadScripts().add("tbgTreeStates('" + clientId + "')");
    }
    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();
    writer.startElement("div", tree);
    writer.writeComponentClass();
    writer.writeAttribute("style", null, ATTR_STYLE);

    String value = ";";
    List<UITreeNode> expandPath = tree.getExpandPath();
    for (UITreeNode node : expandPath) {
      value += nodeStateId(facesContext, node) + ";";
    }

    writer.startElement("input", tree);
    writer.writeAttribute("type", "hidden", null);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute("value", value, null);
    writer.endElement("input");


    final Set<DefaultMutableTreeNode> selection = tree.getState().getSelection();
    value = ";";
    for (DefaultMutableTreeNode node : selection) {
      value += nodeStateId(facesContext, tree.findUITreeNode(root, node)) + ";";
    }
    writer.startElement("input", tree);
    writer.writeAttribute("type", "hidden", null);
    writer.writeNameAttribute(clientId + UITreeListbox.SELECT_STATE);
    writer.writeIdAttribute(clientId + UITreeListbox.SELECT_STATE);
    writer.writeAttribute("value", value, null);
    writer.endElement("input");

    String script = createJavascript(facesContext, clientId, root);

    final String[] scripts = {"script/tree.js"};
    ComponentUtil.findPage(tree).getScriptFiles().add(scripts[0]);

    if (!TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      HtmlRendererUtil.startJavascript(writer);
      writer.writeText(script, null);
      HtmlRendererUtil.endJavascript(writer);
    } else {
      HtmlRendererUtil.writeScriptLoader(facesContext, scripts,
          new String[] {script.replaceAll("\n", " ")});
    }

  }

  private String  createJavascript(FacesContext facesContext, String clientId,
                                   UITreeNode root) throws IOException {

    StringBuffer sb = new StringBuffer();
    sb.append("{\n");

    sb.append("  var treeResourcesHelp = new Object();\n");
    sb.append("  treeResourcesHelp.getImage = function (name) {\n");
    sb.append("    return \"");
    sb.append(ResourceManagerUtil.getImageWithPath(facesContext, "image/blank.gif"));
    sb.append("\";\n");
    sb.append("  };;\n");

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
//    writer.startElement("table", tree);
//    writer.writeAttribute("cellpadding", "0", null);
//    writer.writeAttribute("cellspacing", "0", null);
//    writer.writeAttribute("border", "0", null);
//    writer.writeAttribute("summary", "", null);
//    writer.writeComponentClass(ATTR_STYLE_CLASS);
//    writer.startElement("tr", null);
//
//
//    String clientId = tree.getClientId(facesContext);
//
//    for (int level = 0; level < depth; level++) {
//
//      writer.startElement("td", null);
//
//
//
//      renderListbox(writer, clientId, level, tree);
//
//
//
//      writer.endElement("td");
//      if ((level + 1) % cols == 0) {
//        writer.endElement("tr");
//        writer.startElement("tr", null);
//      }
//    }
//
//
//    writer.endElement("tr");
//    writer.endElement("table");
//
//    // ///////////////////////////////////////////////////
//    // end of layouted list containers
//
//
//  }

  public void encodeEndTobago(FacesContext facesContext,
                              UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    writer.endElement("div");
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
//    writer.startElement("select", null);
//    writer.writeIdAttribute(listboxId);
//    writer.writeClassAttribute(className);
//    writer.writeAttribute("style" , "width: 150px; height: 100px;", null);
//    writer.writeAttribute("size", "2", null);
//    writer.writeAttribute("onchange", onChange, null);
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
//        writer.startElement("option", null);
//        writer.writeAttribute("value", Integer.toString(i), null);
//        if (treeNode.equals(tree.getSelectedNode(level))) {
//          writer.writeAttribute("selected", "selected", null);
//        }
//
//        writer.writeText(treeNode.getAttributes().get(ATTR_NAME), null);
//        if (node.getChildCount() > 0) {
//          writer.writeText(" -->", null);
//        }
//
//        writer.endElement("option");
//      }
//
//    }
//
//
//    writer.endElement("select");
//  }

// ///////////////////////////////////////////// bean getter + setter

}
