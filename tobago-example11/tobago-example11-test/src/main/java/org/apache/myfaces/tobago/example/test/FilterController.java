package org.apache.myfaces.tobago.example.test;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.model.Wizard;

public class FilterController {

  private static final Logger LOG = LoggerFactory.getLogger(FilterController.class);

  private Wizard wizard;
  
  private String filterType;
  private String filterValue;

  public FilterController() {
    wizard = new Wizard();
  }

  public Wizard getWizard() {
    return wizard;
  }
  
  public String createFilter() {
    LOG.info("Filter type: '" + filterType + "'");
    if ("fileInto".equals(filterType)) {
      return "fileIntoCondition";
    } else if ("forward".equals(filterType)) {
      return "forwardCondition";
    }
    throw new RuntimeException("No filter type set.");
  }

  public String getFilterType() {
    return filterType;
  }

  public void setFilterType(String filterType) {
    this.filterType = filterType;
  }

  public String getFilterValue() {
    return filterValue;
  }

  public void setFilterValue(String filterValue) {
    this.filterValue = filterValue;
  }
}
