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
 * Created: 28.04.2004, 18:23:31
 * $Id$
 */
package org.apache.myfaces.tobago.util;

import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.faces.el.VariableResolver;

public class VariableResolverUtil {

  public static Object resolveVariable(FacesContext context, String variable) {
    Application application = context.getApplication();
    VariableResolver variableResolver = application.getVariableResolver();
    return variableResolver.resolveVariable(context, variable);
  }
}
