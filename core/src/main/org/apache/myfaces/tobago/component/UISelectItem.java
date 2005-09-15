/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.component;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;

import javax.faces.el.ValueBinding;

/**
 * User: weber
 * Date: Apr 11, 2005
 * Time: 11:15:35 AM
 */
public class UISelectItem extends javax.faces.component.UISelectItem {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.SelectItem";


  private String itemImage;

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
