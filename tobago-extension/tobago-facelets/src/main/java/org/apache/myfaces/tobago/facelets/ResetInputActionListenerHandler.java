package org.apache.myfaces.tobago.facelets;

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

import com.sun.facelets.FaceletContext;
import com.sun.facelets.el.LegacyValueBinding;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagException;
import com.sun.facelets.tag.TagHandler;
import com.sun.facelets.tag.jsf.ComponentSupport;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.event.ResetFormActionListener;
import org.apache.myfaces.tobago.event.ResetInputActionListener;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.FacesVersion;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import java.io.IOException;

public class ResetInputActionListenerHandler extends TagHandler {

  private final TagAttribute execute;

  public ResetInputActionListenerHandler(TagConfig config) {
    super(config);
    execute = getAttribute(Attributes.EXECUTE);
  }

  public void apply(FaceletContext faceletContext, UIComponent parent)
      throws IOException, FacesException, ELException {
    if (parent instanceof ActionSource) {
      if (ComponentSupport.isNew(parent)) {
        ActionSource actionSource = (ActionSource) parent;
        if (execute == null) {
          actionSource.addActionListener(new ResetFormActionListener());
        } else if (execute.isLiteral())  {
          actionSource.addActionListener(new ResetInputActionListener(ComponentUtils.splitList(execute.getValue())));
        } else {
          ValueExpression forValueExpression = execute.getValueExpression(faceletContext, String.class);
          if (FacesVersion.supports12()) {
            FacesUtils.addBindingOrExpressionResetActionListener(actionSource, forValueExpression);
          } else {
            FacesUtils.addBindingOrExpressionResetActionListener(actionSource,
                new LegacyValueBinding(forValueExpression));
          }
        }
      }
    } else {
      throw new TagException(tag, "Parent is not of type ActionSource, type is: " + parent);
    }
  }
}


