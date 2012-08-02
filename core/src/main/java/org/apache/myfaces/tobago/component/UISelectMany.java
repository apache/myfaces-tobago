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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TAB_INDEX;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.List;

/*
 * User: weber
 * Date: May 31, 2005
 * Time: 7:47:11 PM
 */
public class UISelectMany extends javax.faces.component.UISelectMany implements SupportsMarkup {

  @SuppressWarnings("UnusedDeclaration")
  private static final Log LOG = LogFactory.getLog(UISelectMany.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.SelectMany";

  private String[] markup;
  private Integer tabIndex;

  public Object[] getSelectedValues() {
    Object value = getValue();
    if (value instanceof List) {
      List list = (List) value;
      return list.toArray();
    } else {
      return (Object[]) value;
    }
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    markup = (String[]) values[1];
    tabIndex = (Integer) values[2];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[3];
    values[0] = super.saveState(context);
    values[1] = markup;
    values[2] = tabIndex;
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

  public void encodeBegin(FacesContext facesContext) throws IOException {
    // TODO change this should be renamed to DimensionUtils.prepare!!!
    UILayout.getLayout(this).layoutBegin(facesContext, this);
    super.encodeBegin(facesContext);
  }

  public Integer getTabIndex() {
    if (tabIndex != null) {
      return tabIndex;
    }
    ValueBinding vb = getValueBinding(ATTR_TAB_INDEX);
    if (vb != null) {
      Number number = (Number) vb.getValue(getFacesContext());
      if (number != null) {
        return Integer.valueOf(number.intValue());
      }
    }
    return null;
  }

  public void setTabIndex(Integer tabIndex) {
    this.tabIndex = tabIndex;
  }

}
