package org.apache.myfaces.tobago.example.addressbook;

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

/*
 * Created 29.11.2004 17:36:20.
 * $Id: Controller.java,v 1.2 2005/08/10 11:57:55 lofwyr Exp $
 */

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.model.SheetState;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;

public class Controller {

  private static final Log LOG = LogFactory.getLog(Controller.class);

  private static final String OUTCOME_LIST = "list";
  private static final String OUTCOME_EDITOR = "editor";

  private List<Address> currentAddressList;
  private Address currentAddress;
  private SheetState selectedAddresses;

  private boolean renderPopup;
  private boolean renderFirstName = true;
  private boolean renderLastName = true;
  private boolean renderDayOfBirth = false;

  private AddressDAO addressDAO;

  private FileItem uploadedFile;
  private boolean renderFileUploadPopup;

  public Controller() {
    LOG.debug("Creating new Controller");
  }

  public void setAddressDAO(AddressDAO addressDAO) throws AddressDAOException {
    this.addressDAO = addressDAO;
    LOG.debug("AddressDAO set.");
    ApplicationContext ctx
        = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
    LOG.debug("applicationContext: "+ctx);
    currentAddressList = addressDAO.findAddresses();
    currentAddress = new Address();
  }

  public String createAddress() {
    LOG.debug("action: createAddress");
    currentAddress = new Address();
    return OUTCOME_EDITOR;
  }

  public String editAddress() {
    LOG.debug("action: editAddress");
    List<Integer> selection = selectedAddresses.getSelectedRows();
    if (selection.size() != 1) {
      FacesMessage error = new FacesMessage("Please select exactly one address.");
      FacesContext.getCurrentInstance().addMessage(null, error);
      return null;
    }
    Address selectedAddress = currentAddressList.get(selection.get(0));
    currentAddress.fill(selectedAddress);
    return OUTCOME_EDITOR;
  }

  public String storeAddress() throws AddressDAOException {
    LOG.debug("action: storeAddress");
    currentAddress = addressDAO.updateAddress(currentAddress);
    selectedAddresses.resetSelected();
    currentAddressList = addressDAO.findAddresses();
    return OUTCOME_LIST;
  }

  public String deleteAddresses() throws AddressDAOException {
    LOG.debug("action: deleteAddresses");
    List<Integer> selection = selectedAddresses.getSelectedRows();
    if (selection.size() < 1) {
      FacesMessage error = new FacesMessage("Please select at least one address.");
      FacesContext.getCurrentInstance().addMessage(null, error);
      return null;
    }
    Collections.sort(selection); // why?
    for (int i = selection.size() - 1; i >= 0; i--) {
      Address address = currentAddressList.get(selection.get(i));
      addressDAO.removeAddress(address);
    }
    selectedAddresses.resetSelected();
    currentAddressList = addressDAO.findAddresses();
    return OUTCOME_LIST;
  }

  public List getCurrentAddressList() {
    return currentAddressList;
  }

  public Address getCurrentAddress() {
    return currentAddress;
  }

  public SheetState getSelectedAddresses() {
    return selectedAddresses;
  }

  public void setSelectedAddresses(SheetState selectedAddresses) {
    this.selectedAddresses = selectedAddresses;
  }

  public boolean isRenderFileUploadPopup() {
    return renderFileUploadPopup;
  }

  public void setRenderFileUploadPopup(boolean renderFileUploadPopup) {
    LOG.debug(">>> "+renderFileUploadPopup);
    this.renderFileUploadPopup = renderFileUploadPopup;
  }

  public String cancelFileUploadPopup() {
    setRenderFileUploadPopup(false);
    return OUTCOME_EDITOR;
  }

  public void setRenderPopup(boolean renderPopup) {
    this.renderPopup = renderPopup;
  }

 public boolean isRenderPopup() {
    return renderPopup;
  }

  public String selectColumns() {
    setRenderPopup(true);
    return OUTCOME_LIST;
  }

  public String okFileUpload() {
    setRenderFileUploadPopup(false);
    return null;
  }

  public String cancelFileUpload() {
    setRenderFileUploadPopup(false);
    return null;
  }


  public String cancelPopup() {
    setRenderPopup(false);
    return OUTCOME_LIST;
  }

  public boolean isRenderFirstName() {
    return renderFirstName;
  }

  public void setRenderFirstName(boolean renderFirstName) {
    this.renderFirstName = renderFirstName;
  }

  public boolean isRenderLastName() {
    return renderLastName;
  }

  public void setRenderLastName(boolean renderLastName) {
    this.renderLastName = renderLastName;
  }

  public boolean isRenderDayOfBirth() {
    return renderDayOfBirth;
  }

  public void setRenderDayOfBirth(boolean renderDayOfBirth) {
    this.renderDayOfBirth = renderDayOfBirth;
  }

  public FileItem getUploadedFile() {
    return uploadedFile;
  }

  public void setUploadedFile(FileItem uploadedFile) {
    this.uploadedFile = uploadedFile;
  }

  public String popupFileUpload() {
    LOG.error("AHHHHHHH");
    setRenderFileUploadPopup(true);
    return OUTCOME_EDITOR;
  }

}
