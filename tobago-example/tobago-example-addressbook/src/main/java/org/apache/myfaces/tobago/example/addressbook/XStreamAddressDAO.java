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

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamAddressDAO implements AddressDAO {

  private static final Log LOG = LogFactory.getLog(XStreamAddressDAO.class);

  private String storageFileName = "addresses.xml";
  private XStream xstream;

  public XStreamAddressDAO() {
    xstream = new XStream(new DomDriver());
  }

  public String getStorageFileName() {
    return storageFileName;
  }

  public void setStorageFileName(String storageFileName) {
    this.storageFileName = storageFileName;
  }

  public synchronized Address updateAddress(Address address) throws AddressDAOException {
    List<Address> addresses = loadAddresses();
    Address storedAddress = getAddress(address.getId(), addresses);
    if (storedAddress == null) {
      address.setId(addresses.size()+1);
      addresses.add(address);
    } else {
      storedAddress.fill(address);
    }
    saveAddresses(addresses);
    return address;
  }

  public synchronized List<Address> findAddresses() throws AddressDAOException{
    return loadAddresses();
  }

  public synchronized void removeAddress(Address address) throws AddressDAOException {
    List<Address> addresses = loadAddresses();
    Iterator<Address> it = addresses.iterator();
    while (it.hasNext()) {
      if (it.next().getId() == address.getId()) {
        it.remove();
      }
    }
    saveAddresses(addresses);
  }

  private List<Address> loadAddresses() throws AddressDAOException {
    FileReader fr = null;
    try {
      if (!new File(storageFileName).exists()) {
        return new ArrayList<Address>();
      }
      fr = new FileReader(storageFileName);
      return (List<Address>)xstream.fromXML(fr);
    } catch(Exception e) {
      throw new AddressDAOException("error loading addresses", e);
    } finally{
      if (fr != null) {
        try {
          fr.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
  }

  private void saveAddresses(List<Address> addresses) throws AddressDAOException {
    FileWriter fw = null;
    try {
      fw = new FileWriter(storageFileName, false);
      xstream.toXML(addresses, fw);
    } catch(Exception e) {
      throw new AddressDAOException("error saving addresses", e);
    } finally{
      if (fw != null) {
        try {
          fw.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
  }

  private Address getAddress(int id, List<Address> addresses) {
    for (Address address : addresses) {
      if (address.getId() == id) {
        return address;
      }
    }
    return null;
  }
}
