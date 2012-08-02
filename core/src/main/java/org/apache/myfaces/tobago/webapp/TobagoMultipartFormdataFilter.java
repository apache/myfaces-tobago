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

package org.apache.myfaces.tobago.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.File;
import java.util.Locale;


/**
 * This filter handles multipart request. It must be enabled in the web.xml of your web application.
 * Usage:
 * <p/>
 * <p><blockquote><pre>
 * &lt;filter&gt;
 * &lt;filter-name&gt;multipartFormdataFilter&lt;/filter-name&gt;
 * &lt;filter-class&gt;org.apache.myfaces.tobago.webapp.TobagoMultipartFormdataFilter&lt;/filter-class&gt;
 * &lt;init-param&gt;
 * &lt;description&gt;Set the size limit for uploaded files. Default value is 1 MB.
 * Format: 10 = 10 bytes
 * 10k = 10 KB
 * 10m = 10 MB
 * 1g = 1 GB
 * &lt;/description&gt;
 * &lt;param-name&gt;uploadMaxFileSize&lt;/param-name&gt;
 * &lt;param-value&gt;20m&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;init-param&gt;
 * &lt;description&gt;Set the upload repository path for uploaded files.
 * Default value is java.io.tmpdir.&lt;/description&gt;
 * &lt;param-name&gt;uploadRepositoryPath&lt;/param-name&gt;
 * &lt;param-value&gt;/tmp&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 * &lt;filter-name&gt;multipartFormdataFilter&lt;/filter-name&gt;
 * &lt;url-pattern&gt;/faces/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre></blockquote><p>
 */
public class TobagoMultipartFormdataFilter implements Filter {

  private static final Log LOG = LogFactory.getLog(TobagoMultipartFormdataFilter.class);

  private String repositoryPath = System.getProperty("java.io.tmpdir");
  private long maxSize = TobagoMultipartFormdataRequest.ONE_MB;

  public void init(FilterConfig filterConfig) throws ServletException {
    String repositoryPath = filterConfig.getInitParameter("uploadRepositoryPath");
    if (repositoryPath != null) {
      File file = new File(repositoryPath);
      if (!file.exists()) {
        LOG.error("Given repository Path for " + getClass().getName() + " " + repositoryPath + " doesn't exists");
      } else if (!file.isDirectory()) {
        LOG.error("Given repository Path for " + getClass().getName() + " " + repositoryPath + " is not a directory");
      } else {
        this.repositoryPath = repositoryPath;
      }
    }

    LOG.info("Configure uploadRepositryPath for " + getClass().getName() + " to " + this.repositoryPath);

    maxSize = TobagoMultipartFormdataRequest.getMaxSize(filterConfig.getInitParameter("uploadMaxFileSize"));

    LOG.info("Configure uploadMaxFileSize for " + getClass().getName() + " to " + this.maxSize);

  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    ServletRequest wrapper;
    if (request instanceof HttpServletRequest) {
      if (request instanceof TobagoMultipartFormdataRequest) {
        wrapper = request;
      } else {
        String contentType = request.getContentType();
        if (contentType != null
            && contentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/form-data")) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Wrapping " + request.getClass().getName()
                + " with ContentType=\"" + contentType + "\" "
                + "into TobagoMultipartFormdataRequest");
          }
          wrapper = new TobagoMultipartFormdataRequest(
              (HttpServletRequest) request, repositoryPath, maxSize);
        } else {
          wrapper = request;
        }
      }
    } else {
      LOG.error("Not implemented for non HttpServletRequest");
      wrapper = request;
    }
    ServletResponse wrappedResponse;
    if (response instanceof HttpServletResponse) {
      wrappedResponse = new TobagoResponse((HttpServletResponse) response);
    } else {
      wrappedResponse = response;
    }

    chain.doFilter(wrapper, wrappedResponse);
  }

  public void destroy() {
  }

}
