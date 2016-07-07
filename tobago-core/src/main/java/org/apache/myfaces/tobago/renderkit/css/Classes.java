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

package org.apache.myfaces.tobago.renderkit.css;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p>
 * Builds the CSS class attribute of tags. The names will be generated in a formal way, so generic name (and
 * abbreviation) are possible. The class works like a factory, so caching will be possible.
 * </p>
 * <p>
 * The default naming conventions allow these values:
 * </p>
 * <ul>
 *   <li>tobago-&lt;rendererName&gt;
 *   <li>tobago-&lt;rendererName&gt;-markup-&lt;markupName&gt;
 *   <li>tobago-&lt;rendererName&gt;-&lt;subElement&gt;
 *   <li>tobago-&lt;rendererName&gt;-&lt;subElement&gt;-markup-&lt;markupName&gt;
 * </ul>
 * <br>
 * where <ul> <li>&lt;rendererName&gt;, &lt;subElement&gt; and &lt;markupName&gt;
 * must only contain ASCII-chars and -numbers</li>
 * <li>&lt;rendererName&gt; is the rendererType with a lower case char as first char</li> <li>&lt;subElement&gt; is a
 * sub element of the main tag in the output language (e.g. HTML)</li> <li>&lt;markupName&gt; is the name of an existing
 * markup</li> </ul> If the markup contains more than one name, there will be generated more than one output string.
 * E.g.: UIIn with Markup [readonly, error] will get the class "tobago-in tobago-in-markup-readonly
 * tobago-in-markup-error".
 *
 * @deprecated since Tobago 3.0.0
 */
@Deprecated
public final class Classes implements CssItem {

  private static final Logger LOG = LoggerFactory.getLogger(Classes.class);

  /* With cache it seems to be 10 times faster */
  private static final MultiKeyMap CACHE = new MultiKeyMap();

  private final String stringValue;

  public static Classes create(final UIComponent component) {
    return create(component, true, null, null, false);
  }

  public static Classes create(final UIComponent component, final String sub) {
    return create(component, true, sub, null, false);
  }

  public static Classes create(final UIComponent component, final Markup explicit) {
    return create(component, true, null, explicit, false);
  }

  // TODO: clean up..., hope this is no longer needed...
  public static Classes create(final UIComponent component, final String sub, final Markup explicit) {
    return create(component, false, sub, explicit, false);
  }

  // XXX optimize synchronized
  private static synchronized Classes create(
      final UIComponent component, final boolean markupFromComponent, final String sub,
      final Markup explicit, final boolean ignoreCheck) {
    final String rendererName = StringUtils.uncapitalize(component.getRendererType());

    Markup markup;
    if (markupFromComponent) {
      final Visual visual = (Visual) component;
      markup = ComponentUtils.updateMarkup(component, visual.getMarkup());
      if (explicit != null) {
        markup = explicit.add(markup);
      }
    } else {
      markup = explicit;
    }

//    Classes value = (Classes) CACHE.get(rendererName, markup, sub);
//    if (value == null) {
    final Classes value;
    value = new Classes(rendererName, markup, sub, ignoreCheck);
//      CACHE.put(rendererName, markup, sub, value);
//      if (LOG.isDebugEnabled()) {
//        LOG.debug("Element added (size={}) to cache (renderName='{}', markup='{}', sub='{}')",
//            CACHE.size(), rendererName, markup, sub);
//      }
//    }
    return value;
  }
  private Classes(final String rendererName, final Markup markup, final String sub, final boolean ignoreMarkupCheck) {

    assert sub == null || StringUtils.isAlphanumeric(sub) : "Invalid sub element name: '" + sub + "'";

    // These values are statistically tested length of the html class attribute
    final StringBuilder builder = new StringBuilder(markup != null ? 80 : 32);
    builder.append("tobago-");
    builder.append(rendererName);
    if (sub != null) {
      builder.append('-');
      builder.append(sub);
    }
    if (markup != null) {
      final Theme theme = ClientProperties.getInstance(FacesContext.getCurrentInstance()).getTheme();
      for (final String markupString : markup) {
        if (ignoreMarkupCheck || theme.getRenderersConfig().isMarkupSupported(rendererName, markupString)) {
          builder.append(' ');
          builder.append("tobago-");
          builder.append(rendererName);
          if (sub != null) {
            builder.append('-');
            builder.append(sub);
          }
          builder.append("-markup-");
          builder.append(markupString);
        } else if ("none".equals(markupString)) {
          Deprecation.LOG.warn("Markup 'none' is deprecated, please use a NULL pointer instead.");
        } else {
          LOG.warn("Ignoring unknown markup='" + markupString + "' for rendererName='" + rendererName + "'");
        }
      }
    }
    this.stringValue = builder.toString();
  }

  public String getName() {
    return stringValue;
  }
}
