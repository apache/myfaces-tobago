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

package org.apache.myfaces.tobago.context;

import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.application.ProjectStage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.StringTokenizer;

public final class UserAgent implements Serializable {

  public static final String DEFAULT_NAME = "standard";

  public static final UserAgent DEFAULT = new UserAgent(null, null);

  public static final UserAgent MSIE = new UserAgent("msie", null);

  /**
   * @deprecated no longer supported
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent MSIE_5_0 = new UserAgent("msie", "5_0");

  /**
   * @deprecated no longer supported
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent MSIE_5_5 = new UserAgent("msie", "5_5");

  public static final UserAgent MSIE_6_0 = new UserAgent(
      "msie", "6_0", EnumSet.noneOf(Capability.class), CspHeader.NOT_SUPPORTED, CsproHeader.NOT_SUPPORTED);

  public static final UserAgent MSIE_7_0 = new UserAgent(
      "msie", "7_0", EnumSet.noneOf(Capability.class), CspHeader.NOT_SUPPORTED, CsproHeader.NOT_SUPPORTED);

  public static final UserAgent MSIE_7_0_COMPAT = new UserAgent(
      "msie", "7_0", EnumSet.of(Capability.IE_COMPATIBILITY_MODE), CspHeader.NOT_SUPPORTED, CsproHeader.NOT_SUPPORTED);

  /**
   * @deprecated no longer supported. Misspelled. Use {@link #MSIE_7_0}
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent MSIE_7_O = MSIE_7_0;

  public static final UserAgent MSIE_8_0 = new UserAgent(
      "msie", "8_0", EnumSet.of(Capability.CONTENT_TYPE_XHTML), CspHeader.NOT_SUPPORTED, CsproHeader.NOT_SUPPORTED);

  public static final UserAgent MSIE_9_0 = new UserAgent(
      "msie", "9_0", EnumSet.of(Capability.CONTENT_TYPE_XHTML), CspHeader.NOT_SUPPORTED, CsproHeader.NOT_SUPPORTED);

  // CSP is not fully supported, only sandboxing
  public static final UserAgent MSIE_10_0 = new UserAgent(
      "msie", "10_0", EnumSet.of(Capability.CONTENT_TYPE_XHTML), CspHeader.X, CsproHeader.X);

  // CSP is not fully supported, only sandboxing
  public static final UserAgent MSIE_11_0 = new UserAgent(
      "msie", "11_0", EnumSet.of(Capability.CONTENT_TYPE_XHTML), CspHeader.X, CsproHeader.X);

  /**
   * @deprecated no longer supported
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent MSIE_5_0_MAC = new UserAgent("msie", "5_0_mac");

  /**
   * @deprecated no longer supported
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent MSIE_6_0_MAC = new UserAgent("msie", "6_0_mac");

  /**
   * e.g. Opera 10
   */
  public static final UserAgent PRESTO = new UserAgent("presto", null, EnumSet.of(Capability.CONTENT_TYPE_XHTML));

  /**
   * @deprecated no longer supported. Please use {@link #PRESTO}.
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent OPERA = new UserAgent("opera", null);

  /**
   * @deprecated no longer supported. Please use {@link #PRESTO}.
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent OPERA_5_0 = new UserAgent("opera", "5_0");

  /**
   * @deprecated no longer supported. Please use {@link #PRESTO}.
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent OPERA_6_0 = new UserAgent("opera", "6_0");

  /**
   * @deprecated no longer supported. Please use {@link #PRESTO}.
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent OPERA_7_11 = new UserAgent("opera", "7_11");

  /**
   * @deprecated no longer supported
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent MOZILLA = new UserAgent("mozilla", null);

  /**
   * @deprecated no longer supported
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent MOZILLA_4_7 = new UserAgent("mozilla", "4_7");

  /**
   * @deprecated no longer supported
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent MOZILLA_5_0 = new UserAgent("mozilla", "5_0");

  /**
   * @deprecated no longer supported
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static final UserAgent MOZILLA_5_0_R1_6 = new UserAgent("mozilla", "5_0_r1_6");

  /**
   * e.g. Firefox
   */
  public static final UserAgent GECKO = new UserAgent("gecko", null, EnumSet.of(Capability.CONTENT_TYPE_XHTML));

  /**
   * e.g. Firefox 2.0
   */
  public static final UserAgent GECKO_1_8 = new UserAgent("gecko", "1_8", EnumSet.of(Capability.CONTENT_TYPE_XHTML),
      CspHeader.NOT_SUPPORTED, CsproHeader.NOT_SUPPORTED);

  /**
   * e.g. Firefox 3.0, 3.5, 3.6
   */
  public static final UserAgent GECKO_1_9 = new UserAgent("gecko", "1_9", EnumSet.of(Capability.CONTENT_TYPE_XHTML),
      CspHeader.NOT_SUPPORTED, CsproHeader.NOT_SUPPORTED);

  /**
   * e.g. Firefox 4 to 22
   */
  public static final UserAgent GECKO_2_0
      = new UserAgent("gecko", null,
      EnumSet.of(Capability.PLACEHOLDER, Capability.CONTENT_TYPE_XHTML), CspHeader.X, CsproHeader.X);

  /**
   * e.g. Firefox 23 or higher
   */
  public static final UserAgent GECKO_23_0
      = new UserAgent("gecko", null,
      EnumSet.of(Capability.PLACEHOLDER, Capability.CONTENT_TYPE_XHTML), CspHeader.STANDARD, CsproHeader.STANDARD);

  /**
   * e.g. Safari 4, Safari 5, Chrome
   */
  public static final UserAgent WEBKIT
      = new UserAgent("webkit", null,
      EnumSet.of(Capability.PLACEHOLDER, Capability.CONTENT_TYPE_XHTML), CspHeader.WEBKIT, CsproHeader.WEBKIT);

