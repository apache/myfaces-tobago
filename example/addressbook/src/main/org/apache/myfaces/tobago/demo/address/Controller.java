/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 29.11.2004 17:36:20.
 * $Id: Controller.java,v 1.2 2005/08/10 11:57:55 lofwyr Exp $
 */
package com.atanion.tobago.demo.address;

import org.apache.myfaces.tobago.model.SheetState;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Controller {

  private static final Log LOG = LogFactory.getLog(Controller.class);

  private List addressList;

  private Address currentAddress;

  private boolean currentStored;

  private SheetState selectedAddresses;

  public Controller() {
    LOG.debug("Creating new Controller");
    addressList = new ArrayList();
  }

  public String createAddress() {
    LOG.debug("action: createAddress");
    currentAddress = new Address();
    currentStored = false;
    return "editor";
  }

  public String editAddress() {
    LOG.debug("action: editAddress");
    int[] selection = selectedAddresses.getSelectedIndices();
    if (selection.length != 1) {
      FacesMessage error = new FacesMessage("Please select exactly one address.");
      FacesContext.getCurrentInstance().addMessage(null, error);
      return null;
    }
    currentAddress = (Address) addressList.get(selection[0]);
    currentStored = true;
    return "editor";
  }

  public String storeAddress() {
    LOG.debug("action: storeAddress");
    if (currentStored) {
      // nothing to do for this backend
    } else {
      addressList.add(currentAddress);
      selectedAddresses.resetSelected();
    }
    return "list";
  }

  public String deleteAddresses() {
    LOG.debug("action: deleteAddresses");
    int[] selection = selectedAddresses.getSelectedIndices();
    if (selection.length < 1) {
      FacesMessage error = new FacesMessage("Please select at least one address.");
      FacesContext.getCurrentInstance().addMessage(null, error);
      return null;
    }
    Arrays.sort(selection);
    for (int i = selection.length - 1; i >= 0; i--) {
      addressList.remove(selection[i]);
    }
    selectedAddresses.resetSelected();
    return "list";
  }

  public List getAddressList() {
    return addressList;
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
}
