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

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class InMemoryAddressDAO implements AddressDAO {

  private List<Address> addresses;

  public InMemoryAddressDAO() {
    addresses = new ArrayList<Address>();
  }

  public synchronized Address updateAddress(Address address) {
    Address storedAddress = getAddress(address.getId());
    if (storedAddress == null) {
      address.setId(addresses.size());
      addresses.add(address);
    } else {
      storedAddress.fill(address);
    }
    return address;
  }

  public synchronized List<Address> findAddresses() {
    return Collections.unmodifiableList(addresses);
  }

  public synchronized void removeAddress(Address address) {
    Iterator<Address> it = addresses.iterator();
    while (it.hasNext()) {
      if (it.next().getId() == address.getId()) {
        it.remove();
      }
    }
  }

  private Address getAddress(int id) {
    for (int i = 0; i < addresses.size(); i++) {
      Address address = addresses.get(i);
      if (address.getId() == id) {
        return address;
      }
    }
    return null;
  }
}
