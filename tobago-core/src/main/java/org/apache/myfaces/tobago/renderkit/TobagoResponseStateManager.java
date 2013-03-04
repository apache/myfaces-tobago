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

import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.ResponseStateManager;
import java.io.IOException;
import java.util.Map;

public class TobagoResponseStateManager extends ResponseStateManager {
  private static final Logger LOG = LoggerFactory.getLogger(TobagoResponseStateManager.class);

  public static final String TREE_PARAM = "jsf_tree";
  public static final String VIEW_STATE_PARAM = "javax.faces.ViewState";
  private static final String VIEWID_PARAM = "jsf_viewid";

  public Object getState(FacesContext context, String viewId) {
    Object treeStructure = getTreeStructureToRestore(context, viewId);
    Object componentStateToRestore = getComponentStateToRestore(context);
    if (treeStructure != null && componentStateToRestore != null) {
        return new Object[] {treeStructure, componentStateToRestore};
    }
    return null;
  }

  public Object getTreeStructureToRestore(FacesContext facescontext, String viewId) {
    Map requestMap = facescontext.getExternalContext().getRequestParameterMap();
    Object requestViewId = requestMap.get(VIEWID_PARAM);
    if (requestViewId == null || !requestViewId.equals(viewId)) {
      //no saved state or state of different viewId
      return null;
    }

    return requestMap.get(TREE_PARAM);

  }

  public Object getComponentStateToRestore(FacesContext facesContext) {
    Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
    return requestMap.get(VIEW_STATE_PARAM);
  }

  public void writeState(FacesContext facesContext,
      StateManager.SerializedView serializedview) throws IOException {
    ResponseWriter responseWriter = facesContext.getResponseWriter();
    Object treeStruct = serializedview.getStructure();
    Object compStates = serializedview.getState();

    if (treeStruct != null) {
      if (treeStruct instanceof String) {
        responseWriter.startElement(HtmlElements.INPUT, null);
        responseWriter.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, null);
        responseWriter.writeAttribute(HtmlAttributes.NAME, TREE_PARAM, null);
        responseWriter.writeAttribute(HtmlAttributes.ID, TREE_PARAM, null);
        responseWriter.writeAttribute(HtmlAttributes.VALUE, treeStruct, null);
        responseWriter.endElement(HtmlElements.INPUT);
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("No tree structure to be saved in client response!");
      }
    }

    responseWriter.startElement(HtmlElements.INPUT, null);
    responseWriter.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, null);
    responseWriter.writeAttribute(HtmlAttributes.NAME, VIEW_STATE_PARAM, null);
    responseWriter.writeAttribute(HtmlAttributes.ID, VIEW_STATE_PARAM, null);
    if (compStates != null) {
      if (compStates instanceof String) {
        responseWriter.writeAttribute(HtmlAttributes.VALUE, compStates, null);
      } else {
        LOG.error("No component states written in response! Unknown instance of " + compStates.getClass().getName());
      }
    } else {
      responseWriter.writeAttribute(HtmlAttributes.VALUE, "", null);
      if (LOG.isDebugEnabled()) {
        LOG.debug("No component states to be saved in client response!");
      }
    }
    responseWriter.endElement(HtmlElements.INPUT);

    responseWriter.startElement(HtmlElements.INPUT, null);
    responseWriter.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, null);
    responseWriter.writeAttribute(HtmlAttributes.NAME, VIEWID_PARAM, null);
    responseWriter.writeAttribute(HtmlAttributes.ID, VIEWID_PARAM, null);
    responseWriter.writeAttribute(HtmlAttributes.VALUE, facesContext.getViewRoot().getViewId(), null);
    responseWriter.endElement(HtmlElements.INPUT);
  }

}
