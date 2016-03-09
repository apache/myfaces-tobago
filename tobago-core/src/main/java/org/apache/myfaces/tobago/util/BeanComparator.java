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

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.io.Serializable;

public class BeanComparator extends ComparatorBase implements Serializable {

  private static final long serialVersionUID = -7450094725566090886L;

  private static final Logger LOG = LoggerFactory.getLogger(BeanComparator.class);

  private String property;

  public BeanComparator(final String property) {
    this.property = property;
  }

  public BeanComparator(final String property, final boolean reverse) {
    super(reverse);
    this.property = property;
  }

  public BeanComparator(final String property, final Comparator comparator) {
    super(comparator);
    this.property = property;
  }

  public BeanComparator(final String property, final Comparator comparator, final boolean reverse) {
    super(reverse, comparator);
    this.property = property;
  }

  /**
   * @param param1 <description>
   * @return <description>
   */
  public boolean equals(final Object param1) {
    if (this == param1) {
      return true;
    }
    if (param1 instanceof BeanComparator) {
      return ((BeanComparator) param1).getProperty().equals(property)
          && super.equals(param1);
    }
    return false;
  }

  public int hashCode() {
    int result;
    result = (property != null ? property.hashCode() : 0);
    result = 29 * result + super.hashCode();
    return result;
  }

  // implementation of java.util.Comparator interface

  /**
   * @param param1 <description>
   * @param param2 <description>
   * @return <description>
   */
  @Override
  public int compare(final Object param1, final Object param2) {
    final Object obj1;
    final Object obj2;
    try {
      obj1 = PropertyUtils.getProperty(param1, property);
      obj2 = PropertyUtils.getProperty(param2, property);

    } catch (final Exception e) {
      LOG.error(e.getMessage(), e);
      return 0;
    }

    return internalCompare(obj1, obj2);
  }

  public String getProperty() {
    return this.property;
  }
}
