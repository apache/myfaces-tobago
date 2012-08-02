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

package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;

public class UISelectItem extends javax.faces.component.UISelectItem implements SupportsMarkup {

  private static final Log LOG = LogFactory.getLog(UISelectItem.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.SelectItem";

  private String itemImage;
  private String[] markup;

  private boolean itemValueLiteral;

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

  @Override
  public Object getItemValue() {
    Object itemValue = super.getItemValue();
    if (itemValueLiteral) {
      // this is to make it possible to use values directly in the page.
      itemValue = ComponentUtil.getConvertedValue(
          FacesContext.getCurrentInstance(), (javax.faces.component.UIInput) getParent(), (String) itemValue);
    }
    return itemValue;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    itemImage = (String) values[1];
    markup = (String[]) values[2];
    itemValueLiteral = (Boolean) values[3];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[4];
    values[0] = super.saveState(context);
    values[1] = itemImage;
    values[2] = markup;
    values[3] = itemValueLiteral;
    return values;
  }

  public String[] getMarkup() {
    if (markup != null) {
      return markup;
    }
    return ComponentUtil.getMarkupBinding(getFacesContext(), this);
  }

  public void setMarkup(String[] markup) {
    this.markup = markup;
  }

  public void setImage(String image) {
    setItemImage(image);
  }

  public String getItemImage() {
    if (itemImage != null) {
      return itemImage;
    }

    ValueBinding vb = getValueBinding(ATTR_IMAGE);
    if (vb != null) {
      return ((String) vb.getValue(getFacesContext()));
    } else {
      return null;
    }
  }

  public void setItemImage(String itemImage) {
    this.itemImage = itemImage;
  }

}
