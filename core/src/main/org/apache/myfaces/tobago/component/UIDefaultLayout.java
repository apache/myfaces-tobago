/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 06.12.2004 20:49:49.
 * $Id$
 */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import static org.apache.myfaces.tobago.TobagoConstants.*;

public class UIDefaultLayout extends UILayout {
  private static final Log LOG = LogFactory.getLog(UIDefaultLayout.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.DefaultLayout";
  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.Layout";


  private static UIDefaultLayout instance;

  public static synchronized UIDefaultLayout getInstance() {
    if (instance == null) {
      instance = (UIDefaultLayout)
          ComponentUtil.createComponent(COMPONENT_TYPE, RENDERER_TYPE_DEFAULT_LAYOUT);
      instance.setId("UIDefaultLayout");
    }
    return instance;
  }

  public void layoutBegin(FacesContext facesContext, UIComponent component) {
    super.layoutBegin(facesContext, component);
    for (Object child : component.getChildren()) {
      ((UIComponent)child).getAttributes().remove(ATTR_INNER_WIDTH);
      ((UIComponent)child).getAttributes().remove(ATTR_INNER_HEIGHT);
      ((UIComponent)child).getAttributes().remove(ATTR_LAYOUT_WIDTH);
      ((UIComponent)child).getAttributes().remove(ATTR_LAYOUT_HEIGHT);
    }
  }

  public String getFamily() {
    return COMPONENT_FAMILY;
  }

}
