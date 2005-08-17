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
 * $Id: TobagoDemoController.java 1270 2005-08-08 20:21:38 +0200 (Mo, 08 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.example.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.example.demo.model.solar.Solar;
import org.apache.myfaces.tobago.example.demo.model.solar.SolarObject;
import org.apache.myfaces.tobago.model.TreeState;
import org.apache.myfaces.tobago.taglib.component.ToolBarTag;

import javax.faces.component.UIColumn;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TobagoDemoController {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TobagoDemoController.class);

// ///////////////////////////////////////////// attribute

  private String[] salutation;

  private SelectItem[] salutationItems;

  private Boolean[] bool;

  private Boolean boolTest;

  private String[] text;

  private SolarObject[] solarArray;

  private List<UIColumn> solarArrayColumns;

  private String solarArrayColumnLayout;

  private TreeState treeState;

  private DefaultMutableTreeNode tree;

  private boolean showJunctions;

  private boolean showIcons;

  private boolean showRoot;

  private boolean showRootJunction;

  private boolean multiselect;

  private String selectionType;

  private SelectItem[] selectionItems;

  private boolean mutable;

  private boolean required;

  private Solar solar;

  private Date date = new Date();

  private Object tabState0;

  private Object tabState1;

  private Object tabState2;

  private Object sheetState;

  private String toolbarIconSize;

  private SelectItem[] toolbarIconItems;

  private String toolbarTextPosition;

  private SelectItem[] toolbarTextItems;

// ///////////////////////////////////////////// constructor

  public TobagoDemoController() {

    String[] salutationKeys = {
      "salutation_unknown",
      "salutation_mr",
      "salutation_mrs",
      "salutation_family"
    };

    salutationItems = getSelectItems(salutationKeys, "demo");
    salutation = new String[4];

    bool = new Boolean[10];
    bool[0] = new Boolean(true);
    bool[1] = new Boolean(false);
    bool[2] = new Boolean(true);
    bool[3] = new Boolean(false);
    bool[4] = new Boolean(true);
    bool[5] = new Boolean(false);
    bool[6] = new Boolean(true);
    bool[7] = new Boolean(false);
    bool[8] = new Boolean(true);
    bool[9] = new Boolean(false);
    boolTest = Boolean.TRUE;

    text = new String[11];
    text[0] = "Value 1";
    text[1] = "Value 2";
    text[2] = "Value 3";
    text[3] = "Value 4";
    text[4] = "Value 5";
    text[5] = "longer \nmultiline\nText";
    text[6] = "you can't edit me";
    text[7] = "inline";
    text[8] = "labeled";
    text[9] = "longer Text abcdefg hijklmnop";
    text[10]
        = "**strong text**\n\n__emphasis__\n\nnormaler text\n\n__dieses "
        +
        "ist emphasis__\n\n**und nochmal strong**\n\n**__ strong und emphasis__**";
    solarArray = SolarObject.getArray();
    solarArrayColumns = createSolarArrayColumns();
    solarArrayColumnLayout = "3*;3*;3*";

    tree = new DefaultMutableTreeNode(
        new Node("Root Node", "root"));
    tree.insert(new DefaultMutableTreeNode(new Node("Sports", "sports")), 0);
    tree.insert(new DefaultMutableTreeNode(new Node("Movies", "movies")), 0);
    DefaultMutableTreeNode music = new DefaultMutableTreeNode(
        new Node("Music", "music"));
    tree.insert(music, 0);
    tree.insert(new DefaultMutableTreeNode(new Node("Games", "games")), 0);
    MutableTreeNode temp = new DefaultMutableTreeNode(
        new Node("Science", "science"));
    temp.insert(
        new DefaultMutableTreeNode(new Node("Geography", "geography")), 0);
    temp.insert(
        new DefaultMutableTreeNode(new Node("Mathematics", "math")), 0);
    DefaultMutableTreeNode temp2 = new DefaultMutableTreeNode(
        new Node("Astronomy", "astro"));
    temp2.insert(new DefaultMutableTreeNode(new Node("Education", "edu")), 0);
    temp2.insert(new DefaultMutableTreeNode(new Node("Pictures", "pic")), 0);
    temp.insert(temp2, 2);
    tree.insert(temp, 2);
    treeState = new TreeState();
    treeState.addExpandState(tree);
    treeState.addSelection(temp2);
    treeState.setMarker(music);
    String[] values = {"none", "single", "singleLeafOnly", "multi", "multiLeafOnly"};
    selectionItems = getSelectItems(values, "demo");
    selectionType = (String) selectionItems[0].getValue();

    showIcons = showJunctions = showRoot = showRootJunction = true;

    solar = new Solar();

    String[] toolbarIconKeys
        = {ToolBarTag.ICON_OFF, ToolBarTag.ICON_SMALL, ToolBarTag.ICON_BIG};
    toolbarIconItems = getSelectItems(toolbarIconKeys, "demo");
    toolbarIconSize = ToolBarTag.ICON_SMALL;

    String[] toolbarTextKeys =
        {ToolBarTag.LABEL_OFF, ToolBarTag.LABEL_BOTTOM, ToolBarTag.LABEL_RIGHT};
    toolbarTextItems = getSelectItems(toolbarTextKeys, "demo");
    toolbarTextPosition = ToolBarTag.LABEL_BOTTOM;
  }

  private List<UIColumn> createSolarArrayColumns() {

    List<UIColumn> columns = new ArrayList<UIColumn>(3);

    UIInput textbox = (UIInput)
        ComponentUtil.createComponent(UIInput.COMPONENT_TYPE, TobagoConstants.RENDERER_TYPE_IN);
    ComponentUtil.setStringProperty(
        textbox, TobagoConstants.ATTR_VALUE, "#{luminary.population}", null);
    columns.add(ComponentUtil.createColumn(
        "#{overviewBundle.solarArrayPopulation}", "true", null, textbox));

    columns.add(ComponentUtil.createTextColumn(
        "#{overviewBundle.solarArrayDistance}", "true", "right", "#{luminary.distance}"));

    columns.add(ComponentUtil.createTextColumn(
        "#{overviewBundle.solarArrayPeriod}", "true", "right", "#{luminary.period}"));


    return columns;
  }


  public static SelectItem[] getSelectItems(String[] keys, String bundle) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    SelectItem[] items = new SelectItem[keys.length];
    for (int i = 0; i < items.length; i++) {
      String label = ResourceManagerUtil.getProperty(facesContext, bundle, keys[i]);
      items[i] = new SelectItem(keys[i], label);
    }
    return items;
  }

