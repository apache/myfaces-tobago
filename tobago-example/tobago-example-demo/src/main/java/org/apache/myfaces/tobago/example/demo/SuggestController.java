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

package org.apache.myfaces.tobago.example.demo;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.example.data.LocaleList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIInput;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named
public class SuggestController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(SuggestController.class);
  private String query;
  private String selection1;
  private String selection2;

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public List<String> getLanguages() {
    final String substring = query != null ? query : "";
    LOG.info("Creating items for substring: '" + substring + "'");
    final List<String> result = new ArrayList<String>(LocaleList.COUNTRY_LANGUAGE.size());
    for (final String name : LocaleList.COUNTRY_LANGUAGE) {
      if (StringUtils.containsIgnoreCase(name, substring)) {
        result.add(name);
      }
    }
    return result;
  }

  public List<String> getAllLanguages() {
    return LocaleList.COUNTRY_LANGUAGE;
  }

  /*
   * use <tc:selectItems/> instead
   */
  @Deprecated
  public List<String> getInputSuggestItems(final UIInput component) {
    String substring = (String) component.getSubmittedValue();
    if (substring == null) {
      substring = "";
    }
    LOG.info("Creating items for substring: '" + substring + "'");
    final List<String> result = new ArrayList<String>();
    for (final String name : LocaleList.COUNTRY_LANGUAGE) {
      if (StringUtils.containsIgnoreCase(name, substring)) {
        result.add(name);
      }
      if (result.size() > 100) { // this value should not be smaller than the value of the suggest control
        break;
      }
    }
    return result;
  }

  public String getSelection1() {
    return selection1;
  }

  public void setSelection1(String selection1) {
    LOG.info("setSelection1 ->" + selection1);
    this.selection1 = selection1;
  }

  public String getSelection2() {
    return selection2;
  }

  public void setSelection2(String selection2) {
    this.selection2 = selection2;
  }
}
