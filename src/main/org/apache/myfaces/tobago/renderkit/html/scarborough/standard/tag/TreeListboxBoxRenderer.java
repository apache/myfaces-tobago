package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.component.UITreeListbox;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.component.UITreeListboxBox;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.List;

/**
 * User: weber
 * Date: Mar 21, 2005
 * Time: 11:19:03 AM
 * To change this template use File | Settings | File Templates.
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
    String onChange = "tobagoTreeListboxChange(this, '" + treeId + "')";
    String onClick = "tobagoTreeListboxClick(this, '" + treeId + "')";
    writer.startElement("select", component);
    writer.writeIdAttribute(listboxId);
    writer.writeClassAttribute(className);
    writer.writeAttribute("style" , null, ATTR_STYLE);
    writer.writeAttribute("size", "2", null);
    writer.writeAttribute("onchange", onChange, null);
    writer.writeAttribute("onclick", onClick, null);


    List nodes = ((UITreeListboxBox)component).getNodes();

    for (int i = 0; i < nodes.size(); i++) {
      UITreeNode treeNode = (UITreeNode) nodes.get(i);
      DefaultMutableTreeNode node = treeNode.getTreeNode();

      writer.startElement("option", null);
      writer.writeAttribute("value", Integer.toString(i), null);
      if (treeNode.equals(tree.getSelectedNode(level))) {
        writer.writeAttribute("selected", "selected", null);
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
