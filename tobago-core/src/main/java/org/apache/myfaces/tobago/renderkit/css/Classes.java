package org.apache.myfaces.tobago.renderkit.css;

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

import org.apache.commons.collections.map.MultiKeyMap;
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
import java.util.EnumSet;
import java.util.Set;

/**
 * Builds the CSS class attribute of tags.
 * The names will be generated in a formal way, so generic name (and abbrevation) are possible.
 * The class works like a factory, so caching will be possible.
 */
public final class Classes {

  private static final Logger LOG = LoggerFactory.getLogger(Classes.class);

  private static final MultiKeyMap CACHE = new MultiKeyMap();

  private final String stringValue;

  public static Classes simple(UIComponent component, String sub) {
    return create(component, false, null, sub);
  }

  public static Classes full(UIComponent component) {
    return create(component, true, null, null);
  }

  public static Classes full(UIComponent component, String sub) {
    return create(component, true, null, sub);
  }

  public static Classes full(UIComponent component, String[] subs) {
    return create(component, true, subs, null);
  }

  private static Classes create(UIComponent component, boolean full, String[] subs, String sub) {
    final Object s = sub != null ? sub : subs;
    final String rendererName = StringUtils.uncapitalize(component.getRendererType());
    final Set<Aspect> aspects = full ? evaluateAspects(component) : null;
    final Markup markup = full ? ((SupportsMarkup) component).getMarkup() : null;
    Classes value = (Classes) CACHE.get(rendererName, aspects, markup, s);
    if (value == null) {
      value = new Classes(rendererName, aspects, markup, subs, sub);
      CACHE.put(rendererName, aspects, markup, s, value);
      LOG.info("Classes cache size = " + CACHE.size());
    }
    return value;
  }

  private Classes(String rendererName, Set<Aspect> aspects, Markup markup, String[] subs, String sub) {
    this.stringValue = encode(rendererName, aspects, markup, subs, sub);
  }

  private static Set<Aspect> evaluateAspects(UIComponent component) {
    Set<Aspect> aspects = EnumSet.noneOf(Aspect.class);
    aspects.add(Aspect.DEFAULT);
    if (ComponentUtils.getBooleanAttribute(component, Attributes.DISABLED)) {
      aspects.add(Aspect.DISABLED);
    }
    if (ComponentUtils.getBooleanAttribute(component, Attributes.READONLY)) {
      aspects.add(Aspect.READONLY);
    }
    if (ComponentUtils.getBooleanAttribute(component, Attributes.INLINE)) {
      aspects.add(Aspect.INLINE);
    }
    if (component instanceof UIInput) {
      UIInput input = (UIInput) component;
      if (ComponentUtils.isError(input)) {
        aspects.add(Aspect.ERROR);
      }
      if (input.isRequired()) {
        aspects.add(Aspect.REQUIRED);
      }
    }
    return aspects;
  }

  private String encode(String rendererName, Set<Aspect> aspects, Markup markup, String[] subs, String sub) {
    StringBuilder builder = new StringBuilder();
    if (aspects != null) {
      for (Aspect aspect : aspects) {
        builder.append("tobago-");
        builder.append(rendererName);
        builder.append(aspect.getClassSuffix());
        builder.append(' ');
      }
    }
    if (markup != null) {
      for (String markupString : markup) {
        Theme theme = VariableResolverUtils.resolveClientProperties(FacesContext.getCurrentInstance()).getTheme();
        if (theme.getRenderersConfig().isMarkupSupported(rendererName, markupString)) {
          builder.append("tobago-");
          builder.append(rendererName);
          builder.append("-markup-");
          builder.append(markupString);
          builder.append(' ');
        } else if ("none".equals(markupString)) {
          Deprecation.LOG.warn("Markup 'none' is deprecated, please use a NULL pointer instead.");
        } else {
          LOG.warn("Unknown markup='" + markupString + "'");
        }
      }

    }
    if (subs != null) {
      for (String subComponent : subs) {
        builder.append("tobago-");
        builder.append(rendererName);
        builder.append("-");
        builder.append(subComponent);
        builder.append(' ');
      }
    }
    if (sub != null) {
      builder.append("tobago-");
      builder.append(rendererName);
      builder.append("-");
      builder.append(sub);
      builder.append(' ');
    }
    if (builder.length() > 0) {
      builder.setLength(builder.length() - 1);
    }
    return builder.toString();
  }

  public String getStringValue() {
    return stringValue;
  }
}
