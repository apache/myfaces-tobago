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
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.mozilla.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.io.IOException;

public class BoxRenderer extends
    org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag.BoxRenderer {

  public void encodeBeginTobago(
      FacesContext facesContext, UIComponent component) throws IOException {
    HtmlRendererUtil.createHeaderAndBodyStyles(facesContext, component);
    super.encodeBeginTobago(facesContext, component);
  }

  protected String getAttrStyleKey() {
    return TobagoConstants.ATTR_STYLE_BODY;
  }

// ///////////////////////////////////////////// bean getter + setter

}

