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

import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import org.apache.myfaces.tobago.component.SupportFieldId;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.SearchOnce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

public abstract class AbstractUISelectOneList extends AbstractUISelectOneBase implements SupportFieldId {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final transient SearchOnce abstractUISelectItemsFilteredSearch = new SearchOnce();

  @Override
  public String getFieldId(final FacesContext facesContext) {
    return getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "field";
  }

  public abstract String getFilter();

  public abstract boolean isExpanded();

  public abstract boolean isLocalMenu();

  public abstract String getFooter();

  public boolean isReadonlyState() {
    final String filter = getFilter();
    return isReadonly() || getAbstractUISelectItemsFiltered() == null && (filter == null || filter.isEmpty());
  }

  public boolean isDisabledState(FacesContext facesContext) {
    final List<SelectItem> items = getItemList(facesContext, false);
    return isDisabled() || isReadonly() || getAbstractUISelectItemsFiltered() == null && !items.iterator().hasNext();
  }

  public AbstractUISelectItemsFiltered getAbstractUISelectItemsFiltered() {
    return abstractUISelectItemsFilteredSearch.findChild(this, AbstractUISelectItemsFiltered.class);
  }

  /**
   * If “filtered=true”, only the filtered SelectItems (without deferred SelectItems) are returned.
   * If server-side filtering is not used, the “filtered” attribute has no meaning.
   */
  public List<SelectItem> getItemList(FacesContext facesContext, boolean filtered) {
    final AbstractUISelectItemsFiltered abstractUISelectItemsFiltered = getAbstractUISelectItemsFiltered();
    if (abstractUISelectItemsFiltered != null) { //server side filtering

      if (filtered) {
        return abstractUISelectItemsFiltered.getFilteredItemList(facesContext, this);
      } else {
        return abstractUISelectItemsFiltered.getItemList(facesContext, this);
      }

    } else {
      return SelectItemUtils.getItemList(facesContext, this);
    }
  }
}
