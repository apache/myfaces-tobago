/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
 * Created Nov 20, 2002 at 11:39:23 AM.
 * $Id: UIInput.java 1361 2005-08-15 09:46:20Z lofwyr $
 */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class UILabel extends UIOutput {

  private static final Log LOG = LogFactory.getLog(UILabel.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Label";

  @Override
  public void encodeBegin(FacesContext facesContext) throws IOException {
    String _for = (String) getAttributes().get(TobagoConstants.ATTR_FOR);
    if (LOG.isDebugEnabled()) {
      LOG.debug("_for = '" + _for + "'");
    }
    if ("@auto".equals(_for)) {
      for(Object object : getParent().getChildren()) {
        UIComponent child = (UIComponent) object;
        if (child instanceof javax.faces.component.UIInput) {
          _for = child.getId();
          getAttributes().put(TobagoConstants.ATTR_FOR, _for);
          break;
        }
      }
    }

    super.encodeBegin(facesContext);
  }

}
