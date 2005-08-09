/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * $Id: TobagoDemoController.java 1270 2005-08-08 20:21:38 +0200 (Mo, 08 Aug 2005) lofwyr $
 */
package com.atanion.tobago.demo;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIInput;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.demo.model.banking.BankingForm;
import com.atanion.tobago.demo.model.solar.Solar;
import com.atanion.tobago.demo.model.solar.SolarObject;
import com.atanion.tobago.model.TreeState;
import com.atanion.tobago.taglib.component.ToolBarTag;
import com.atanion.tobago.tool.BuilderModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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

  private String[][] phoneProtocols;

  private SelectItem[] phoneProtocolItems;

  private Integer one;

  private SelectItem[] oneItems;

  private String[] many;

  private SelectItem[] manyItems;

  private Boolean[] bool;

  private Boolean boolTest;

  private String url = "http://www.atanion.com";

  private String[] text;

  private int counter;

  private String lastCommand;

  private boolean viewSource;

  private String[] planet = new String[41];

  private FileItem fileItem;

  private String fileContent;

  private List<FileItem> files;

  private FileItem fileItem2;

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

  private Integer integer;

  private Double aDouble;

  private Solar solar;

  private BankingForm banking;

  private BuilderModel builder;

  private Date date = new Date();

  private Object tabState0;

  private Object tabState1;

  private Object tabState2;

  private Object sheetState;

  private boolean popup;

  private String popupText;

  private String toolbarIconSize;

  private SelectItem[] toolbarIconItems;

  private String toolbarTextPosition;

  private SelectItem[] toolbarTextItems;

  private String objectSrc;

