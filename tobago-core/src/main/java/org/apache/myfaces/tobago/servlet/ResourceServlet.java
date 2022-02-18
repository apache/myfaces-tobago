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

package org.apache.myfaces.tobago.servlet;

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.internal.util.MimeTypeUtils;
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.ProjectStage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <pre>
 * &lt;servlet&gt;
 *   &lt;servlet-name&gt;ResourceServlet&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;org.apache.myfaces.tobago.servlet.ResourceServlet&lt;/servlet-class&gt;
 *   &lt;init-param&gt;
 *     &lt;description&gt;The value for the expires header in seconds.
 *       The default for ProjectStage.Production is 86400 sec (24 h) otherwise no expires header.&lt;/description&gt;
 *     &lt;param-name&gt;expires&lt;/param-name&gt;
 *     &lt;param-value&gt;14400&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 *   &lt;init-param&gt;
 *     &lt;description&gt;The value for the copy buffer size.
 *            Default is 4096.&lt;/description&gt;
 *     &lt;param-name&gt;bufferSize&lt;/param-name&gt;
 *     &lt;param-value&gt;4096&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 * &lt;/servlet&gt;
 * &lt;servlet-mapping&gt;
 *   &lt;servlet-name&gt;ResourceServlet&lt;/servlet-name&gt;
 *   &lt;url-pattern&gt;/org/apache/myfaces/tobago/renderkit/*&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;
 * </pre>
 *
 * @since 1.0.7
 * @deprecated since Tobago 3.0.x this is no longer needed
 */
@Deprecated
public class ResourceServlet extends HttpServlet {

  private static final long serialVersionUID = -4491419290205206466L;

  private static final Logger LOG = LoggerFactory.getLogger(ResourceServlet.class);

  private Long expires;
  private int bufferSize;
  private boolean nosniffHeader;

  @Override
  public void init(final ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    final TobagoConfig tobagoConfig = TobagoConfig.getInstance(servletConfig.getServletContext());
    if (tobagoConfig.getProjectStage() == ProjectStage.Production) {
      expires = 24 * 60 * 60 * 1000L;
    }

    final String expiresString = servletConfig.getInitParameter("expires");
    if (expiresString != null) {
      try {
        expires = new Long(expiresString) * 1000;
      } catch (final NumberFormatException e) {
        LOG.error("Caught: " + e.getMessage(), e);
      }
    }
    final String bufferSizeString = servletConfig.getInitParameter("bufferSize");
    bufferSize = 1024 * 4;
    if (bufferSizeString != null) {
      try {
        bufferSize = Integer.parseInt(bufferSizeString);
      } catch (final NumberFormatException e) {
        LOG.error("Caught: " + e.getMessage(), e);
      }
    }
    nosniffHeader = tobagoConfig.isSetNosniffHeader();
  }

  @Override
  protected void doGet(
      final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {

    final String requestURI = request.getRequestURI();
    final String resource = requestURI.substring(request.getContextPath().length() + 1);
    if (expires != null) {
      response.setDateHeader("Last-Modified", 0);
      response.setHeader("Cache-Control", "Public, max-age=" + expires);
      response.setDateHeader("Expires", System.currentTimeMillis() + expires);
    }
    final String contentType = MimeTypeUtils.getMimeTypeForFile(requestURI);
    if (contentType != null) {
      response.setContentType(contentType);
      if (nosniffHeader) {
        ResponseUtils.ensureNosniffHeader(response);
      }
    } else {
      final String message = "Unsupported mime type of resource='" + resource + "'";
      LOG.warn(message + " (because of security reasons)");
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }

    try (InputStream inputStream = locateResource(resource)) {
      if (inputStream != null) {
        copy(inputStream, response.getOutputStream());
      } else {
        final String message = "Resource '" + resource + "' not found!";
        LOG.warn(message);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    }
  }

  private InputStream locateResource(String resource) {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream; // meta inf (like in servlet 3.0)
    inputStream = classLoader.getResourceAsStream("META-INF/resources/" + resource);

    // "normal" classpath
    if (inputStream == null) {
      inputStream = classLoader.getResourceAsStream(resource);
    }
    return inputStream;
  }

  @Override
  protected long getLastModified(final HttpServletRequest request) {
    if (expires != null) {
      return 0;
    } else {
      return super.getLastModified(request);
    }
  }

  private void copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
    final byte[] buffer = new byte[bufferSize];
    int count;
    while (-1 != (count = inputStream.read(buffer))) {
      outputStream.write(buffer, 0, count);
    }
  }
}
