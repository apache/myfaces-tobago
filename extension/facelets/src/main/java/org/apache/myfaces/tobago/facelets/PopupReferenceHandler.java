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

import com.sun.facelets.tag.TagHandler;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagException;
import com.sun.facelets.tag.jsf.ComponentSupport;
import com.sun.facelets.FaceletContext;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import java.io.IOException;

import org.apache.myfaces.tobago.event.PopupActionListener;
import org.apache.myfaces.tobago.TobagoConstants;

/*
 * User: bommel
 * Date: Jan 4, 2007
 * Time: 6:25:09 PM
 */
public class PopupReferenceHandler extends TagHandler {

  private final TagAttribute forComponent;

  public PopupReferenceHandler(TagConfig config) {
    super(config);
    forComponent = getAttribute(TobagoConstants.ATTR_FOR);
  }

  public void apply(FaceletContext faceletContext, UIComponent parent)
      throws IOException, FacesException, ELException {
    if (parent instanceof ActionSource) {
      if (ComponentSupport.isNew(parent)) {
        ActionSource actionSource = (ActionSource) parent;
        actionSource.addActionListener(new PopupActionListener(forComponent.getValue()));
      }
    } else {
      throw new TagException(tag, "Parent is not of type ActionSource, type is: " + parent);
    }
  }
}