// ///////////////////////////////////////////// code

  public void updateTree() {

  }

  public static Node createNode(String name, String id) {
    return new Node(name, id);
  }

// /////////////////////////////////////////////// action for links and buttons

  public String clickButton() {
    LOG.info("clickButton");
    return "display";
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getSelectable() {
    return multiselect ? "multi" : "none";
  }

  public String[] getSalutation() {
    return salutation;
  }

  public void setSalutation(String[] salutation) {
    this.salutation = salutation;
  }

  public SelectItem[] getSalutationItems() {
    return salutationItems;
  }

  public void setSalutationItems(SelectItem[] salutationItems) {
    this.salutationItems = salutationItems;
  }

  public Boolean[] getBool() {
    return this.bool;
  }

  public void setBool(Boolean[] argBool) {
    this.bool = argBool;
  }

  public Boolean getBoolTest() {
    return boolTest;
  }

  public void setBoolTest(Boolean boolTest) {
    this.boolTest = boolTest;
  }

  public String[] getText() {
    return text;
  }

  public void setText(String[] text) {
    this.text = text;
  }

  public SolarObject[] getSolarArray() {
    return solarArray;
  }

  public void setSolarArray(SolarObject[] solarArray) {
    this.solarArray = solarArray;
  }

  public List<UIColumn> getSolarArrayColumns() {
    return solarArrayColumns;
  }

  public void setSolarArrayColumns(List<UIColumn> solarArrayColumns) {
    this.solarArrayColumns = solarArrayColumns;
  }

  public String getSolarArrayColumnLayout() {
    return solarArrayColumnLayout;
  }

  public void setSolarArrayColumnLayout(String solarArrayColumnLayout) {
    this.solarArrayColumnLayout = solarArrayColumnLayout;
  }

  public DefaultMutableTreeNode getTree() {
    return tree;
  }

  public void setTree(DefaultMutableTreeNode tree) {
    this.tree = tree;
  }

  public TreeState getTreeState() {
    return treeState;
  }

  public void setTreeState(TreeState treeState) {
    this.treeState = treeState;
  }

  public boolean isShowJunctions() {
    return showJunctions;
  }

  public void setShowJunctions(boolean showJunctions) {
    this.showJunctions = showJunctions;
  }

  public boolean isShowIcons() {
    return showIcons;
  }

  public void setShowIcons(boolean showIcons) {
    this.showIcons = showIcons;
  }

  public boolean isShowRoot() {
    return showRoot;
  }

  public void setShowRoot(boolean showRoot) {
    this.showRoot = showRoot;
  }

  public boolean isShowRootJunction() {
    return showRootJunction;
  }

  public void setShowRootJunction(boolean showRootJunction) {
    this.showRootJunction = showRootJunction;
  }

  public boolean isMultiselect() {
    return multiselect;
  }

  public void setMultiselect(boolean multiselect) {
    this.multiselect = multiselect;
  }

  public String getSelectionType() {
    return selectionType;
  }

  public void setSelectionType(String selectionType) {
    this.selectionType = selectionType;
  }

  public SelectItem[] getSelectionItems() {
    return selectionItems;
  }

  public void setSelectionItems(SelectItem[] selectionItems) {
    this.selectionItems = selectionItems;
  }

  public boolean isMutable() {
    return mutable;
  }

  public void setMutable(boolean mutable) {
    this.mutable = mutable;
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public Solar getSolar() {
    return solar;
  }

  public void setSolar(Solar solar) {
    this.solar = solar;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Object getTabState0() {
    return tabState0;
  }

  public void setTabState0(Object tabState0) {
    this.tabState0 = tabState0;
  }

  public Object getTabState1() {
    return tabState1;
  }

  public void setTabState1(Object tabState1) {
    this.tabState1 = tabState1;
  }

  public Object getTabState2() {
    return tabState2;
  }

  public void setTabState2(Object tabState2) {
    this.tabState2 = tabState2;
  }

  public Object getSheetState() {
    return sheetState;
  }

  public void setSheetState(Object sheetState) {
    this.sheetState = sheetState;
  }

  public SelectItem[] getToolbarIconItems() {
    return toolbarIconItems;
  }

  public String getToolbarIconSize() {
    return toolbarIconSize;
  }

  public void setToolbarIconSize(String toolbarIconSize) {
    this.toolbarIconSize = toolbarIconSize;
  }

  public SelectItem[] getToolbarTextItems() {
    return toolbarTextItems;
  }

  public String getToolbarTextPosition() {
    return toolbarTextPosition;
  }

  public void setToolbarTextPosition(String toolbarTextPosition) {
    this.toolbarTextPosition = toolbarTextPosition;
  }

  public Object getNull() {
    return null;
  }

  public void setNull(Object o) {

  }

// /////////////////////////////////////////////////////////////////////////
// /////////////////////////////////////////////////////////////////////////
// /////////////////////////////////////////////////////////////////////////

  public static class Node {

    private String name;

    private String id;

    public Node(String name, String id) {
      this.name = name;
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }


}
