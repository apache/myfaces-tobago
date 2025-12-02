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

import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.renderkit.css.CustomClass;

import java.io.Serial;

public class SelectItem extends jakarta.faces.model.SelectItem implements Visual {

  @Serial
  private static final long serialVersionUID = 2582455665060354639L;

  private String image;
  private Markup markup = Markup.NULL;
  private CustomClass customClass;

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

  public SelectItem(
      final Object value, final String label, final String tip, final String image, final Markup markup) {
    this(value, label, tip, false, image, markup);
  }

  public SelectItem(
      final Object value, final String label, final String tip, final boolean disabled, final String image) {
    this(value, label, tip, disabled, image, Markup.NULL);
  }

  public SelectItem(
      final Object value, final String label, final String tip, final boolean disabled, final String image,
      final Markup markup) {
    super(value, label, tip, disabled);
    this.image = image;
    this.markup = markup;
  }

  public SelectItem(
      final Object value, final String label, final String tip, final boolean disabled, final boolean escape,
      final boolean noSelectionOption, final String image, final Markup markup) {
    super(value, label, tip, disabled, escape, noSelectionOption);
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
  public void setTip(final String tip) {
    setDescription(tip);
  }

  public String getImage() {
    return image;
  }

  public void setImage(final String image) {
    this.image = image;
  }

  @Override
  public Markup getMarkup() {
    return markup;
  }

  @Override
  public void setMarkup(final Markup markup) {
    this.markup = markup;
  }

  @Override
  public CustomClass getCustomClass() {
    return customClass;
  }

  @Override
  public void setCustomClass(final CustomClass customClass) {
    this.customClass = customClass;
  }

  @Override
  public String getRendererType() {
    throw new UnsupportedOperationException();
  }
}
