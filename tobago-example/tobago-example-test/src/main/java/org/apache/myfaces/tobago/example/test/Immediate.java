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

package org.apache.myfaces.tobago.example.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class Immediate {

  private String in;
  private int selectOne;
  private Integer[] selectMany;

  private SelectItem[] selectItems = new SelectItem[] {
        new SelectItem(0, "Zero"),
        new SelectItem(1, "One"),
        new SelectItem(2, "Two"),
        new SelectItem(3, "Tree"),
        new SelectItem(4, "Four"),
    };

  private static final Logger LOG = LoggerFactory.getLogger(Immediate.class);

  public String test() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
//    LOG.warn("user: " + facesContext.getExternalContext().getRemoteUser().hashCode());
    LOG.warn("requ: " + facesContext.getExternalContext().getRequest().hashCode());
    LOG.warn("sess: " + facesContext.getExternalContext().getSessionMap().hashCode());
    return null;
  }
  
  public String getIn() {
    return in;
  }

  public void setIn(final String in) {
    this.in = in;
  }

  public int getSelectOne() {
    return selectOne;
  }

  public void setSelectOne(final int selectOne) {
    this.selectOne = selectOne;
  }

  public SelectItem[] getSelectItems() {
    return selectItems;
  }

  public void setSelectMany(final Integer[] selectMany) {
    this.selectMany = selectMany;
  }

  public Integer[] getSelectMany() {
    return selectMany;
  }
}
