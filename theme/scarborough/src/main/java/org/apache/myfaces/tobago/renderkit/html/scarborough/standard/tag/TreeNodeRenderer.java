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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.component.UITreeListbox;
import org.apache.myfaces.tobago.component.UITreeMenu;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.ResourceUtils;
import org.apache.myfaces.tobago.event.TreeExpansionEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNode;
import org.apache.myfaces.tobago.internal.context.ResponseWriterDivider;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseWriterImpl;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.model.TreeState;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TreeNodeRenderer extends CommandRendererBase {

  private static final Log LOG = LogFactory.getLog(TreeNodeRenderer.class);

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
    int level = node.getJunctions().size();
    String id = node.getClientId(facesContext);
    boolean expanded = isExpanded(tree, node);

    if (level > 0) { // root will not rendered as an option
      writer.startElement(HtmlConstants.OPTION, null);
      writer.writeAttribute(HtmlAttributes.VALUE, node.getValue().toString(), true); // XXX converter?
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
        StyleClasses levelClass = new StyleClasses();
        levelClass.addClass("treeListbox", "level");
        writer.writeClassAttribute(levelClass);
        Style levelStyle = new Style();
        levelStyle.setLeft(Measure.valueOf(level * 160)); // xxx 160 should be configurable
        writer.writeStyleAttribute(levelStyle);
        // at the start of each div there is an empty and disabled select tag to show empty area.
        // this is not needed for the 1st level.
        if (level > 0) {
          writer.startElement(HtmlConstants.SELECT, null);
          writer.writeAttribute(HtmlAttributes.DISABLED, true);
          writer.writeAttribute(HtmlAttributes.SIZE, 2); // must be > 1, but the size comes from the layout
          StyleClasses selectClass = new StyleClasses();
          selectClass.addClass("treeListbox", "select");
          writer.writeClassAttribute(selectClass);
          writer.endElement(HtmlConstants.SELECT);
        }
      }

      writer.startElement(HtmlConstants.SELECT, node);
      writer.writeIdAttribute(id + ComponentUtils.SUB_SEPARATOR + "select");
      StyleClasses selectClass = new StyleClasses();
      selectClass.addClass("treeListbox", "select");
      writer.writeClassAttribute(selectClass);
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

    String treeId = tree.getClientId(facesContext);
    boolean folder = node.isFolder();
    boolean marked = node.isMarked();
    String id = node.getClientId(facesContext);
    int depth = node.getDepth();
    boolean hasNextSibling = node.isHasNextSibling();
    List<Boolean> junctions = node.getJunctions();

    boolean expanded = isExpanded(tree, node);
    boolean showIcons = false;
    boolean showJunctions = false;
    boolean showRootJunction = false;
    if (tree instanceof UITree) {
      showIcons = ((UITree) tree).isShowIcons();
      showJunctions = ((UITree) tree).isShowJunctions();
      showRootJunction = ((UITree) tree).isShowRootJunction();
    }
    boolean showRoot = tree.isShowRoot();

    if (!showRoot && junctions.size() > 0) {
      junctions.remove(0);
    }

    String source;
    String openSource = null;
    String closedSource;

    String image = ComponentUtils.getStringAttribute(node, "image");
    if (image != null) { // application image
      closedSource = ResourceManagerUtil.getImageWithPath(facesContext, image);
    } else { // theme image
      closedSource = ResourceManagerUtil.getImageWithPath(facesContext, CLOSED_FOLDER);
    }
    if (folder) {
      if (image != null) { // application image
        openSource = ResourceManagerUtil.getImageWithPath(facesContext,
            ResourceUtils.addPostfixToFilename(image, "open"), true);
      } else { // theme image
        openSource = ResourceManagerUtil.getImageWithPath(facesContext, OPEN_FOLDER);
      }
      source = expanded ? openSource : closedSource;
    } else {
      if (image != null) { // application image
        source = ResourceManagerUtil.getImageWithPath(facesContext,
            ResourceUtils.addPostfixToFilename(image, "leaf"), true);
      } else { // theme image
        source = ResourceManagerUtil.getImageWithPath(facesContext, LEAF);
      }
      if (source == null) {
        source = closedSource;
      }
    }

    CommandRendererHelper helper = new CommandRendererHelper(facesContext, node, CommandRendererHelper.Tag.ANCHOR);
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    if (showRoot || depth != 0) {
      writer.startElement(HtmlConstants.DIV, null);

      // div id
      writer.writeIdAttribute(id);
      if (!folder) {
        HtmlRendererUtils.renderDojoDndItem(node, writer, true);
      }

      // div class (css)
      StyleClasses styleClasses = StyleClasses.ensureStyleClasses(node);
      styleClasses.updateClassAttributeAndMarkup(node, "treeNode");
      if (isMenu) {
        styleClasses.addClass("treeNode", "menu");
        if (marked) {
          styleClasses.addClass("treeNode", "marker");
        }
      }
      styleClasses.addMarkupClass(node, "treeNode");
      writer.writeClassAttribute(styleClasses);

      // div style (width)
      Style style = new Style(facesContext, tree);
      String widthString;
      if (style != null && style.getWidth() != null) {
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

      encodeIndent(facesContext, writer, isMenu, junctions);

      encodeTreeJunction(facesContext, writer, id, treeId, showJunctions, showRootJunction, showRoot, expanded,
          folder, depth, hasNextSibling, openSource, closedSource);

      encodeTreeIcons(writer, id, treeId, showIcons, folder, source, openSource, closedSource);

      encodeLabel(writer, helper, node, marked, treeId);

      UIComponent facet = node.getFacet(Facets.ADDENDUM);
      if (facet != null) {
        RenderUtil.encode(facesContext, facet);
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

  private void encodeExpandedHidden(TobagoResponseWriter writer, AbstractUITreeNode node, String clientId,
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
    String menuOpen = ResourceManagerUtil.getImageWithPath(facesContext, "image/treeMenuOpen.gif");
    String menuClose = ResourceManagerUtil.getImageWithPath(facesContext, "image/treeMenuClose.gif");
    String onclick = "tobagoTreeNodeToggle(this.parentNode, '" + treeId + "', null, null, '"
        + menuOpen + "', '" + menuClose + "')";
    Object objOnclick = node.getAttributes().get("onclick");
    if (null != objOnclick) {
      onclick += ";" + objOnclick;
    }
    String src = expanded ? menuOpen : menuClose;
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-tree-menu-icon");
    writer.writeIdAttribute(id + "-menuIcon");
    writer.writeAttribute("src", src, true);
    writer.writeAttribute("onclick", onclick, true);
    writer.writeAttribute("alt", "", false);
    writer.endElement(HtmlConstants.IMG);
  }

  private void encodeIndent(
      FacesContext facesContext, TobagoResponseWriter writer, boolean menuMode, List<Boolean> junctions)
      throws IOException {

    String blank = ResourceManagerUtil.getImageWithPath(facesContext, "image/blank.gif");
    String perpendicular = ResourceManagerUtil.getImageWithPath(facesContext, "image/I.gif");

    for (Boolean junction : junctions) {
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeClassAttribute("tree-junction");
      if (junction && !menuMode) {
        writer.writeAttribute("src", perpendicular, true);
      } else {
        writer.writeAttribute("src", blank, true);
      }
      writer.endElement(HtmlConstants.IMG);
    }
  }

  private void encodeTreeJunction(
      FacesContext facesContext, TobagoResponseWriter writer, String id, String treeId,
      boolean showJunctions, boolean showRootJunction, boolean showRoot, boolean expanded, boolean folder,
      int depth, boolean hasNextSibling, String openSource, String closedSource)
      throws IOException {
    if (!(!showJunctions
        || !showRootJunction && depth == 0
        || !showRootJunction && !showRoot && depth == 1)) {
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeClassAttribute("tree-junction");
      writer.writeIdAttribute(id + "-junction");

      String gif = folder && expanded
          ? (depth == 0
            ? "Rminus.gif"
            : (hasNextSibling ? "Tminus.gif" : "Lminus.gif"))
          : ((depth == 0)
            ? "Rplus.gif"
            : (hasNextSibling)
              ? (folder ? "Tplus.gif" : "T.gif")
              : (folder ? "Lplus.gif" : "L.gif")
      );

      String src = ResourceManagerUtil.getImageWithPath(facesContext, "image/" + gif);
      writer.writeAttribute("src", src, true);
      if (folder) {
        writer.writeAttribute("onclick", createOnclickForToggle(treeId, openSource, closedSource), true);
      }
      writer.writeAttribute("alt", "", false);
//    } else if (( !this.hideRoot && depth >0 ) || (this.hideRoot && depth > 1)) {
//      str += '<img class="tree-junction" id="' + this.id
//          + '-junction" src="' + this.treeResources.getImage("blank.gif")
//          + '" alt="">';
      writer.endElement(HtmlConstants.IMG);
    }
  }

  private void encodeTreeIcons(
      TobagoResponseWriter writer, String id, String treeId,
      boolean showIcons, boolean folder, String source, String openSource, String closedSource)
      throws IOException {

    if (showIcons) {
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeClassAttribute("tree-icon");
      writer.writeIdAttribute(id + "-icon");

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

    if (helper.isDisabled()) {
      writer.startElement(HtmlConstants.SPAN, null);
    } else {
      writer.startElement(HtmlConstants.A, null);
      writer.writeAttribute(HtmlAttributes.HREF, helper.getHref(), true);
      writer.writeAttribute(HtmlAttributes.ONCLICK, helper.getOnclick(), true); // xxx is escaping required?
      writer.writeAttribute(
          HtmlAttributes.ONFOCUS, "Tobago.Tree.storeMarker(this.parentNode, '" + treeId + "')", false);
      if (helper.getTarget() != null) {
        writer.writeAttribute(HtmlAttributes.TARGET, helper.getTarget(), true);
      }
    }
    if (marked) {
      StyleClasses classes = new StyleClasses();
      classes.addClass("treeNode", "marker");
      writer.writeClassAttribute(classes);
    }
    String objTip = node.getTip();
    if (objTip != null) {
//XXX is needed?      tip = StringEscapeUtils.escapeJavaScript(tip);
      writer.writeAttribute(HtmlAttributes.TITLE, String.valueOf(objTip), true);
    }
    String label = node.getLabel();
    if (label == null) {
      LOG.warn("label == null");
      label = "label";
    }
    writer.writeText(label);
    if (helper.isDisabled()) {
      writer.endElement(HtmlConstants.SPAN);
    } else {
      writer.endElement(HtmlConstants.A);
    }
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
