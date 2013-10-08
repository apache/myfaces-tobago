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

package org.apache.myfaces.tobago.portlet;

import org.apache.myfaces.tobago.webapp.Secret;

import javax.faces.context.FacesContext;
import javax.portlet.ActionRequest;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import java.io.UnsupportedEncodingException;


/**
 * Static utility class for portlet-related operations.
 */
public final class PortletUtils {

  private static final boolean PORTLET_API_AVAILABLE = portletApiAvailable();

  /**
   * This flag is imbedded in the request.
   * It signifies that the request is coming from a portlet.
   */
//  public static final String PORTLET_REQUEST = PortletUtils.class.getName() + ".PORTLET_REQUEST";
  private static final String VIEW_ID = PortletUtils.class.getName() + ".VIEW_ID";

  private static boolean portletApiAvailable() {
    try {
      return PortletRequest.class != null; // never false
    } catch (NoClassDefFoundError e) {
      return false;
    }
  }

  private PortletUtils() {
    // avoid instantiation
  }

  /**
   * Determine if we are processing a portlet RenderResponse.
   *
   * @param facesContext The current FacesContext.
   * @return <code>true</code> if we are processing a RenderResponse,
   *         <code>false</code> otherwise.
   */
  public static boolean isRenderResponse(FacesContext facesContext) {
    return PORTLET_API_AVAILABLE && facesContext.getExternalContext().getResponse() instanceof RenderResponse;
  }

  /**
   * Determine if we are running as a portlet.
   *
   * @param facesContext The current FacesContext.
   * @return <code>true</code> if we are running as a portlet,
   *         <code>false</code> otherwise.
   */
//  public static boolean isPortletRequest(FacesContext facesContext) {
//    return facesContext.getExternalContext().getSessionMap().get(PORTLET_REQUEST) != null;
//  }
  public static boolean isPortletRequest(FacesContext facesContext) {
    return PORTLET_API_AVAILABLE && facesContext.getExternalContext().getContext() instanceof PortletContext;
  }

  public static String getViewId(FacesContext facesContext) {
    PortletRequest request = (PortletRequest) facesContext.getExternalContext().getRequest();
    return request.getParameter(PortletUtils.VIEW_ID);
  }

  /**
   * @return The action url.
   */
  public static String setViewIdForUrl(FacesContext facesContext, String viewId) {
    RenderResponse response = (RenderResponse) facesContext.getExternalContext().getResponse();
    PortletURL url = response.createActionURL();
    url.setParameter(VIEW_ID, viewId);
    return url.toString();
  }

  public static void ensureEncoding(FacesContext facesContext) throws UnsupportedEncodingException {
    ActionRequest request = (ActionRequest) facesContext.getExternalContext().getRequest();
    if (request.getCharacterEncoding() == null) {
      request.setCharacterEncoding("UTF-8");
    }
  }

  public static Secret getAttributeFromSessionForApplication(Object session, String name) {

    if (PORTLET_API_AVAILABLE && session instanceof PortletSession) {
      return (Secret) ((PortletSession) session).getAttribute(name, PortletSession.APPLICATION_SCOPE);
    } else {
      throw new IllegalArgumentException("Unknown session type: " + session.getClass().getName());
    }
  }
}
