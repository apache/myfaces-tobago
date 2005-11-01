/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
 * Created 29.11.2004 17:36:20.
 * $Id: Controller.java,v 1.2 2005/08/10 11:57:55 lofwyr Exp $
 */
package org.apache.myfaces.tobago.example.addressbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.model.SheetState;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {

  private static final Log LOG = LogFactory.getLog(Controller.class);

  private List<Address> addressList;

  private Address currentAddress;

  private boolean currentStored;

  private SheetState selectedAddresses;

  public Controller() {
    LOG.debug("Creating new Controller");
    addressList = new ArrayList<Address>();
  }

  public String createAddress() {
    LOG.debug("action: createAddress");
    currentAddress = new Address();
    currentStored = false;
    return "editor";
  }

  public String editAddress() {
    LOG.debug("action: editAddress");
    List<Integer> selection = selectedAddresses.getSelectedRows();
    if (selection.size() != 1) {
      FacesMessage error = new FacesMessage("Please select exactly one address.");
      FacesContext.getCurrentInstance().addMessage(null, error);
      return null;
    }
    currentAddress = addressList.get(selection.get(0));
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
    List<Integer> selection = selectedAddresses.getSelectedRows();
    if (selection.size() < 1) {
      FacesMessage error = new FacesMessage("Please select at least one address.");
      FacesContext.getCurrentInstance().addMessage(null, error);
      return null;
    }
    Collections.sort(selection);
    for (int i = selection.size() - 1; i >= 0; i--) {
      addressList.remove((int)selection.get(i));
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
