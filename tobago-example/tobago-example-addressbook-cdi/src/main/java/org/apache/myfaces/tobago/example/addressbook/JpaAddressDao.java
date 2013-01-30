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
//import org.springframework.transaction.annotation.Transactional;

import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@DataSourceDefinition(
    name = "addressBookDataSource",
    url = "jdbc:derby:target/addressDB;create=true",
    className = "org.apache.derby.jdbc.EmbeddedDriver"
)
//@Transactional
@ApplicationScoped
public class JpaAddressDao implements AddressDao, Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(JpaAddressDao.class);

  @PersistenceContext(unitName = "addressBook")
  private EntityManager entityManager;

  public Address updateAddress(Address address) {
    if (address.getId() == null) {
      entityManager.persist(address);
    } else {
      Picture picture = address.getPicture();
      if (picture != null && picture.getId() == null) {
        entityManager.persist(picture);
      }
      entityManager.merge(address);
    }
    return address;
  }
//  @Transactional(readOnly = true)
  public List<Address> findAddresses(String filter) {
    return findAddresses(filter, null, true);
  }

//  @Transactional(readOnly = true)
  @SuppressWarnings("unchecked")
  public List<Address> findAddresses(String filter, String column, boolean order) {
    StringBuilder builder = new StringBuilder();
    builder.append("select a from Address a");
    if (filter != null) {
      if (filter.indexOf('_') == -1 && filter.indexOf('%') == -1) {
        filter = "%" + filter + "%";
      }
      builder.append(" where a.firstName like '");
      builder.append(filter);
      builder.append("' or a.lastName like '");
      builder.append(filter);
      builder.append("'");
    }
    if (column != null) {
      builder.append(" order by a.");
      builder.append(column);
      builder.append(order ? " desc" : " asc");
    }
    Query query = entityManager.createQuery(builder.toString());
    return query.getResultList();
  }

  public void removeAddress(Address address) {
    address = getAddress(address.getId());
    entityManager.remove(address);
  }
//  @Transactional(readOnly = true)
  public Address getAddress(Integer id) {
    return entityManager.find(Address.class, id);
  }
}
