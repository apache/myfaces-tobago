package org.apache.myfaces.tobago.internal.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;

public class AbstractUISelectItem extends UISelectItem implements OnComponentPopulated {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUISelectItem.class);

  private boolean itemValueLiteral;

  public void onComponentPopulated(FacesContext facesContext, UIComponent parent) {
    if (itemValueLiteral) {
      Object converted = ComponentUtils.getConvertedValue(
          FacesContext.getCurrentInstance(), (javax.faces.component.UIInput) parent, (String)getItemValue());
      super.setItemValue(converted);
    }
  }

  @Override
  public void setItemValue(Object itemValue) {
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
}
