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
package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.TobagoConstants;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * User: weber
 * Date: Feb 28, 2005
 * Time: 3:05:19 PM
 */
public class UIPanel extends javax.faces.component.UIPanel {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Panel";


  public void encodeBegin(FacesContext facesContext) throws IOException {
    UILayout.getLayout(this).layoutBegin(facesContext, this);
    super.encodeBegin(facesContext);
  }




  public void encodeChildren(FacesContext facesContext) throws IOException {
   if (isRendered() ) {
     UILayout.getLayout(this).encodeChildrenOfComponent(facesContext, this);
   }
  }
}
