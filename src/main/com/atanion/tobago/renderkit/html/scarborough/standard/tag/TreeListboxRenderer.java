/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UITree;
import com.atanion.tobago.component.UITreeNode;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.model.TreeState;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Collections;

public class TreeListboxRenderer extends TreeRenderer{

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TreeListboxRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    UITree tree = (UITree) component;

    String clientId = tree.getClientId(facesContext);
    UITreeNode root = tree.getRoot();

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("div", tree);
    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
    writer.writeAttribute("style", null, ATTR_STYLE);

    writer.startElement("input", tree);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("value", ";", null);
    writer.endElement("input");

      writer.startElement("input", tree);
      writer.writeAttribute("type", "hidden", null);
      writer.writeAttribute("name", clientId + UITree.SELECT_STATE, null);
      writer.writeAttribute("id", clientId + UITree.SELECT_STATE, null);
      writer.writeAttribute("value", ";", null);
      writer.endElement("input");

    ComponentUtil.findPage(tree).getScriptFiles().add("script/tree.js");

    writer.startElement("script", null);
    writer.writeAttribute("type", "text/javascript", null);
    writer.writeText("\n<!--\n", null);
    writer.writeText("{", null);

    writer.writeText("var treeResourcesHelp = new Object();\n", null);
    writer.writeText("treeResourcesHelp.getImage = function (name) {\n", null);
    writer.writeText("    return \"", null);
    writer.writeText(ResourceManagerUtil.getImage(facesContext, "image/blank.gif"), null);
    writer.writeText("\";\n", null);
    writer.writeText("}\n", null);


    RenderUtil.encodeHtml(facesContext, root);

    writer.writeText("var hidden =   document.getElementById('", null);
    writer.writeText(clientId, null);
    writer.writeText("'); \n", null);
    writer.writeText("hidden.rootNode = ", null);
    writer.writeText(TreeListboxRenderer.createJavascriptVariable(root.getClientId(facesContext)), null);
    writer.writeText(";\n", null);

    writer.writeText("}", null);

    writer.writeText("\n// -->\n", null);
    writer.endElement("script");



    List<UITreeNode> selectionPath = tree.getSelectionPath();




    // start of layouted list containers
    // ///////////////////////////////////////////////////

    int depth = root.getTreeNode().getDepth();
    int cols = 4;


    writer.startElement("table", tree);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("summary", "", null);
    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
    writer.startElement("tr", null);



    for (int level = 0; level < depth; level++) {

      writer.startElement("td", null);



      renderListbox(writer, clientId, level, root, selectionPath);



      writer.endElement("td");
      if ((level + 1) % cols == 0) {
        writer.endElement("tr");
        writer.startElement("tr", null);
      }
    }


    writer.endElement("tr");
    writer.endElement("table");

    // ///////////////////////////////////////////////////
    // end of layouted list containers


    writer.endElement("div");
  }

  private void renderListbox(ResponseWriter writer, String clientId, int level,
                    UITreeNode rootNode, List<UITreeNode> selectionPath)
      throws IOException {

    List nodes;
    if (level == 0) {
      nodes = rootNode.getChildren();
    }
    else if (selectionPath.size() > level) {
      nodes = selectionPath.get(level).getChildren();
    }
    else {
      nodes = Collections.EMPTY_LIST;
    }

    UITreeNode selectedComponent;
    if (selectionPath.size() > level + 1) {
      selectedComponent = selectionPath.get(level + 1);
    } else {
      selectedComponent = null;
    }

    String className = "tobago-listbox-default";
    if (selectionPath.size() - 1 <= level
        && selectionPath.get(selectionPath.size() - 1).getTreeNode().isLeaf()) {
      className += " tobago-treeListbox-unused";
    }

    String listboxId = clientId + SUBCOMPONENT_SEP + "cont_" + level;
    String onChange = "tobagoTreeListboxChange(this, '" + clientId + "')";
    writer.startElement("select", null);
    writer.writeAttribute("id", listboxId, null);
    writer.writeAttribute("class", className, null);
    writer.writeAttribute("style" , "width: 150px; height: 100px;", null);
    writer.writeAttribute("size", "8", null);
    writer.writeAttribute("onchange", onChange, null);



//    if (level == 0 || selectedComponent != null) {

      for (int i = 0; i < nodes.size(); i++) {
        if (nodes.get(i) instanceof UITreeNode) {
          UITreeNode treeNode = (UITreeNode) nodes.get(i);
          DefaultMutableTreeNode node = treeNode.getTreeNode();

          writer.startElement("option", null);
          writer.writeAttribute("value", Integer.toString(i), null);
          if (treeNode.equals(selectedComponent)) {
            writer.writeAttribute("selected", "selected", null);
          }

          writer.writeText(treeNode.getAttributes().get(ATTR_NAME), null);
          if (node.getChildCount() > 0) {
            writer.writeText(" -->", null);
          }


          writer.endElement("option");
        }

      }


    writer.endElement("select");
  }

// ///////////////////////////////////////////// bean getter + setter

}
