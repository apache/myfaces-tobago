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

package org.apache.myfaces.tobago.renderkit.html;


import org.apache.myfaces.tobago.component.UISelectManyBox;
import org.apache.myfaces.tobago.component.UISelectOneChoice;

import javax.faces.context.FacesContext;

public class Select2Options {

  private Boolean allowClear;
  private Boolean dropdownAutoWidth;
  private Boolean hideDropdown;
  private String language;
  private String matcher;
  private Integer maximumInputLength;
  private Integer minimumInputLength;
  private Integer maximumSelectionLength;
  private Integer minimumResultsForSearch;
  private boolean minimumResultsForSearchSet;
  private String placeholder;
  private boolean renderSelect2;
  private Boolean tags;
  private String tokenizer;
  private String[] tokenSeparators;

  public static Select2Options of(UISelectOneChoice select) {
    Select2Options options = new Select2Options();
    options.renderSelect2 = select.isSelect2();
    options.dropdownAutoWidth = true;

    if (select.isMinimumResultsForSearchSet()) {
      options.setMinimumResultsForSearch(select.getMinimumResultsForSearch());
    }

    if (select.isAllowCustomSet()) {
      options.setTags(select.isAllowCustom());
    }

    if (select.isAllowClearSet()) {
      options.setAllowClear(select.isAllowClear());
    }

    FacesContext facesContext = FacesContext.getCurrentInstance();
    if (facesContext != null) {
      options.setLanguage(facesContext.getViewRoot().getLocale().getLanguage());
    }

    if (select.isMatcherSet()) {
      options.setMatcher(select.getMatcher());
    }

    if (select.isMaximumInputLengthSet()) {
      options.setMaximumInputLength(select.getMaximumInputLength());
    }

    if (select.isMinimumInputLengthSet()) {
      options.setMinimumInputLength(select.getMinimumInputLength());
    }

    if (select.isMaximumSelectionLengthSet()) {
      options.setMaximumSelectionLength(select.getMaximumSelectionLength());
    }

    String placeholder = select.getPlaceholder();
    if (placeholder != null && placeholder.length() > 0) {
      options.setPlaceholder(placeholder);
    }

    return options;
  }

  public static Select2Options of(UISelectManyBox select) {
    Select2Options options = new Select2Options();

    if (select.isMinimumResultsForSearchSet()) {
      options.setMinimumResultsForSearch(select.getMinimumResultsForSearch());
    }

    if (select.isAllowCustomSet()) {
      options.setTags(select.isAllowCustom());
    }

    if (select.isAllowClearSet()) {
      options.setAllowClear(select.isAllowClear());
    }

    if (select.isHideDropdown()) {
      options.setHideDropdown(true);
    }

    FacesContext facesContext = FacesContext.getCurrentInstance();
    if (facesContext != null) {
      options.setLanguage(facesContext.getViewRoot().getLocale().getLanguage());
    }

    if (select.isMatcherSet()) {
      options.setMatcher(select.getMatcher());
    }

    if (select.isMaximumInputLengthSet()) {
      options.setMaximumInputLength(select.getMaximumInputLength());
    }

    if (select.isMinimumInputLengthSet()) {
      options.setMinimumInputLength(select.getMinimumInputLength());
    }

    if (select.isMaximumSelectionLengthSet()) {
      options.setMaximumSelectionLength(select.getMaximumSelectionLength());
    }

    if (select.isTokenizerSet()) {
      options.setTokenizer(select.getTokenizer());
    }

    if (select.isTokenSeparatorsSet()) {
      options.setTokenSeparators(select.getTokenSeparators());
    }

    String placeholder = select.getPlaceholder();
    if (placeholder != null && placeholder.length() > 0) {
      options.setPlaceholder(placeholder);
    }

    return options;
  }

