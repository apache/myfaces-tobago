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

package org.apache.myfaces.tobago.taglib.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SCROLLBARS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SPAN_X;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SPAN_Y;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_PANEL;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICell;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.tagext.BodyTag;

/*
 * Created 29.07.2003 at 15:09:53.
 * $Id$
 */

// Some Weblogic versions need explicit 'implements' for BodyTag
public class CellTag extends TobagoBodyTag implements BodyTag, CellTagDeclaration {

  private static final Log LOG = LogFactory.getLog(CellTag.class);

  private String spanX = "1";

  private String spanY = "1";

  private String scrollbars;


  public String getComponentType() {
    return UICell.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return RENDERER_TYPE_PANEL;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setIntegerProperty(component, ATTR_SPAN_X, spanX);
    ComponentUtil.setIntegerProperty(component, ATTR_SPAN_Y, spanY);
    ComponentUtil.setStringProperty(component, ATTR_SCROLLBARS, scrollbars);

    if (LOG.isDebugEnabled()) {
      LOG.debug("spanX=" + spanX + " spanY=" + spanY);
      LOG.debug("spanX=" + component.getAttributes().get(ATTR_SPAN_X)
          + " spanY=" + component.getAttributes().get(ATTR_SPAN_Y));
      LOG.debug("component = " + getComponentInstance());
    }
  }

  public void release() {
    super.release();
    spanX = "1";
    spanY = "1";
    scrollbars = null;
  }

  public String getSpanX() {
    return spanX;
  }

  public void setSpanX(String spanX) {
    this.spanX = spanX;
  }

  public String getSpanY() {
    return spanY;
  }

  public void setSpanY(String spanY) {
    this.spanY = spanY;
  }

  public String getScrollbars() {
    return scrollbars;
  }

  public void setScrollbars(String scrollbars) {
    this.scrollbars = scrollbars;
  }
}