// ///////////////////////////////////////////// constructor

  public TobagoDemoController() {
    planet = new String[41];

    String[] salutationKeys = {
      "salutation_unknown",
      "salutation_mr",
      "salutation_mrs",
      "salutation_family"
    };

    salutationItems = getSelectItems(salutationKeys, "demo");
    salutation = new String[4];

    String[] phoneProtocolKeys = {
      "phoneProtocols_voice",
      "phoneProtocols_fax",
      "phoneProtocols_cell",
      "phoneProtocols_pager",
      "phoneProtocols_video",
      "phoneProtocols_ums",
      "phoneProtocols_msg"
    };

    phoneProtocolItems = getSelectItems(phoneProtocolKeys, "demo");
    phoneProtocols = new String[4][];

    oneItems = new SelectItem[]{
      new SelectItem(new Integer(1), "Merkur"),
      new SelectItem(new Integer(2), "Venus"),
      new SelectItem(new Integer(3), "Erde"),
      new SelectItem(new Integer(4), "Mars"),
    };
    manyItems = oneItems;

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
    viewSource = false;
    solarArray = SolarObject.getArray();
    solarArrayColumns = createSolarArrayColumns();
    solarArrayColumnLayout = "3*;3*;3*";
    aDouble = new Double(1234.56);
    integer = new Integer(1234567);

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
    banking = new BankingForm();
    builder = new BuilderModel();

    popup = false;
    popupText = "empty";


    String[] toolbarIconKeys
        = {ToolBarTag.ICON_OFF, ToolBarTag.ICON_SMALL, ToolBarTag.ICON_BIG};
    toolbarIconItems = getSelectItems(toolbarIconKeys, "demo");
    toolbarIconSize = ToolBarTag.ICON_SMALL;

    String[] toolbarTextKeys =
        {ToolBarTag.LABEL_OFF, ToolBarTag.LABEL_BOTTOM, ToolBarTag.LABEL_RIGHT};
    toolbarTextItems = getSelectItems(toolbarTextKeys, "demo");
    toolbarTextPosition = ToolBarTag.LABEL_BOTTOM;

    fileContent = "NO CONTENT";

    objectSrc = "none";
  }

  private List<UIColumn> createSolarArrayColumns() {

  /* <t:column label="#{bundle.solarArrayPopulation}" sortable="true">
        <t:in value="#{luminary.population}" width="100px" id="t_population" />
      </t:column>
      <t:column label="#{bundle.solarArrayDistance}" cssClass="custom-text-align-right"  sortable="true" align="right" >
        <t:out value="#{luminary.distance}" id="t_distance" />
      </t:column>
      <t:column label="#{bundle.solarArrayPeriod}" cssClass="custom-text-align-right"  sortable="true" align="right" >
        <t:out value="#{luminary.period}" id="t_period" />
      </t:column> */

    List<UIColumn> columns = new ArrayList<UIColumn>(3);

    UIInput textbox = (UIInput)
        ComponentUtil.createComponent(UIInput.COMPONENT_TYPE, TobagoConstants.RENDERER_TYPE_IN);
    ComponentUtil.setStringProperty(
        textbox, TobagoConstants.ATTR_VALUE, "#{luminary.population}", null);
    columns.add(ComponentUtil.createColumn(
        "#{bundle.solarArrayPopulation}", "true", null, textbox));

    columns.add(ComponentUtil.createTextColumn(
        "#{bundle.solarArrayDistance}", "true", "right", "#{luminary.distance}"));

    columns.add(ComponentUtil.createTextColumn(
        "#{bundle.solarArrayPeriod}", "true", "right", "#{luminary.period}"));


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

  public void count() {
    counter++;
  }

  public void updateTree() {

  }

  public static Node createNode(String name, String id) {
    return new Node(name, id);
  }

// /////////////////////////////////////////////// action for links and buttons

  public String clickLink() {
    count();
    lastCommand = methodName();
    return "display";
  }

  public String clickLinkImage() {
    count();
    lastCommand = methodName();
    return "display";
  }

  public String clickLinkInline() {
    count();
    lastCommand = methodName();
    return "display";
  }

  public String clickButton() {
    count();
    lastCommand = methodName();
    return "display";
  }

  public String clickButtonImage() {
    count();
    lastCommand = methodName();
    return "display";
  }

  public String clickButtonInline() {
    count();
    lastCommand = methodName();
    return "display";
  }

  private String methodName() {
    StackTraceElement stackTraceElement = new Exception().getStackTrace()[1];
    return stackTraceElement.getMethodName();
  }

  public void addFile(ActionEvent actionEvent) {
    if (StringUtils.isEmpty(fileItem.getName())) {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      facesContext.addMessage(null,
          new FacesMessage(
              FacesMessage.SEVERITY_WARN, "no file ...", "no file ...")); /* fixme ?bersetzen  */
      LOG.debug("addFile: no file ...");
      return;
    }
    if (files == null) {
      files = new ArrayList<FileItem>();
    }
    files.add(fileItem);
    fileItem = null;
  }

  public void removeFile(ActionEvent actionEvent) {
    UIComponent source = actionEvent.getComponent();
    FileItem file = (FileItem) ComponentUtil.findParameter(source, "file");
    LOG.debug("removing file from list size " + files.size());
    LOG.debug("file " + file);
    files.remove(file);
    LOG.debug("list size " + files.size());
    fileItem = null;
  }



  public String fileItemUpload() {
    if (fileItem != null) {
     fileContent = fileItem.getString();
    } else {
       fileContent = "NO CONTENT";
    }
    return "show";
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

  public Integer getOne() {
    return one;
  }

  public void setOne(Integer one) {
    this.one = one;
  }

  public SelectItem[] getOneItems() {
    return oneItems;
  }

  public void setOneItems(SelectItem[] oneItems) {
    this.oneItems = oneItems;
  }

  public String[] getMany() {
    return many;
  }

  public void setMany(String[] many) {
    this.many = many;
  }

  public SelectItem[] getManyItems() {
    return manyItems;
  }

  public void setManyItems(SelectItem[] manyItems) {
    this.manyItems = manyItems;
  }

  public String[][] getPhoneProtocols() {
    return phoneProtocols;
  }

  public void setPhoneProtocols(String[][] phoneProtocols) {
    this.phoneProtocols = phoneProtocols;
  }

  public SelectItem[] getPhoneProtocolItems() {
    return phoneProtocolItems;
  }

  public void setPhoneProtocolItems(SelectItem[] phoneProtocolItems) {
    this.phoneProtocolItems = phoneProtocolItems;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String v) {
    this.url = v;
  }

  public String[] getText() {
    return text;
  }

  public void setText(String[] text) {
    this.text = text;
  }

  public int getCounter() {
    return counter;
  }

  public String getLastCommand() {
    return lastCommand;
  }

  public void setLastCommand(String lastCommand) {
    this.lastCommand = lastCommand;
  }

  public boolean isViewSource() {
    return viewSource;
  }

  public void setViewSource(boolean b) {
    viewSource = b;
  }

  public String[] getPlanet() {
    return planet;
  }

  public void setPlanet(String[] planet) {
    this.planet = planet;
  }

  public FileItem getFileItem() {
    return fileItem;
  }

  public void setFileItem(FileItem fileItem) {
    this.fileItem = fileItem;
  }

  public String getFileContent() {
    return fileContent;
  }

  public List<FileItem> getFiles() {
    return files;
  }

  public void setFiles(List<FileItem> files) {
    this.files = files;
  }

  public FileItem getFileItem2() {
    return fileItem2;
  }

  public void setFileItem2(FileItem fileItem2) {
    this.fileItem2 = fileItem2;
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

  public Integer getInteger() {
    return integer;
  }

  public void setInteger(Integer integer) {
    this.integer = integer;
  }

  public Double getaDouble() {
    return aDouble;
  }

  public void setaDouble(Double aDouble) {
    this.aDouble = aDouble;
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

  public BankingForm getBanking() {
    return banking;
  }

  public void setBanking(BankingForm banking) {
    this.banking = banking;
  }

  public BuilderModel getBuilder() {
    return builder;
  }

  public void setBuilder(BuilderModel builder) {
    this.builder = builder;
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

  public boolean isPopup() {
    return popup;
  }
  public void showPopup() {
    popup = true;
  }
  public String  hidePopup() {
    popup = false;
    return "utils";
  }

  public String getPopupText() {
    return popupText;
  }

  public void setPopupText(String popupText) {
    this.popupText = popupText;
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

  public String getObjectSrc() {
    return objectSrc;
  }

  public void setObjectSrc(String objectSrc) {
    this.objectSrc = objectSrc;
  }

  public String getObjectContentType() {
    String value = "none";
    if (objectSrc != null) {
      if (objectSrc.endsWith(".pdf")) {
        value = "application/pdf";
      } else if (objectSrc.endsWith("swf")) {
        value = "application/swf";
      } else if (objectSrc.endsWith(".html")) {
        value = "text/html";
      } else if (objectSrc.endsWith(".xml")) {
        value = "text/xml";
      }
    }
    return value;
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
