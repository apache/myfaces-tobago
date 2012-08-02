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

package org.apache.myfaces.tobago.renderkit;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.awt.Dimension;

/*
 * Date: Apr 9, 2007
 * Time: 4:53:07 PM
 */
public interface LayoutInformationProvider {
  int getHeaderHeight(FacesContext facesContext, UIComponent component);

  int getPaddingWidth(FacesContext facesContext, UIComponent component);

  int getPaddingHeight(FacesContext facesContext, UIComponent component);

  int getComponentExtraWidth(FacesContext facesContext, UIComponent component);

  int getComponentExtraHeight(FacesContext facesContext, UIComponent component);

  Dimension getMinimumSize(FacesContext facesContext, UIComponent component);

  int getFixedWidth(FacesContext facesContext, UIComponent component);

  int getFixedHeight(FacesContext facesContext, UIComponent component);

  int getFixedSpace(FacesContext facesContext, UIComponent component, boolean width);
}
