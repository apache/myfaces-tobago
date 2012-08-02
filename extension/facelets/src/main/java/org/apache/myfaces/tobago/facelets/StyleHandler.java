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

import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TextHandler;
import com.sun.facelets.tag.TagHandler;
import com.sun.facelets.tag.TagException;
import com.sun.facelets.FaceletContext;

import javax.faces.component.UIComponent;
import javax.faces.FacesException;
import javax.el.ELException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.myfaces.tobago.component.UIPage;

/*
 * User: bommel
 * Date: Feb 3, 2007
 * Time: 9:35:18 AM
 */
public class StyleHandler extends TagHandler {

  private final TagAttribute style;

  public StyleHandler(TagConfig config) {
    super(config);
    style = getAttribute("style");
  }

  public void apply(FaceletContext faceletContext, UIComponent parent)
      throws IOException, FacesException, ELException {

    if (parent instanceof UIPage) {
      UIPage page = (UIPage) parent;
      if (style != null) {
        page.getStyleFiles().add(style.getValue(faceletContext));
      }
      StringBuffer buffer = new StringBuffer();
      Iterator iter = findNextByType(TextHandler.class);
      while (iter.hasNext()) {
        TextHandler text = (TextHandler) iter.next();
        buffer.append(text.getText(faceletContext));
      }
      String content = buffer.toString().trim();

      if (content.length() > 0) {
        page.getStyleBlocks().add(content);
      }
    } else {
      throw new TagException(tag, "Parent is not of type UIPage, type is: " + parent);
    }
  }
}
