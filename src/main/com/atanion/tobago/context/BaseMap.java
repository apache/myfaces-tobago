/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 09.01.2004 11:30:59.
 * $Id$
 */
package com.atanion.tobago.context;

import java.util.AbstractMap;
import java.util.Map;

abstract class BaseMap extends AbstractMap {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// bean getter + setter


// ///////////////////////////////////////////// entry class

  protected static class Entry implements Map.Entry {

    private final Object key;
    private final Object value;

    public Entry(Object key, Object value) {
      this.key = key;
      this.value = value;
    }

    public int hashCode() {
      return (key == null ? 0 : key.hashCode())
          ^ (value == null ? 0 : value.hashCode());
    }

    public Object getValue() {
      return value;
    }

    public Object setValue(Object value) {
      throw new UnsupportedOperationException();
    }

    public Object getKey() {
      return key;
    }

    public boolean equals(Object object) {
      if (object == null || !(object instanceof Map.Entry)) {
        return false;
      }
      Map.Entry entry = (Map.Entry) object;
      Object entryKey = entry.getKey();
      Object entryVal = entry.getValue();
      return (entryKey == key || entryKey != null && entryKey.equals(key))
          && (entryVal == value || entryVal != null && entryVal.equals(value));
    }
  }

}
