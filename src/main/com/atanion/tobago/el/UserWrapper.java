/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 20.10.2004 11:38:48.
 * $Id$
 */
package com.atanion.tobago.el;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.context.FacesContext;
import java.security.Principal;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

public class UserWrapper {

  private static final Log LOG = LogFactory.getLog(UserWrapper.class);

  public Principal getPrincipal() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Principal principal = facesContext.getExternalContext().getUserPrincipal();
    LOG.info("getPrincipal(): " + principal);
    return principal;
  }

  public Map getRoles() {
    // todo: optimize me: not a new object every time
    // todo: but keep "thread save"
    return new RolesMap();
  }

  private static class RolesMap implements Map {

    public Object get(Object key) {
      String role = (String) key;
      FacesContext facesContext = FacesContext.getCurrentInstance();
      boolean inRole = facesContext.getExternalContext().isUserInRole(role);
      LOG.info("inRole: " + inRole);
      return new Boolean(inRole);
    }

    public int size() {
      throw new UnsupportedOperationException();
    }

    public void clear() {
      throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
      throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
      throw new UnsupportedOperationException();
    }

    public boolean containsValue(Object value) {
      throw new UnsupportedOperationException();
    }

    public Collection values() {
      throw new UnsupportedOperationException();
    }

    public void putAll(Map t) {
      throw new UnsupportedOperationException();
    }

    public Set entrySet() {
      throw new UnsupportedOperationException();
    }

    public Set keySet() {
      throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
      throw new UnsupportedOperationException();
    }

    public Object put(Object key, Object value) {
      throw new UnsupportedOperationException();
    }
  }

}
