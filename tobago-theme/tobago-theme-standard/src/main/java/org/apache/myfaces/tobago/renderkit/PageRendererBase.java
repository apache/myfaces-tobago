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

import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.Box;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.model.PageState;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.StringTokenizer;

public class PageRendererBase extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(PageRendererBase.class);

  public void decode(final FacesContext facesContext, final UIComponent component) {
    if (component instanceof AbstractUIPage) {
      final AbstractUIPage page = (AbstractUIPage) component;

      decodeActionPosition(facesContext, page);
      decodePageState(facesContext, page);
    }
  }

  private void decodeActionPosition(final FacesContext facesContext, final AbstractUIPage page) {
    final String actionIdName = page.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "form-action";
    final String newActionId = facesContext.getExternalContext().getRequestParameterMap().get(actionIdName);
    if (LOG.isDebugEnabled()) {
      LOG.debug("action = " + newActionId);
    }
    page.setActionId(newActionId);
    FacesContextUtils.setActionId(facesContext, newActionId);
    try {
      final String actionPositionName
          = page.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "action-position";
      final String actionPositionString
          = facesContext.getExternalContext().getRequestParameterMap().get(actionPositionName);
      if (LOG.isDebugEnabled()) {
        LOG.debug("actionPosition='" + actionPositionString + "'");
      }
      if (StringUtils.isNotEmpty(actionPositionString)) {
        final Box actionPosition = new Box(actionPositionString);
        page.setActionPosition(actionPosition);
      } else {
        page.setActionPosition(null);
      }
    } catch (final Exception e) {
      LOG.warn("Can't analyse parameter for action-position", e);
    }
  }

  @SuppressWarnings("unchecked")
  private void decodePageState(final FacesContext facesContext, final AbstractUIPage page) {
    final String name;
    String value = null;
    try {
      name = page.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "form-clientDimension";
      value = facesContext.getExternalContext().getRequestParameterMap().get(name);
      if (StringUtils.isNotBlank(value)) {
        final StringTokenizer tokenizer = new StringTokenizer(value, ";");
        final Measure width = Measure.valueOf(tokenizer.nextToken());
        final Measure height = Measure.valueOf(tokenizer.nextToken());
        // XXX remove me later
        final PageState pageState = page.getPageState(facesContext);
        if (pageState != null) {
          pageState.setClientWidth(width.getPixel());
          pageState.setClientHeight(height.getPixel());
        }
        final ClientProperties clientProperties = ClientProperties.getInstance(facesContext);
        clientProperties.setPageWidth(width);
        clientProperties.setPageHeight(height);
      }
    } catch (final Exception e) {
      LOG.error("Error in decoding state: value='" + value + "'", e);
    }
  }
}
