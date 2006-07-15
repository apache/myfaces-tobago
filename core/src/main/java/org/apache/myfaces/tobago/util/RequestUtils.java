package org.apache.myfaces.tobago.util;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 03.07.2006
 * Time: 21:19:38
 * To change this template use File | Settings | File Templates.
 */
public class RequestUtils {

  private static final Log LOG = LogFactory.getLog(RequestUtils.class);

  public static void ensureEncoding(ExternalContext externalContext) {
    try {
      // TODO PortletRequest
      if (externalContext.getRequest() instanceof HttpServletRequest) {
        HttpServletRequest request =
            (HttpServletRequest) externalContext.getRequest();
        if (request.getCharacterEncoding() == null) {
          request.setCharacterEncoding("UTF-8");
        }
      }

    } catch (UnsupportedEncodingException e) {
      LOG.error("" + e, e);
    }
  }

  public static void ensureNoCacheHeader(ExternalContext externalContext) {
    // TODO PortletRequest
    if (externalContext.getResponse() instanceof HttpServletResponse) {
      HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
      response.setHeader("Cache-Control", "private,no-cache,no-store");
      response.setHeader("Pragma", "no-cache");
      response.setDateHeader("Expires", 0);
      response.setDateHeader("max-age", 0);
    }
  }
}
