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

import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.event.ResetFormActionListener;
import org.apache.myfaces.tobago.event.ResetInputActionListener;
import org.apache.myfaces.tobago.event.ValueExpressionResetInputActionListener;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * A ResetInputActionListener is a declarative way to allow an action source to reset all EditableValueHolder
 * of a page or in a sub-form or part of the component tree.
 */
@Tag(name = "resetInputActionListener")
@SimpleTag(
    faceletHandler = "org.apache.myfaces.tobago.facelets.ResetInputActionListenerHandler")
public abstract class ResetInputActionListenerTag extends TagSupport {

  private static final long serialVersionUID = 2L;

  private ValueExpression execute;

  public int doStartTag() throws JspException {

    // Locate our parent UIComponentTag
    final UIComponentClassicTagBase tag =
        UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
    if (tag == null) {
      // TODO Message resource i18n
      throw new JspException("Not nested in faces tag");
    }

    if (!tag.getCreated()) {
      return (SKIP_BODY);
    }

    final UIComponent component = tag.getComponentInstance();
    if (component == null) {
      // TODO Message resource i18n
      throw new JspException("Component Instance is null");
    }
    if (!(component instanceof ActionSource)) {
      // TODO Message resource i18n
      throw new JspException("Component " + component.getClass().getName() + " is not instanceof ActionSource");
    }

    final ELContext elContext = FacesContext.getCurrentInstance().getELContext();

    final ActionSource actionSource = (ActionSource) component;
    if (execute == null) {
      actionSource.addActionListener(new ResetFormActionListener());
    } else if (execute.isLiteralText()) {
      actionSource.addActionListener(new ResetInputActionListener(
          ComponentUtils.splitList((String) execute.getValue(elContext))));
    } else {
      actionSource.addActionListener(new ValueExpressionResetInputActionListener(execute));
    }
    return (SKIP_BODY);
  }

  @Override
  public void release() {
    super.release();
    execute = null;
  }

  /**
   * A list of ids of components. For each id, the surrounding (virtual) UIForm will be searched, and for each of
   * them, all containing EditableValueHolder will be reset.
   */
  @TagAttribute(required = false, name = Attributes.EXECUTE, type = "java.lang.String")
  public void setExecute(final javax.el.ValueExpression execute) {
    this.execute = execute;
  }

}
