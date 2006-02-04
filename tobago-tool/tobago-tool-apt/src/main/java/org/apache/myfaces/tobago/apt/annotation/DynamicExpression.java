package org.apache.myfaces.tobago.apt.annotation;

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

/**
 * Created: Apr 27, 2005 5:08:45 PM
 * User: bommel
 * $Id: DynamicExpression.java,v 1.1 2005/05/11 15:20:34 bommel Exp $
 */
public enum DynamicExpression {

  VALUE_BINDING, METHOD_BINDING, NONE;

  public String toString() {
    switch (this) {
      case VALUE_BINDING:
        return "VB";
      case METHOD_BINDING:
        return "MB";
      case NONE:
        return "NONE";
    }
    throw new IllegalStateException("Unexpected DynamicExpression "+ name());
  }

}
