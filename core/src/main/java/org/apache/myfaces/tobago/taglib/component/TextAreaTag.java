package org.apache.myfaces.tobago.taglib.component;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ROWS;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public class TextAreaTag extends TextInputTag
    implements TextAreaTagDeclaration {

  private static final Log LOG = LogFactory.getLog(TextAreaTag.class);

  private String rows;
  private String markup;


  @Override
  public void release() {
    super.release();
    rows = null;
    markup = null;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    if (getLabel() != null) {
      LOG.warn("the label attribute is deprecated in t:in, please use tx:in instead.");
    }

    ComponentUtil.setStringProperty(component, ATTR_ROWS, rows);
    ComponentUtil.setMarkup(component, markup);
  }

  public String getRows() {
    return rows;
  }

  public void setRows(String rows) {
    this.rows = rows;
  }

   public String getMarkup() {
    return markup;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }
}
