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

package org.apache.myfaces.tobago.model;

import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UISelectItem;

/*
 * Date: Apr 5, 2005
 * Time: 6:11:16 PM
 */
public class SelectItem extends javax.faces.model.SelectItem implements SupportsMarkup {

  private static final long serialVersionUID = 2582455665060354639L;

  private String image;
  private String[] markup = new String[0];

  public SelectItem() {
    super();
  }

  public SelectItem(UISelectItem component) {
    this(component.getItemValue() == null ? "" : component.getItemValue(),
        component.getItemLabel(), component.getItemDescription(),
        component.isItemDisabled(), component.getItemImage(), component.getMarkup());
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
    this(value, label, description, false, image);
  }

  public SelectItem(Object value, String label, String description, String image, String[] markup) {
    this(value, label, description, false, image, markup);
  }

  public SelectItem(Object value, String label, String description,
      boolean disabled, String image) {
    this(value, label, description, disabled, image, null);
  }

  public SelectItem(Object value, String label, String description,
      boolean disabled, String image, String[] markup) {
    super(value, label, description, disabled);
    this.image = image;
    this.markup = markup;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String[] getMarkup() {
    return markup;
  }

  public void setMarkup(String[] markup) {
    this.markup = markup;
  }
}
