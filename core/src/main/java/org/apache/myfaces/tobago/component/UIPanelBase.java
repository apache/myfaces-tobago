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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.ajax.api.AjaxComponent;
import org.apache.myfaces.tobago.ajax.api.AjaxPhaseListener;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;

import javax.faces.context.FacesContext;
import java.io.IOException;

/*
 * User: weber
 * Date: Feb 28, 2005
 * Time: 3:05:19 PM
 */
public class UIPanelBase extends javax.faces.component.UIPanel
    implements AjaxComponent {

  public void encodeBegin(FacesContext facesContext) throws IOException {
    // TODO change this should be renamed to DimensionUtils.prepare!!!
    UILayout.getLayout(this).layoutBegin(facesContext, this);
    super.encodeBegin(facesContext);
  }

  public void encodeChildren(FacesContext facesContext) throws IOException {
    if (isRendered()) {
      UILayout.getLayout(this).encodeChildrenOfComponent(facesContext, this);
    }
  }

  public void encodeAjax(FacesContext facesContext) throws IOException {
    AjaxUtils.encodeAjaxComponent(facesContext, this);
  }

  public void processAjax(FacesContext facesContext) throws IOException {
    final String ajaxId = (String) facesContext.getExternalContext()
        .getRequestParameterMap().get(AjaxPhaseListener.AJAX_COMPONENT_ID);

    if (ajaxId.equals(getClientId(facesContext))) {
      AjaxUtils.processActiveAjaxComponent(facesContext, this);
    } else {
      AjaxUtils.processAjaxOnChildren(facesContext, this);
    }
  }
}
