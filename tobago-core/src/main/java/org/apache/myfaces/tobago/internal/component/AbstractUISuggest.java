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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.InputSuggest2;
import org.apache.myfaces.tobago.model.SuggestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractUISuggest
    extends UIComponentBase implements InputSuggest2, ClientBehaviorHolder {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUISuggest.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Suggest";
  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.Suggest";

  // todo generate
  private static final Collection<String> EVENT_NAMES = Arrays.asList("suggest");

  private transient String query;

  // todo generate
  @Override
  public String getDefaultEventName() {
    return "suggest";
  }

  // todo generate
  @Override
  public Collection<String> getEventNames() {
    return EVENT_NAMES;
  }

  @Override
  public String getFamily() {
    return COMPONENT_FAMILY;
  }

  public abstract Integer getDelay();

  public abstract void setDelay(Integer delay);

  public abstract Integer getMinimumCharacters();

  public abstract void setMinimumCharacters(Integer minimumCharacters);

  public abstract void setFilter(SuggestFilter filter);

  public String getQuery() {
    final ValueExpression expression = this.getValueExpression("query");
    if (expression != null) {
      try {
        return (String) expression.getValue(FacesContext.getCurrentInstance().getELContext());
      } catch (Exception e) {
        LOG.error("", e);
        return null;
      }
    } else {
      return query;
    }
  }

  public void setQuery(String query) {
    final ValueExpression expression = this.getValueExpression("query");
    if (expression != null) {
      try {
        expression.setValue(FacesContext.getCurrentInstance().getELContext(), query);
      } catch (Exception e) {
        LOG.error("query='" + query + "'", e);
      }
    } else {
      this.query = query;
    }
  }

  public abstract boolean isUpdate();

  public abstract Integer getTotalCount();

  public abstract Integer getMaximumItems();
}
