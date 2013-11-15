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

package org.apache.myfaces.tobago.context;

import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.layout.Box;
import org.apache.myfaces.tobago.layout.Dimension;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.context.FacesContext;

public class TobagoContext {

  private static final TobagoResourceBundle RESOURCE_BUNDLE = new TobagoResourceBundle();
  private static final TobagoMessageBundle MESSAGE_BUNDLE = new TobagoMessageBundle();

  public Box getActionPosition() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final AbstractUIPage page = ComponentUtils.findPage(facesContext);
    return page.getActionPosition();
  }

  /**
   * Returns the dimension of the page. Might be useful to set the size of a popup.
   * E. g. <code>width="#{tobagoContext.pageDimension.width.pixel - 100}"</code>
   */
  public Dimension getPageDimension() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final AbstractUIPage page = ComponentUtils.findPage(facesContext);
    return new Dimension(page.getWidth(), page.getHeight());
  }

  public TobagoResourceBundle getResourceBundle() {
    return RESOURCE_BUNDLE;
  }

  public TobagoMessageBundle getMessageBundle() {
    return MESSAGE_BUNDLE;
  }
}
