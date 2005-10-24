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
 * Created 09.01.2004 11:57:24.
 * $Id$
 */
package org.apache.myfaces.tobago.el;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import java.io.Serializable;

public class ConstantMethodBinding
    extends MethodBinding implements Serializable {

  private String outcome;

  public ConstantMethodBinding(String outcome) {
    this.outcome = outcome;
  }

  // todo: check if needed, in the moment this is needed for MyFaces state saving 
  public ConstantMethodBinding() {
  }

  public Object invoke(FacesContext facescontext, Object aobj[])
      throws EvaluationException, MethodNotFoundException {
    return outcome;
  }

  public Class getType(FacesContext facescontext)
      throws MethodNotFoundException {
    return String.class;
  }

  public String getExpressionString() {
    return outcome;
  }
}
