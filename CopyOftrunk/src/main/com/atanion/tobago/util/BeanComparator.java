/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * $Id: BeanComparator.java,v 1.6 2004/05/19 14:57:13 lofwyr Exp $
 */
package com.atanion.tobago.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;


/**
 * BeanComparator.java
 *
 *
 * Created: Mon Apr 15 15:56:44 2002
 *
 * @author <a href="mailto: Volker.Weber@atanion.com "> Volker Weber</a>
 */



public class BeanComparator implements Comparator  {

  private static final Log LOG = LogFactory.getLog(BeanComparator.class);

  private String property;

  private Comparator comparator;

  private boolean reverse;

  public BeanComparator (String property){
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
      return ((BeanComparator)param1).getProperty().equals(property)
          && ((BeanComparator)param1).getComparator().equals(comparator);
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

    if (! obj1.getClass().isInstance(obj2)) {
      throw new ClassCastException(obj1.getClass().getName() + " != " +
                                   obj2.getClass().getName());
    }

    int result;


    if (comparator instanceof Collator) {
      CollationKey collationKey1
          = ((Collator)comparator).getCollationKey(obj1.toString());
      CollationKey collationKey2
          = ((Collator)comparator).getCollationKey(obj2.toString());
      result = collationKey1.compareTo(collationKey2);

    } else if (comparator != null) {
      result = comparator.compare(obj1, obj2);
    } else {
      if (obj1 instanceof Comparable) {
        result = ((Comparable)obj1).compareTo(obj2);
      }
      else {
        result = obj1.toString().compareTo(obj2.toString());
      }
    }
    return reverse ? -result : result;
  }

  // ----------------------------------------------------------- getter + setter

  public String getProperty() {
    return this.property;
  }

  public Comparator getComparator() {
    return comparator;
  }
}
