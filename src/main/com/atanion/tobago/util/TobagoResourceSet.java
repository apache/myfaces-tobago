/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 13.02.2004 09:13:08.
 * $Id$
 */
package com.atanion.tobago.util;

import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.util.collections.ListOrderedSet;

import javax.faces.context.FacesContext;

public class TobagoResourceSet extends ListOrderedSet {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public boolean add(String resource, boolean i18n) {
    return super.add(new Resource(resource, i18n));
  }

  public void add(int index, String resource, boolean i18n) {
    super.add(index, new Resource(resource, i18n));
  }

// ///////////////////////////////////////////// bean getter + setter


  public static class Resource {
    private String name;
    private boolean i18n;

    public Resource(String name, boolean i18n) {
      this.name = name;
      this.i18n = i18n;
    }

    public String getName() {
      return name;
    }

    public boolean isI18n() {
      return i18n;
    }

    public String getScript(FacesContext facesContext) {
      if (i18n) {
        return ResourceManagerUtil.getScript(facesContext, name);
      } else {
        return name;
      }
    }

    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Resource)) {
        return false;
      }

      final Resource resource = (Resource) o;

      if (i18n != resource.i18n) {
        return false;
      }
      if (name != null ? !name.equals(resource.name) : resource.name != null) {
        return false;
      }

      return true;
    }

    public int hashCode() {
      int result;
      result = (name != null ? name.hashCode() : 0);
      result = 29 * result + (i18n ? 1 : 0);
      return result;
    }
  }
}
