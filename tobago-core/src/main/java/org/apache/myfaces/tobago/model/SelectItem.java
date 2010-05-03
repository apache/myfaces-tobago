package org.apache.myfaces.tobago.model;

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

import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.context.Markup;

public class SelectItem extends javax.faces.model.SelectItem implements SupportsMarkup {

  private static final long serialVersionUID = 2582455665060354639L;

  private String image;
  private Markup markup = Markup.NULL;

  public SelectItem() {
    super();
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

  /** @deprecated since 1.5.0 */
  @Deprecated
  public SelectItem(Object value, String label, String description, String image, String[] markup) {
    this(value, label, description, false, image, markup);
  }

  public SelectItem(Object value, String label, String description, String image, Markup markup) {
    this(value, label, description, false, image, markup);
  }

  public SelectItem(Object value, String label, String description, boolean disabled, String image) {
    this(value, label, description, disabled, image, Markup.NULL);
  }

  /** @deprecated since 1.5.0 */
  public SelectItem(Object value, String label, String description, boolean disabled, String image, String[] markup) {
    this(value, label, description, disabled, image, Markup.valueOf(markup));
  }

  public SelectItem(Object value, String label, String description, boolean disabled, String image, Markup markup) {
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

  public Markup getMarkup() {
    return markup;
  }

  public void setMarkup(Markup markup) {
    this.markup = markup;
  }
}
