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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Alternative
@ApplicationScoped
public class InMemoryAddressDao implements AddressDao, Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(InMemoryAddressDao.class);

  private List<Address> addresses;

  public InMemoryAddressDao() {
    addresses = new ArrayList<Address>();
  }

  @Override
  public synchronized Address updateAddress(final Address address) {
    LOG.debug("Trying address: "+address);
    final Address storedAddress = getAddress(address.getId());
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

  @Override
  public List<Address> findAddresses(final String filter, final String column, final boolean order) {
    return findAddresses(filter);
  }

  @Override
  public synchronized List<Address> findAddresses(final String filter) {
    LOG.debug("Find addresses: "+addresses);
    return Collections.unmodifiableList(addresses);
  }

  @Override
  public synchronized void removeAddress(final Address address) {
    final Iterator<Address> it = addresses.iterator();
    while (it.hasNext()) {
      if (it.next().getId().equals(address.getId())) {
        it.remove();
      }
    }
  }

  @Override
  public Address getAddress(final Integer id) {
    for (final Address address : addresses) {
      if (address.getId().equals(id)) {
        return address;
      }
    }
    return null;
  }
}
