/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
/*
 * Created 30.01.2003 17:00:56.
 * $Id$
 */
package org.apache.myfaces.tobago.webapp;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.FORM_ACCEPT_CHARSET;

import javax.faces.FacesException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TobagoMultipartFormdataRequest extends HttpServletRequestWrapper {

// ///////////////////////////////////////////// constant

  private static final Log LOG
      = LogFactory.getLog(TobagoMultipartFormdataRequest.class);

// ///////////////////////////////////////////// attribute

  private Map parameters;

  private Map fileItems;

// ///////////////////////////////////////////// constructor

  TobagoMultipartFormdataRequest(HttpServletRequest request) {
    super(request);
    String contentType = request.getContentType();
    if (contentType == null ||
        !contentType.toLowerCase().startsWith("multipart/form-data")) {
      String errorText = "contentType is not multipart/form-data but '"
          + contentType + "'";
      LOG.error(errorText);
      throw new FacesException(errorText);
    } else {
      parameters = new HashMap();
      fileItems = new HashMap();
      DiskFileUpload fileUpload = new DiskFileUpload();
      fileUpload.setSizeMax(1024 * 1024);
      fileUpload.setRepositoryPath(System.getProperty("java.io.tmpdir"));
      // TODO: make sizeMax and repositoryPath configurable
      List itemList;
      try {
        itemList = fileUpload.parseRequest(request);
      } catch (FileUploadException e) {
        LOG.error(e);
        throw new FacesException(e);
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("parametercount = " + itemList.size());
      }
      Iterator items = itemList.iterator();
      while (items.hasNext()) {
        FileItem item = (FileItem) items.next();
        String key = item.getFieldName();
        if (LOG.isDebugEnabled()) {
          String value = item.getString();
          if (value.length() > 100) {
            value = value.substring(0, 100) + " [...]";
          }
          LOG.debug(
              "Parameter : '" + key + "'='" + value + "' isFormField="
              + item.isFormField() + " contentType='" + item.getContentType() + "'");

        }
        if (item.isFormField()) {
          Object inStock = parameters.get(key);
          if (inStock == null) {
            String[] values;
            try {
              // TODO: enable configuration of  'accept-charset'
              values = new String[] {item.getString(FORM_ACCEPT_CHARSET)};
            } catch (UnsupportedEncodingException e) {
              LOG.error("Catched: " + e.getMessage(), e);
              values = new String[] {item.getString()};
            }
            parameters.put(key, values);
          } else if (inStock instanceof String[]) { // double (or more) parameter
            String[] oldValues = (String[]) inStock;
            String[] values = new String[oldValues.length + 1];
            int i = 0;
            for (; i < oldValues.length; i++) {
              values[i] = oldValues[i];
            }
            try {
              // TODO: enable configuration of  'accept-charset'
              values[i] = item.getString(FORM_ACCEPT_CHARSET);
            } catch (UnsupportedEncodingException e) {
              LOG.error("Catched: " + e.getMessage(), e);
              values[i] = item.getString();
            }
            parameters.put(key, values);
          } else {
            LOG.error(
                "Program error. Unsupported class: "
                + inStock.getClass().getName());
          }
        } else {
          fileItems.put(key, item);
        }
      }
    }
  }

// ////////////////////////////////////////////// code

  public FileItem getFileItem(String key) {
    if (fileItems != null) {
      return (FileItem) fileItems.get(key);
    }
    return null;
  }

// ////////////////////////////////////////////// overwriten


  public String getParameter(String key) {
    String parameter;
    String[] values = (String[]) parameters.get(key);
    if (values == null) {
      parameter = null;
    } else {
      parameter = values[0];
    }
    return parameter;
  }

  public Enumeration getParameterNames() {
    return Collections.enumeration(parameters.keySet());
  }

  public String[] getParameterValues(String key) {
    return (String[]) parameters.get(key);
  }

  public Map getParameterMap() {
    return parameters;
  }
}
