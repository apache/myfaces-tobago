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

package org.apache.myfaces.tobago.internal.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

public final class AccessKeyMap {

  private static final Logger LOG = LoggerFactory.getLogger(AccessKeyMap.class);

  private static final char[] KEYS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
      'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

  private static final String REQUEST_MAP_KEY = "accessKeysRequestMapKey";

  private HashSet set;
  private StringBuilder duplicated = new StringBuilder();


  public static AccessKeyMap getInstance(final FacesContext facesContext) {
    final Map requestMap = facesContext.getExternalContext().getRequestMap();
    AccessKeyMap keyMap = (AccessKeyMap) requestMap.get(REQUEST_MAP_KEY);
    if (keyMap == null) {
      keyMap = new AccessKeyMap();
      requestMap.put(REQUEST_MAP_KEY, keyMap);
    }
    return keyMap;
  }

  private AccessKeyMap() {
    set = new HashSet();
  }

  private HashSet getSet() {
    return set;
  }

  private String getDuplicated() {
    return duplicated.toString();
  }

  private void addDublicated(final char key) {
    duplicated = duplicated.append(key);
  }

  public static boolean addAccessKey(final FacesContext facesContext, Character key) {
    key = key.toString().toLowerCase(Locale.ENGLISH).charAt(0);
    final AccessKeyMap instance = getInstance(facesContext);
    if (instance.getSet().contains(key)) {
      instance.addDublicated(key);
      return false;
    } else {
      instance.getSet().add(key);
      return true;
    }
  }

  public static String getDublicatedKeys(final FacesContext facesContext) {
    return getInstance(facesContext).getDuplicated();
  }

  public static String getUnusedKeys(final FacesContext facesContext) {
    final HashSet set = getInstance(facesContext).getSet();
    final StringBuilder sb = new StringBuilder();
    for (final char key : KEYS) {
      if (!set.contains(Character.valueOf(key))) {
        sb.append(key);
      }
    }
    return sb.toString();
  }
}
