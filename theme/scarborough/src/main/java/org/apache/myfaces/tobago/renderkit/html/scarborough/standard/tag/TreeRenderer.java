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

import org.apache.myfaces.tobago.component.AbstractUITree;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.PageFacesContextWrapper;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.util.FastStringWriter;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class TreeRenderer extends LayoutableRendererBase {

  /**
   * Resources to display the tree.
   */
  private static final String[] TREE_IMAGES = {
      "openfoldericon.gif",
      "foldericon.gif",
      "unchecked.gif",
      "uncheckedDisabled.gif",
      "checked.gif",
      "checkedDisabled.gif",
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

  private static final String SCRIPT = "script/tobago-tree.js";

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    if (facesContext instanceof PageFacesContextWrapper) {
      // todo: this may be removed, it is twice on the page 1. in the header 2. in the ScriptLoader
      ((PageFacesContextWrapper) facesContext).getScriptFiles().add(SCRIPT);
    }
  }


  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    AbstractUITree tree = (AbstractUITree) component;
    tree.setValid(true);
  }

  public static boolean isSelectable(AbstractUITree tree) {
    return tree.isSelectableTree();
  }

  public static String createJavascriptVariable(String clientId) {
    return clientId == null
        ? null
        : clientId.replace(NamingContainer.SEPARATOR_CHAR, '_');
  }

  @Override
  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    // will be rendered in encodeEnd()
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    AbstractUITree tree = (AbstractUITree) component;

    String clientId = tree.getClientId(facesContext);
    UIComponent root = tree.getRoot();

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlConstants.DIV, tree);
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
    writer.writeNameAttribute(clientId + AbstractUITree.MARKER);
    writer.writeIdAttribute(clientId + AbstractUITree.MARKER);
    writer.writeAttribute(HtmlAttributes.VALUE, "", false);
    writer.endElement(HtmlConstants.INPUT);

    if (isSelectable(tree)) {
      writer.startElement(HtmlConstants.INPUT, tree);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.writeNameAttribute(clientId + AbstractUITree.SELECT_STATE);
      writer.writeIdAttribute(clientId + AbstractUITree.SELECT_STATE);
      writer.writeAttribute(HtmlAttributes.VALUE, ";", false);
      writer.endElement(HtmlConstants.INPUT);
    }

    String scriptTexts = createJavascript(facesContext);

    HtmlRendererUtil.writeScriptLoader(facesContext, new String[]{SCRIPT}, new String[]{scriptTexts});

    RenderUtil.encode(facesContext, root);

    writer.endElement(HtmlConstants.DIV);
  }

  private String createJavascript(FacesContext facesContext) throws IOException {
    StringBuilder sb = new StringBuilder(128);

    sb.append("{\n");

    sb.append("  var treeResourcesHelp = new Object();\n");
    for (String images : TREE_IMAGES) {
      sb.append("  treeResourcesHelp[\"");
      sb.append(images);
      sb.append("\"] = \"");
      sb.append(ResourceManagerUtil.getImageWithPath(facesContext, "image/" + images));
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
    sb.append("}");
    return sb.toString();
  }

  protected String getNodesAsJavascript(FacesContext facesContext, UIComponent root) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    FastStringWriter stringWriter = new FastStringWriter();
    facesContext.setResponseWriter(writer.cloneWithWriter(stringWriter));
    RenderUtil.encode(facesContext, root);
    facesContext.setResponseWriter(writer);
    return stringWriter.toString();
  }

  protected String nodeStateId(FacesContext facesContext, UIComponent node) {
    // this must do the same as nodeStateId() in tree.js
    String clientId = node.getClientId(facesContext);
    int last = clientId.lastIndexOf(':') + 1;
    return clientId.substring(last);
  }
}
