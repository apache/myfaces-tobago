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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIInput;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SessionScoped
@Named
public class SuggestController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private List<String> solarObjects;
  private String query;
  private String selection1;
  private String selection2;

  @Inject
  private AstroData astroData;

  @PostConstruct
  private void init() {
    solarObjects = astroData.findAll().map(SolarObject::getName).collect(Collectors.toList());
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(final String query) {
    this.query = query;
  }

  public List<String> getSolarObjects() {
    final String substring = query != null ? query : "";
    final List<String> filtered =
        solarObjects.stream().filter(s -> StringUtils.containsIgnoreCase(s, substring)).collect(Collectors.toList());
    LOG.info("Found {} items for substring: '{}'", filtered.size(), substring);
    return filtered;
  }

  public List<String> getAllSolarObjects() {
    LOG.info("Found all {} items", solarObjects.size());
    return solarObjects;
  }

  /**
   * @deprecated use &lt;tc:selectItems/&gt; instead
   */
  @Deprecated
  public List<String> getInputSuggestItems(final UIInput component) {
    String substring = (String) component.getSubmittedValue();
    if (substring == null) {
      substring = "";
    }
    LOG.info("Creating items for substring: '" + substring + "'");
    final List<String> result = new ArrayList<>();
    for (final String name : solarObjects) {
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

  public void setSelection1(final String selection1) {
    LOG.info("setSelection1 ->" + selection1);
    this.selection1 = selection1;
  }

  public String getSelection2() {
    return selection2;
  }

  public void setSelection2(final String selection2) {
    this.selection2 = selection2;
  }
}
