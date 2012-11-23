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

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.event.PopupActionListener;

import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/*
 * Date: Jan 3, 2007
 * Time: 10:42:11 PM
 */

/**
 * Register an PopupActionListener instance on the UIComponent
 * associated with the closest parent UIComponent.
 */
@Tag(name = "popupReference", bodyContent = BodyContent.EMPTY)
@TagGeneration(className = "org.apache.myfaces.tobago.internal.taglib.PopupReferenceTag")
public abstract class PopupReferenceTag extends TagSupport {

  private static final long serialVersionUID = 1L;

  /**
   * The id of a Popup.
   */
  @TagAttribute(required = true, name ="for", type = "java.lang.String")
  public abstract void setFor(ValueExpression forValue);

  public abstract String getForValue();

  public abstract boolean isForLiteral();

  public abstract Object getForAsBindingOrExpression();

  public int doStartTag() throws JspException {

    // Locate our parent UIComponentTag
    UIComponentClassicTagBase tag =
        UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
    if (tag == null) {
      // TODO Message resource i18n
      throw new JspException("Not nested in faces tag");
    }

    if (!tag.getCreated()) {
      return (SKIP_BODY);
    }

    UIComponent component = tag.getComponentInstance();
    if (component == null) {
      // TODO Message resource i18n
      throw new JspException("Component Instance is null");
    }
    if (!(component instanceof ActionSource)) {
      // TODO Message resource i18n
      throw new JspException("Component " + component.getClass().getName() + " is not instanceof ActionSource");
    }
    ActionSource actionSource = (ActionSource) component;
    if (isForLiteral()) {
      actionSource.addActionListener(new PopupActionListener(getForValue()));
    } else {
      FacesUtils.addBindingOrExpressionPopupActionListener(actionSource, getForAsBindingOrExpression());
    }
    return (SKIP_BODY);
  }

}
