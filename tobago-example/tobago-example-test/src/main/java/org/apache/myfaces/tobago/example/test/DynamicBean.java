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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

public class DynamicBean {

  private List<DynamicPanel> panels = new ArrayList<DynamicPanel>();

  public String addPanel() {

    switch (panels.size()) {
      case 0:
        panels.add(new DynamicPanel1());
        break;
      case 1:
        panels.add(new DynamicPanel2());
        break;
      case 2:
        panels.add(new DynamicPanel3());
        break;
      default:
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "All panels where added!", null));
    }
    return "/test/forEach/dynamic-include.xhtml";
//    return null;
  }

  public String reset() {
    panels.clear();
    return "/test/forEach/dynamic-include.xhtml";
//    return null;
  }

  public List<DynamicPanel> getPanels() {
    return panels;
  }

}
