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

@RequestScoped
@Named
public class SelectOneListboxController implements Serializable {

  private String river;
  private SelectItem[] mountains;
  private String mountain;

  public SelectOneListboxController() {
    mountains = new SelectItem[]{
            new SelectItem("8848 m", "Everest"),
            new SelectItem("8611 m", "K2"),
            new SelectItem("8586 m", "Kangchenjunga"),
            new SelectItem("8516 m", "Lhotse"),
            new SelectItem("8481 m", "Makalu")
    };
  }

  public String getRiver() {
    return river;
  }

  public void setRiver(String river) {
    this.river = river;
  }

  public SelectItem[] getMountains() {
    return mountains;
  }

  public String getMountain() {
    return mountain;
  }

  public void setMountain(String mountain) {
    this.mountain = mountain;
  }
}
