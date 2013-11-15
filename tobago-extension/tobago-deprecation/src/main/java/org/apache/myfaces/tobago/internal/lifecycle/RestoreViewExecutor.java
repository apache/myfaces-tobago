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

package org.apache.myfaces.tobago.internal.lifecycle;

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.Secret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.PhaseId;
import javax.faces.render.ResponseStateManager;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implements the life cycle as described in Spec. 1.0 PFD Chapter 2
 * <p/>
 * Restore view phase (JSF Spec 2.2.1)
 *
 * Not longer needed.
 *
 * @deprecated since Tobago 2.0.0
 */
@Deprecated
class RestoreViewExecutor implements PhaseExecutor {

  private static final Logger LOG = LoggerFactory.getLogger(RestoreViewExecutor.class);

  public boolean execute(final FacesContext facesContext) {
    final ExternalContext externalContext = facesContext.getExternalContext();

    final Map sessionMap = externalContext.getSessionMap();
    UIViewRoot viewRoot = (UIViewRoot) sessionMap.get(TobagoLifecycle.VIEW_ROOT_KEY);
    if (viewRoot != null) {
      facesContext.setViewRoot(viewRoot);
      sessionMap.remove(TobagoLifecycle.VIEW_ROOT_KEY);
      //noinspection unchecked
      final List<Object[]> messageHolders = (List<Object[]>) sessionMap.get(TobagoLifecycle.FACES_MESSAGES_KEY);
      if (messageHolders != null) {
        for (final Object[] messageHolder : messageHolders) {
          facesContext.addMessage((String) messageHolder[0], (FacesMessage) messageHolder[1]);
        }
      }
      sessionMap.remove(TobagoLifecycle.FACES_MESSAGES_KEY);
      if (viewRoot.getChildCount() > 0 && viewRoot.getChildren().get(0) instanceof AbstractUIPage) {
        viewRoot.getChildren().get(0).decode(facesContext);
      }
      facesContext.renderResponse();
      return true;
    }

    if (facesContext.getViewRoot() != null) {
      facesContext.getViewRoot().setLocale(facesContext.getExternalContext().getRequestLocale());
      ComponentUtils.resetPage(facesContext);
      recursivelyHandleComponentReferencesAndSetValid(facesContext, facesContext.getViewRoot());
      return false;
    }

    // Derive view identifier
    final String viewId = deriveViewId(facesContext);

    if (viewId == null) {

      if (externalContext.getRequestServletPath() == null) {
        return true;
      }

      if (!externalContext.getRequestServletPath().endsWith("/")) {
        try {
          externalContext.redirect(externalContext.getRequestServletPath() + "/");
          facesContext.responseComplete();
          return true;
        } catch (final IOException e) {
          throw new FacesException("redirect failed", e);
        }
      }
    }

    final Application application = facesContext.getApplication();
    final ViewHandler viewHandler = application.getViewHandler();

    final boolean postBack = isPostBack(facesContext);
    if (postBack) {
      viewRoot = viewHandler.restoreView(facesContext, viewId);
    }
    if (viewRoot == null) {
      viewRoot = viewHandler.createView(facesContext, viewId);
      viewRoot.setViewId(viewId);
      facesContext.renderResponse();
    }

    facesContext.setViewRoot(viewRoot);
    ComponentUtils.resetPage(facesContext);

    if (!postBack) {
      // no POST or query parameters --> set render response flag
      facesContext.renderResponse();
    }

    if (!isSessionSecretValid(facesContext)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Secret is invalid!");
      }
      facesContext.renderResponse();
    }

    recursivelyHandleComponentReferencesAndSetValid(facesContext, viewRoot);
    //noinspection unchecked
    facesContext.getExternalContext().getRequestMap().put(TobagoLifecycle.VIEW_ROOT_KEY, viewRoot);
    return false;
  }

  private boolean isPostBack(final FacesContext facesContext) {
    final Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    return requestParameterMap.containsKey(ResponseStateManager.VIEW_STATE_PARAM);
  }

  private boolean isSessionSecretValid(final FacesContext facesContext) {
    if (TobagoConfig.getInstance(FacesContext.getCurrentInstance()).isCheckSessionSecret()) {
      return Secret.check(facesContext);
    } else {
      return true;
    }
  }

  public PhaseId getPhase() {
    return PhaseId.RESTORE_VIEW;
  }

  private static String deriveViewId(final FacesContext facesContext) {
    final ExternalContext externalContext = facesContext.getExternalContext();

/*
    if (PortletUtils.isPortletRequest(facesContext)) {
      return PortletUtils.getViewId(facesContext);
    }

*/
    String viewId = externalContext.getRequestPathInfo(); // getPathInfo
    if (viewId == null) {
      // No extra path info found, so it is probably extension mapping
      viewId = externalContext.getRequestServletPath(); // getServletPath
      if (viewId == null) {
        final String msg = "RequestServletPath is null, cannot determine viewId of current page.";
        LOG.error(msg);
        throw new FacesException(msg);
      }

      // TODO: JSF Spec 2.2.1 - what do they mean by "if the default
      // ViewHandler implementation is used..." ?
      final String defaultSuffix = externalContext.getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
      final String suffix = defaultSuffix != null ? defaultSuffix : ViewHandler.DEFAULT_SUFFIX;
      if (suffix.charAt(0) != '.') {
        final String msg = "Default suffix must start with a dot!";
        LOG.error(msg);
        throw new FacesException(msg);
      }

      final int dot = viewId.lastIndexOf('.');
      if (dot == -1) {
        LOG.error("Assumed extension mapping, but there is no extension in " + viewId);
        viewId = null;
      } else {
        viewId = viewId.substring(0, dot) + suffix;
      }
    }

    return viewId;
  }

  // next two methods are taken from 'org.apache.myfaces.shared.util.RestoreStateUtils'

  public static void recursivelyHandleComponentReferencesAndSetValid(final FacesContext facesContext,
      final UIComponent parent) {
    final boolean forceHandle = false;

    final Method handleBindingsMethod = getBindingMethod(parent);

    if (handleBindingsMethod != null && !forceHandle) {
      try {
        handleBindingsMethod.invoke(parent, new Object[]{});
      } catch (final Throwable th) {
        LOG.error("Exception while invoking handleBindings on component with client-id:"
            + parent.getClientId(facesContext), th);
      }
    } else {
      for (final Iterator it = parent.getFacetsAndChildren(); it.hasNext();) {
        final UIComponent component = (UIComponent) it.next();

        final ValueBinding binding = component.getValueBinding("binding");    //TODO: constant
        if (binding != null && !binding.isReadOnly(facesContext)) {
          binding.setValue(facesContext, component);
        }

        if (component instanceof UIInput) {
          ((UIInput) component).setValid(true);
        }

        recursivelyHandleComponentReferencesAndSetValid(facesContext, component);
      }
    }
  }

  /**
   * This is all a hack to work around a spec-bug which will be fixed in JSF2.0
   *
   * @return true if this component is bindingAware (e.g. aliasBean)
   */
  private static Method getBindingMethod(final UIComponent parent) {
    final Class[] interfaces = parent.getClass().getInterfaces();

    for (final Class clazz : interfaces) {
      if (clazz.getName().contains("BindingAware")) {
        try {
          return parent.getClass().getMethod("handleBindings", new Class[]{});
        } catch (final NoSuchMethodException e) {
          // return
        }
      }
    }

    return null;
  }

}
