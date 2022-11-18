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

import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UISelectItem;
import jakarta.faces.component.UISelectMany;
import jakarta.faces.context.FacesContext;

import java.lang.invoke.MethodHandles;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.SelectItemTagDeclaration}
 */
public abstract class AbstractUISelectItem extends UISelectItem implements Visual {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private boolean itemValueLiteral;

  @Override
  public void setItemValue(final Object itemValue) {
    if (itemValue instanceof String) {
      itemValueLiteral = true;
    } else if (itemValue == null) {
      // ignore
    } else {
      LOG.warn("Unexpected type of literal for attribute 'itemValue': "
          + "type=" + itemValue.getClass().getName() + " value='" + itemValue + "'.");
    }
    super.setItemValue(itemValue);
  }

  @Override
  public Object getItemValue() {
    if (itemValueLiteral && !(getParent() instanceof UISelectMany)) {
      final Object converted = ComponentUtils.getConvertedValue(
          FacesContext.getCurrentInstance(), getParent(), (String) super.getItemValue());
      return converted;
    } else {
      return super.getItemValue();
    }
  }
}
