package org.apache.myfaces.tobago.context;

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

import org.apache.myfaces.tobago.internal.util.Deprecation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class UserAgent implements Serializable {

  private static final long serialVersionUID = 2L;

  public static final String DEFAULT_NAME = "standard";

  public static final UserAgent DEFAULT = new UserAgent(null, null);


  public static final UserAgent MSIE = new UserAgent("msie", null);

  /**
   * @deprecated no longer supported, since Tobago 1.5
   */
  @Deprecated
  public static final UserAgent MSIE_5_0 = new UserAgent("msie", "5_0");

  /**
   * @deprecated no longer supported, since Tobago 1.5
   */
  @Deprecated
  public static final UserAgent MSIE_5_5 = new UserAgent("msie", "5_5");

  public static final UserAgent MSIE_6_0 = new UserAgent("msie", "6_0");

  public static final UserAgent MSIE_7_0 = new UserAgent("msie", "7_0");

  /**
   * @deprecated no longer supported, since Tobago 1.5. Misspelled. Use {@link #MSIE_7_0}
   */
  @Deprecated
  public static final UserAgent MSIE_7_O = MSIE_7_0;

  public static final UserAgent MSIE_8_0 = new UserAgent("msie", "8_0");

  public static final UserAgent MSIE_9_0 = new UserAgent("msie", "9_0", EnumSet.of(Capability.CONTENT_TYPE_XHTML));

  /**
   * @deprecated no longer supported, since Tobago 1.5
   */
  @Deprecated
  public static final UserAgent MSIE_5_0_MAC = new UserAgent("msie", "5_0_mac");

  /**
   * @deprecated no longer supported, since Tobago 1.5
   */
  @Deprecated
  public static final UserAgent MSIE_6_0_MAC = new UserAgent("msie", "6_0_mac");


  /**
   * e. g. Opera 10
   */
  public static final UserAgent PRESTO = new UserAgent("presto", null, EnumSet.of(Capability.CONTENT_TYPE_XHTML));

  /**
   * @deprecated no longer supported, since Tobago 1.5. Please use {@link #PRESTO}.
   */
  public static final UserAgent OPERA = new UserAgent("opera", null);

  /**
   * @deprecated no longer supported, since Tobago 1.5. Please use {@link #PRESTO}.
   */
  @Deprecated
  public static final UserAgent OPERA_5_0 = new UserAgent("opera", "5_0");

  /**
   * @deprecated no longer supported, since Tobago 1.5. Please use {@link #PRESTO}.
   */
  @Deprecated
  public static final UserAgent OPERA_6_0 = new UserAgent("opera", "6_0");

  /**
   * @deprecated no longer supported, since Tobago 1.5. Please use {@link #PRESTO}.
   */
  @Deprecated
  public static final UserAgent OPERA_7_11 = new UserAgent("opera", "7_11");

  /**
   * @deprecated no longer supported, since Tobago 1.5
   */
  @Deprecated
  public static final UserAgent MOZILLA = new UserAgent("mozilla", null);

  /**
   * @deprecated no longer supported, since Tobago 1.5
   */
  @Deprecated
  public static final UserAgent MOZILLA_4_7 = new UserAgent("mozilla", "4_7");

  /**
   * @deprecated no longer supported, since Tobago 1.5
   */
  @Deprecated
  public static final UserAgent MOZILLA_5_0 = new UserAgent("mozilla", "5_0");

  /**
   * @deprecated no longer supported, since Tobago 1.5
   */
  @Deprecated
  public static final UserAgent MOZILLA_5_0_R1_6 = new UserAgent("mozilla", "5_0_r1_6");

  /**
   * e. g. Firefox
   */
  public static final UserAgent GECKO = new UserAgent("gecko", null, EnumSet.of(Capability.CONTENT_TYPE_XHTML));

  /**
   * e. g. Firefox 2.0
   */
  public static final UserAgent GECKO_1_8 = new UserAgent("gecko", "1_8", EnumSet.of(Capability.CONTENT_TYPE_XHTML));

  /**
   * e. g. Firefox 3.0, 3.5, 3.6
   */
  public static final UserAgent GECKO_1_9 = new UserAgent("gecko", "1_9", EnumSet.of(Capability.CONTENT_TYPE_XHTML));

  /**
   * e. g. Firefox 4.0
   */
  public static final UserAgent GECKO_2_0
      = new UserAgent("gecko", "2_0", EnumSet.of(Capability.PLACEHOLDER, Capability.CONTENT_TYPE_XHTML));

  /**
   * e. g. Safari 4, Safari 5, Chrome
   */
  public static final UserAgent WEBKIT
      = new UserAgent("webkit", null, EnumSet.of(Capability.PLACEHOLDER, Capability.CONTENT_TYPE_XHTML));

  private final String name;

  private final String version;

  private final EnumSet<Capability> capabilities;

  private UserAgent(String name, String version) {
    this(name , version, EnumSet.of(Capability.CONTENT_TYPE_XHTML));
  }

  private UserAgent(String name, String version, EnumSet<Capability> capabilities) {
    this.name = name;
    this.version = version;
    this.capabilities = capabilities;
  }

  public boolean hasCapability(Capability capability) {
    return capabilities.contains(capability);
  }

  public boolean isMsie() {
    return MSIE.name.equals(name);
  }

  public boolean isMsie6() {
    return MSIE_6_0.name.equals(name) && MSIE_6_0.version.equals(version);
  }

  /**
   * @deprecated no longer supported, since Tobago 1.5
   */
  @Deprecated
  public boolean isMozilla() {
    return MOZILLA.name.equals(name);
  }

  public List<String> getFallbackList() {
    return getFallbackList(false);
  }

  private List<String> getFallbackList(boolean reverseOrder) {
    List<String> list = new ArrayList<String>(3);
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

  public static UserAgent getInstance(String header) {
    if (header == null) {
      return DEFAULT;
    }

    if (header.contains("MSIE")) {
      if (header.contains("MSIE 6.0")) {
        return MSIE_6_0;
      } else if (header.contains("MSIE 7.0")) {
        return MSIE_7_0;
      } else if (header.contains("MSIE 8.0")) {
        return MSIE_8_0;
      } else if (header.contains("MSIE 9.0")) {
        return MSIE_9_0;
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
      } else if (header.contains("rv:2.0")) {
        return GECKO_2_0;
      } else {
        return GECKO;
      }
    } else if (header.contains("Presto")) {
      return PRESTO;
    }

    return DEFAULT;
  }

  /**
   * @deprecated no longer supported, since Tobago 1.5
   */
  @Deprecated
  public static UserAgent getInstanceForId(String id) {
    Deprecation.LOG.error("Getting the user agent from its id is no longer supported! id='" + id + "'");
    return DEFAULT;
  }

  /**
   * @deprecated don't use toString() functionality, but for logging!
   */
  @Deprecated
  public String toString() {
    return version != null
        ? name + '_' + version
        : name;
  }
}
