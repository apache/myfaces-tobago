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

package org.apache.myfaces.tobago.el;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class UserWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(UserWrapper.class);

  private Map roles;

  public UserWrapper() {
    roles = new RolesMap();
  }

  public Principal getPrincipal() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Principal principal = facesContext.getExternalContext().getUserPrincipal();
    if (LOG.isDebugEnabled()) {
      LOG.debug("getPrincipal(): {}", principal);
    }
    return principal;
  }

  public Map getRoles() {
    return roles;
  }

  private static class RolesMap implements Map {

    @Override
    public Object get(final Object key) {
      final String role = (String) key;
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final boolean inRole = facesContext.getExternalContext().isUserInRole(role);
      if (LOG.isDebugEnabled()) {
        LOG.debug("is in role '{}': {}", key, inRole);
      }
      return Boolean.valueOf(inRole);
    }

    @Override
    public int size() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(final Object key) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(final Object value) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Collection values() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(final Map t) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Set entrySet() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Set keySet() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(final Object key) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Object put(final Object key, final Object value) {
      throw new UnsupportedOperationException();
    }
  }
}
