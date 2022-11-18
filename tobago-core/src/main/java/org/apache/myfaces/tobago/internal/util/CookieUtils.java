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

import org.apache.myfaces.tobago.context.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.invoke.MethodHandles;

public class CookieUtils {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final int ONE_YEAR_IN_SECONDS = 365 * 24 * 60 * 60;

  private CookieUtils() {
  }

  public static String getThemeNameFromCookie(final HttpServletRequest request) {
    String themeName = null;
    final Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (final Cookie cookie : cookies) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("cookie name  ='{}'", cookie.getName());
          LOG.debug("cookie value ='{}'", cookie.getValue());
          LOG.debug("cookie path  ='{}'", cookie.getPath());
        }
        if (Theme.THEME_KEY.equals(cookie.getName())) {
          themeName = cookie.getValue();
          if (LOG.isDebugEnabled()) {
            LOG.debug("theme from cookie {}='{}'", Theme.THEME_KEY, themeName);
          }
          break;
        }
      }
    }
    return themeName;
  }

  public static void setThemeNameToCookie(
      final HttpServletRequest request, final HttpServletResponse response, final String themeName) {

    String path = request.getContextPath();
    path = StringUtils.isBlank(path) ? "/" : path;
    boolean found = false;
    final Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (final Cookie cookie : cookies) {
        if (Theme.THEME_KEY.equals(cookie.getName())) {
          if (found) {
            if (LOG.isDebugEnabled()) {
              LOG.debug("Found more than one cookie {}, try to remove them...", Theme.THEME_KEY);
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
          cookie.setSecure(request.isSecure());
          response.addCookie(cookie);
        }
      }
    }
    if (!found) {
      final Cookie cookie = new Cookie(Theme.THEME_KEY, themeName);
      cookie.setPath(path);
      cookie.setMaxAge(ONE_YEAR_IN_SECONDS);
      cookie.setSecure(request.isSecure());
      response.addCookie(cookie);
    }
  }

  public static void removeThemeNameCookie(
      final HttpServletRequest request, final HttpServletResponse response) {

    final Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (final Cookie cookie : cookies) {
        if (Theme.THEME_KEY.equals(cookie.getName())) {
          cookie.setMaxAge(0);
          cookie.setValue(null);
          cookie.setSecure(request.isSecure());
          response.addCookie(cookie);
        }
      }
    }
  }
}
