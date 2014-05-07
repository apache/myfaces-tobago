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

import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.example.data.CategoryTree;
import org.apache.myfaces.tobago.example.data.Solar;
import org.apache.myfaces.tobago.example.data.SolarObject;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SessionScoped
@Named("demo")
public class TobagoDemoController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoDemoController.class);

  private String[] salutation;

  private SelectItem[] salutationItems;

  // XXX jetty 6.1.22 has problems with boolean[] (primitive), see: http://jira.codehaus.org/browse/JETTY-1181
  private Boolean[] bool;

  private Boolean boolTest;

  private String[] text;

  private SolarObject[] solarArray;

  private List<SolarObject> solarList;

  private SolarObject currentSolarObject;

  private DefaultMutableTreeNode solarTree;

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

  private SheetState sheetTreeState;

  private String toolbarIconSize;

  private SelectItem[] toolbarIconItems;

  private String toolbarTextPosition;

  private SelectItem[] toolbarTextItems;

  private TabChangeListener tabChangeListener;

  public TobagoDemoController() {

    final String[] salutationKeys = {
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
    solarTree = SolarObject.getTree();
    sheetTreeState = new SheetState();
    sheetTreeState.setExpandedState(new ExpandedState(1));

    tree = CategoryTree.createSample();
    final String[] values = {"none", "single", "singleLeafOnly", "multi", "multiLeafOnly"};
    selectionItems = getSelectItems(values, "demo");
    selectionType = (String) selectionItems[0].getValue();

    showIcons = true;
    showJunctions = true;
    showRoot = true;
    showRootJunction = true;

    solar = new Solar();

    final String[] toolbarIconKeys
        = {UIToolBar.ICON_OFF, UIToolBar.ICON_SMALL, UIToolBar.ICON_BIG};
    toolbarIconItems = getSelectItems(toolbarIconKeys, "demo");
    toolbarIconSize = UIToolBar.ICON_SMALL;

    final String[] toolbarTextKeys =
        {UIToolBar.LABEL_OFF, UIToolBar.LABEL_BOTTOM, UIToolBar.LABEL_RIGHT};
    toolbarTextItems = getSelectItems(toolbarTextKeys, "demo");
    toolbarTextPosition = UIToolBar.LABEL_BOTTOM;
  }

  public String resetSession() throws IOException {
    LOG.info("Resetting the session.");
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
    if (session != null) {
      session.invalidate();
    }
    final ExternalContext externalContext = facesContext.getExternalContext();
    externalContext.redirect(externalContext.getRequestContextPath() + "/");
    facesContext.responseComplete();
    return null;
  }

  public TabChangeListener getTabChangeListener() {
    LOG.debug("getTabChangeListener " + tabChangeListener);
    return tabChangeListener;
  }

  public void setTabChangeListener(final TabChangeListener tabChangeListener) {
    LOG.debug("Setting TabChangeListener " + tabChangeListener);
    this.tabChangeListener = tabChangeListener;
  }

  public static SelectItem[] getSelectItems(final String[] keys, final String bundle) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final SelectItem[] items = new SelectItem[keys.length];
    for (int i = 0; i < items.length; i++) {
      final String label = ResourceManagerUtils.getPropertyNotNull(facesContext, bundle, keys[i]);
      items[i] = new SelectItem(keys[i], label);
    }
    return items;
  }

  public void updateTree() {

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

  public void setSalutation(final String[] salutation) {
    this.salutation = salutation;
  }

  public SelectItem[] getSalutationItems() {
    return salutationItems;
  }

  public void setSalutationItems(final SelectItem[] salutationItems) {
    this.salutationItems = salutationItems;
  }

  public Boolean[] getBool() {
    return this.bool;
  }

  public Boolean getBoolTest() {
    return boolTest;
  }

  public void setBoolTest(final Boolean boolTest) {
    this.boolTest = boolTest;
  }

  public String[] getText() {
    return text;
  }

  public void setText(final String[] text) {
    this.text = text;
  }

  public SolarObject[] getSolarArray() {
    return solarArray;
  }

  public void setSolarArray(final SolarObject[] solarArray) {
    this.solarArray = solarArray;
  }

  public List<SolarObject> getSolarList() {
    return solarList;
  }

  public DefaultMutableTreeNode getSolarTree() {
    return solarTree;
  }

  public void selectOrbit(final ActionEvent event) {
    final SolarObject clicked = (SolarObject) ComponentUtils.findParameter(event.getComponent(), "luminary");
    boolean add = false;
    final List<Integer> selectedRows = sheetState.getSelectedRows();
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


  public void selectLuminary(final ActionEvent actionEvent) {
    LOG.info("actionEvent=" + actionEvent);
//    final List<Integer> selectedRows = sheetState.getSelectedRows();
    final UIData data = ComponentUtils.findAncestor(actionEvent.getComponent(), UIData.class);
    if (data != null) {
      currentSolarObject = (SolarObject) data.getRowData();
      LOG.info("Selected: " + currentSolarObject.getName());
    } else {
      currentSolarObject = null;
      LOG.info("Deselect.");
    }
  }

  public void setSolarList(final List<SolarObject> solarList) {
    this.solarList = solarList;
  }

  public DefaultMutableTreeNode getTree() {
    return tree;
  }

  public void setTree(final DefaultMutableTreeNode tree) {
    this.tree = tree;
  }

  public boolean isShowJunctions() {
    return showJunctions;
  }

  public void setShowJunctions(final boolean showJunctions) {
    this.showJunctions = showJunctions;
  }

  public boolean isShowIcons() {
    return showIcons;
  }

  public void setShowIcons(final boolean showIcons) {
    this.showIcons = showIcons;
  }

  public boolean isShowRoot() {
    return showRoot;
  }

  public void setShowRoot(final boolean showRoot) {
    this.showRoot = showRoot;
  }

  public boolean isShowRootJunction() {
    return showRootJunction;
  }

  public void setShowRootJunction(final boolean showRootJunction) {
    this.showRootJunction = showRootJunction;
  }

  public boolean isMultiselect() {
    return multiselect;
  }

  public void setMultiselect(final boolean multiselect) {
    this.multiselect = multiselect;
  }

  public String getSelectionType() {
    return selectionType;
  }

  public void setSelectionType(final String selectionType) {
    this.selectionType = selectionType;
  }

  public SelectItem[] getSelectionItems() {
    return selectionItems;
  }

  public void setSelectionItems(final SelectItem[] selectionItems) {
    this.selectionItems = selectionItems;
  }

  public boolean isMutable() {
    return mutable;
  }

  public void setMutable(final boolean mutable) {
    this.mutable = mutable;
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(final boolean required) {
    this.required = required;
  }

  public Solar getSolar() {
    return solar;
  }

  public void setSolar(final Solar solar) {
    this.solar = solar;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(final Date date) {
    this.date = date;
  }

  public Object getTabState0() {
    return tabState0;
  }

  public void setTabState0(final Object tabState0) {
    this.tabState0 = tabState0;
  }

  public Object getTabState1() {
    return tabState1;
  }

  public void setTabState1(final Object tabState1) {
    this.tabState1 = tabState1;
  }

  public Object getTabState2() {
    return tabState2;
  }

  public void setTabState2(final Object tabState2) {
    this.tabState2 = tabState2;
  }

  public Object getTabState3() {
    return tabState3;
  }

  public void setTabState3(final Object tabState3) {
    this.tabState3 = tabState3;
  }

  public SheetState getSheetState() {
    return sheetState;
  }

  public void setSheetState(final SheetState sheetState) {
    this.sheetState = sheetState;
  }

  public SheetState getSheetTreeState() {
    return sheetTreeState;
  }

  public void stateChangeListener(final ActionEvent e) {
    LOG.info("SheetState has Changed: " + e);

  }

  public void customValidator(final FacesContext context, final UIComponent component, final Object value)
      throws ValidatorException {
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

  public void setToolbarIconSize(final String toolbarIconSize) {
    this.toolbarIconSize = toolbarIconSize;
  }

  public SelectItem[] getToolbarTextItems() {
    return toolbarTextItems;
  }

  public String getToolbarTextPosition() {
    return toolbarTextPosition;
  }

  public void setToolbarTextPosition(final String toolbarTextPosition) {
    this.toolbarTextPosition = toolbarTextPosition;
  }

  public Object getNull() {
    return null;
  }

  public void setNull(final Object o) {

  }

  public SolarObject getCurrentSolarObject() {
    return currentSolarObject;
  }

  public void hideTab2(ActionEvent actionEvent) {
    FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("hideTab2", Boolean.TRUE);
  }
}
