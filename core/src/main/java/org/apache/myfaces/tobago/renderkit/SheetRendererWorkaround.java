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
 * Created 12.08.2005 09:44:12.
 * $Id: $
 */
package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.UIData;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

// fixme: this is only a workaround to separate the theme from the core
/** @deprecated */
public interface SheetRendererWorkaround {

  boolean needVerticalScrollbar(FacesContext facesContext, UIData data);

  int getScrollbarWidth(FacesContext facesContext, UIComponent component);

  int getContentBorder(FacesContext facesContext, UIData data);
}
