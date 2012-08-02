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

package org.apache.myfaces.tobago.example.addressbook;

/*
 * Created 29.11.2004 17:36:20.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class InMemoryAddressDao implements AddressDao {

  private static final Log LOG = LogFactory.getLog(InMemoryAddressDao.class);

  private List<Address> addresses;

  public InMemoryAddressDao() {
    addresses = new ArrayList<Address>();
  }

  public synchronized Address updateAddress(Address address) {
    LOG.debug("Trying address: "+address);
    Address storedAddress = getAddress(address.getId());
    if (storedAddress == null) {
      address.setId(addresses.size()+1);
      LOG.debug("Creating address: "+address);
      addresses.add(address);
    } else {
      LOG.debug("Updating address : "+address);
      LOG.debug("Stored address is: "+storedAddress);
      storedAddress.fill(address);
    }
    return address;
  }

  public List<Address> findAddresses(String filter, String column, boolean order) {
    return findAddresses(filter);
  }

  public synchronized List<Address> findAddresses(String filter) {
    LOG.debug("Find addresses: "+addresses);
    return Collections.unmodifiableList(addresses);
  }

  public synchronized void removeAddress(Address address) {
    Iterator<Address> it = addresses.iterator();
    while (it.hasNext()) {
      if (it.next().getId().equals(address.getId())) {
        it.remove();
      }
    }
  }

  public Address getAddress(Integer id) {
    for (Address address : addresses) {
      if (address.getId().equals(id)) {
        return address;
      }
    }
    return null;
  }
}
