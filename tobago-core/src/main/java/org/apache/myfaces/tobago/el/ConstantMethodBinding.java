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

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import java.io.Serializable;

public class ConstantMethodBinding
    extends MethodBinding implements Serializable {

  private static final long serialVersionUID = 5019857148558549340L;

  private String outcome;

  public ConstantMethodBinding(final String outcome) {
    this.outcome = outcome;
  }

  // TODO: check if needed, in the moment this is needed for MyFaces state saving
  public ConstantMethodBinding() {
  }

  public Object invoke(final FacesContext facesContext, final Object[] parameters) {
    return outcome;
  }

  public Class getType(final FacesContext facesContext) {
    return String.class;
  }

  public String getExpressionString() {
    return outcome;
  }
}
