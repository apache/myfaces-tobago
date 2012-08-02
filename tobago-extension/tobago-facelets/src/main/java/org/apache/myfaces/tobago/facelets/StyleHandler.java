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

package org.apache.myfaces.tobago.facelets;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.TextHandler;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentHandler;
import org.apache.myfaces.tobago.component.UIStyle;

import javax.faces.component.UIComponent;
import java.util.Iterator;

/*
 * Date: Feb 3, 2007
 * Time: 9:35:18 AM
 */
public class StyleHandler extends ComponentHandler {

  public StyleHandler(ComponentConfig config) {
    super(config);
  }

  protected void onComponentCreated(FaceletContext context, UIComponent component, UIComponent parent) {
    StringBuffer content = new StringBuffer();
    Iterator iter = findNextByType(TextHandler.class);
    while (iter.hasNext()) {
      TextHandler text = (TextHandler) iter.next();
      content.append(text.getText(context));
    }
    if (component instanceof UIStyle) {
      ((UIStyle) component).setStyle(content.toString());
    }
  }

  protected void applyNextHandler(FaceletContext ctx, UIComponent c) {
  }
}
