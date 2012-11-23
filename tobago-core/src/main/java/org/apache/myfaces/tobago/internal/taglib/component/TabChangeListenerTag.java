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
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.event.TabChangeSource;

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
@Tag(name = "tabChangeListener", bodyContent = BodyContent.EMPTY)
@TagGeneration(className = "org.apache.myfaces.tobago.internal.taglib.TabChangeListenerTag")
public abstract class TabChangeListenerTag extends TagSupport {

  private static final long serialVersionUID = 1L;

  /**
   * Fully qualified Java class name of a TabChangeListener to be
   * created and registered.
   */
  @TagAttribute(required = true, name = "type", type = "java.lang.String")
  public abstract void setType(ValueExpression type);

  public abstract String getTypeValue();

  public abstract boolean isTypeSet();

  public abstract boolean isTypeLiteral();

  /**
   * The value binding expression to a TabChangeListener.
   */
  @TagAttribute(name = "binding", type = "org.apache.myfaces.tobago.event.TabChangeListener")
  public abstract void setBinding(ValueExpression binding);

  public abstract TabChangeListener getBindingValue();

  public abstract boolean isBindingSet();

  public abstract boolean isBindingLiteral();

  public abstract Object getBindingAsBindingOrExpression();

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
    if (!(component instanceof TabChangeSource)) {
      // TODO Message resource i18n
      throw new JspException("Component " + component.getClass().getName() + " is not instanceof TabChangeSource");
    }
    TabChangeSource changeSource = (TabChangeSource) component;

    TabChangeListener handler = null;
    Object valueBinding = null;
    if (isBindingSet() && !isBindingLiteral()) {
      valueBinding = getBindingAsBindingOrExpression();
      if (valueBinding != null) {
        Object obj = FacesUtils.getValueFromBindingOrExpression(valueBinding);
        if (obj != null && obj instanceof TabChangeListener) {
          handler = (TabChangeListener) obj;
        }
      }
    }

    if (handler == null && isTypeSet()) {
      handler = createTabChangeListener(getTypeValue());
      if (handler != null && valueBinding != null) {
        FacesUtils.setValueOfBindingOrExpression(FacesContext.getCurrentInstance(), handler, valueBinding);
      }
    }
    if (handler != null) {
      if (valueBinding != null) {
        FacesUtils.addBindingOrExpressionTabChangeListener(changeSource, getTypeValue(), valueBinding);
      } else {
        changeSource.addTabChangeListener(handler);
      }
    }
    // TODO else LOG.warn?
    return (SKIP_BODY);
  }



  /**
   * <p>Create and return a new {@link TabChangeListener} to be registered
   * on our surrounding {@link javax.faces.component.UIComponent}.</p>
   *
   * @throws javax.servlet.jsp.JspException if a new instance cannot be created
   */
  protected TabChangeListener createTabChangeListener(String className) throws JspException {
    try {
      Class clazz = getClass().getClassLoader().loadClass(className);
      return ((TabChangeListener) clazz.newInstance());
    } catch (Exception e) {
      throw new JspException(e);
    }
  }
}
