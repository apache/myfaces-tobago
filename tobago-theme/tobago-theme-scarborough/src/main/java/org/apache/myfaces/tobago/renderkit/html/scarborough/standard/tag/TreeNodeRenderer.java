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

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.component.UITreeListbox;
import org.apache.myfaces.tobago.component.UITreeMenu;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.context.ResourceUtils;
import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.internal.context.ResponseWriterDivider;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseWriterImpl;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.model.TreeState;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TreeNodeRenderer extends CommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TreeNodeRenderer.class);

  protected static final String OPEN_FOLDER
      = ResourceUtils.createString("image", "treeNode", "icon", "open", ResourceUtils.GIF);
  protected static final String CLOSED_FOLDER
      = ResourceUtils.createString("image", "treeNode", "icon", ResourceUtils.GIF);
  protected static final String LEAF
      = ResourceUtils.createString("image", "treeNode", "icon", "leaf", ResourceUtils.GIF);

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {

    UITreeNode node = (UITreeNode) component;

    super.decode(facesContext, node);

    if (ComponentUtils.isOutputOnly(node)) {
      return;
    }

    AbstractUITree tree = node.findTree();
    String treeId = tree.getClientId(facesContext);
    String nodeStateId = node.nodeStateId(facesContext);
    Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String id = node.getClientId(facesContext);

    // expand state
    boolean expanded = Boolean.parseBoolean((String) requestParameterMap.get(id + "-expanded"));
    setExpanded(node, tree, expanded);

    // select
    String searchString;
    if (TreeRenderer.isSelectable(tree)) { // selection
      String selected = (String) requestParameterMap.get(treeId + AbstractUITree.SELECT_STATE);
      searchString = ";" + nodeStateId + ";";
      if (StringUtils.contains(selected, searchString)) {
        // TODO: add selection to Component
        //state.addSelection((DefaultMutableTreeNode) node.getValue());
      }
    }

    // marker
    String marked = (String) requestParameterMap.get(treeId + AbstractUITree.MARKER);
    if (marked != null) {
      searchString = treeId + NamingContainer.SEPARATOR_CHAR + nodeStateId;
      node.setMarked(marked.equals(searchString));
    } else {
      LOG.warn("This log message is help clarifying the occurrence of this else case.");
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    UITreeNode node = (UITreeNode) component;
    AbstractUITree tree = node.findTree();
    if (tree instanceof UITree || tree instanceof UITreeMenu) {
      encodeBeginMenuAndNormal(facesContext, node, tree, tree instanceof UITreeMenu);
    } else { // if (tree instanceof UITreeListbox)
      encodeBeginListbox(facesContext, node, (UITreeListbox) tree);
    }
  }

  public void encodeBeginListbox(FacesContext facesContext, UITreeNode node, UITreeListbox tree) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    boolean folder = node.isFolder();
    int level = node.getLevel();
    String id = node.getClientId(facesContext);
    boolean expanded = isExpanded(tree, node);

    if (level > 0) { // root will not rendered as an option
      writer.startElement(HtmlConstants.OPTION, null);
// todo: define where to store the selection of a tree, node.getValue() seems not to be a god place.
//        writer.writeAttribute(HtmlAttributes.VALUE, node.getValue().toString(), true); // XXX converter?
      writer.writeIdAttribute(id);
      writer.writeAttribute(HtmlAttributes.SELECTED, expanded);
      writer.writeText("(" + level + ") " + node.getLabel());
      if (folder) {
        writer.writeText(" \u2192");
      }
      writer.endElement(HtmlConstants.OPTION);
    }

    if (folder) {
      boolean siblingMode = "siblingLeafOnly".equals(tree.getAttributes().get(Attributes.SELECTABLE));
      ResponseWriterDivider divider = ResponseWriterDivider.getInstance(facesContext, TreeListboxRenderer.DIVIDER);
      boolean alreadyExists = divider.activateBranch(facesContext);
      writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
      if (!alreadyExists) {
        writer.startElement(HtmlConstants.DIV, null);
        writer.writeClassAttribute(Classes.create(tree, "level"));
        Style levelStyle = new Style();
        levelStyle.setLeft(Measure.valueOf(level * 160)); // xxx 160 should be configurable
        writer.writeStyleAttribute(levelStyle);
        // at the start of each div there is an empty and disabled select tag to show empty area.
        // this is not needed for the 1st level.
        if (level > 0) {
          writer.startElement(HtmlConstants.SELECT, null);
          writer.writeAttribute(HtmlAttributes.DISABLED, true);
          writer.writeAttribute(HtmlAttributes.SIZE, 2); // must be > 1, but the size comes from the layout
          writer.writeClassAttribute(Classes.create(tree, "select"));
          writer.endElement(HtmlConstants.SELECT);
        }
      }

      writer.startElement(HtmlConstants.SELECT, node);
      writer.writeIdAttribute(id + ComponentUtils.SUB_SEPARATOR + "select");
      writer.writeClassAttribute(Classes.create(tree, "select"));
      if (!expanded) {
        Style selectStyle = new Style();
        selectStyle.setDisplay(Display.NONE);
      }
      writer.writeAttribute(HtmlAttributes.SIZE, 2); // must be > 1, but the size comes from the layout
      writer.writeAttribute(HtmlAttributes.MULTIPLE, siblingMode);

// XXX insert options here
      writer.startElement(HtmlConstants.OPTGROUP, null);
      writer.writeAttribute(HtmlAttributes.LABEL, node.getLabel(), true);
      writer.endElement(HtmlConstants.OPTGROUP);
    }
  }

  public void encodeBeginMenuAndNormal(FacesContext facesContext, UITreeNode node, AbstractUITree tree, boolean isMenu) 
      throws IOException {

    final String treeId = tree.getClientId(facesContext);
    final boolean folder = node.isFolder();
    final boolean marked = node.isMarked();
    final String id = node.getClientId(facesContext);
    final int level = node.getLevel();
    final boolean hasNextSibling = node.isHasNextSibling();
    final List<Boolean> junctions = node.getJunctions();

    final boolean showRoot = tree.isShowRoot();
    final boolean expanded = isExpanded(tree, node) || !showRoot && level == 0;
    final boolean showIcons;
    final boolean showJunctions;
    final boolean showRootJunction;
    if (tree instanceof UITree) {
      showIcons = ((UITree) tree).isShowIcons();
      showJunctions = ((UITree) tree).isShowJunctions();
      showRootJunction = ((UITree) tree).isShowRootJunction();
    } else { // UITreeMenu
      showIcons = false;
      showJunctions = false;
      showRootJunction = false;
    }

    if (!showRoot && junctions.size() > 0) {
      junctions.remove(0);
    }

    String source;
    final String openSource;
    final String closedSource;

    final String image = ComponentUtils.getStringAttribute(node, "image");
    if (image != null) { // application image
      closedSource = ResourceManagerUtils.getImageWithPath(facesContext, image);
    } else { // theme image
      closedSource = ResourceManagerUtils.getImageWithPath(facesContext, CLOSED_FOLDER);
    }
    if (folder) {
      if (image != null) { // application image
        openSource = ResourceManagerUtils.getImageWithPath(facesContext,
            ResourceUtils.addPostfixToFilename(image, "open"), true);
      } else { // theme image
        openSource = ResourceManagerUtils.getImageWithPath(facesContext, OPEN_FOLDER);
      }
      source = expanded ? openSource : closedSource;
    } else {
      openSource = null;
      if (image != null) { // application image
        source = ResourceManagerUtils.getImageWithPath(facesContext,
            ResourceUtils.addPostfixToFilename(image, "leaf"), true);
      } else { // theme image
        source = ResourceManagerUtils.getImageWithPath(facesContext, LEAF);
      }
      if (source == null) {
        source = closedSource;
      }
    }

    CommandRendererHelper helper = new CommandRendererHelper(facesContext, node, CommandRendererHelper.Tag.ANCHOR);
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    if (showRoot || level != 0) {
      writer.startElement(HtmlConstants.DIV, null);

      // div id
      writer.writeIdAttribute(id);
      if (!folder) {
        HtmlRendererUtils.renderDojoDndItem(node, writer, true);
      }

      // div class (css)
      String classes;
      classes = Classes.create(node).getStringValue(); // XXX: not nice, menu may use its own renderer
      if (isMenu) {
        classes = classes + " " + Classes.create(node, "menu").getStringValue();
        if (marked) {
          classes = classes + " " + Classes.create(node, "marker").getStringValue(); // TODO: marker as markup 
        }
      }
      writer.writeClassAttribute(classes);

      // div style (width)
      Style style = new Style(facesContext, tree);
      String widthString;
      if (style.getWidth() != null) {
        widthString = "width: " + Integer.toString(style.getWidth().getPixel() - 22); // fixme: 4 + 18 for scrollbar
      } else {
        widthString = "100%";
      }
      writer.writeStyleAttribute(widthString);

      if (folder) {
        encodeExpandedHidden(writer, node, id, expanded);
      }

      if (folder && isMenu) {
        encodeMenuIcon(facesContext, writer, treeId, id, expanded, node);
      }

      encodeIndent(facesContext, writer, node, isMenu, showJunctions, junctions);

      encodeTreeJunction(facesContext, writer, node, id, treeId, showJunctions, showRootJunction, showRoot, expanded,
          folder, level, hasNextSibling, openSource, closedSource);

      encodeTreeIcons(writer, node, id, treeId, showIcons, folder, source, openSource, closedSource);

      encodeLabel(writer, helper, node, marked, treeId);

      UIComponent facet = node.getFacet(Facets.ADDENDUM);
      if (facet != null) {
        RenderUtils.encode(facesContext, facet);
      }

      writer.endElement(HtmlConstants.DIV);
    }

    if (folder) {
      String contentStyle = "display: " + (expanded ? "block" : "none") + ";";
      writer.startElement(HtmlConstants.DIV, null);
      writer.writeIdAttribute(id + "-cont");
      writer.writeStyleAttribute(contentStyle);
    }
  }

  private void encodeExpandedHidden(TobagoResponseWriter writer, UITreeNode node, String clientId,
                                    boolean expanded) throws IOException {
    writer.startElement(HtmlConstants.INPUT, node);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + "-expanded");
    writer.writeIdAttribute(clientId + "-expanded");
    writer.writeAttribute(HtmlAttributes.VALUE, Boolean.toString(expanded), false);
    writer.endElement(HtmlConstants.INPUT);
  }

  private void encodeMenuIcon(
      FacesContext facesContext, TobagoResponseWriter writer, String treeId, String id, boolean expanded,
      UIComponent node)
      throws IOException {
    String menuOpen = ResourceManagerUtils.getImageWithPath(facesContext, "image/treeMenuOpen.gif");
    String menuClose = ResourceManagerUtils.getImageWithPath(facesContext, "image/treeMenuClose.gif");
    String onclick = "tobagoTreeNodeToggle(this.parentNode, '" + treeId + "', null, null, '"
        + menuOpen + "', '" + menuClose + "')";
    Object objOnclick = node.getAttributes().get("onclick");
    if (null != objOnclick) {
      onclick += ";" + objOnclick;
    }
    String src = expanded ? menuOpen : menuClose;
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute(Classes.create(node, "menuIcon"));
    writer.writeIdAttribute(id + "-menuIcon");
    writer.writeAttribute("src", src, true);
    writer.writeAttribute("onclick", onclick, true);
    writer.writeAttribute("alt", "", false);
    writer.endElement(HtmlConstants.IMG);
  }

  private void encodeIndent(
      final FacesContext facesContext, final TobagoResponseWriter writer, final UITreeNode node, final boolean menuMode,
      final boolean showJunctions, final List<Boolean> junctions)
      throws IOException {

    String blank = ResourceManagerUtils.getImageWithPath(facesContext, "image/blank.gif");
    String perpendicular = ResourceManagerUtils.getImageWithPath(facesContext, "image/I.gif");

    for (Boolean junction : junctions) {
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeClassAttribute(Classes.create(node, "junction"));
      if (junction && !menuMode && showJunctions) {
        writer.writeAttribute("src", perpendicular, true);
      } else {
        writer.writeAttribute("src", blank, true);
      }
      writer.endElement(HtmlConstants.IMG);
    }
  }

  private void encodeTreeJunction(
      FacesContext facesContext, TobagoResponseWriter writer, UITreeNode node, String id, String treeId,
      boolean showJunctions, boolean showRootJunction, boolean showRoot, boolean expanded, boolean folder,
      int level, boolean hasNextSibling, String openSource, String closedSource)
      throws IOException {
    if (!(!showJunctions
        || !showRootJunction && level == 0
        || !showRootJunction && !showRoot && level == 1)) {
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeClassAttribute(Classes.create(node, "junction"));
      writer.writeIdAttribute(id + "-junction");

      String gif = folder && expanded
          ? (level == 0
            ? "Rminus.gif"
            : (hasNextSibling ? "Tminus.gif" : "Lminus.gif"))
          : ((level == 0)
            ? "Rplus.gif"
            : (hasNextSibling)
              ? (folder ? "Tplus.gif" : "T.gif")
              : (folder ? "Lplus.gif" : "L.gif")
      );

      String src = ResourceManagerUtils.getImageWithPath(facesContext, "image/" + gif);
      writer.writeAttribute("src", src, true);
      if (folder) {
        writer.writeAttribute("onclick", createOnclickForToggle(treeId, openSource, closedSource), true);
      }
      writer.writeAttribute("alt", "", false);
      writer.endElement(HtmlConstants.IMG);
    }
  }

  private void encodeTreeIcons(
      TobagoResponseWriter writer, UITreeNode node, String id, String treeId,
      boolean showIcons, boolean folder, String source, String openSource, String closedSource)
      throws IOException {

    if (showIcons) {
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeClassAttribute(Classes.create(node, "icon"));
      writer.writeIdAttribute(id + "-icon"); //XXX may not okay with naming conventions

      writer.writeAttribute("src", source, true);
      if (folder) {
        writer.writeAttribute("onclick", createOnclickForToggle(treeId, openSource, closedSource), true);
      }
      writer.writeAttribute("alt", "", false);
      writer.endElement(HtmlConstants.IMG);
    }
  }

  private String createOnclickForToggle(String treeId, String openSouce, String closedSource) {
    if (openSouce == null) { // default
      openSouce = closedSource;
    }
    return "tobagoTreeNodeToggle(this.parentNode, '" + treeId + "', '"
        + openSouce + "', '" + closedSource + "', null, null)";
  }

