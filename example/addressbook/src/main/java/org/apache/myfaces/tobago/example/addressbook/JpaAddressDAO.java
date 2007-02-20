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

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;


@Repository
@Transactional()
public class JpaAddressDAO extends JpaDaoSupport implements AddressDao {

  private static final Log LOG = LogFactory.getLog(JpaAddressDAO.class);

  public Address updateAddress(Address address) throws AddressDaoException {
    if (address.getId() == null) {
      getJpaTemplate().persist(address);
    } else {
      Picture picture = address.getPicture();
      if (picture != null && picture.getId() == null) {
        getJpaTemplate().persist(picture);
      }
      getJpaTemplate().merge(address);
    }
    return address;
  }

  public List<Address> findAddresses() throws AddressDaoException {
    return getJpaTemplate().find("select a from Address a");
  }

  public  void removeAddress(Address address) throws AddressDaoException {
    address = getAddress(address.getId());
    getJpaTemplate().remove(address);
  }


  public Address getAddress(Integer id) {
    return getJpaTemplate().find(Address.class, id);
  }
}
