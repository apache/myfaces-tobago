package org.apache.myfaces.tobago.example.nonfacesrequest;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.servlet.NonFacesRequestServlet;
import org.apache.myfaces.tobago.util.VariableResolverUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;

public class FishServlet extends NonFacesRequestServlet {

  private static final Log LOG = LogFactory.getLog(FishServlet.class);

  public String invokeApplication(FacesContext facesContext) {

    String id = (String) facesContext.getExternalContext().getRequestParameterMap().get("id");
    LOG.info("id='" + id + "'");

    FishPond fishPond = (FishPond) VariableResolverUtil.resolveVariable(facesContext, "fishPond");

    String outcome = fishPond.select(id);

    return outcome;
  }
}