/*
  if (this.isFolder) {
    str += '<img class="tree-icon" id="' + this.id + '-icon" '
        + 'src="' + (this.expanded ? this.openIcon : this.icon) + ' " '
        + 'onclick="toggle(this.parentNode, \'' + this.treeHiddenId
        + '\', \'' + this.treeResources.getImage("image/treeNode-icon-open.gif")
        + '\', \'' + this.treeResources.getImage("image/treeNode-icon.gif")
        + '\')"'
        + ' alt="">';
  } else {
    str += '<img class="tree-icon" id="' + this.id
        + '-icon" src="' + this.treeResources.getImage("image/treeNode-icon-leaf.gif") + '" alt="">';
  }
*/

  private void encodeLabel(
      TobagoResponseWriter writer, CommandRendererHelper helper, UITreeNode node, boolean marked, String treeId)
      throws IOException {

    writer.startElement(HtmlConstants.A, null);
    if (!helper.isDisabled()) {
      writer.writeAttribute(HtmlAttributes.HREF, helper.getHref(), true);
      writer.writeAttribute(HtmlAttributes.ONCLICK, helper.getOnclick(), true); // xxx is escaping required?
      writer.writeAttribute(
          HtmlAttributes.ONFOCUS, "Tobago.Tree.storeMarker(this.parentNode, '" + treeId + "')", false);
      if (helper.getTarget() != null) {
        writer.writeAttribute(HtmlAttributes.TARGET, helper.getTarget(), true);
      }
    }
    if (marked) {
      writer.writeClassAttribute(Classes.create(node, "marker"));
    }
    String tip = node.getTip();
    if (tip != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    }
    String label = node.getLabel();
    if (label == null) {
      label = "";
    }
    writer.writeText(label);
    writer.endElement(HtmlConstants.A);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    UITreeNode node = (UITreeNode) component;
    AbstractUITree tree = node.findTree();
    if (tree instanceof UITree || tree instanceof UITreeMenu) {
      encodeEndMenuAndNormal(facesContext, node);
    } else { // if (tree instanceof UITreeListbox)
      encodeEndListbox(facesContext, node);
    }
  }

  protected void encodeEndListbox(FacesContext facesContext, UITreeNode node) throws IOException {
    boolean folder = node.isFolder();
    if (folder) {
      TobagoResponseWriterImpl writer 
          = (TobagoResponseWriterImpl) HtmlRendererUtils.getTobagoResponseWriter(facesContext);
      writer.endElement(HtmlConstants.SELECT);
      ResponseWriterDivider divider = ResponseWriterDivider.getInstance(facesContext, TreeListboxRenderer.DIVIDER);
      divider.passivateBranch(facesContext);
    }
  }

  protected void encodeEndMenuAndNormal(FacesContext facesContext, UITreeNode node) throws IOException {

    boolean folder = node.isFolder();
    String id = node.getClientId(facesContext);

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    if (folder) {
      writer.endElement(HtmlConstants.DIV);
      writer.writeComment("\nend of " + id + "-cont ");
    }
  }

  private boolean isExpanded(AbstractUITree tree, UITreeNode node) {
    TreeState state = (TreeState) tree.getState();
    if (state != null) {
      return state.getExpanded().contains(node.getPath());
    } else {
      return node.isExpanded();
    }
  }

  private void setExpanded(UITreeNode node, AbstractUITree tree, boolean expanded) {
    boolean oldExpanded = isExpanded(tree, node);

    if (tree.getState() != null) {
      if (expanded) {
        ((TreeState) tree.getState()).getExpanded().add(node.getPath());
      } else {
        ((TreeState) tree.getState()).getExpanded().remove(node.getPath());
      }
    } else {
      node.setExpanded(expanded);
    }
    if (oldExpanded != expanded) {
      new TreeExpansionEvent(node, node.isExpanded(), expanded).queue();
    }
  }
}
