package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TreeDataRenderer extends LayoutComponentRendererBase {

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    RenderUtils.prepareRendererAll(facesContext, component);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
  }
/*

  @Override
  public boolean getPrepareRendersChildren() {
    return true;
  }

  @Override
  public void prepareRendersChildren(FacesContext context, UIComponent component) {
  }
*/
}
