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

package org.apache.myfaces.tobago.util;

import org.apache.myfaces.tobago.context.ResourceManagerUtils;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class BundleMapWrapper implements Map, Serializable {

  private String basename;

  public BundleMapWrapper(final String basename) {
    this.basename = basename;
  }

  public void clear() {
    throw new UnsupportedOperationException();
  }

  public boolean containsKey(final Object key) {
    if (null == key) {
      return false;
    }
    final String value = ResourceManagerUtils.getPropertyNotNull(
        FacesContext.getCurrentInstance(), basename, key.toString());
    return value != null;
  }

  public boolean containsValue(final Object value) {
    throw new UnsupportedOperationException();
  }

  public Set entrySet() {
    throw new UnsupportedOperationException();
  }

  public Object get(final Object key) {
    if (null == key) {
      return null;
    }
    return ResourceManagerUtils.getPropertyNotNull(
        FacesContext.getCurrentInstance(), basename, key.toString());
  }

  public int hashCode() {
    return basename.hashCode();
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final BundleMapWrapper that = (BundleMapWrapper) o;
    return basename.equals(that.basename);
  }

  public boolean isEmpty() {
    throw new UnsupportedOperationException();
  }

  public Set keySet() {
    throw new UnsupportedOperationException();
  }

  public Object put(final Object k, final Object v) {
    throw new UnsupportedOperationException();
  }

  public void putAll(final Map t) {
    throw new UnsupportedOperationException();
  }

  public Object remove(final Object k) {
    throw new UnsupportedOperationException();
  }

  public int size() {
    throw new UnsupportedOperationException();
  }

  public Collection values() {
    throw new UnsupportedOperationException();
  }
}
