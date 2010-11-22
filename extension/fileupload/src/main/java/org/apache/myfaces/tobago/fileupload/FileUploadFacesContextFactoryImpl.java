package org.apache.myfaces.tobago.fileupload;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.util.JndiUtils;
import org.apache.myfaces.tobago.webapp.TobagoMultipartFormdataRequest;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * This FacesContextFactory handles multipart request. Add the tobago-fileupload.jar to your web application.
 * Configuration:
 *
 * <p><blockquote><pre>
    &lt;env-entry&gt;
      &lt;description&gt;Set the size limit for uploaded files. Default value is 1 MB.
        Format: 10 = 10 bytes
        10k = 10 KB
        10m = 10 MB
        1g = 1 GB
      &lt;/description&gt;
      &lt;env-entry-name&gt;uploadMaxFileSize&lt;/env-entry-name&gt;
      &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;
      &lt;env-entry-value&gt;20m&lt;/env-entry-value&gt;
    &lt;/env-entry&gt;
    &lt;env-entry&gt;
      &lt;description&gt;Set the upload repository path for uploaded files.
             Default value is java.io.tmpdir.&lt;/description&gt;
      &lt;env-entry-name&gt;uploadRepositoryPath&lt;/env-entry-name&gt;
      &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;
      &lt;env-entry-value&gt;/tmp&lt;/env-entry-value&gt;
    &lt;/env-entry&gt;
 </pre></blockquote><p>

 *
 */
public class FileUploadFacesContextFactoryImpl extends FacesContextFactory {
  private static final Log LOG = LogFactory.getLog(FileUploadFacesContextFactoryImpl.class);
  private FacesContextFactory facesContextFactory;
  private String repositoryPath = System.getProperty("java.io.tmpdir");
  private long maxSize = TobagoMultipartFormdataRequest.ONE_MB;

  public FileUploadFacesContextFactoryImpl(FacesContextFactory facesContextFactory) {
    // TODO get Configuration from env entries in the web.xml or context-param
    this.facesContextFactory = facesContextFactory;
    if (LOG.isDebugEnabled()) {
      LOG.debug("Wrap FacesContext for file upload");
    }
    InitialContext ic = null;
    try {
      ic = new InitialContext();

      try {
        String repositoryPath = (String) JndiUtils.getJndiProperty(ic, "uploadRepositoryPath");
        if (repositoryPath != null) {
          File file = new File(repositoryPath);
          if (!file.exists()) {
            LOG.error("Given repository Path for "
                + getClass().getName() + " " + repositoryPath + " doesn't exists");
          } else if (!file.isDirectory()) {
            LOG.error("Given repository Path for "
                + getClass().getName() + " " + repositoryPath + " is not a directory");
          } else {
            this.repositoryPath = repositoryPath;
          }
        }
      } catch (NamingException ne) {
        // ignore
      }

      try {
        String size = (String) JndiUtils.getJndiProperty(ic, "uploadMaxFileSize");
        maxSize = TobagoMultipartFormdataRequest.getMaxSize(size);
      } catch (NamingException ne) {
        // ignore
      }
    } catch (NamingException e) {
      // ignore no naming available
    } finally {
      if (ic != null) {
        try {
          ic.close();
        } catch (NamingException e) {
          // ignore
        }
      }
    }
    if (LOG.isInfoEnabled()) {
      LOG.info("Configure uploadMaxFileSize for "+ getClass().getName() + " to "+ this.maxSize);
      LOG.info("Configure uploadRepositryPath for "+ getClass().getName() + " to "+ this.repositoryPath);
    }
  }

  public FacesContext getFacesContext(Object context, Object request, Object response, Lifecycle lifecycle)
      throws FacesException {
    if (request instanceof HttpServletRequest && !(request instanceof TobagoMultipartFormdataRequest)) {
      String contentType = ((HttpServletRequest) request).getContentType();
      if (contentType != null && contentType.toLowerCase().startsWith("multipart/form-data")) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Wrap HttpServletRequest for file upload");
        }
        try {
          request = new TobagoMultipartFormdataRequest((HttpServletRequest) request, repositoryPath, maxSize);
        } catch (FacesException e) {
          LOG.error("", e);
          FacesContext facesContext = facesContextFactory.getFacesContext(context, request, response, lifecycle);
          // TODO  better Message i18n Message?
          FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getCause().getMessage(), null);
          facesContext.addMessage(null, facesMessage);
          facesContext.renderResponse();
          return facesContext;
        }
      }
    }
    return facesContextFactory.getFacesContext(context, request, response, lifecycle);
  }
}
