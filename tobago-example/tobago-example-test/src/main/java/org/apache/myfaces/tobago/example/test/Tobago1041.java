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

import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Tobago1041 {

  public String getHackFacesMessages() {

    final FacesContext facesContext = FacesContext.getCurrentInstance();

    createMessage(facesContext, "page:literal-tc-both");
    createMessage(facesContext, "page:literal-tc-item");
    createMessage(facesContext, "page:literal-tc-label");
    createMessage(facesContext, "page:literal-tc-no");

    createMessage(facesContext, "page:literal-tx-both");
    createMessage(facesContext, "page:literal-tx-item");
    createMessage(facesContext, "page:literal-tx-label");
    createMessage(facesContext, "page:literal-tx-no");

    createMessage(facesContext, "page:expression-tc-both");
    createMessage(facesContext, "page:expression-tc-item");
    createMessage(facesContext, "page:expression-tc-label");
    createMessage(facesContext, "page:expression-tc-no");

    createMessage(facesContext, "page:expression-tx-both");
    createMessage(facesContext, "page:expression-tx-item");
    createMessage(facesContext, "page:expression-tx-label");
    createMessage(facesContext, "page:expression-tx-no");

    return "hack";
  }

  private void createMessage(final FacesContext facesContext, final String id) {
    String label = ((UISelectBooleanCheckbox) facesContext.getViewRoot().findComponent(id)).getLabel();
    if (StringUtils.isBlank(label)) {
      label = "-";
    }
    facesContext.addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR, label, label));
  }
}
