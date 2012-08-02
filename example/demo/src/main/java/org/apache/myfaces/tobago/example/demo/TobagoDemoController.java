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

package org.apache.myfaces.tobago.example.demo;

/*
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_ITEMS;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_IN;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.component.UISelectOne;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.example.demo.model.solar.Solar;
import org.apache.myfaces.tobago.example.demo.model.solar.SolarObject;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.model.TreeState;
import org.apache.myfaces.tobago.taglib.component.ToolBarTag;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TobagoDemoController {

  private static final Log LOG = LogFactory.getLog(TobagoDemoController.class);

  private String[] salutation;

  private SelectItem[] salutationItems;

  private boolean[] bool;

  private boolean update;

  private Boolean boolTest;

  private String[] text;

  private SolarObject[] solarArray;

  private List<SolarObject> solarList;

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

  private Object tabState3;

  private SheetState sheetState;

  private String toolbarIconSize;

  private SelectItem[] toolbarIconItems;

  private String toolbarTextPosition;

  private SelectItem[] toolbarTextItems;

  private TabChangeListener tabChangeListener;


  public TobagoDemoController() {

    String[] salutationKeys = {
      "salutation_unknown",
      "salutation_mr",
      "salutation_mrs",
      "salutation_family"
    };

    salutationItems = getSelectItems(salutationKeys, "demo");
    this.salutation = new String[]{"", "", "", ""};

    bool = new boolean[10];
    bool[0] = true;
    bool[1] = false;
    bool[2] = true;
    bool[3] = false;
    bool[4] = true;
    bool[5] = false;
    bool[6] = true;
    bool[7] = false;
    bool[8] = true;
    bool[9] = false;
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
        + "ist emphasis__\n\n**und nochmal strong**\n\n**__ strong und emphasis__**";
    solarArray = SolarObject.getArray();
    solarList = SolarObject.getList();
    solarArrayColumns = createSolarArrayColumns();
    solarArrayColumnLayout = "3*; 3*; 3*";

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

    showIcons = true;
    showJunctions = true;
    showRoot = true;
    showRootJunction = true;

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


  public boolean isUpdate() {
    update = !update;
    return update;
  }

  public void resetSession() throws IOException {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
    if (session != null) {
      session.invalidate();
    }
    ExternalContext externalContext = facesContext.getExternalContext();
    externalContext.redirect(externalContext.getRequestContextPath());
    facesContext.responseComplete(); 
  }

  public TabChangeListener getTabChangeListener() {
    LOG.error("getTabChangeListener " + tabChangeListener);
    return tabChangeListener;
  }

  public void setTabChangeListener(TabChangeListener tabChangeListener) {
    LOG.error("Setting TabChangeListener " + tabChangeListener);
    this.tabChangeListener = tabChangeListener;
  }

  private List<UIColumn> createSolarArrayColumns() {

    List<UIColumn> columns = new ArrayList<UIColumn>(3);

    UIInput textbox = (UIInput)
        ComponentUtil.createComponent(UIInput.COMPONENT_TYPE, RENDERER_TYPE_IN);
    ComponentUtil.setStringProperty(
        textbox, ATTR_VALUE, "#{luminary.population}");
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
      String label = ResourceManagerUtil.getPropertyNotNull(facesContext, bundle, keys[i]);
      items[i] = new SelectItem(keys[i], label);
    }
    return items;
  }

  public void updateTree() {

  }

  public static Node createNode(String name, String id) {
    return new Node(name, id);
  }

  public String clickButton() {
    LOG.info("clickButton");
    return "display";
  }

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

  public boolean[] getBool() {
    return this.bool;
  }

  public void setBool(boolean[] argBool) {
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

  public List<SolarObject> getSolarList() {
    return filteredList == null ? solarList : filteredList;
  }

  public void setSolarList(List<SolarObject> solarList) {
    this.solarList = solarList;
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

  public Object getTabState3() {
    return tabState3;
  }

  public void setTabState3(Object tabState3) {
    this.tabState3 = tabState3;
  }

  public SheetState getSheetState() {
    return sheetState;
  }

  public void setSheetState(SheetState sheetState) {
    this.sheetState = sheetState;
  }

  public void stateChangeListener(SheetStateChangeEvent e) {
    LOG.error("SheetState has Changed: " + e);

  }

  public void customValidator(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    if (value == null) {
      return;
    }
    if (!"tobago".equalsIgnoreCase(value.toString())) {
      throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please type in 'Tobago'",
          "Please type in 'Tobago'"));
    }
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

  private List<SolarObject> filteredList;
  private SelectItem[] orbitItems;
  private static final String SHOW_ALL_ORBITS = "Show all";

  public void selectOrbit(ActionEvent event) {
    SolarObject clicked = (SolarObject) ComponentUtil.findParameter(event.getComponent(), "luminary");
    boolean add = false;
    List<Integer> selectedRows = sheetState.getSelectedRows();
    for (int i = 0; i < solarList.size(); i++) {
      if (clicked.getOrbit().equals(solarList.get(i).getOrbit())) {
        add = !selectedRows.contains(i);
        LOG.info(" add = " + add);
        LOG.info(" i = " + i);
        break;
      }
    }

    for (int i = 0; i < solarList.size(); i++) {
      if (clicked.getOrbit().equals(solarList.get(i).getOrbit())) {
        if (add && !selectedRows.contains(i)) {
          selectedRows.add(i);
        } else {
          selectedRows.remove((Object) i);
        }
      }
    }
  }
  
  public void filterOrbit(ActionEvent event) {
    UISelectOne selectOne = (UISelectOne) event.getComponent().getFacet(FACET_ITEMS);
    if (filteredList == null) {
      filteredList = new ArrayList<SolarObject>();
    } else {
      filteredList.clear();
    }

    String filter = (String) selectOne.getValue();
    if (SHOW_ALL_ORBITS.equals(filter)) {
      filteredList.addAll(solarList);
    } else {
      for (SolarObject solarObject : solarList) {
        if (solarObject.getOrbit().equalsIgnoreCase(filter)) {
          filteredList.add(solarObject);
        }
      }
    }
  }

  public SelectItem[] getOrbitItems() {
    if (orbitItems == null) {
      createOrbitItems();
    }
    return orbitItems;
  }

  private SelectItem[] createOrbitItems() {
    orbitItems = new SelectItem[11];
    orbitItems[0] = new org.apache.myfaces.tobago.model.SelectItem(SHOW_ALL_ORBITS);
    orbitItems[1] = new org.apache.myfaces.tobago.model.SelectItem("Sun");
    orbitItems[2] = new org.apache.myfaces.tobago.model.SelectItem("Mercury");
    orbitItems[3] = new org.apache.myfaces.tobago.model.SelectItem("Venus");
    orbitItems[4] = new org.apache.myfaces.tobago.model.SelectItem("Earth");
    orbitItems[5] = new org.apache.myfaces.tobago.model.SelectItem("Mars");
    orbitItems[6] = new org.apache.myfaces.tobago.model.SelectItem("Jupiter");
    orbitItems[7] = new org.apache.myfaces.tobago.model.SelectItem("Saturn");
    orbitItems[8] = new org.apache.myfaces.tobago.model.SelectItem("Uranus");
    orbitItems[9] = new org.apache.myfaces.tobago.model.SelectItem("Neptune");
    orbitItems[10] = new org.apache.myfaces.tobago.model.SelectItem("Pluto");
    return orbitItems;
  }

//  public void setOrbitFilter(String orbitFilter) {
//    this.orbitFilter = orbitFilter;
//  }
//
//  public String getOrbitFilter() {
//    return orbitFilter;
//  }

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

    public String toString() {
      return "Node name="+name+" id="+id;
    }
  }
}
