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
public class SelectController implements Serializable {

  private SelectItem[] entries;

  public SelectController() {
    entries = new SelectItem[]{
            new SelectItem("1", "Entry One"),
            new SelectItem("2", "Entry Two"),
            new SelectItem("3", "Entry Three"),
            new SelectItem("4", "Entry Four"),
            new SelectItem("5", "Entry Five"),
            new SelectItem("6", "Entry Six"),
            new SelectItem("7", "Entry Seven"),
            new SelectItem("8", "Entry Eight"),
            new SelectItem("9", "Entry Nine")
    };
  }

  public SelectItem[] getEntries() {
    return entries;
  }
}
