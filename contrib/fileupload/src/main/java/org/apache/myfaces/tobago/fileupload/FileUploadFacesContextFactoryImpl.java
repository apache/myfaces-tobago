package org.apache.myfaces.tobago.fileupload;

/*
 * Copyright 2002-2006 The Apache Software Foundation.
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

import org.apache.myfaces.tobago.webapp.TobagoMultipartFormdataRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FacesException;
import javax.servlet.http.HttpServletRequest;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Oct 8, 2006
 * Time: 4:45:47 PM
 */
public class FileUploadFacesContextFactoryImpl extends FacesContextFactory {
  private static final Log LOG = LogFactory.getLog(FileUploadFacesContextFactoryImpl.class);
  private FacesContextFactory facesContextFactory;

  public FileUploadFacesContextFactoryImpl(FacesContextFactory facesContextFactory) {
    this.facesContextFactory = facesContextFactory;
    if (LOG.isDebugEnabled()) {
      LOG.debug("Wrap FacesContext for file upload");
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
        request = new TobagoMultipartFormdataRequest((HttpServletRequest) request);
      }
    }
    return facesContextFactory.getFacesContext(context, request, response, lifecycle);
  }
}
