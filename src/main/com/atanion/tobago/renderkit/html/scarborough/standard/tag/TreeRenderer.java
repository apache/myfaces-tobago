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
import java.io.IOException;

public class TreeRenderer extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TreeRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UITree tree = (UITree) component;
    TreeState state = (TreeState) tree.getValue();

    if (state != null) {
      state.clearExpandState();
      if (isSelectable(tree)) {
        state.clearSelection();
      }
      if (ComponentUtil.getBooleanAttribute(tree, ATTR_MUTABLE)) {
        state.setMarker(null);
      }
    }

    tree.setValid(true);
  }

  public static boolean isSelectable(UITree tree) {
    final Object selectable = ComponentUtil.getAttribute(tree, ATTR_SELECTABLE);
    return selectable != null
        && (selectable.equals("multi") || selectable.equals("single"));
  }

  public static String createJavascriptVariable(String clientId) {
    return clientId == null
        ? null
        : clientId.replace(NamingContainer.SEPARATOR_CHAR, '_');
  }

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
    writer.writeAttribute("name", clientId + UITree.MARKER, null);
    writer.writeAttribute("id", clientId + UITree.MARKER, null);
    writer.writeAttribute("value", "", null);
    writer.endElement("input");

    if (isSelectable(tree)) {
      writer.startElement("input", tree);
      writer.writeAttribute("type", "hidden", null);
      writer.writeAttribute("name", clientId + UITree.SELECT_STATE, null);
      writer.writeAttribute("id", clientId + UITree.SELECT_STATE, null);
      writer.writeAttribute("value", ";", null);
      writer.endElement("input");
    }

    if (ComponentUtil.getBooleanAttribute(tree, ATTR_MUTABLE)) {

      Application application = facesContext.getApplication();

      UITree.Command[] commands = tree.getCommands();

      ActionListener handler;

      ActionListener[] handlers = tree.getActionListeners();
      if (handlers != null && handlers.length > 0) {
        handler = handlers[0];
      } else {
        LOG.error("No actionListener found in tree, so tree editing will not work!");
        handler = null;
      }

//      String type = (String)component.getAttributes().get(ATTR_ACTION_LISTENER);
//        try {
//          handler = ComponentUtil.createActionListener(type);
//        } catch (JspException e) {
//          LOG.error("", e);
//          throw new IOException(e.toString());
//        }

      writer.startElement("div", null);
      writer.writeAttribute("style", "border: 2px groove #ddeeff", null);
      writer.writeText("", null);

      UIComponent mutableToolbar = tree.getFacet("mutableToolbar");
      if (null != mutableToolbar) {
        RenderUtil.encodeChildren(facesContext, mutableToolbar);
      } else { // default buttons
        for (int i = 0; i < commands.length; i++) {
          // create a UILink and add it to the UITree
          UICommand link = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
          link.setId("button" + i);
          link.getAttributes().put(ATTR_ACTION_STRING,
              commands[i].getCommand());
          link.setRendererType(RENDERER_TYPE_LINK);
          if (handler != null) {
            link.addActionListener(handler);
          }
          tree.getFacets().put(commands[i].getCommand(), link);

          // create a UIImage and add it to the UILink
          UIComponent image = application.createComponent(UIGraphic.COMPONENT_TYPE);
          image.getAttributes().put(ATTR_VALUE,
              "image/tobago.tree." + commands[i].getCommand() + ".gif");
          String title = ResourceManagerUtil.getProperty(facesContext, "tobago",
              "tree" + StringUtil.firstToUpperCase(commands[i].getCommand()));
          image.getAttributes().put(ATTR_TITLE, title);
          link.getChildren().add(image);

          RenderUtil.encode(facesContext, link);
        }
      }

      writer.endElement("div");
    }

//    writer.startElement("div", null);
    writer.startElement("table", tree);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("summary", "", null);
    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
    writer.startElement("tr", null);
    writer.startElement("td", null);
    writer.writeAttribute("id", clientId + "-cont", null);
    writer.writeComment("placeholder for treecontent");
    writer.endElement("td");
    writer.endElement("tr");
    writer.endElement("table");
//    writer.endElement("div");

    ComponentUtil.findPage(tree).getScriptFiles().add("script/tree.js");

    writer.startElement("script", null);
    writer.writeAttribute("type", "text/javascript", null);
    writer.writeText("{", null);

    // tree resources (images)
    String[] images = {
      "openfoldericon.gif", "foldericon.gif", "unchecked.gif", "checked.gif",
      "new.gif", "T.gif", "L.gif", "I.gif",
      "Lminus.gif", "Tminus.gif", "Rminus.gif",
      "Lplus.gif", "Tplus.gif", "Rplus.gif",
    };
    writer.writeText("var treeResourcesHelp = new Object();\n", null);
    for (int i = 0; i < images.length; i++) {
      writer.writeText("treeResourcesHelp.", null);
      writer.writeText(images[i].replace('.', '_'), null);
      writer.writeText(" = \"", null);
      writer.writeText(ResourceManagerUtil.getImage(facesContext, "image/" + images[i]), null);
      writer.writeText("\";\n", null);
    }
    writer.writeText("treeResourcesHelp.getImage = function (name) {\n", null);
    writer.writeText("  var result = this[name.replace('.', '_')];\n", null);
    writer.writeText("  if (result) {\n", null);
    writer.writeText("    return result;\n", null);
    writer.writeText("  } else {\n", null);
    writer.writeText("    return \"", null);
    writer.writeText(ResourceManagerUtil.getImage(facesContext, "image/blank.gif"), null);
    writer.writeText("\";\n", null);
    writer.writeText("  }\n", null);
    writer.writeText("}\n", null);

    RenderUtil.encode(facesContext, root);

    writer.writeText("  var treeDiv = document.getElementById('", null);
    writer.writeText(clientId, null);
    writer.writeText("-cont');\n", null);
    writer.writeText("treeDiv.innerHTML = ", null);
    writer.writeText(TreeRenderer.createJavascriptVariable(root.getClientId(facesContext)),
        null);
    writer.writeText(".toString(0, true);", null);
    writer.writeText("}", null);
    writer.endElement("script");

    writer.endElement("div");
  }

// ///////////////////////////////////////////// bean getter + setter

}
