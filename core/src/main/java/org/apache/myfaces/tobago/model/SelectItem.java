package org.apache.myfaces.tobago.model;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.component.UISelectItem;

/**
 * User: weber
 * Date: Apr 5, 2005
 * Time: 6:11:16 PM
 */
public class SelectItem extends javax.faces.model.SelectItem {

  private String image;

  public SelectItem() {
    super();
  }

  public SelectItem(UISelectItem component) {
    super(component.getItemValue() == null ? "" : component.getItemValue(),
        component.getItemLabel(), component.getItemDescription(),
        component.isItemDisabled());
    image = component.getItemImage();
  }

  public SelectItem(Object value) {
    super(value);
  }

  public SelectItem(Object value, String label) {
    super(value, label);
  }

  public SelectItem(Object value, String label, String description) {
    super(value, label, description);
  }

  public SelectItem(Object value, String label, String description, String image) {
    super(value, label, description);
    this.image = image;
  }

  public SelectItem(Object value, String label, String description,
                    boolean disabled, String image) {
    super(value, label, description, disabled);
    this.image = image;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
