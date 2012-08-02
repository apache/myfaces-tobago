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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;

public class UILabel extends UIOutput implements SupportsMarkup {

  private static final Log LOG = LogFactory.getLog(UILabel.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Label";

  private String[] markup;
  private String tip;


  public String getTip() {
    if (tip != null) {
      return tip;
    }
    ValueBinding vb = getValueBinding(ATTR_TIP);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  public void setTip(String tip) {
    this.tip = tip;
  }


  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    markup = (String[]) values[1];
    tip = (String) values[2];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[3];
    values[0] = super.saveState(context);
    values[1] = markup;
    values[2] = tip;
    return values;
  }

  public String[] getMarkup() {
    if (markup != null) {
      return markup;
    }
    return ComponentUtil.getMarkupBinding(getFacesContext(), this);
  }

  public void setMarkup(String[] markup) {
    this.markup = markup;
  }

  @Override
  public void encodeBegin(FacesContext facesContext) throws IOException {
    String forComponent = (String) getAttributes().get(TobagoConstants.ATTR_FOR);
    if (LOG.isDebugEnabled()) {
      LOG.debug("for = '" + forComponent + "'");
    }
    if ("@auto".equals(forComponent)) {
      for (Object object : getParent().getChildren()) {
        UIComponent child = (UIComponent) object;
        if (child instanceof javax.faces.component.UIInput) {
          forComponent = child.getId();
          getAttributes().put(TobagoConstants.ATTR_FOR, forComponent);
          break;
        }
      }
    }
    super.encodeBegin(facesContext);
  }
}
