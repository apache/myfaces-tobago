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
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UITreeListbox;
import org.apache.myfaces.tobago.component.UITreeListboxBox;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.List;

/**
 * User: weber
 * Date: Mar 21, 2005
 * Time: 11:19:03 AM
 */
public class TreeListboxBoxRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(TreeListboxBoxRenderer.class);

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {

    int level = ((UITreeListboxBox)component).getLevel();

    UITreeListbox tree = (UITreeListbox) component.getParent();
    List<UITreeNode> selectionPath = tree.getSelectionPath();
    String className = "tobago-treeListbox-default";
    if (selectionPath.size() > 0 &&  selectionPath.size() - 1 <= level
        && selectionPath.get(selectionPath.size() - 1).getTreeNode().isLeaf()) {
      className += " tobago-treeListbox-unused";
    }

    String treeId = tree.getClientId(facesContext);
    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();


    String listboxId = treeId + SUBCOMPONENT_SEP + "cont_" + level;
//    String onChange = "tobagoTreeListboxChange(this, '" + treeId + "')";
    String onClick = "tobagoTreeListboxClick(this, '" + treeId + "')";
    writer.startElement("select", component);
    writer.writeIdAttribute(listboxId);
    writer.writeClassAttribute(className);
    writer.writeAttribute("style" , null, ATTR_STYLE);
    writer.writeAttribute("size", "2", null);
//    writer.writeAttribute("onchange", onChange, null);
    writer.writeAttribute("onclick", onClick, null);
    writer.writeAttribute("multiple",
        "siblingLeafOnly".equals(tree.getAttributes().get(ATTR_SELECTABLE)));


    List nodes = ((UITreeListboxBox)component).getNodes();

    for (int i = 0; i < nodes.size(); i++) {
      UITreeNode treeNode = (UITreeNode) nodes.get(i);
      DefaultMutableTreeNode node = treeNode.getTreeNode();

      writer.startElement("option", null);
//      writer.writeAttribute("onclick", "tbgTreeListboxClick(this, '" + treeId + "')", null);
      writer.writeAttribute("value", Integer.toString(i), null);
      if (treeNode.equals(tree.getSelectedNode(level))
          || tree.isSelectedNode(node)) {
        writer.writeAttribute("selected", true);
      }

      writer.writeText(treeNode.getAttributes().get(ATTR_NAME), null);
      if (node.getChildCount() > 0) {
        writer.writeText(" \u2192", null);
      }
      writer.endElement("option");
    }


    writer.endElement("select");
  }

}
