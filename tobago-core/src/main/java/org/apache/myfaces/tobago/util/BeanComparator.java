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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

/**
 * Created: Mon Apr 15 15:56:44 2002
 * @author Volker Weber
 */

public class BeanComparator implements Comparator  {

  private static final Log LOG = LogFactory.getLog(BeanComparator.class);

  private String property;

  private Comparator comparator;

  private boolean reverse;

  public BeanComparator(String property) {
    this.property = property;
  }

  public BeanComparator(String property, boolean reverse) {
    this.property = property;
    this.reverse = reverse;
  }

  public BeanComparator(String property, Comparator comparator) {
    this.property = property;
    this.comparator = comparator;
  }

  public BeanComparator(String property, Comparator comparator, boolean reverse) {
    this.property = property;
    this.comparator = comparator;
    this.reverse = reverse;
  }

  /**
   *
   * @param param1 <description>
   * @return <description>
   */
  public boolean equals(Object param1) {
    if (param1 instanceof BeanComparator) {
      return ((BeanComparator) param1).getProperty().equals(property)
          && ((BeanComparator) param1).getComparator().equals(comparator);
    }
    return false;
  }

  public int hashCode() {
    int result;
    result = (property != null ? property.hashCode() : 0);
    result = 29 * result + (comparator != null ? comparator.hashCode() : 0);
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
      obj1 = PropertyUtils.getProperty(param1, property);
      obj2 = PropertyUtils.getProperty(param2, property);

    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
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

  public String getProperty() {
    return this.property;
  }

  public Comparator getComparator() {
    return comparator;
  }
}
