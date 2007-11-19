package org.apache.myfaces.tobago.component;

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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;

import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/*
 * User: weber
 * Date: Apr 11, 2005
 * Time: 11:15:35 AM
 */
public class UISelectItem extends javax.faces.component.UISelectItem implements SupportsMarkup {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.SelectItem";

  private String itemImage;
  private String[] markup;

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    itemImage = (String) values[1];
    markup = (String[]) values[2];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[3];
    values[0] = super.saveState(context);
    values[1] = itemImage;
    values[2] = markup;
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
