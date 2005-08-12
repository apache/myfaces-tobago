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
 * All rights reserved. Created 20.10.2004 11:21:16.
 * $Id$
 */
package org.apache.myfaces.tobago.el;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.el.VariableResolver;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;

public class VariableResolverImpl extends VariableResolver {

  private static final Log LOG = LogFactory.getLog(VariableResolverImpl.class);

  private VariableResolver base;
  private UserWrapper userWrapper;

  public VariableResolverImpl(VariableResolver base) {
    LOG.info("Hiding ri base implemation: " + base);
    this.base = base;
  }

  public Object resolveVariable(FacesContext facesContext, String name)
      throws EvaluationException {

    if ("user".equals(name)) {
      // todo: optimize me: put it in request?
      if (LOG.isDebugEnabled()) {
        LOG.debug("resolving: " + name);
      }
      if (userWrapper == null) {
        userWrapper = new UserWrapper();
      }
      return userWrapper;
    } else {
      return base.resolveVariable(facesContext, name);
    }
  }
}
