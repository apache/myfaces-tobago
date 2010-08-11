package org.apache.myfaces.tobago.renderkit.html;

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

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.Theme;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;

public class StyleClasses implements Serializable {

  private static final long serialVersionUID = 3738052927067803517L;

  private static final Log LOG = LogFactory.getLog(StyleClasses.class);

  public static final char SEPERATOR = '-';
  public static final String PREFIX = "tobago" + SEPERATOR;
  public static final String MARKUP = SEPERATOR + "markup" + SEPERATOR;

  private ListOrderedSet classes;

  public StyleClasses() {
    classes = new ListOrderedSet();
  }

  private StyleClasses(StyleClasses base) {
    this();
    classes.addAll(base.classes);
  }

  public static StyleClasses ensureStyleClasses(UIComponent component) {
    Map attributes = component.getAttributes();
    StyleClasses classes = (StyleClasses) attributes.get(ATTR_STYLE_CLASS);
    if (classes == null) {
      classes = new StyleClasses();
      attributes.put(ATTR_STYLE_CLASS, classes);
    }
    return classes;
  }

  public static StyleClasses ensureStyleClassesCopy(UIComponent component) {
    return new StyleClasses(ensureStyleClasses(component));
  }

  @Deprecated
  public void addFullQualifiedClass(String clazz) {
    classes.add(clazz);
  }

  @Deprecated
  public void removeFullQualifiedClass(String clazz) {
    classes.remove(clazz);
  }

  public void addClass(String renderer, String sub) {
    classes.add(nameOfClass(renderer, sub));
  }

  public void removeClass(String renderer, String sub) {
    classes.remove(nameOfClass(renderer, sub));
  }

  private String nameOfClass(String renderer, String sub) {
    StringBuilder builder = new StringBuilder(PREFIX);
    builder.append(renderer);
    builder.append(SEPERATOR);
    builder.append(sub);
    return builder.toString();
  }

  public void addMarkupClass(String renderer, String markup) {
    addMarkupClass(renderer, null, markup);
  }

  public void removeMarkupClass(String renderer, String markup) {
    removeMarkupClass(renderer, null, markup);
  }

  public void addMarkupClass(String renderer, String sub, String markup) {
    classes.add(nameOfMarkupClass(renderer, sub, markup));
  }

  public void removeMarkupClass(String renderer, String sub, String markup) {
    classes.remove(nameOfMarkupClass(renderer, sub, markup));
  }

  private String nameOfMarkupClass(String renderer, String sub, String markup) {
    StringBuilder builder = new StringBuilder(PREFIX);
    builder.append(renderer);
    if (sub != null) {
      builder.append(SEPERATOR);
      builder.append(sub);
    }
    builder.append(MARKUP);
    builder.append(markup);
    return builder.toString();
  }

  public void addMarkupClass(UIComponent component, String rendererName) {
    if (component instanceof SupportsMarkup) {
      addMarkupClass((SupportsMarkup) component, rendererName, null);
    }
  }

  public void addMarkupClass(SupportsMarkup supportsMarkup, String rendererName, String sub) {
    for (String markup : supportsMarkup.getMarkup()) {
      if (!StringUtils.isBlank(markup)) {
        Theme theme = ClientProperties.getInstance(FacesContext.getCurrentInstance().getViewRoot()).getTheme();
        if (theme.getRenderersConfig().isMarkupSupported(rendererName, markup)) {
          addMarkupClass(rendererName, sub, markup);
        } else if (!"none".equals(markup)) {
          LOG.warn("Ignoring unknown markup='" + markup + "' for rendererName='" + rendererName + "'");
        }
      }
    }
  }

  public void addAspectClass(String renderer, Aspect aspect) {
    classes.add(nameOfAspectClass(renderer, aspect));
  }

  public void removeAspectClass(String renderer, Aspect aspect) {
    classes.remove(nameOfAspectClass(renderer, aspect));
  }

  private String nameOfAspectClass(String renderer, Aspect aspect) {
    StringBuilder builder = new StringBuilder(PREFIX);
    builder.append(renderer);
    builder.append(aspect);
    return builder.toString();
  }


  public void addAspectClass(String renderer, String sub, Aspect aspect) {
    classes.add(nameOfAspectClass(renderer, sub, aspect));
  }

  public void removeAspectClass(String renderer, String sub, Aspect aspect) {
    classes.remove(nameOfAspectClass(renderer, sub, aspect));
  }

  private String nameOfAspectClass(String renderer, String sub, Aspect aspect) {
    StringBuilder builder = new StringBuilder(PREFIX);
    builder.append(renderer);
    builder.append(SEPERATOR);
    builder.append(sub);
    builder.append(aspect);
    return builder.toString();
  }

  public void addClasses(StyleClasses styleClasses) {
    for (String clazz : (Iterable<String>) styleClasses.classes) {
      classes.add(clazz);
    }
  }

  public void removeClass(String clazz) {
    classes.remove(clazz);
  }

  public void removeTobagoClasses(String rendererName) {
    for (Iterator i = classes.iterator(); i.hasNext();) {
      String clazz = (String) i.next();
      if (clazz.startsWith(PREFIX + rendererName)) {
        i.remove();
      }
    }
  }

  public void updateClassAttributeAndMarkup(UIComponent component, String rendererName) {
    updateClassAttribute(component, rendererName);
    addMarkupClass(component, rendererName);
  }

  public void updateClassAttribute(UIComponent component, String rendererName) {
    // first remove old tobago-<rendererName>-<type> classes from class-attribute
    removeTobagoClasses(rendererName);

    addAspectClass(rendererName, Aspect.DEFAULT);
    if (ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      addAspectClass(rendererName, Aspect.DISABLED);
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_READONLY)) {
      addAspectClass(rendererName, Aspect.READONLY);
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {
      addAspectClass(rendererName, Aspect.INLINE);
    }
    FacesMessage.Severity severity = ComponentUtil.getMaximumSeverity(component);
    if (severity != null) {
      if (severity.equals(FacesMessage.SEVERITY_FATAL)) {
        addMarkupClass(rendererName, "fatal");
        addAspectClass(rendererName, Aspect.ERROR);
      } else if (severity.equals(FacesMessage.SEVERITY_ERROR)) {
        addMarkupClass(rendererName, "error");
        addAspectClass(rendererName, Aspect.ERROR);
      } else if (severity.equals(FacesMessage.SEVERITY_WARN)) {
        addMarkupClass(rendererName, "warn");
      } else if (severity.equals(FacesMessage.SEVERITY_INFO)) {
        addMarkupClass(rendererName, "info");
      } else {
        assert false : "Ordinal constants may be wrong";
      }
    }
    if (component instanceof UIInput) {
      UIInput input = (UIInput) component;
      if (input.isRequired()) {
        addAspectClass(rendererName, Aspect.REQUIRED);
      }
    }
  }

  public boolean isEmpty() {
    return classes.isEmpty();
  }

  @Override
  public String toString() {
    if (classes.isEmpty()) {
      return null;
    }
    StringBuilder buffer = new StringBuilder(32);
    for (Iterator i = classes.iterator(); i.hasNext();) {
      String clazz = (String) i.next();
      buffer.append(clazz);
      if (i.hasNext()) {
        buffer.append(' ');
      }
    }
    return buffer.toString();
  }

  public enum Aspect {

    DEFAULT,
    DISABLED,
    READONLY,
    INLINE,
    ERROR,
    REQUIRED;

    private String aspect;

    Aspect() {
      aspect = '-' + name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String toString() {
      return aspect;
    }
  }
}
