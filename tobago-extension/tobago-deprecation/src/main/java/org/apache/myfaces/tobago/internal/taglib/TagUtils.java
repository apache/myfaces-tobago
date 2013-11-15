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

package org.apache.myfaces.tobago.internal.taglib;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.InputSuggest;
import org.apache.myfaces.tobago.el.ConstantMethodBinding;
import org.apache.myfaces.tobago.event.SheetStateChangeSource;
import org.apache.myfaces.tobago.event.SortActionSource;
import org.apache.myfaces.tobago.event.TabChangeSource;
import org.apache.myfaces.tobago.internal.component.AbstractUIMessages;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import java.lang.reflect.InvocationTargetException;

/**
 * @deprecated since 2.0.0
 */
@Deprecated
public class TagUtils {
  private static final Logger LOG = LoggerFactory.getLogger(TagUtils.class);

  private TagUtils() {
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setIntegerProperty(final UIComponent component, final String name, String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        if ((component instanceof AbstractUIPage
            || component instanceof javax.faces.component.UIGraphic
            || component instanceof AbstractUIPopup)
            && (Attributes.WIDTH.equals(name) || Attributes.HEIGHT.equals(name))) {
          if (value.endsWith("px")) {
            value = value.substring(0, value.length() - 2);
          }
        }
        component.getAttributes().put(name, new Integer(value));
      }
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setBooleanProperty(final UIComponent component, final String name, final String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        component.getAttributes().put(name, Boolean.valueOf(value));
      }
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setStringProperty(final UIComponent component, final String name, final String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        component.getAttributes().put(name, value);
      }
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setConverterProperty(final UIComponent component, final String name, final String value) {
    if (value != null && component instanceof ValueHolder) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final Application application = facesContext.getApplication();
      if (UIComponentTag.isValueReference(value)) {
        final ValueBinding valueBinding = application.createValueBinding(value);
        component.setValueBinding(name, valueBinding);
      } else {
        final Converter converter = application.createConverter(value);
        ((ValueHolder) component).setConverter(converter);
      }
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setSeverityProperty(final UIComponent component, final String name, final String value) {
    setStringProperty(component, name, value);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setObjectProperty(final UIComponent component, final String name, final String value) {
    setStringProperty(component, name, value);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setCharacterProperty(final UIComponent component, final String name, final String value) {
    setStringProperty(component, name, value);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static ValueBinding createValueBinding(final String value) {
    return FacesContext.getCurrentInstance().getApplication().createValueBinding(value);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setStateChangeListenerMethodBinding(
      final UIComponent component, final String value, final Class[] args) {
    if (value != null && UIComponentTag.isValueReference(value)) {
      final MethodBinding methodBinding = FacesContext.getCurrentInstance().getApplication()
          .createMethodBinding(value, args);
      ((SheetStateChangeSource) component).setStateChangeListener(methodBinding);
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setSortActionListenerMethodBinding(
      final UIComponent component, final String value, final Class[] args) {
    if (value != null && UIComponentTag.isValueReference(value)) {
      final MethodBinding methodBinding = FacesContext.getCurrentInstance().getApplication()
          .createMethodBinding(value, args);
      ((SortActionSource) component).setSortActionListener(methodBinding);
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setSuggestMethodMethodBinding(
      final UIComponent component, final String value, final Class[] args) {
    if (value != null && UIComponentTag.isValueReference(value)) {
      final MethodBinding methodBinding = FacesContext.getCurrentInstance().getApplication()
          .createMethodBinding(value, args);
      ((InputSuggest) component).setSuggestMethod(methodBinding);
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setValueChangeListenerMethodBinding(
      final UIComponent component, final String value, final Class[] args) {
    if (value != null && UIComponentTag.isValueReference(value)) {
      final MethodBinding methodBinding = FacesContext.getCurrentInstance().getApplication()
          .createMethodBinding(value, args);
      ((EditableValueHolder) component).setValueChangeListener(methodBinding);
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setValidatorMethodBinding(final UIComponent component, final String value, final Class[] args) {
    if (value != null && UIComponentTag.isValueReference(value)) {
      final MethodBinding methodBinding = FacesContext.getCurrentInstance().getApplication()
          .createMethodBinding(value, args);
      ((EditableValueHolder) component).setValidator(methodBinding);
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setActionListenerMethodBinding(
      final UIComponent component, final String value, final Class[] args) {
    if (value != null && UIComponentTag.isValueReference(value)) {
      final MethodBinding methodBinding = FacesContext.getCurrentInstance().getApplication()
          .createMethodBinding(value, args);
      ((ActionSource) component).setActionListener(methodBinding);
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setActionMethodBinding(final UIComponent component, final String value, final Class[] args) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        final MethodBinding methodBinding =
            FacesContext.getCurrentInstance().getApplication().createMethodBinding(value, args);
        ((ActionSource) component).setAction(methodBinding);
      } else {
        ((ActionSource) component).setAction(new ConstantMethodBinding(value));
      }
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setTabChangeListenerMethodBinding(
      final UIComponent component, final String value, final Class[] args) {
    if (value != null && UIComponentTag.isValueReference(value)) {
      final MethodBinding methodBinding = FacesContext.getCurrentInstance().getApplication()
          .createMethodBinding(value, args);
      ((TabChangeSource) component).setTabChangeListener(methodBinding);
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setStringArrayProperty(final UIComponent component, final String name, final String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        final String[] components = ComponentUtils.splitList(value);
        try {
          PropertyUtils.setProperty(component, name, components);
        } catch (final IllegalAccessException e) {
          LOG.error("Ignoring Property", e);
        } catch (final InvocationTargetException e) {
          LOG.error("Ignoring Property", e);
        } catch (final NoSuchMethodException e) {
          LOG.error("Ignoring Property", e);
        }
      }
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setValueBindingProperty(final UIComponent component, final String name, final String value) {
    if (value != null && UIComponentTag.isValueReference(value)) {
      final ValueBinding valueBinding = createValueBinding(value);
      component.setValueBinding(name, valueBinding);
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setOrderByProperty(final UIComponent component, final String name, final String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        component.getAttributes().put(name, AbstractUIMessages.OrderBy.parse(value));
      }
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static String getValueFromEl(String script) {
    if (UIComponentTag.isValueReference(script)) {
      final ValueBinding valueBinding = createValueBinding(script);
      script = (String) valueBinding.getValue(FacesContext.getCurrentInstance());
    }
    return script;
  }
}
