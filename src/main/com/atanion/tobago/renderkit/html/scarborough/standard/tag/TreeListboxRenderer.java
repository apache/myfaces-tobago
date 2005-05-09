/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UITreeListbox;
import com.atanion.tobago.component.UITreeNode;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.util.LayoutUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Set;
import java.util.Iterator;

public class TreeListboxRenderer extends TreeRenderer{

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TreeListboxRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeBeginTobago(
      FacesContext facesContext, UIComponent component) throws IOException {


    UITreeListbox tree = (UITreeListbox) component;

    String clientId = tree.getClientId(facesContext);
    UITreeNode root = tree.getRoot();

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("div", tree);
    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
    writer.writeAttribute("style", null, ATTR_STYLE);

    final Set<DefaultMutableTreeNode> selection = tree.getState().getSelection();
    final Iterator<DefaultMutableTreeNode> iterator = selection.iterator();
    writer.startElement("input", tree);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("value", ";", null);
    writer.endElement("input");

    String value = ";";
    if (iterator.hasNext()) {
      final DefaultMutableTreeNode node = iterator.next();

//      value = node
    }
    writer.startElement("input", tree);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("name", clientId + UITreeListbox.SELECT_STATE, null);
    writer.writeAttribute("id", clientId + UITreeListbox.SELECT_STATE, null);
    writer.writeAttribute("value", value, null);
    writer.endElement("input");

    ComponentUtil.findPage(tree).getScriptFiles().add("script/tree.js");

    HtmlRendererUtil.startJavascript(writer);

    writer.writeText("{", null);

    writer.writeText("var treeResourcesHelp = new Object();\n", null);
    writer.writeText("treeResourcesHelp.getImage = function (name) {\n", null);
    writer.writeText("    return \"", null);
    writer.writeText(ResourceManagerUtil.getImage(facesContext, "image/blank.gif"), null);
    writer.writeText("\";\n", null);
    writer.writeText("}\n", null);


    HtmlRendererUtil.encodeHtml(facesContext, root);

    writer.writeText("var hidden =   document.getElementById('", null);
    writer.writeText(clientId, null);
    writer.writeText("'); \n", null);
    writer.writeText("hidden.rootNode = ", null);
    String rootNode = createJavascriptVariable(root.getClientId(facesContext));
    writer.writeText(rootNode, null);
    writer.writeText(";\n", null);

    writer.writeText(rootNode, null);
    writer.writeText(".initSelection();\n", null);
    writer.writeText("}", null);

    HtmlRendererUtil.endJavascript(writer);


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
//    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
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
//    writer.writeAttribute("id", listboxId, null);
//    writer.writeAttribute("class", className, null);
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
