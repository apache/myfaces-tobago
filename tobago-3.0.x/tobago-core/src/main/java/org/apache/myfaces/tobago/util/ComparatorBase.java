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

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;


public abstract class ComparatorBase implements Comparator {

  private Comparator comparator;

  private boolean reverse;

  protected ComparatorBase() {
  }

  protected ComparatorBase(final boolean reverse, final Comparator comparator) {
    this.comparator = comparator;
    this.reverse = reverse;
  }

  protected ComparatorBase(final boolean reverse) {
    this.reverse = reverse;
  }

  protected ComparatorBase(final Comparator comparator) {
    this.comparator = comparator;
  }

  protected int internalCompare(final Object obj1, final Object obj2) {

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

    final int result;


    if (comparator instanceof Collator) {
      final CollationKey collationKey1
          = ((Collator) comparator).getCollationKey(obj1.toString());
      final CollationKey collationKey2
          = ((Collator) comparator).getCollationKey(obj2.toString());
      result = collationKey1.compareTo(collationKey2);

    } else if (comparator != null) {
      result = comparator.compare(obj1, obj2);
    } else {
      if (obj1 instanceof String) {
        result = ((String) obj1).compareToIgnoreCase((String) obj2);
      } else if (obj1 instanceof Comparable) {
        result = ((Comparable) obj1).compareTo(obj2);
      } else {
        result = obj1.toString().compareTo(obj2.toString());
      }
    }
    return reverse ? -result : result;
  }

  /*

  // TODO use this??
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final ComparatorBase that = (ComparatorBase) o;

    return !(comparator != null ? !comparator.equals(that.comparator) : that.comparator != null);

  }

  public int hashCode() {
    return (comparator != null ? comparator.hashCode() : 0);
  } */

  public boolean equals(final Object o) {
    if (o == null) {
      return false;
    }
    return ((ComparatorBase) o).getComparator().equals(comparator);
  }

  public int hashCode() {
    return (comparator != null ? comparator.hashCode() : 0);
  }

  public Comparator getComparator() {
    return comparator;
  }
}
