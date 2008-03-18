package org.apache.myfaces.tobago.component;

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

import org.apache.myfaces.tobago.ajax.api.AjaxComponent;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.compat.InvokeOnComponent;

import javax.faces.context.FacesContext;
import javax.faces.component.ContextCallback;
import javax.faces.FacesException;
import java.io.IOException;

/*
 * User: weber
 * Date: Feb 28, 2005
 * Time: 3:05:19 PM
 */
public class UIPanelBase extends javax.faces.component.UIPanel
    implements AjaxComponent, InvokeOnComponent {

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

  public int encodeAjax(FacesContext facesContext) throws IOException {
    return AjaxUtils.encodeAjaxComponent(facesContext, this);
  }

  public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback)
     throws FacesException {
    return FacesUtils.invokeOnComponent(context, this, clientId, callback);
  }
}
