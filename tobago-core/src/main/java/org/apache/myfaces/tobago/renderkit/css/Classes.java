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
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Builds the CSS class attribute of tags.
 * The names will be generated in a formal way, so generic name (and abbrevation) are possible.
 * The class works like a factory, so caching will be possible.
 * <p/>
 * The default naming conventions allow these values:<br/>
 *
 * <ul>
 * <li>tobago-&lt;rendererName></li>
 * <li>tobago-&lt;rendererName>-markup-&lt;markupName></li>
 * <li>tobago-&lt;rendererName>-&lt;subElement></li>
 * <li>tobago-&lt;rendererName>-&lt;subElement>-markup-&lt;markupName></li>
 * </ul>
 *
 * where
 * <ul>
 * <li>&lt;rendererName>, &lt;subElement> and &lt;markupName> must only contain ASCII-chars and -numbers</li>
 * <li>&lt;rendererName> is the rendererType with a lower case char as first char</li>
 * <li>&lt;subElement> is a sub element of the main tag in the output language (e.g. HTML)</li>
 * <li>&lt;markupName> is the name of an existing markup</li>
 * </ul>
 * If the markup contains more than one name, there will be generated more than one output string.
 * E.g.: UIIn with Markup [readonly, error] will get the class
 * "tobago-in tobago-in-markup-readonly tobago-in-markup-error".
 *
 */
public final class Classes {

  private static final Logger LOG = LoggerFactory.getLogger(Classes.class);

  private static final MultiKeyMap CACHE = new MultiKeyMap();

  private final String stringValue;

  public static Classes create(UIComponent component) {
    return create(component, true, null, null, false);
  }

  public static Classes create(UIComponent component, String sub) {
    return create(component, true, sub, null, false);
  }

  public static Classes create(UIComponent component, Markup explicit) {
    return create(component, false, null, explicit, false);
  }

  public static Classes create(UIComponent component, String sub, Markup explicit) {
    return create(component, false, sub, explicit, false);
  }

  /**
   * Workaround to enforce unregistered markups. (May be removed after finding a better solution)
   * A solution can be: using a specific renderer to render e. g. the button in the sheet.
   * @deprecated
   */
  @Deprecated
  public static Classes createIgnoreCheck(UIComponent component, String sub, Markup explicit) {
    return create(component, false, sub, explicit, true);
  }

  private static Classes create(
      UIComponent component, boolean markupFromComponent, String sub, Markup explicit, boolean ignoreCheck) {
    final String rendererName = StringUtils.uncapitalize(component.getRendererType());
    final Markup markup = markupFromComponent ? ((SupportsMarkup) component).getCurrentMarkup() : explicit;
    Classes value = (Classes) CACHE.get(rendererName, markup, sub);
    if (value == null) {
      value = new Classes(rendererName, markup, sub, ignoreCheck);
      CACHE.put(rendererName, markup, sub, value);
      LOG.info("Classes cache size = " + CACHE.size());
    }
    return value;
  }

  private Classes(String rendererName, Markup markup, String sub, boolean ignoreCheck) {

    if (sub != null && !StringUtils.isAlphanumeric(sub)) {
      throw new IllegalArgumentException("Invalid sub element name: '" + sub + "'");
    }

    StringBuilder builder = new StringBuilder();
    builder.append("tobago-");
    builder.append(rendererName);
    if (sub != null) {
      builder.append('-');
      builder.append(sub);
    }
    builder.append(' ');
    if (markup != null) {
      for (String markupString : markup) {
        Theme theme = VariableResolverUtils.resolveClientProperties(FacesContext.getCurrentInstance()).getTheme();
        if (ignoreCheck || theme.getRenderersConfig().isMarkupSupported(rendererName, markupString)) {
          builder.append("tobago-");
          builder.append(rendererName);
          if (sub != null) {
            builder.append('-');
            builder.append(sub);
          }
          builder.append("-markup-");
          builder.append(markupString);
          builder.append(' ');
        } else if ("none".equals(markupString)) {
          Deprecation.LOG.warn("Markup 'none' is deprecated, please use a NULL pointer instead.");
        } else {
          LOG.warn("Ignoring unknown markup='" + markupString + "' for rendererName='" + rendererName + "'");
        }
      }
    }
    if (builder.length() > 0) {
      builder.setLength(builder.length() - 1);
    }
    this.stringValue = builder.toString();
  }

  public String getStringValue() {
    return stringValue;
  }

  /** @deprecated This workaround will be removed later */
  @Deprecated
  public static String required(UIComponent component) {
    final String rendererName = StringUtils.uncapitalize(component.getRendererType());
    return "tobago-" + rendererName + "-markup-required";
  }
}
