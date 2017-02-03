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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is for debugging the access keys.
 */
public final class AccessKeyLogger extends HashMap<Character, List<String>> {

  private static final Logger LOG = LoggerFactory.getLogger(AccessKeyLogger.class);

  private static final char[] KEYS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
      'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

  private AccessKeyLogger() {
  }

  private static AccessKeyLogger getInstance(final FacesContext facesContext) {
    final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
    AccessKeyLogger keyMap = (AccessKeyLogger) requestMap.get(AccessKeyLogger.class.getName());
    if (keyMap == null) {
      keyMap = new AccessKeyLogger();
      requestMap.put(AccessKeyLogger.class.getName(), keyMap);
    }
    return keyMap;
  }

  public static void addAccessKey(final FacesContext facesContext, final Character key, String clientId) {
    if (LOG.isDebugEnabled()) {
      final AccessKeyLogger instance = getInstance(facesContext);
      List<String> clientIds;
      if (instance.containsKey(key)) {
        clientIds = instance.get(key);
      } else {
        clientIds = new ArrayList<String>();
        instance.put(key, clientIds);
      }
      clientIds.add(clientId);
      // should only be called, when debug is enabled
      LOG.debug("Using accessKey='{}' for clientId='{}'", key, clientId);
    }
  }

  public static void logStatus(final FacesContext facesContext) {
    if (LOG.isDebugEnabled()) {
      final StringBuilder builder = new StringBuilder();
      final AccessKeyLogger instance = AccessKeyLogger.getInstance(facesContext);
      builder.append("Used access keys:");
      for (Map.Entry<Character, List<String>> entry : instance.entrySet()) {
        builder.append("\n'");
        builder.append(entry.getKey());
        builder.append("' -> ");
        builder.append(entry.getValue());
      }
      builder.append("\nFree access keys: ");
      for (final char key : KEYS) {
        builder.append(instance.containsKey(Character.valueOf(key)) ? '.' : key);
      }
      LOG.debug(builder.toString());
    }
  }
}
