/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/**
 * User: weber
 * Date: Apr 5, 2005
 * Time: 3:23:40 PM
 */
package org.apache.myfaces.tobago.util;

import javax.faces.context.FacesContext;
import java.util.HashSet;
import java.util.Map;

public class AccessKeyMap {

  private static final char[] KEYS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
      'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

  private final static String request_map_key = "accessKeysRequestMapKey";

  public static AccessKeyMap getInstance(FacesContext facesContext) {
    final Map requestMap = facesContext.getExternalContext().getRequestMap();
    AccessKeyMap keyMap = (AccessKeyMap) requestMap.get(request_map_key);
    if (keyMap == null) {
      keyMap = new AccessKeyMap();
      requestMap.put(request_map_key, keyMap);
    }
    return keyMap;
  }

  private HashSet set;
  private String dublicated = "";

  private AccessKeyMap() {
    set = new HashSet();
  }

  private HashSet getSet() {
    return set;
  }

  private String getDublicated() {
    return dublicated;
  }

  private void addDublicated(char key) {
    dublicated = dublicated + key;
  }

  public static boolean addAccessKey(FacesContext facesContext, Character key) {
    key = new Character(key.toString().toLowerCase().charAt(0));
    final AccessKeyMap instance = getInstance(facesContext);
    if (instance.getSet().contains(key)) {
      instance.addDublicated(key.charValue());
      return false;
    } else {
      instance.getSet().add(key);
      return true;
    }
  }


  public static String getDublicatedKeys(FacesContext facesContext) {
    return getInstance(facesContext).getDublicated();
  }

  public static String getUnusedKeys(FacesContext facesContext) {
    HashSet set = getInstance(facesContext).getSet();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < KEYS.length; i++) {
      if (! set.contains(new Character(KEYS[i]))) {
        sb.append(KEYS[i]);
      }
    }
    return sb.toString();
  }
}
