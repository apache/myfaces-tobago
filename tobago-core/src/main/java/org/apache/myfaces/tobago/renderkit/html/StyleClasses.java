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
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * @deprecated since Tobago 1.5.0. Please use {@link org.apache.myfaces.tobago.renderkit.css.Classes}.
 */
@Deprecated
public class StyleClasses implements Serializable {

  private static final long serialVersionUID = 3738052927067803517L;

  private static final Logger LOG = LoggerFactory.getLogger(StyleClasses.class);

  public static final char SEPARATOR = '-';
  public static final String PREFIX = "tobago" + SEPARATOR;
  public static final String MARKUP = SEPARATOR + "markup" + SEPARATOR;

  private ListOrderedSet classes;

  public StyleClasses() {
    classes = new ListOrderedSet();
  }

  /**
   * Creates a StyleClasses element and adds one entry for a sub-component with the name of the renderer.
   * E. g.: UITreeNode + "icon" -> tobago-treeNode-icon
   */
  public StyleClasses(UIComponent component, String sub) {
    this();
    addClass(StringUtils.uncapitalize(component.getRendererType()), sub);
  }

  private StyleClasses(StyleClasses base) {
    this();
    classes.addAll(base.classes);
  }

  public static StyleClasses ensureStyleClasses(UIComponent component) {
    Map attributes = component.getAttributes();
    StyleClasses classes = (StyleClasses) attributes.get(Attributes.STYLE_CLASS);
    if (classes == null) {
      classes = new StyleClasses();
      attributes.put(Attributes.STYLE_CLASS, classes);
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

  public boolean isEmpty() {
    return classes.isEmpty();
  }

  private String nameOfClass(String renderer, String sub) {
    StringBuilder builder = new StringBuilder(PREFIX);
    builder.append(renderer);
    builder.append(SEPARATOR);
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
      builder.append(SEPARATOR);
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
    Markup m = supportsMarkup.getMarkup();
    if (m != null) {
      for (String markup : m) {
        Theme theme = VariableResolverUtils.resolveClientProperties(FacesContext.getCurrentInstance()).getTheme();
        if (theme.getRenderersConfig().isMarkupSupported(rendererName, markup)) {
          addMarkupClass(rendererName, sub, markup);
        } else if ("none".equals(markup)) {
          Deprecation.LOG.warn("Markup 'none' is deprecated, please use a NULL pointer instead.");
        } else {
          LOG.warn("Unknown markup='" + markup + "'");
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
    builder.append(SEPARATOR);
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
    if (ComponentUtils.getBooleanAttribute(component, Attributes.DISABLED)) {
      addAspectClass(rendererName, Aspect.DISABLED);
    }
    if (ComponentUtils.getBooleanAttribute(component, Attributes.READONLY)) {
      addAspectClass(rendererName, Aspect.READONLY);
    }
    if (ComponentUtils.getBooleanAttribute(component, Attributes.INLINE)) {
      addAspectClass(rendererName, Aspect.INLINE);
    }
    if (component instanceof UIInput) {
      UIInput input = (UIInput) component;
      if (ComponentUtils.isError(input)) {
        addAspectClass(rendererName, Aspect.ERROR);
      }
      if (input.isRequired()) {
        addAspectClass(rendererName, Aspect.REQUIRED);
      }
    }
  }

  @Override
  public String toString() {
    if (classes.isEmpty()) {
      return null;
    }
    StringBuilder buffer = new StringBuilder(16 * classes.size());
    for (Iterator i = classes.iterator(); i.hasNext();) {
      String clazz = (String) i.next();
      buffer.append(clazz);
      if (i.hasNext()) {
        buffer.append(' ');
      }
    }
    return buffer.toString();
  }

  /**
   * @deprecated since Tobago 1.5.0. Please use {@link org.apache.myfaces.tobago.renderkit.css.Aspect}.
   */
  @Deprecated
  public enum Aspect {

    DEFAULT,
    DISABLED,
    READONLY,
    INLINE,
    ERROR,
    REQUIRED;

    private String aspect;

    Aspect() {
      aspect = name().equals("DEFAULT") ? "" : '-' + name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public String toString() {
      return aspect;
    }
  }
}
