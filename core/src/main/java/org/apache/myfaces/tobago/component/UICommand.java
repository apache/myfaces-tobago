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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DEFAULT_COMMAND;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RENDERED_PARTIALLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TARGET;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TRANSITION;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.Iterator;

/*
 * Date: Apr 4, 2005
 * Time: 5:02:10 PM
 * $Id$
 */
public class UICommand extends javax.faces.component.UICommand {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Command";

  private static final Log LOG = LogFactory.getLog(UICommand.class);
  private static final String[] RENDERED_PARTIALLY_DEFAULT = {};

  private Boolean defaultCommand;
  private Boolean disabled;
  private String[] renderedPartially;
  private String target;
  private Boolean transition;

  public boolean isDefaultCommand() {
    if (defaultCommand != null) {
      return defaultCommand;
    }
    ValueBinding vb = getValueBinding(ATTR_DEFAULT_COMMAND);
    if (vb != null) {
      return Boolean.TRUE.equals(vb.getValue(getFacesContext()));
    } else {
      return false;
    }
  }

  public void setDefaultCommand(boolean defaultCommand) {
    this.defaultCommand = defaultCommand;
  }

  public String[] getRenderedPartially() {
    if (renderedPartially != null) {
      return renderedPartially;
    }
    ValueBinding vb = getValueBinding(ATTR_RENDERED_PARTIALLY);
    if (vb != null) {
      Object value = vb.getValue(getFacesContext());
      if (value != null) {
        if (value instanceof String[]) {
          return (String[]) value;
        } else if (value instanceof String) {
          return StringUtils.split((String) value, ",");
        } else {
          LOG.error("Ignoring RenderedPartially value binding. Unknown instance " + value.getClass().getName());
        }
      }
    }
    return RENDERED_PARTIALLY_DEFAULT;
  }

  public void setRenderedPartially(String renderedPartially) {
    if (renderedPartially != null) {
      String[] components = StringUtils.split(renderedPartially, ",");
      setRenderedPartially(components);
    }
  }

  public void setRenderedPartially(String[] renderedPartially) {
    this.renderedPartially = renderedPartially;
  }

  public boolean isDisabled() {
    if (disabled != null) {
      return disabled;
    }
    ValueBinding vb = getValueBinding(ATTR_DISABLED);
    if (vb != null) {
      return Boolean.TRUE.equals(vb.getValue(getFacesContext()));
    } else {
      return false;
    }
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public boolean isTransition() {
    if (transition != null) {
      return transition;
    }
    ValueBinding vb = getValueBinding(ATTR_TRANSITION);
    if (vb != null) {
      return Boolean.TRUE.equals(vb.getValue(getFacesContext()));
    } else {
      return true;
    }
  }

  public void setTransition(boolean transition) {
    this.transition = transition;
  }

  public String getTarget() {
    if (target != null) {
      return target;
    }
    ValueBinding vb = getValueBinding(ATTR_TARGET);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  public void setTarget(String target) {
    this.target = target;
  }


  public Object saveState(FacesContext context) {
    Object[] saveState = new Object[6];
    saveState[0] = super.saveState(context);
    saveState[1] = defaultCommand;
    saveState[2] = disabled;
    saveState[3] = renderedPartially;
    saveState[4] = target;
    saveState[5] = transition;
    return saveState;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    defaultCommand = (Boolean) values[1];
    disabled = (Boolean) values[2];
    renderedPartially = (String[]) values[3];
    target = (String) values[4];
    transition = (Boolean) values[5];
  }


  public void processDecodes(FacesContext context) {
    if (context == null) {
      throw new NullPointerException();
    }

    // Skip processing if our rendered flag is false
    if (!isRendered()) {
      return;
    }

    // Process this component itself
    try {
      decode(context);
    } catch (RuntimeException e) {
      context.renderResponse();
      throw e;
    }

    Iterator kids = getFacetsAndChildren();
    while (kids.hasNext()) {
      UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(context);
    }

  }

  public void encodeChildren(FacesContext facesContext) throws IOException {
    if (isRendered()) {
      UILayout.getLayout(this).encodeChildrenOfComponent(facesContext, this);
    }
  }

  public void queueEvent(FacesEvent facesEvent) {
    // fix for TOBAGO-262
    super.queueEvent(facesEvent);
    if (this == facesEvent.getSource()) {
      if (isImmediate()) {
        facesEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
      } else {
        facesEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
      }
    }
  }
}
