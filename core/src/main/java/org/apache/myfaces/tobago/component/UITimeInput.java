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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TAB_INDEX;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import static javax.faces.convert.DateTimeConverter.CONVERTER_ID;
import javax.faces.el.ValueBinding;
import java.util.TimeZone;

/*
 * Date: 10.02.2006
 * Time: 20:50:49
 */
public class UITimeInput extends javax.faces.component.UIInput {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TimeInput";

  private Integer tabIndex;

  public Converter getConverter() {
    Converter converter = super.getConverter();
    if (converter == null) {
      // setting required default converter
      Application application
          = FacesContext.getCurrentInstance().getApplication();
      DateTimeConverter dateTimeConverter
          = (DateTimeConverter) application.createConverter(CONVERTER_ID);
      dateTimeConverter.setPattern("HH:mm");
      dateTimeConverter.setTimeZone(TimeZone.getDefault());
      setConverter(dateTimeConverter);
    }
    return converter;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    tabIndex = (Integer) values[1];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[2];
    values[0] = super.saveState(context);
    values[1] = tabIndex;
    return values;
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
