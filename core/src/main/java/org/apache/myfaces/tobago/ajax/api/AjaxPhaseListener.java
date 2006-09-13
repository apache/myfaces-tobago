package org.apache.myfaces.tobago.ajax.api;

/*
 * Copyright 2004-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.util.RequestUtils;

import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 *
 *  !! adapted copy of sandbox org.apache.myfaces.custom.ajax.api.AjaxPhaseListener !!
 *
 * @author Martin Marinschek
 * @version $Revision: $ $Date: $
 *          <p/>
 *          $Log: $
 */
public class AjaxPhaseListener implements PhaseListener {
  private static final Log LOG = LogFactory.getLog(AjaxPhaseListener.class);
  public static final String AJAX_COMPONENT_ID = "affectedAjaxComponent";

  public static final String CODE_SUCCESS = "<status code=\"200\"/>";
  public static final String CODE_NOT_MODIFIED = "<status code=\"304\"/>";
  public static final String CODE_RELOAD_REQUIRED = "<status code=\"309\"/>";

  public static Object getValueForComponent(
      FacesContext facesContext, UIComponent component) {
    String possibleClientId = component.getClientId(facesContext);

    final Map requestParameterMap
        = facesContext.getExternalContext().getRequestParameterMap();
    if (requestParameterMap.containsKey(possibleClientId)) {
      return requestParameterMap.get(possibleClientId);
    } else {
      possibleClientId = (String) requestParameterMap.get(AJAX_COMPONENT_ID);

      UIViewRoot root = facesContext.getViewRoot();

      UIComponent ajaxComponent = root.findComponent(possibleClientId);

      if (ajaxComponent == component) {
        return requestParameterMap.get(possibleClientId);
      } else {
        LOG.error("No value found for this component : " + possibleClientId);
        return null;
      }
    }
  }


  public void afterPhase(PhaseEvent event) {

    if (event.getPhaseId().getOrdinal() != PhaseId.APPLY_REQUEST_VALUES.getOrdinal()) {
      return;
    }

    FacesContext facesContext = event.getFacesContext();

    final ExternalContext externalContext = facesContext.getExternalContext();
    if (externalContext.getRequestParameterMap().containsKey(AJAX_COMPONENT_ID)) {
      try {
        if (LOG.isDebugEnabled()) {
          LOG.debug("AJAX: componentID found :"
              + externalContext.getRequestParameterMap().get(AJAX_COMPONENT_ID));
        }

        RequestUtils.ensureEncoding(externalContext);
        RequestUtils.ensureNoCacheHeader(externalContext);
        final UIViewRoot viewRoot = facesContext.getViewRoot();
        StringWriter content = new StringWriter();
        RenderKitFactory renderFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderFactory.getRenderKit(
            facesContext, viewRoot.getRenderKitId());
        ResponseWriter contentWriter
            = renderKit.createResponseWriter(content, null , null);
        facesContext.setResponseWriter(contentWriter);

        AjaxUtils.processAjax(facesContext, viewRoot);

        writeAjaxResponse(facesContext, content.toString());

        final StateManager stateManager
            = facesContext.getApplication().getStateManager();
        if (!stateManager.isSavingStateInClient(facesContext)) {
          stateManager.saveSerializedView(facesContext);
        }
        facesContext.responseComplete();

      } catch (IOException e) {
        LOG.error("Exception while processing Ajax", e);
      }
    }
  }

  private void writeAjaxResponse(FacesContext facesContext, String content)
      throws IOException {

    ExternalContext externalContext = facesContext.getExternalContext();
    StringBuffer buf = new StringBuffer(content);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Size of AjaxResponse:\n" + buf.length()
          + " = 0x" + Integer.toHexString(buf.length()));
    }

    buf.insert(0, CODE_SUCCESS);

    buf.insert(0, Integer.toHexString(buf.length()) + "\r\n");
    buf.append("\r\n" + 0 + "\r\n\r\n");

    //TODO: fix this to work in PortletRequest as well
    if (externalContext.getResponse() instanceof HttpServletResponse) {
      final HttpServletResponse httpServletResponse
          = (HttpServletResponse) externalContext.getResponse();
      httpServletResponse.addHeader("Transfer-Encoding", "chunked");
      PrintWriter responseWriter = httpServletResponse.getWriter();
      // buf.delete(buf.indexOf("<"), buf.indexOf(">")+1);
      responseWriter.print(buf.toString());
      //System.out.println("PhaseListener: buf = " + buf.toString());
      responseWriter.flush();
      responseWriter.close();
    }
  }

  public void beforePhase(PhaseEvent event) {

    if (event.getPhaseId().getOrdinal() != PhaseId.RENDER_RESPONSE.getOrdinal()) {
      return;
    }

    try {
      FacesContext facesContext = event.getFacesContext();
      final ExternalContext externalContext = facesContext.getExternalContext();
      final Map requestParameterMap = externalContext.getRequestParameterMap();
      if (requestParameterMap.containsKey(AJAX_COMPONENT_ID)) {
        LOG.error("Ignoring AjaxRequest without valid component tree!");

        final String message = ResourceManagerUtil.getPropertyNotNull(
            facesContext, "tobago", "tobago.ajax.response.error");

        writeAjaxResponse(facesContext, message);

        facesContext.responseComplete();
      }

    } catch (IOException e) {
      LOG.error("Exception while processing Ajax", e);
    }
  }

  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
    //return PhaseId.RESTORE_VIEW;
//        return PhaseId.INVOKE_APPLICATION;
//    return PhaseId.APPLY_REQUEST_VALUES;
  }

}
