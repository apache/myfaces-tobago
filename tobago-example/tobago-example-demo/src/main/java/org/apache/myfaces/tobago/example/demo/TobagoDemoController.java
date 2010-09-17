package org.apache.myfaces.tobago.example.demo;

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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.CreateComponentUtils;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.example.data.CategoryTree;
import org.apache.myfaces.tobago.example.data.Solar;
import org.apache.myfaces.tobago.example.data.SolarObject;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TobagoDemoController {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoDemoController.class);

  private String[] salutation;

  private SelectItem[] salutationItems;

  // XXX jetty 6.1.22 has problems with boolean[] (primitive), see: http://jira.codehaus.org/browse/JETTY-1181
  private Boolean[] bool;

  private boolean update;

  private Boolean boolTest;

  private String[] text;

  private SolarObject[] solarArray;

  private List<SolarObject> solarList;

  private List<UIColumn> solarArrayColumns;

  private String solarArrayColumnLayout;

  private DefaultMutableTreeNode tree;

  private boolean showJunctions;

  private int value;

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

    bool = new Boolean[]{true, false, true, false, true, false, true, false, true, false};
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

    tree = CategoryTree.createSample();
    String[] values = {"none", "single", "singleLeafOnly", "multi", "multiLeafOnly"};
    selectionItems = getSelectItems(values, "demo");
    selectionType = (String) selectionItems[0].getValue();

    showIcons = true;
    showJunctions = true;
    showRoot = true;
    showRootJunction = true;

    solar = new Solar();

    String[] toolbarIconKeys
        = {UIToolBar.ICON_OFF, UIToolBar.ICON_SMALL, UIToolBar.ICON_BIG};
    toolbarIconItems = getSelectItems(toolbarIconKeys, "demo");
    toolbarIconSize = UIToolBar.ICON_SMALL;

    String[] toolbarTextKeys =
        {UIToolBar.LABEL_OFF, UIToolBar.LABEL_BOTTOM, UIToolBar.LABEL_RIGHT};
    toolbarTextItems = getSelectItems(toolbarTextKeys, "demo");
    toolbarTextPosition = UIToolBar.LABEL_BOTTOM;
  }


  public boolean isUpdate() {
    update = !update;
    return update;
  }

  public boolean isJsp() {
    String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    int end = viewId.lastIndexOf(".xhtml");
    return end < 0;
  }

  public boolean isDoubleDefined() {
    String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    String name = viewId.substring(1, viewId.lastIndexOf("."));
    String path = ResourceManagerUtils.getImageWithPath(FacesContext.getCurrentInstance(), name + ".xhtml", true);
    return path != null;
  }

  public String asJsp() {
    String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    int end = viewId.lastIndexOf(".xhtml");
    if (end >= 0) {
      return viewId.substring(1, end) + ".jspx";
    }
    LOG.warn("Can't create the outcome");
    return null;
  }

  public String asFacelet() {
    String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    int end = viewId.lastIndexOf(".jspx");
    if (end >= 0) {
      return viewId.substring(1, end) + ".xhtml";
    }
    LOG.warn("Can't create the outcome");
    return null;
  }

  public String resetSession() throws IOException {
    LOG.info("Resetting the session.");
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
    if (session != null) {
      session.invalidate();
    }
    ExternalContext externalContext = facesContext.getExternalContext();
    externalContext.redirect(externalContext.getRequestContextPath());
    facesContext.responseComplete();
    return null;
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

    FacesContext facesContext = FacesContext.getCurrentInstance();
    UIInput textbox = (UIInput)
        CreateComponentUtils.createComponent(facesContext, UIInput.COMPONENT_TYPE, RendererTypes.IN, "sac1i");
    textbox.setValueBinding(
        Attributes.VALUE, facesContext.getApplication().createValueBinding("#{luminary.population}"));

    columns.add(CreateComponentUtils.createColumn(
        "#{overviewBundle.solarArrayPopulation}", "true", null, textbox, "sac1"));

    columns.add(CreateComponentUtils.createTextColumn(
        "#{overviewBundle.solarArrayDistance}", "true", "right", "#{luminary.distance}", "sac1"));

    columns.add(CreateComponentUtils.createTextColumn(
        "#{overviewBundle.solarArrayPeriod}", "true", "right", "#{luminary.period}", "sac1"));


    return columns;
  }


  public static SelectItem[] getSelectItems(String[] keys, String bundle) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    SelectItem[] items = new SelectItem[keys.length];
    for (int i = 0; i < items.length; i++) {
      String label = ResourceManagerUtils.getPropertyNotNull(facesContext, bundle, keys[i]);
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

  public Boolean[] getBool() {
    return this.bool;
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
    solarList.get(0).setDistance(value++);
    if (value > 100) {
      value = 0;
    }
    return solarList;
  }


  public void selectOrbit(ActionEvent event) {
    SolarObject clicked = (SolarObject) ComponentUtils.findParameter(event.getComponent(), "luminary");
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

  public void stateChangeListener(ActionEvent e) {
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

}
