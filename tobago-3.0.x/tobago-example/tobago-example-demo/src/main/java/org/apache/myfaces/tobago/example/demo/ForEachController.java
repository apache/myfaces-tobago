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

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SessionScoped
@Named
public class ForEachController implements Serializable {

  private List<River> rivers;
  private String name;
  private String length;
  private String discharge;

  public ForEachController() {
    reset();
  }

  public List<River> getRivers() {
    return rivers;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLength() {
    return length;
  }

  public void setLength(String length) {
    this.length = length;
  }

  public String getDischarge() {
    return discharge;
  }

  public void setDischarge(String discharge) {
    this.discharge = discharge;
  }

  public String addNewRiver() {
    rivers.add(new River(name, Integer.valueOf(length), Integer.valueOf(discharge)));
    resetInputFields();
    return null;
  }

  public String reset() {
    rivers = new ArrayList<River>(Arrays.asList(
            new River("Nile", 6853, 2830),
            new River("Amazon", 6437, 209000),
            new River("Yangtze", 6300, 30166)));
    resetInputFields();
    return null;
  }

  private void resetInputFields() {
    name = null;
    length = null;
    discharge = null;
  }

  public class River {
    private String name;
    private int length;
    private int discharge;

    public River(String name, int length, int discharge) {
      this.name = name;
      this.length = length;
      this.discharge = discharge;
    }

    public String getName() {
      return name;
    }

    public int getLength() {
      return length;
    }

    public int getDischarge() {
      return discharge;
    }
  }
}
