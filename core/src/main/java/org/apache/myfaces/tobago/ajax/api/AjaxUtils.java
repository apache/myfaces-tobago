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
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIViewRoot;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: 12.10.2005
 * Time: 13:11:05
 * To change this template use File | Settings | File Templates.
 */
public class AjaxUtils {

  private static final Log LOG = LogFactory.getLog(AjaxUtils.class);

//  public static void processAjax(FacesContext facesContext, UIComponent component) throws IOException {
//
//    component.processValidators(facesContext);
//
//    if (! facesContext.getRenderResponse()) {
//      component.processUpdates(facesContext);
//    }
//
//    // invokeApplication here ??
//
//    renderAjax(facesContext, component);
//  }
//
//  private static void renderAjax(FacesContext facesContext, UIComponent component) throws IOException {
//    final Iterator facetsAndChildren = component.getFacetsAndChildren();
//    while (facetsAndChildren.hasNext()) {
//      UIComponent child = (UIComponent) facetsAndChildren.next();
//      if (child instanceof AjaxComponent) {
//        ((AjaxComponent)child).processAjax(facesContext);
//      }
//      else {
//        renderAjax(facesContext, child);
//      }
//      if (facesContext.getResponseComplete()) {
//        return;
//      }
//    }
//  }

  public static void checkParamValidity(FacesContext facesContext, UIComponent uiComponent, Class compClass) {
    if (facesContext == null) {
      throw new NullPointerException("facesContext may not be null");
    }
    if (uiComponent == null) {
      throw new NullPointerException("uiComponent may not be null");
    }
    //if (compClass != null && !(compClass.isAssignableFrom(uiComponent.getClass())))
    // why isAssignableFrom with additional getClass method call if isInstance does the same?
    if (compClass != null && !(compClass.isInstance(uiComponent))) {
      throw new IllegalArgumentException("uiComponent : "
          + uiComponent.getClass().getName() + " is not instance of "
          + compClass.getName() + " as it should be");
    }
  }




  public static void encodeAjaxComponent(FacesContext facesContext, UIComponent component) throws IOException {
    if (facesContext == null) {
      throw new NullPointerException("facesContext");
    }
    if (!component.isRendered()) {
      return;
    }
    Renderer renderer = ComponentUtil.getRenderer(facesContext, component);
    if (renderer != null && renderer instanceof AjaxRenderer) {
      ((AjaxRenderer) renderer).encodeAjax(facesContext, component);
    }
  }


  public static void processAjax(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (component instanceof AjaxComponent) {
      ((AjaxComponent) component).processAjax(facesContext);
    } else {
      processAjaxOnChildren(facesContext, component);
    }
  }

  public static void processActiveAjaxComponent(FacesContext facesContext,
                                                UIComponent component)
      throws IOException {

    if (component instanceof AjaxComponent) {
      final UIViewRoot viewRoot = (UIViewRoot) facesContext.getViewRoot();

      // TODO: handle phaseListeners ??

      component.processValidators(facesContext);
      viewRoot.broadcastEventsForPhase(facesContext, PhaseId.PROCESS_VALIDATIONS);

      if (!facesContext.getRenderResponse()) {
        component.processUpdates(facesContext);
        viewRoot.broadcastEventsForPhase(facesContext, PhaseId.UPDATE_MODEL_VALUES);
      }

      if (!facesContext.getRenderResponse()) {
        viewRoot.processApplication(facesContext);
      }

      ((AjaxComponent) component).encodeAjax(facesContext);
    } else {
      LOG.error("Can't process non AjaxComponent : \""
          + component.getClientId(facesContext) + "\" = "
          + component.getClass().getName());
    }
  }

  public static void processAjaxOnChildren(FacesContext facesContext,
      UIComponent component) throws IOException {

    final Iterator<UIComponent> facetsAndChildren = component.getFacetsAndChildren();
    while (facetsAndChildren.hasNext() && !facesContext.getResponseComplete()) {
      AjaxUtils.processAjax(facesContext, facetsAndChildren.next());
    }
  }
}
