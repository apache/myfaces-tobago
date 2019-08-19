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

package org.apache.myfaces.tobago.internal.component;

public abstract class AbstractUISelectOneChoice extends AbstractUISelectOneBase {

  enum Select2Keys {
    allowClear,
    allowCustom,
    hideDropdown,
    isSelect2,
    language,
    matcher,
    maximumInputLength,
    minimumInputLength,
    maximumSelectionLength,
    minimumResultsForSearch,
    tokenizer,
    tokenSeparators
  }





  public boolean isAllowClear() {
    Boolean allowClear = (Boolean) getStateHelper().eval(Select2Keys.allowClear);
    if (allowClear != null) {
      return allowClear;
    }
    return false;
  }
  public boolean isAllowClearSet() {
    return getStateHelper().eval(Select2Keys.allowClear) != null;
  }

  public void setAllowClear(boolean allowClear) {
    getStateHelper().put(Select2Keys.allowClear, allowClear);
  }


  public boolean isAllowCustom() {
    Boolean allowCustom = (Boolean) getStateHelper().eval(Select2Keys.allowCustom);
    if (allowCustom != null) {
      return allowCustom;
    }
    return false;
  }
  public boolean isAllowCustomSet() {
    return getStateHelper().eval(Select2Keys.allowCustom) != null;
  }

  public void setAllowCustom(boolean allowCustom) {
    getStateHelper().put(Select2Keys.allowCustom, allowCustom);
  }

  public String getMatcher() {
    String matcher = (String) getStateHelper().eval(Select2Keys.matcher);
    if (matcher != null) {
      return matcher;
    }
    return null;
  }

  public boolean isMatcherSet() {
    return getStateHelper().eval(Select2Keys.matcher) != null;
  }

  public void setMatcher(String matcher) {
    getStateHelper().put(Select2Keys.matcher, matcher);
  }

  public int getMaximumInputLength() {
    Integer maximumInputLength = (Integer) getStateHelper().eval(Select2Keys.maximumInputLength);
    if (maximumInputLength != null) {
      return maximumInputLength;
    }
    return 0;
  }
  public boolean isMaximumInputLengthSet() {
    return getStateHelper().eval(Select2Keys.maximumInputLength) != null;
  }

  public void setMaximumInputLength(int minimumInputLength) {
    getStateHelper().put(Select2Keys.maximumInputLength, minimumInputLength);
  }

  public int getMinimumInputLength() {
    Integer minimumInputLength = (Integer) getStateHelper().eval(Select2Keys.minimumInputLength);
    if (minimumInputLength != null) {
      return minimumInputLength;
    }
    return 0;
  }
  public boolean isMinimumInputLengthSet() {
    return getStateHelper().eval(Select2Keys.minimumInputLength) != null;
  }

  public void setMinimumInputLength(int minimumInputLength) {
    getStateHelper().put(Select2Keys.minimumInputLength, minimumInputLength);
  }

  public int getMaximumSelectionLength() {
    Integer maximumSelectionLength = (Integer) getStateHelper().eval(Select2Keys.maximumSelectionLength);
    if (maximumSelectionLength != null) {
      return maximumSelectionLength;
    }
    return 0;
  }
  public boolean isMaximumSelectionLengthSet() {
    return getStateHelper().eval(Select2Keys.maximumSelectionLength) != null;
  }

  public void setMaximumSelectionLength(int maximumSelectionLength) {
    getStateHelper().put(Select2Keys.maximumSelectionLength, maximumSelectionLength);
  }

  public void setMinimumResultsForSearch(int minimumResultsForSearch) {
    getStateHelper().put(Select2Keys.minimumResultsForSearch, minimumResultsForSearch);
  }

  public int getMinimumResultsForSearch() {
    Integer minimumResultsForSearch = (Integer) getStateHelper().eval(Select2Keys.minimumResultsForSearch);
    if (minimumResultsForSearch != null) {
      return minimumResultsForSearch;
    }
    return 20;
  }

  public boolean isMinimumResultsForSearchSet() {
    return getStateHelper().eval(Select2Keys.minimumResultsForSearch) != null;
  }

}