  public boolean hasAnyOption() {
    return renderSelect2
        || allowClear != null
        || matcher != null
        || maximumInputLength != null
        || minimumInputLength != null
        || maximumSelectionLength != null
        || minimumResultsForSearchSet
        || placeholder != null
        || tags != null
        || tokenizer != null
        || tokenSeparators != null;
  }

  public Boolean isTags() {
    return tags;
  }

  public void setTags(Boolean tags) {
    this.tags = tags;
  }

  public String getTokenizer() {
    return tokenizer;
  }

  public void setTokenizer(String tokenizer) {
    this.tokenizer = tokenizer;
  }

  public String[] getTokenSeparators() {
    return tokenSeparators;
  }

  public void setTokenSeparators(String[] tokenSeparators) {
    this.tokenSeparators = tokenSeparators;
  }

  public Boolean isAllowClear() {
    return allowClear;
  }

  public void setAllowClear(Boolean allowClear) {
    this.allowClear = allowClear;
  }

  public Boolean getHideDropdown() {
    return hideDropdown;
  }

  public void setHideDropdown(Boolean hideDropdown) {
    this.hideDropdown = hideDropdown;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getMatcher() {
    return matcher;
  }

  public void setMatcher(String matcher) {
    this.matcher = matcher;
  }

  public Integer getMaximumInputLength() {
    return maximumInputLength;
  }

  public void setMaximumInputLength(Integer maximumInputLength) {
    this.maximumInputLength = maximumInputLength;
  }

  public Integer getMinimumInputLength() {
    return minimumInputLength;
  }

  public void setMinimumInputLength(Integer minimumInputLength) {
    this.minimumInputLength = minimumInputLength;
  }

  public Integer getMaximumSelectionLength() {
    return maximumSelectionLength;
  }

  public void setMaximumSelectionLength(Integer maximumSelectionLength) {
    this.maximumSelectionLength = maximumSelectionLength;
  }

  public Integer getMinimumResultsForSearch() {
    return minimumResultsForSearch;
  }

  public void setMinimumResultsForSearch(Integer minimumResultsForSearch) {
    this.minimumResultsForSearch = minimumResultsForSearch;
    minimumResultsForSearchSet = true;
  }

  public String getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
  }

  public String toJson() {
    final StringBuilder builder = new StringBuilder();
    builder.append("{");

    if (tags != null) {
      JsonUtils.encode(builder, "tags", tags);
    }
    if (tokenizer != null) {
      JsonUtils.encode(builder, "tokenizer", tokenizer);
    }
    if (tokenSeparators != null) {
      JsonUtils.encode(builder, "tokenSeparators", tokenSeparators);
    }
    if (dropdownAutoWidth != null) {
      JsonUtils.encode(builder, "dropdownAutoWidth", dropdownAutoWidth);
    }
    if (allowClear != null) {
      JsonUtils.encode(builder, "allowClear", allowClear);
    }

    if (hideDropdown != null && hideDropdown) {
      JsonUtils.encode(builder, "dropdownCssClass", "tobago-select2-hide-dropdown");
    }

    if (language != null) {
      JsonUtils.encode(builder, "language", language);
    }
    if (matcher != null) {
      JsonUtils.encode(builder, "matcher", matcher);
    }
    if (maximumInputLength != null) {
      JsonUtils.encode(builder, "maximumInputLength", maximumInputLength);
    }
    if (minimumInputLength != null) {
      JsonUtils.encode(builder, "minimumInputLength", minimumInputLength);
    }
    if (maximumSelectionLength != null) {
      JsonUtils.encode(builder, "maximumSelectionLength", maximumSelectionLength);
    }
    if (minimumResultsForSearch != null) {
      JsonUtils.encode(builder, "minimumResultsForSearch", minimumResultsForSearch);
    }
    if (placeholder != null) {
      JsonUtils.encode(builder, "placeholder", placeholder);
    }



    if (builder.length() > 0 && builder.charAt(builder.length() - 1) == ',') {
      builder.deleteCharAt(builder.length() - 1);
    }
    builder.append("}");
    return builder.toString();
  }

}
