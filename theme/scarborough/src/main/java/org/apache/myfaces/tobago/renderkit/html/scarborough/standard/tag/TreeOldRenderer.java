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

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITreeOld;
import org.apache.myfaces.tobago.component.UITreeOldNode;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.model.TreeState;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.FastStringWriter;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.lang.StringUtils;

import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@Deprecated
public class TreeOldRenderer extends LayoutableRendererBase {

  /**
   * Resources to display the tree.
   */
  private static final String[] TREE_IMAGES = {
      "openfoldericon.gif",
      "foldericon.gif",
      "new.gif",
      "T.gif",
      "L.gif",
      "I.gif",
      "Lminus.gif",
      "Tminus.gif",
      "Rminus.gif",
      "Lplus.gif",
      "Tplus.gif",
      "Rplus.gif",
      "treeMenuOpen.gif",
      "treeMenuClose.gif",
  };


  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }
    UITreeOld tree = (UITreeOld) component;
    String treeId = tree.getClientId(facesContext);
    TreeState state = tree.getState();
    final Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();

    if (state != null) {
      if ("TreeOld".equals(tree.getRendererType())) {
        state.clearExpandState();
      }
      if (isSelectable(tree)) {
        state.clearSelection();
      }
      if (ComponentUtil.getBooleanAttribute(tree, TobagoConstants.ATTR_MUTABLE)) {
        state.setMarker(null);
      }
      // scroll position
      String value = (String) requestParameterMap.get(treeId + UITreeOld.SCROLL_POSITION);
      if (value != null) {
        Integer[] scrollPosition = TreeState.parseScrollPosition(value);
        if (scrollPosition != null) {
          state.setScrollPosition(scrollPosition);
        }
      }
    }
    tree.setValid(true);
  }

  public static boolean isSelectable(UITreeOld tree) {
    return tree.isSelectableTree();
  }

  public static String createJavascriptVariable(String clientId) {
    return clientId == null
        ? null
        : clientId.replace(NamingContainer.SEPARATOR_CHAR, '_');
  }

  @Override
  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    UITreeOld tree = (UITreeOld) component;

    String clientId = tree.getClientId(facesContext);
    UITreeOldNode root = tree.getRoot();
    TreeState state = tree.getState();

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.DIV, tree);
    writer.writeNameAttribute(clientId + UITreeOld.TREE_DIV);
    writer.writeIdAttribute(clientId + UITreeOld.TREE_DIV);
    writer.writeClassAttribute();
    writer.writeStyleAttribute();

    writer.startElement(HtmlConstants.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
    writer.endElement(HtmlConstants.INPUT);

    writer.startElement(HtmlConstants.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + UITreeOld.MARKER);
    writer.writeIdAttribute(clientId + UITreeOld.MARKER);
    writer.writeAttribute(HtmlAttributes.VALUE, "", false);
    writer.endElement(HtmlConstants.INPUT);

    if (isSelectable(tree)) {
      writer.startElement(HtmlConstants.INPUT, tree);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.writeNameAttribute(clientId + UITreeOld.SELECT_STATE);
      writer.writeIdAttribute(clientId + UITreeOld.SELECT_STATE);
      writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
      writer.endElement(HtmlConstants.INPUT);
    }

    writer.startElement(HtmlConstants.INPUT, tree);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + UITreeOld.SCROLL_POSITION);
    writer.writeIdAttribute(clientId + UITreeOld.SCROLL_POSITION);
    Integer[] scrollPosition = state.getScrollPosition();
    if (scrollPosition != null) {
      String scroll = scrollPosition[0] + ";" + scrollPosition[1];
      writer.writeAttribute(HtmlAttributes.VALUE, scroll, false);
    } else {
      writer.writeAttribute(HtmlAttributes.VALUE, "", false);
    }
    writer.endElement(HtmlConstants.INPUT);

    if (ComponentUtil.getBooleanAttribute(tree, TobagoConstants.ATTR_MUTABLE)) {


//      writer.startElement(HtmlConstants.DIV, null);
//      writer.writeAttribute(HtmlAttributes.STYLE, "border: 2px groove #ddeeff", null);
//      writer.writeText("", null);

      UIComponent toolbar = tree.getFacet("mutableToolbar");
      if (toolbar == null) {
        toolbar = tree.getFacet("defaultToolbar");
      }
      RenderUtil.encode(facesContext, toolbar);


//      writer.endElement(HtmlConstants.DIV);
    }

//    writer.startElement(HtmlConstants.DIV, null);
    writer.startElement(HtmlConstants.TABLE, tree);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, 0);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
    writer.writeAttribute(HtmlAttributes.BORDER, 0);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
    writer.writeClassAttribute();
    writer.startElement(HtmlConstants.TR, null);
    writer.startElement(HtmlConstants.TD, null);
    writer.writeIdAttribute(clientId + "-cont");
    writer.writeComment("placeholder for treecontent");
    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);
    writer.endElement(HtmlConstants.TABLE);
