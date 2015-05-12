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
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.event.TabChangeSource2;
import org.apache.myfaces.tobago.event.ValueExpressionTabChangeListener;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Register an TabChangedListener instance on the UIComponent
 * associated with the closest parent UIComponent custom action.
 */
@Tag(name = "tabChangeListener")
@SimpleTag(
    faceletHandler = "org.apache.myfaces.tobago.facelets.TabChangeListenerHandler")
public abstract class TabChangeListenerTag extends TagSupport {

  private static final long serialVersionUID = 2L;

  private ValueExpression type;
  private ValueExpression binding;

  /**
   * Fully qualified Java class name of a TabChangeListener to be
   * created and registered.
   */
  @TagAttribute(required = true, name = "type", type = "java.lang.String")
  public void setType(final ValueExpression type) {
    this.type = type;
  }

  /**
   * The value binding expression to a TabChangeListener.
   */
  @TagAttribute(name = "binding", type = "org.apache.myfaces.tobago.event.TabChangeListener")
  public void setBinding(final ValueExpression binding) {
    this.binding = binding;
  }

  /**
   * <p>Create a new instance of the specified {@link TabChangeListener}
   * class, and register it with the {@link javax.faces.component.UIComponent} instance associated
   * with our most immediately surrounding {@link javax.faces.webapp.UIComponentELTag} instance, if
   * the {@link javax.faces.component.UIComponent} instance was created by this execution of the
   * containing JSP page.</p>
   *
   * @throws JspException if a JSP error occurs
   */
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
    if (!(component instanceof TabChangeSource2)) {
      // TODO Message resource i18n
      throw new JspException("Component " + component.getClass().getName() + " is not instanceof TabChangeSource2");
    }
    final TabChangeSource2 changeSource = (TabChangeSource2) component;
    final ELContext elContext = FacesContext.getCurrentInstance().getELContext();

    TabChangeListener handler = null;
    if (binding != null && !binding.isLiteralText()) {

      final Object value = binding.getValue(elContext);
      if (value instanceof TabChangeListener) {
        handler = (TabChangeListener) value;
      }
    }

    if (handler == null && type != null) {
      handler = createTabChangeListener((String) type.getValue(elContext));
      if (handler != null && binding != null) {
        binding.setValue(elContext, handler);
      }
    }

    if (handler != null) {
      if (binding != null) {
        changeSource.addTabChangeListener(
            new ValueExpressionTabChangeListener((String) type.getValue(elContext), binding));
      } else {
        changeSource.addTabChangeListener(handler);
      }
    }

    return (SKIP_BODY);
  }

  /**
   * <p>Create and return a new {@link TabChangeListener} to be registered
   * on our surrounding {@link javax.faces.component.UIComponent}.</p>
   *
   * @throws javax.servlet.jsp.JspException if a new instance cannot be created
   */
  protected TabChangeListener createTabChangeListener(final String className) throws JspException {
    try {
      final Class clazz = getClass().getClassLoader().loadClass(className);
      return ((TabChangeListener) clazz.newInstance());
    } catch (final Exception e) {
      throw new JspException(e);
    }
  }
}
