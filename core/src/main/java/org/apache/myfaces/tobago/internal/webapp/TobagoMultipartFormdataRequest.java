package org.apache.myfaces.tobago.internal.webapp;

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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;

import javax.faces.FacesException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TobagoMultipartFormdataRequest extends HttpServletRequestWrapper {

  private static final Log LOG
      = LogFactory.getLog(TobagoMultipartFormdataRequest.class);

  public static final long ONE_KB = 1024;
  public static final long ONE_MB = ONE_KB * ONE_KB;
  public static final long ONE_GB = ONE_KB * ONE_MB;

  private Map parameters;

  private Map fileItems;


  public TobagoMultipartFormdataRequest(HttpServletRequest request) {
    this(request, System.getProperty("java.io.tmpdir"), ONE_MB);
  }

  public TobagoMultipartFormdataRequest(HttpServletRequest request, String repositoryPath, long maxSize) {
    super(request);
    init(request, repositoryPath, maxSize);
  }

  private void init(HttpServletRequest request, String repositoryPath, long maxSize) {
    if (!ServletFileUpload.isMultipartContent(request)) {
      String errorText = "contentType is not multipart/form-data but '"
          + request.getContentType() + "'";
      LOG.error(errorText);
      throw new FacesException(errorText);
    } else {
      parameters = new HashMap();
      fileItems = new HashMap();
      DiskFileItemFactory factory = new DiskFileItemFactory();

      factory.setRepository(new File(repositoryPath));

      ServletFileUpload upload = new ServletFileUpload(factory);

      upload.setSizeMax(maxSize);

      if (upload.getHeaderEncoding() != null) {
        // TODO: enable configuration of  'accept-charset'
        upload.setHeaderEncoding(AbstractUIPage.FORM_ACCEPT_CHARSET);
      }
      List<FileItem> itemList;
      try {
        itemList = (List<FileItem>) upload.parseRequest(request);
      } catch (FileUploadException e) {
        //LOG.error(e);
        throw new FacesException(e);
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("parametercount = " + itemList.size());
      }
      for (FileItem item : itemList) {
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
              values = new String[]{item.getString(AbstractUIPage.FORM_ACCEPT_CHARSET)};
            } catch (UnsupportedEncodingException e) {
              LOG.error("Caught: " + e.getMessage(), e);
              values = new String[]{item.getString()};
            }
            parameters.put(key, values);
          } else if (inStock instanceof String[]) { // double (or more) parameter
            String[] oldValues = (String[]) inStock;
            String[] values = new String[oldValues.length + 1];
            System.arraycopy(oldValues, 0, values, 0, oldValues.length);
            try {
              // TODO: enable configuration of  'accept-charset'
              values[oldValues.length] = item.getString(AbstractUIPage.FORM_ACCEPT_CHARSET);
            } catch (UnsupportedEncodingException e) {
              LOG.error("Caught: " + e.getMessage(), e);
              values[oldValues.length] = item.getString();
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

  public FileItem getFileItem(String key) {
    if (fileItems != null) {
      return (FileItem) fileItems.get(key);
    }
    return null;
  }

  public String getParameter(String key) {
    String parameter = null;
    String[] values = (String[]) parameters.get(key);
    if (values != null) {
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

  public static long getMaxSize(String param) {
    if (param != null) {
      String number = param.toLowerCase(Locale.ENGLISH);
      long factor = 1;
      if (number.endsWith("g")) {
        factor = ONE_GB;
        number = number.substring(0, number.length() - 1);
      } else if (number.endsWith("m")) {
        factor = ONE_MB;
        number = number.substring(0, number.length() - 1);
      } else if (number.endsWith("k")) {
        factor = ONE_KB;
        number = number.substring(0, number.length() - 1);
      }
      try {
        return Long.parseLong(number.trim()) * factor;
      } catch (NumberFormatException e) {
        LOG.error("Given max file size for "
            + TobagoMultipartFormdataRequest.class.getName() + " " + param + " couldn't parsed to a number");
      }
    }
    return ONE_MB;
  }
}
