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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_NAME;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.component.UITreeListbox;
import org.apache.myfaces.tobago.component.UITreeListboxBox;
import org.apache.myfaces.tobago.component.UITreeOldNode;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.List;

/*
 * User: weber
 * Date: Mar 21, 2005
 * Time: 11:19:03 AM
 */
public class TreeListboxBoxRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(TreeListboxBoxRenderer.class);

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {

    int level = ((UITreeListboxBox) component).getLevel();

    UITreeListbox tree = (UITreeListbox) component.getParent();
    List<UITreeOldNode> selectionPath = tree.getSelectionPath();
    String className = "tobago-treeListbox-default";
    if (selectionPath.size() > 0 &&  selectionPath.size() - 1 <= level
        && selectionPath.get(selectionPath.size() - 1).getTreeNode().isLeaf()) {
      className += " tobago-treeListbox-unused";
    }

    String treeId = tree.getClientId(facesContext);
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    final boolean siblingMode
        = "siblingLeafOnly".equals(tree.getAttributes().get(ATTR_SELECTABLE));

    String listboxId = treeId + SUBCOMPONENT_SEP + "cont_" + level;
    String onChange = "tbgTreeListboxChange(this, '" + treeId + "')";
    String onClick  = "tbgTreeListboxClick(this, '" + treeId + "')";
    writer.startElement(HtmlConstants.SELECT, component);
    writer.writeIdAttribute(listboxId);
    writer.writeClassAttribute(className);
    writer.writeStyleAttribute();
    writer.writeAttribute(HtmlAttributes.SIZE, 2);
    if (siblingMode) {
      writer.writeAttribute(HtmlAttributes.ONCHANGE, onChange, null);
    } else {
      writer.writeAttribute(HtmlAttributes.ONCLICK, onClick, null);
    }
    writer.writeAttribute(HtmlAttributes.MULTIPLE, siblingMode);


    List nodes = ((UITreeListboxBox) component).getNodes();

    for (int i = 0; i < nodes.size(); i++) {
      UITreeOldNode treeNode = (UITreeOldNode) nodes.get(i);
      DefaultMutableTreeNode node = treeNode.getTreeNode();

      writer.startElement(HtmlConstants.OPTION, treeNode);
//      writer.writeAttribute(HtmlAttributes.ONCLICK, "tbgTreeListboxClick(this, '" + treeId + "')", null);
      writer.writeAttribute(HtmlAttributes.VALUE, i);
      if (treeNode.equals(tree.getSelectedNode(level))
          || tree.isSelectedNode(node)) {
        writer.writeAttribute(HtmlAttributes.SELECTED, true);
      }
      HtmlRendererUtil.renderTip(treeNode, writer);
      writer.writeText(treeNode.getAttributes().get(ATTR_NAME), null);
      if (node.getChildCount() > 0) {
        writer.writeText(" \u2192");
      }
      writer.endElement(HtmlConstants.OPTION);
    }
    writer.endElement(HtmlConstants.SELECT);
  }

}
