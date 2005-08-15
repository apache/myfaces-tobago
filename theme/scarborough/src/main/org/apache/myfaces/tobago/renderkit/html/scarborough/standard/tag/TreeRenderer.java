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

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.model.TreeState;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
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
    TreeState state = tree.getState();

    if (state != null) {
      if ("Tree".equals(tree.getRendererType())) {
        state.clearExpandState();
      }
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
    return tree.isSelectableTree();
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

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("div", tree);
    writer.writeComponentClass();
    writer.writeAttribute("style", null, ATTR_STYLE);

    writer.startElement("input", tree);
    writer.writeAttribute("type", "hidden", null);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute("value", ";", null);
    writer.endElement("input");

    writer.startElement("input", tree);
    writer.writeAttribute("type", "hidden", null);
    writer.writeNameAttribute(clientId + UITree.MARKER);
    writer.writeIdAttribute(clientId + UITree.MARKER);
    writer.writeAttribute("value", "", null);
    writer.endElement("input");

    if (isSelectable(tree)) {
      writer.startElement("input", tree);
      writer.writeAttribute("type", "hidden", null);
      writer.writeNameAttribute(clientId + UITree.SELECT_STATE);
      writer.writeIdAttribute(clientId + UITree.SELECT_STATE);
      writer.writeAttribute("value", ";", null);
      writer.endElement("input");
    }

    if (ComponentUtil.getBooleanAttribute(tree, ATTR_MUTABLE)) {


//      writer.startElement("div", null);
//      writer.writeAttribute("style", "border: 2px groove #ddeeff", null);
//      writer.writeText("", null);

      UIComponent toolbar = tree.getFacet("mutableToolbar");
      if (toolbar == null) {
        toolbar = tree.getFacet("defaultToolbar");
      }
      RenderUtil.encode(facesContext, toolbar);


//      writer.endElement("div");
    }

//    writer.startElement("div", null);
    writer.startElement("table", tree);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("summary", "", null);
    writer.writeComponentClass();
    writer.startElement("tr", null);
    writer.startElement("td", null);
    writer.writeIdAttribute(clientId + "-cont");
    writer.writeComment("placeholder for treecontent");
    writer.endElement("td");
    writer.endElement("tr");
    writer.endElement("table");
//    writer.endElement("div");

    ComponentUtil.findPage(tree).getScriptFiles().add("script/tree.js");

    HtmlRendererUtil.startJavascript(writer);
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
    String rootNode = createJavascriptVariable(root.getClientId(facesContext));
    writer.writeText(rootNode, null);
    writer.writeText(".toString(0, true);\n", null);

    writer.writeText(rootNode, null);
    writer.writeText(".initSelection();\n", null);
    writer.writeText("}", null);
    HtmlRendererUtil.endJavascript(writer);

    writer.endElement("div");
  }

// ///////////////////////////////////////////// bean getter + setter

}
