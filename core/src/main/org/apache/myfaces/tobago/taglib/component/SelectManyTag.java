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
package org.apache.myfaces.tobago.taglib.component;

import javax.faces.component.UISelectMany;

/**
 * User: weber
 * Date: Apr 18, 2005
 * Time: 4:59:34 PM
 */

public class SelectManyTag extends InputTag implements org.apache.myfaces.tobago.taglib.decl.SelectManyTag {

  public String getComponentType() {
    return UISelectMany.COMPONENT_TYPE;
  }
}