//    writer.endElement(HtmlConstants.DIV);

    String[] scriptTexts = createJavascript(facesContext, clientId, tree, root);

    String[] scripts = {"script/tree.js"};
    List<String> scriptFiles = ComponentUtil.findPage(tree).getScriptFiles();
    for (String script : scripts) {
      scriptFiles.add(script);
    }

    if (!TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      StringBuilder script = new StringBuilder();
      for (String scriptText : scriptTexts) {
        script.append(scriptText);
        script.append('\n');
      }
      writer.writeJavascript(script.toString());
    } else {
      HtmlRendererUtil.writeScriptLoader(facesContext, scripts, scriptTexts);
    }

    writer.endElement(HtmlConstants.DIV);
  }

  private String[] createJavascript(FacesContext facesContext, String clientId,
      UITreeOld tree, UITreeOldNode root)
  throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append("{\n");

    sb.append("  var treeResourcesHelp = new Object();\n");
    for (String images : TREE_IMAGES) {
      sb.append("  treeResourcesHelp[\"");
      sb.append(images);
      sb.append("\"] = \"");
      sb.append(ResourceManagerUtil.getImageWithPath(facesContext,
          "image/" + images));
      sb.append("\";\n");
    }
    sb.append(" \n  treeResourcesHelp.getImage = function (name) {\n");
    sb.append("    var result = this[name];\n");
    sb.append("    if (result) {\n");
    sb.append("      return result;\n");
    sb.append("    } else {\n");
    sb.append("      return \"");
    sb.append(ResourceManagerUtil.getImageWithPath(facesContext, "image/blank.gif"));
    sb.append("\";\n");
    sb.append("    }\n");
    sb.append("  };\n \n");

    sb.append(getTreeNodeCommandVar(facesContext, tree));
    
    sb.append(getNodesAsJavascript(facesContext, root));

    sb.append("  var treeDiv = document.getElementById('");
    sb.append(clientId);
    sb.append("-cont');\n");
    sb.append("  treeDiv.innerHTML = ");
    String rootNode = createJavascriptVariable(root.getClientId(facesContext));
    sb.append(rootNode);
    sb.append(".toString(0, true);\n  ");

    sb.append(rootNode);
    sb.append(".initSelection();\n  ");
    
    sb.append(rootNode);
    sb.append(".setScrollPosition();\n");

    sb.append("  Tobago.addBindEventListener(Tobago.element('");
    sb.append(clientId);
    sb.append("-div'), 'scroll', ");
    sb.append(rootNode);
    sb.append(", 'doScroll');\n");

    sb.append("}");
//    return sb.toString();
    StringTokenizer tokenizer = new StringTokenizer(sb.toString(), "\n");
    String[] strings = new String[tokenizer.countTokens()];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = tokenizer.nextToken();
    }
    return strings;
  }

  protected String getTreeNodeCommandVar(FacesContext facesContext, UIComponent tree) {
    String clientId = tree.getClientId(facesContext);
    String jsClientId = TreeOldRenderer.createJavascriptVariable(clientId);

    String treeNodeCommandVar = "  var " + jsClientId + "_treeNodeCommand = ";
    UICommand treeNodeCommand = (UICommand) tree.getFacet(UITreeOld.FACET_TREE_NODE_COMMAND);
    if (treeNodeCommand != null) {
      CommandRendererHelper helper = new CommandRendererHelper(facesContext,
          treeNodeCommand);
      if (helper.getOnclick() != null) {
        String onclick = helper.getOnclick();
        String treeNodeCommandClientId = treeNodeCommand.getClientId(facesContext);
        onclick = StringUtils.replace(onclick, "'" + treeNodeCommandClientId + "'", "this.id");
        treeNodeCommandVar += "\"" + onclick + "\";\n";
      } else {
        treeNodeCommandVar += "null;\n";
      }
    } else {
      treeNodeCommandVar += "null;\n";
    }
    return treeNodeCommandVar;
  }

  protected String getNodesAsJavascript(FacesContext facesContext, UITreeOldNode root) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    FastStringWriter stringWriter = new FastStringWriter();
    facesContext.setResponseWriter(writer.cloneWithWriter(stringWriter));
    RenderUtil.encode(facesContext, root);
    facesContext.setResponseWriter(writer);
    return stringWriter.toString();
  }

  protected String nodeStateId(FacesContext facesContext, UITreeOldNode node) {
    // this must do the same as nodeStateId() in tree.js
    String clientId = node.getClientId(facesContext);
    int last = clientId.lastIndexOf(':') + 1;
    return clientId.substring(last);
  }
}
