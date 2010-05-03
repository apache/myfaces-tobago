package org.apache.myfaces.tobago.util;

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

import org.apache.myfaces.tobago.context.ResourceManagerUtils;

import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

/*
 * Date: 20.04.2006
 * Time: 19:31:21
 */
public class BundleMapWrapper implements Map {

  private String basename;

  public BundleMapWrapper(String basename) {
    this.basename = basename;
  }

  public void clear() {
    throw new UnsupportedOperationException();
  }

  public boolean containsKey(Object key) {
    if (null == key) {
      return false;
    }
    String value = ResourceManagerUtils.getPropertyNotNull(
        FacesContext.getCurrentInstance(), basename, key.toString());
    return value != null;
  }

  public boolean containsValue(Object value) {
    throw new UnsupportedOperationException();
  }

  public Set entrySet() {
    throw new UnsupportedOperationException();
  }

  public Object get(Object key) {
    if (null == key) {
      return null;
    }
    return ResourceManagerUtils.getPropertyNotNull(
        FacesContext.getCurrentInstance(), basename, key.toString());
  }

  public int hashCode() {
    return basename.hashCode();
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BundleMapWrapper that = (BundleMapWrapper) o;
    return basename.equals(that.basename);
  }

  public boolean isEmpty() {
    throw new UnsupportedOperationException();
  }

  public Set keySet() {
    throw new UnsupportedOperationException();
  }

  public Object put(Object k, Object v) {
    throw new UnsupportedOperationException();
  }

  public void putAll(Map t) {
    throw new UnsupportedOperationException();
  }

  public Object remove(Object k) {
    throw new UnsupportedOperationException();
  }

  public int size() {
    throw new UnsupportedOperationException();
  }

  public Collection values() {
    throw new UnsupportedOperationException();
  }
}
