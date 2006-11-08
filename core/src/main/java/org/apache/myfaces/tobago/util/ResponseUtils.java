package org.apache.myfaces.tobago.util;

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

import org.apache.myfaces.tobago.context.ClientProperties;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Oct 21, 2006
 * Time: 10:39:25 AM
 */
public class ResponseUtils {

  public static void ensureNoCacheHeader(ExternalContext externalContext) {
    // TODO PortletRequest
    if (externalContext.getResponse() instanceof HttpServletResponse) {
      HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
      response.setHeader("Cache-Control", "no-cache,no-store,max-age=0,must-revalidate");
      response.setHeader("Pragma", "no-cache");
      response.setDateHeader("Expires", 0);
      response.setDateHeader("max-age", 0);
    }
  }

  public static void ensureContentTypeHeader(FacesContext facesContext, String charset) {
    // TODO PortletRequest
    if (facesContext.getExternalContext().getResponse() instanceof HttpServletResponse) {
      HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
      response.setContentType(generateContentType(facesContext, charset));
    }
  }

  private static String generateContentType(FacesContext facesContext, String charset) {
    StringBuffer sb = new StringBuffer("text/");
    ClientProperties clientProperties
        = ClientProperties.getInstance(facesContext.getViewRoot());
    sb.append(clientProperties.getContentType());
    if (charset == null) {
      charset = "UTF-8";
    }
    sb.append("; charset=");
    sb.append(charset);
    return sb.toString();
  }
}
