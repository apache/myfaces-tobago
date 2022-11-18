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

import jakarta.el.ValueExpression;
import jakarta.faces.context.FacesContext;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

public class ValueExpressionComparator extends ComparatorBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final FacesContext facesContext;

  private final String var;

  private final ValueExpression valueExpression;

  public ValueExpressionComparator(
      final FacesContext facesContext, final String var,
      final ValueExpression valueExpression, final boolean reverse, final Comparator comparator) {
    super(reverse, comparator);
    this.facesContext = facesContext;
    this.var = var;
    this.valueExpression = valueExpression;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final ValueExpressionComparator that = (ValueExpressionComparator) o;

    if (!super.equals(o)) {
      return false;
    }
    if (!Objects.equals(facesContext, that.facesContext)) {
      return false;
    }
    if (!Objects.equals(valueExpression, that.valueExpression)) {
      return false;
    }
    return Objects.equals(var, that.var);
  }

  public int hashCode() {
    int result;
    result = facesContext != null ? facesContext.hashCode() : 0;
    result = 29 * result + (var != null ? var.hashCode() : 0);
    result = 29 * result + (valueExpression != null ? valueExpression.hashCode() : 0);
    result = 29 * result + super.hashCode();
    return result;
  }

  @Override
  public int compare(final Object param1, final Object param2) {
    final Object obj1;
    final Object obj2;
    try {
      final Map requestMap = facesContext.getExternalContext().getRequestMap();
      requestMap.put(var, param1);
      obj1 = valueExpression.getValue(facesContext.getELContext());
      requestMap.put(var, param2);
      obj2 = valueExpression.getValue(facesContext.getELContext());
      requestMap.remove(var);
    } catch (final Exception e) {
      LOG.error(e.getMessage(), e);
      return 0;
    }
    return super.internalCompare(obj1, obj2);
  }
}
