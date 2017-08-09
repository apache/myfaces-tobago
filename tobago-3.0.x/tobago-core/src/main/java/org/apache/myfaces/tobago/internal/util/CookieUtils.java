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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

  private static final Logger LOG = LoggerFactory.getLogger(CookieUtils.class);

  private static final String THEME_PARAMETER = "tobago.theme";

  private static final int ONE_YEAR_IN_SECONDS = 365 * 24 * 60 * 60;

  private CookieUtils() {
  }

  public static String getThemeNameFromCookie(HttpServletRequest request) {
    String themeName = null;
    final Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("cookie name  ='{}'", cookie.getName());
          LOG.debug("cookie value ='{}'", cookie.getValue());
          LOG.debug("cookie path  ='{}'", cookie.getPath());
        }
        if (THEME_PARAMETER.equals(cookie.getName())) {
          themeName = cookie.getValue();
          if (LOG.isDebugEnabled()) {
            LOG.debug("theme from cookie {}='{}'", THEME_PARAMETER, themeName);
          }
          break;
        }
      }
    }
    return themeName;
  }

  public static void setThemeNameToCookie(
      HttpServletRequest request, HttpServletResponse response, String themeName) {

    String path = request.getContextPath();
    path = StringUtils.isBlank(path) ? "/" : path;
    boolean found = false;
    final Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (THEME_PARAMETER.equals(cookie.getName())) {
          if (found) {
            if (LOG.isDebugEnabled()) {
              LOG.debug("Found more than one cookie {}, try to remove them...", THEME_PARAMETER);
            }
            cookie.setMaxAge(0);
          } else {
            found = true;
            if (StringUtils.notEquals(cookie.getValue(), themeName)) {
              if (LOG.isDebugEnabled()) {
                LOG.debug("update theme {} -> {}", cookie.getValue(), themeName);
              }
              cookie.setValue(themeName);
            }
            if (StringUtils.notEquals(cookie.getPath(), path)) {
              if (LOG.isDebugEnabled()) {
                LOG.debug("update path  {} -> {}", cookie.getPath(), path);
              }
              cookie.setPath(path);
            }
            cookie.setMaxAge(ONE_YEAR_IN_SECONDS);
          }
          response.addCookie(cookie);
        }
      }
    }
    if (!found) {
      Cookie cookie = new Cookie(THEME_PARAMETER, themeName);
      cookie.setPath(path);
      cookie.setMaxAge(ONE_YEAR_IN_SECONDS);
      response.addCookie(cookie);
    }
  }

/*
  public static void removeThemeNameCookie(
      HttpServletRequest request, HttpServletResponse response) {

    String path = request.getContextPath();
    path = StringUtils.isBlank(path) ? "/" : path;
    final Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (THEME_PARAMETER.equals(cookie.getName())) {
        cookie.setMaxAge(0);
        response.addCookie(cookie);
      }
    }
  }
*/
}
