package org.apache.myfaces.tobago.util;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;
import java.util.Map;

/**
 * Created: Mon Apr 15 15:56:44 2002
 * @author Volker Weber
 */

public class ValueBindingComparator implements Comparator  {

  private static final Log LOG = LogFactory.getLog(ValueBindingComparator.class);

  private FacesContext facesContext;

  private String var;

  private ValueBinding valueBinding;

  private Comparator comparator;

  private boolean reverse;

  public ValueBindingComparator(FacesContext facesContext, String var, ValueBinding valueBinding) {
    this.facesContext = facesContext;
    this.var = var;
    this.valueBinding = valueBinding;
  }

  public ValueBindingComparator(FacesContext facesContext, String var, ValueBinding valueBinding, boolean reverse) {
    this.facesContext = facesContext;
    this.var = var;
    this.valueBinding = valueBinding;
    this.reverse = reverse;
  }

  public ValueBindingComparator(FacesContext facesContext, String var,
      ValueBinding valueBinding, Comparator comparator) {
    this.facesContext = facesContext;
    this.var = var;
    this.valueBinding = valueBinding;
    this.comparator = comparator;
  }

  public ValueBindingComparator(FacesContext facesContext, String var,
      ValueBinding valueBinding, boolean reverse, Comparator comparator) {
    this.facesContext = facesContext;
    this.var = var;
    this.valueBinding = valueBinding;
    this.reverse = reverse;
    this.comparator = comparator;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final ValueBindingComparator that = (ValueBindingComparator) o;

    if (reverse != that.reverse) {
      return false;
    }
    if (comparator != null ? !comparator.equals(that.comparator) : that.comparator != null) {
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
    result = 29 * result + (comparator != null ? comparator.hashCode() : 0);
    result = 29 * result + (reverse ? 1 : 0);
    return result;
  }

  // implementation of java.util.Comparator interface

  /**
   *
   * @param param1 <description>
   * @param param2 <description>
   * @return <description>
   */
  public int compare(Object param1, Object param2){
    Object obj1;
    Object obj2;
    try {
      final Map requestMap = facesContext.getExternalContext().getRequestMap();
      requestMap.put(var, param1);
      obj1 = valueBinding.getValue(facesContext);
      requestMap.put(var, param2);
      obj2 = valueBinding.getValue(facesContext);

    } catch (Exception e) {
      ValueBindingComparator.LOG.error(e.getMessage(), e);
      return 0;
    }

    if (obj1 == null || obj2 == null) {
      if (obj1 == null && obj2 == null) {
        return 0;
      }
      if (obj1 == null) {
        return reverse ? 1 : -1;
      } else {
        return reverse ? -1 : 1;
      }
    }

    if (!obj1.getClass().isInstance(obj2)) {
      throw new ClassCastException(obj1.getClass().getName() + " != "
          + obj2.getClass().getName());
    }

    int result;


    if (comparator instanceof Collator) {
      CollationKey collationKey1
          = ((Collator) comparator).getCollationKey(obj1.toString());
      CollationKey collationKey2
          = ((Collator) comparator).getCollationKey(obj2.toString());
      result = collationKey1.compareTo(collationKey2);

    } else if (comparator != null) {
      result = comparator.compare(obj1, obj2);
    } else {
      if (obj1 instanceof Comparable) {
        result = ((Comparable) obj1).compareTo(obj2);
      } else {
        result = obj1.toString().compareTo(obj2.toString());
      }
    }
    return reverse ? -result : result;
  }

}
