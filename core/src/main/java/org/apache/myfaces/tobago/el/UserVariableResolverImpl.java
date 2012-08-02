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

package org.apache.myfaces.tobago.el;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

/**
 * @deprecated use UserWrapper as managed bean in request scope instead.
 */
@Deprecated
public class UserVariableResolverImpl extends VariableResolver {

  private static final Log LOG = LogFactory.getLog(UserVariableResolverImpl.class);

  private VariableResolver base;
  private UserWrapper userWrapper;

  public UserVariableResolverImpl(VariableResolver base) {
    if (LOG.isInfoEnabled()) {
      LOG.info("Hiding ri base implemation: " + base);
    }
    this.base = base;
  }

  public Object resolveVariable(FacesContext facesContext, String name)
      throws EvaluationException {

    if ("user".equals(name)) {
      // TODO: optimize me: put it in request?
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
