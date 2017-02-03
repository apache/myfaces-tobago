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

import org.apache.myfaces.tobago.model.SelectItem;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Named
public class SelectManyListboxController implements Serializable {

  private List<String> celestials = new ArrayList<String>();
  private SelectItem[] deserts;
  private List<String> selectedDeserts = new ArrayList<String>();

  public SelectManyListboxController() {
    deserts = new SelectItem[]{
            new SelectItem("Antarctic Desert"),
            new SelectItem("Arctic"),
            new SelectItem("Sahara"),
            new SelectItem("Arabian Desert"),
            new SelectItem("Gobi Desert")
    };
  }

  public List<String> getCelestials() {
    return celestials;
  }

  public void setCelestials(List<String> celestials) {
    this.celestials = celestials;
  }

  public String getCelestial() {
    String retValue = "";
    for (String s : celestials) {
      retValue = retValue.concat(s);
    }
    return retValue;
  }

  public SelectItem[] getDeserts() {
    return deserts;
  }

  public List<String> getSelectedDeserts() {
    return selectedDeserts;
  }

  public void setSelectedDeserts(List<String> selectedDeserts) {
    this.selectedDeserts = selectedDeserts;
  }
}
