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

import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.EnumMap;
import java.util.regex.Pattern;

/**
 * @deprecated since 4.0.0. Please use {@link Icons} and render classic: "find usages Icons".
 */
@Deprecated
public class FontAwesomeIconEncoder implements IconEncoder {

  private static final Logger LOG = LoggerFactory.getLogger(FontAwesomeIconEncoder.class);

  /**
   * @deprecated 4.0.0. User {@link Icons#FA }
   */
  @Deprecated
  public static final CssItem FA = Icons.FA;

  private static final Pattern PATTERN = Pattern.compile("^(fa(-[a-z]+)+)$");


  private static final EnumMap<Icons, CssItem> ICONS;

  static {
    ICONS = new EnumMap<Icons, CssItem>(Icons.class);
    for (Icons icon : Icons.values()) {
      ICONS.put(icon, new FontAwesomeCssItem("fa-" + icon.name().toLowerCase().replaceAll("_", "-")));
    }
  }

  @Override
  public void encode(final TobagoResponseWriter writer, final Icons icon, final CssItem... cssItems)
      throws IOException {
    writer.startElement(HtmlElements.I);
    writer.writeClassAttribute(Icons.FA, cssItems, icon);
    writer.endElement(HtmlElements.I);
  }

  /**
   * @deprecated 4.0.0. {@link Icons} implements {@link CssItem}, so this method is no longer needed.
   */
  @Deprecated
  public static CssItem generateClass(final Icons icon) {
    if (icon == null) {
      return null;
    }
    CssItem result = ICONS.get(icon);
    if (result == null) {
      LOG.warn("Missing icon: '" + icon + "'");
    }
    return result;
  }

  /**
   * @deprecated 4.0.0. Use {@link Icons#custom }
   */
  @Deprecated
  public static CssItem generateClass(final String name) {
    return Icons.custom(name);
  }

  private static final class FontAwesomeCssItem implements CssItem {

    private String name;

    FontAwesomeCssItem(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }
  }
}
