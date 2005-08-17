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
package org.apache.myfaces.tobago.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jun 14, 2005
 * Time: 2:24:44 PM
 */
public class TobagoResourceBundle extends ResourceBundle {

  private static final Log LOG = LogFactory.getLog(TobagoResourceBundle.class);

  protected Object handleGetObject(String key) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("search for \"" + key + "\"");
    }
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ResourceManager resourceManager
        = ResourceManagerUtil.getResourceManager(facesContext);
    return resourceManager.getProperty(facesContext.getViewRoot(), "tobago", key);
  }

  public Enumeration<String> getKeys() {
    return Collections.enumeration(Collections.EMPTY_LIST);
  }
}