  private static final long serialVersionUID = 2L;

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final String name;

  /**
   * @deprecated Version shouldn't be used in the future. Use capability instead, even better
   * use same code on the server when possible and use capability via JavaScript.
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  private final String version;

  private final EnumSet<Capability> capabilities;

  private final CspHeader cspHeader;

  private final CsproHeader csproHeader;

  private UserAgent(final String name, final String version) {
    this(name, version, EnumSet.of(Capability.CONTENT_TYPE_XHTML));
  }

  private UserAgent(final String name, final String version, final EnumSet<Capability> capabilities) {
    this(name, version, capabilities, CspHeader.STANDARD, CsproHeader.STANDARD);
  }

  private UserAgent(
      final String name, final String version, final EnumSet<Capability> capabilities, final CspHeader cspHeader,
      final CsproHeader csproHeader) {
    this.name = name;
    this.version = version;
    this.capabilities = capabilities;
    this.cspHeader = cspHeader;
    this.csproHeader = csproHeader;
  }

  public boolean hasCapability(final Capability capability) {
    return capabilities.contains(capability);
  }

  public boolean isMsie() {
    return MSIE.name.equals(name);
  }

  /**
   * @deprecated no longer supported
   */
  @Deprecated(since = "3.0.0", forRemoval = true)
  public boolean isMsie6() {
    return MSIE_6_0.name.equals(name) && MSIE_6_0.version.equals(version);
  }

  /**
   * @deprecated no longer supported.
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public boolean isMozilla() {
    return MOZILLA.name.equals(name);
  }

  public List<String> getFallbackList() {
    return getFallbackList(false);
  }

  private List<String> getFallbackList(final boolean reverseOrder) {
    final List<String> list = new ArrayList<>(3);
    if (version != null) {
      list.add(name + '_' + version);
    }
    if (name != null) {
      list.add(name);
    }
    list.add(DEFAULT_NAME);
    if (reverseOrder) {
      Collections.reverse(list);
    }
    return list;
  }

  /**
   * @return The HTTP header names for Content-Security-Policy.
   */
  public String[] getCspHeaders() {
    return cspHeader.getNames();
  }

  /**
   * @return The HTTP header name for Content-Security-Policy-Report-Only.
   */
  public String[] getCspReportOnlyHeaders() {
    return csproHeader.getNames();
  }

  public static UserAgent getInstance(final String header) {
    if (header == null) {
      return DEFAULT;
    }

    if (header.contains("MSIE") || header.contains("Trident")) {
      if (header.contains("MSIE 6.0")) {
        return MSIE_6_0;
      } else if (header.contains("MSIE 7.0")) {
        if (header.contains("Trident")) {
          return MSIE_7_0_COMPAT;
        } else {
          return MSIE_7_0;
        }
      } else if (header.contains("MSIE 8.0")) {
        return MSIE_8_0;
      } else if (header.contains("MSIE 9.0")) {
        return MSIE_9_0;
      } else if (header.contains("MSIE 10.0")) {
        return MSIE_10_0;
      } else if (header.contains("rv:11")) {
        return MSIE_11_0;
      } else {
        return MSIE;
      }
    } else if (header.contains("AppleWebKit")) {
      return WEBKIT;
    } else if (header.contains("Gecko")) {
      if (header.contains("rv:1.8")) {
        return GECKO_1_8;
      } else if (header.contains("rv:1.9")) {
        return GECKO_1_9;
      } else {
        final int index = header.indexOf("rv:");
        final StringTokenizer tokenizer = new StringTokenizer(header.substring(index + 3), " .");
        final String versionString = tokenizer.nextToken();
        try {
          final int version = Integer.parseInt(versionString);
          if (version >= 23) {
            return GECKO_23_0;
          } else if (version >= 2) {
            return GECKO_2_0;
          }
        } catch (final NumberFormatException e) {
          if (LOG.isDebugEnabled()) {
            LOG.debug(header, e);
          }
        }
        return GECKO;
      }
    } else if (header.contains("Presto")) {
      return PRESTO;
    }

    return DEFAULT;
  }

  /**
   * @deprecated no longer supported
   */
  @Deprecated(since = "1.5.0", forRemoval = true)
  public static UserAgent getInstanceForId(final String id) {
    if (!FacesContext.getCurrentInstance().isProjectStage(ProjectStage.Production)) {
      Deprecation.LOG.error("Getting the user agent from its id is no longer supported! id='" + id + "'");
    }
    return DEFAULT;
  }

  /**
   * @deprecated don't use toString() functionality, but for logging!
   */
  @Deprecated(since = "2.0.0")
  public String toString() {
    return version != null
        ? name + '_' + version
        : name;
  }

  private enum CspHeader {

    NOT_SUPPORTED(new String[]{}),
    X(new String[]{"Content-Security-Policy", "X-Content-Security-Policy"}),
    WEBKIT(new String[]{"Content-Security-Policy", "X-WebKit-CSP"}),
    STANDARD(new String[]{"Content-Security-Policy"});

    private String[] names;

    CspHeader(final String[] names) {
      this.names = names;
    }

    public String[] getNames() {
      return names;
    }
  }

  private enum CsproHeader {

    NOT_SUPPORTED(new String[]{}),
    X(new String[]{"Content-Security-Policy-Report-Only", "X-Content-Security-Policy-Report-Only"}),
    WEBKIT(new String[]{"Content-Security-Policy-Report-Only", "X-WebKit-CSP-Report-Only"}),
    STANDARD(new String[]{"Content-Security-Policy-Report-Only"});

    private String[] names;

    CsproHeader(final String[] names) {
      this.names = names;
    }

    public String[] getNames() {
      return names;
    }
  }
}
