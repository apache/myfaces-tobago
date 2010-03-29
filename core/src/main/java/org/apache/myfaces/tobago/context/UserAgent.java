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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class UserAgent implements Serializable {

  private static final long serialVersionUID = 2L;

  public static final String DEFAULT_NAME = "standard";

  public static final UserAgent DEFAULT = new UserAgent(null, null);


  public static final UserAgent MSIE = new UserAgent("msie", null);

  /** @deprecated no longer supported, since Tobago 1.5 */
  @Deprecated
  public static final UserAgent MSIE_5_0 = new UserAgent("msie", "5_0");

  /** @deprecated no longer supported, since Tobago 1.5 */
  @Deprecated
  public static final UserAgent MSIE_5_5 = new UserAgent("msie", "5_5");

  public static final UserAgent MSIE_6_0 = new UserAgent("msie", "6_0");

  public static final UserAgent MSIE_7_0 = new UserAgent("msie", "7_0");

  /** @deprecated no longer supported, since Tobago 1.5. Misspelled */
  @Deprecated
  public static final UserAgent MSIE_7_O = MSIE_7_0;

  public static final UserAgent MSIE_8_0 = new UserAgent("msie", "8_0");

  /** @deprecated no longer supported, since Tobago 1.5 */
  @Deprecated
  public static final UserAgent MSIE_5_0_MAC = new UserAgent("msie", "5_0_mac");

  /** @deprecated no longer supported, since Tobago 1.5 */
  @Deprecated
  public static final UserAgent MSIE_6_0_MAC = new UserAgent("msie", "6_0_mac");


  public static final UserAgent OPERA = new UserAgent("opera", null);

  public static final UserAgent OPERA_PRESTO = new UserAgent("opera", "Presto", Capability.PLACEHOLDER);

  /** @deprecated no longer supported, since Tobago 1.5 */
  @Deprecated
  public static final UserAgent OPERA_5_0 = new UserAgent("opera", "5_0");

  /** @deprecated no longer supported, since Tobago 1.5 */
  @Deprecated
  public static final UserAgent OPERA_6_0 = new UserAgent("opera", "6_0");

  /** @deprecated no longer supported, since Tobago 1.5 */
  @Deprecated
  public static final UserAgent OPERA_7_11 = new UserAgent("opera", "7_11");


  public static final UserAgent MOZILLA = new UserAgent("mozilla", null);

  /** @deprecated no longer supported, since Tobago 1.5 */
  @Deprecated
  public static final UserAgent MOZILLA_4_7 = new UserAgent("mozilla", "4_7");

  /** @deprecated no longer supported, since Tobago 1.5 */
  @Deprecated
  public static final UserAgent MOZILLA_5_0 = new UserAgent("mozilla", "5_0");

  /** @deprecated no longer supported, since Tobago 1.5 */
  @Deprecated
  public static final UserAgent MOZILLA_5_0_R1_6 = new UserAgent("mozilla", "5_0_r1_6");

  public static final UserAgent GECKO = new UserAgent("gecko", null);

  public static final UserAgent WEBKIT = new UserAgent("webkit", null);

  private String name;

  private String version;

  private Set<Capability> capabilities = new HashSet<Capability>();

  private UserAgent(String name, String version, Capability... capabilities) {
    this.name = name;
    this.version = version;
    this.capabilities.addAll(Arrays.asList(capabilities));
  }

  public boolean hasCapability(Capability capability) {
     return capabilities.contains(capability);
  }

  public boolean isMsie() {
    return MSIE.name.equals(name);
  }

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

    // remove .replace('/', ' ')
    header = header.toLowerCase(Locale.ENGLISH).replace('/', ' ');
    if (header.contains("opera")) {
      if (header.contains("opera 5.0")) {
        return OPERA_5_0;
      } else if (header.contains("opera 6.0")) {
        return OPERA_6_0;
      } else if (header.contains("opera 7.11")) {
        return OPERA_7_11;
      } else if (header.contains("presto")) {
        return OPERA_PRESTO;
      } else {
        return OPERA;
      }
    } else if (header.indexOf("chrome") > -1) {
      /*if (header.indexOf("chrome 5.0") > -1) {
        return WEBKIT_5_0;
      } else {*/
        return WEBKIT;
      //}
    } else if (header.contains("msie")) {
      if (header.contains("msie 5.0")) {
        if (header.contains("mac")) {
          return MSIE_5_0_MAC;
        } else {
          return MSIE_5_0;
        }
      } else if (header.contains("msie 5.5")) {
        return MSIE_5_5;
      } else if (header.contains("msie 6.0")) {
        if (header.contains("mac")) {
          return MSIE_6_0_MAC;
        } else {
          return MSIE_6_0;
        }
      } else if (header.contains("msie 7.0")) {
        return MSIE_7_0;
      } else if (header.contains("msie 8.0")) {
        return MSIE_8_0;
      } else {
        return MSIE;
      }
    } else if (header.contains("mozilla")) {
      if (header.contains("mozilla 4.7")) {
        return MOZILLA_4_7;
      } else if (header.contains("mozilla 5.0")) {
        if (header.contains("rv:1.6")) {
          return MOZILLA_5_0_R1_6;
        } else if (header.contains("gecko")) {
          return GECKO;
        } else {
          return MOZILLA_5_0;
        }
      } else {
        return MOZILLA;
      }
    }

    return DEFAULT;
  }

  /** @deprecated no longer supported, since Tobago 1.5 */
  @Deprecated
  public static UserAgent getInstanceForId(String id) {
    if (id == null) {
      return DEFAULT;
    }

    if (id.indexOf("opera") == 0) {
      if (id.equals("opera_5_0")) {
        return OPERA_5_0;
      } else if (id.equals("opera_6_0")) {
        return OPERA_6_0;
      } else if (id.equals("opera_7_11")) {
        return OPERA_7_11;
      } else {
        return OPERA;
      }
    } else if (id.indexOf("msie") == 0) {
      if (id.equals("msie_5_0")) {
        return MSIE_5_0;
      } else if (id.equals("msie_5_0_mac")) {
        return MSIE_5_0_MAC;
      } else if (id.equals("msie_5_5")) {
        return MSIE_5_5;
      } else if (id.equals("msie_6_0")) {
        return MSIE_6_0;
      } else if (id.equals("msie_6_0_mac")) {
        return MSIE_6_0_MAC;
      } else if (id.equals("msie_7_0")) {
        return MSIE_7_0;
      } else {
        return MSIE;
      }
    } else if (id.indexOf("mozilla") == 0) {
      if (id.equals("mozilla_4_7")) {
        return MOZILLA_4_7;
      } else if (id.equals("mozilla_5_0")) {
        return MOZILLA_5_0;
      } else if (id.equals("mozilla_5_0_r1_6")) {
        return MOZILLA_5_0_R1_6;
      } else {
        return MOZILLA;
      }
    }

    return DEFAULT;
  }


  /**
   * @deprecated don't use toString() functionality!
   */
  @Deprecated
  public String toString() {
    return version != null
        ? name + '_' + version
        : name;
  }
}

