package org.apache.myfaces.tobago.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ALIGN;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RESIZABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SORTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;


/*
 * Date: 18.04.2006
 * Time: 21:50:29
 */
public class UIColumn extends javax.faces.component.UIColumn implements SupportsMarkup {
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Column";
  private Boolean sortable;
  private Boolean resizable;
  private String align;
  private String label;
  private String[] markup;
  private String width;

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    align = (String) values[1];
    sortable = (Boolean) values[2];
    resizable = (Boolean) values[3];
    label = (String) values[4];
    markup = (String[]) values[5];
    width = (String) values[6];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[7];
    values[0] = super.saveState(context);
    values[1] = align;
    values[2] = sortable;
    values[3] = resizable;
    values[4] = label;
    values[5] = markup;
    values[6] = width;
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

  public boolean isSortable() {
    if (sortable != null) {
      return sortable;
    }
    ValueBinding vb = getValueBinding(ATTR_SORTABLE);
    if (vb != null) {
      return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
    } else {
      return false;
    }
  }

  public void setSortable(boolean sortable) {
    this.sortable = sortable;
  }

  public boolean isResizable() {
    if (resizable != null) {
      return resizable;
    }
    ValueBinding vb = getValueBinding(ATTR_RESIZABLE);
    if (vb != null) {
      return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
    } else {
      return true;
    }
  }

  public void setResizable(boolean resizable) {
    this.resizable = resizable;
  }

  public String getAlign() {
    if (align != null) {
      return align;
    }
    ValueBinding vb = getValueBinding(ATTR_ALIGN);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return align;
    }
  }

  public void setAlign(String align) {
    this.align = align;
  }

  public String getLabel() {
    if (label != null) {
      return label;
    }
    ValueBinding vb = getValueBinding(ATTR_LABEL);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return label;
    }
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getWidth() {
    if (width != null) {
      return width;
    }
    ValueBinding vb = getValueBinding(ATTR_WIDTH);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return RelativeLayoutToken.DEFAULT_TOKEN_STRING;
    }
  }

  public void setWidth(String width) {
    this.width = width;
  }

}
