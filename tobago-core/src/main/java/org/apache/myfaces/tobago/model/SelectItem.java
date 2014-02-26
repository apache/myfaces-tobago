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
import org.apache.myfaces.tobago.context.Markup;

public class SelectItem extends javax.faces.model.SelectItem implements SupportsMarkup {

  private static final long serialVersionUID = 2582455665060354639L;

  private String image;
  private Markup markup = Markup.NULL;
  private Markup currentMarkup = null;

  public SelectItem() {
    super();
  }

  public SelectItem(final Object value) {
    super(value);
  }

  public SelectItem(final Object value, final String label) {
    super(value, label);
  }

  public SelectItem(final Object value, final String label, final String tip) {
    super(value, label, tip);
  }

  public SelectItem(final Object value, final String label, final String tip, final String image) {
    this(value, label, tip, false, image);
  }

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
  public SelectItem(
      final Object value, final String label, final String tip, final String image, final String[] markup) {
    this(value, label, tip, false, image, markup);
  }

  public SelectItem(
      final Object value, final String label, final String tip, final String image, final Markup markup) {
    this(value, label, tip, false, image, markup);
  }

  public SelectItem(
      final Object value, final String label, final String tip, final boolean disabled, final String image) {
    this(value, label, tip, disabled, image, Markup.NULL);
  }

  /**
   * @deprecated since 1.5.0
   */
  public SelectItem(
      final Object value, final String label, final String tip, final boolean disabled, final String image,
      final String[] markup) {
    this(value, label, tip, disabled, image, Markup.valueOf(markup));
  }

  public SelectItem(
      final Object value, final String label, final String tip, final boolean disabled, final String image,
      final Markup markup) {
    super(value, label, tip, disabled);
    this.image = image;
    this.markup = markup;
  }

  /**
   * Alias name for description.
   */
  public String getTip() {
    return getDescription();
  }

  /**
   * Alias name for description.
   */
  public void setTip(String tip) {
    setDescription(tip);
  }

  public String getImage() {
    return image;
  }

  public void setImage(final String image) {
    this.image = image;
  }

  public Markup getMarkup() {
    return markup;
  }

  public void setMarkup(final Markup markup) {
    this.markup = markup;
  }

  public Markup getCurrentMarkup() {
    return currentMarkup;
  }

  public void setCurrentMarkup(final Markup currentMarkup) {
    this.currentMarkup = currentMarkup;
  }
}
