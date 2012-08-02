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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.util.Comparator;
import java.util.Map;

/*
 * Created: Mon Apr 15 15:56:44 2002
 */
@SuppressWarnings("deprecation")
public class ValueBindingComparator extends ComparatorBase {

  private static final Logger LOG = LoggerFactory.getLogger(ValueBindingComparator.class);

  private FacesContext facesContext;

  private String var;

  private ValueBinding valueBinding;

  public ValueBindingComparator(FacesContext facesContext, String var, ValueBinding valueBinding) {
    this.facesContext = facesContext;
    this.var = var;
    this.valueBinding = valueBinding;
  }

  public ValueBindingComparator(FacesContext facesContext, String var, ValueBinding valueBinding, boolean reverse) {
    super(reverse);
    this.facesContext = facesContext;
    this.var = var;
    this.valueBinding = valueBinding;
  }

  public ValueBindingComparator(FacesContext facesContext, String var,
      ValueBinding valueBinding, Comparator comparator) {
    super(comparator);
    this.facesContext = facesContext;
    this.var = var;
    this.valueBinding = valueBinding;
  }

  public ValueBindingComparator(FacesContext facesContext, String var,
      ValueBinding valueBinding, boolean reverse, Comparator comparator) {
    super(reverse, comparator);
    this.facesContext = facesContext;
    this.var = var;
    this.valueBinding = valueBinding;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final ValueBindingComparator that = (ValueBindingComparator) o;

    if (!super.equals(o)) {
      return false;
    }
    if (facesContext != null ? !facesContext.equals(that.facesContext) : that.facesContext != null) {
      return false;
    }
    if (valueBinding != null ? !valueBinding.equals(that.valueBinding) : that.valueBinding != null) {
      return false;
    }
    if (var != null ? !var.equals(that.var) : that.var != null) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    int result;
    result = (facesContext != null ? facesContext.hashCode() : 0);
    result = 29 * result + (var != null ? var.hashCode() : 0);
    result = 29 * result + (valueBinding != null ? valueBinding.hashCode() : 0);
    result = 29 * result + super.hashCode();
    return result;
  }

  // implementation of java.util.Comparator interface

  /**
   * @param param1 <description>
   * @param param2 <description>
   * @return <description>
   */
  public int compare(Object param1, Object param2) {
    Object obj1;
    Object obj2;
    try {
      final Map requestMap = facesContext.getExternalContext().getRequestMap();
      requestMap.put(var, param1);
      obj1 = valueBinding.getValue(facesContext);
      requestMap.put(var, param2);
      obj2 = valueBinding.getValue(facesContext);

    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      return 0;
    }
    return super.internalCompare(obj1, obj2);
  }

}
