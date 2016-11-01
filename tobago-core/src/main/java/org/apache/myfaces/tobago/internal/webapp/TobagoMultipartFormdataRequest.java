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

package org.apache.myfaces.tobago.internal.webapp;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TobagoMultipartFormdataRequest extends HttpServletRequestWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoMultipartFormdataRequest.class);

  public static final long ONE_KB = 1024;
  public static final long ONE_MB = ONE_KB * ONE_KB;
  public static final long ONE_GB = ONE_KB * ONE_MB;

  private Map<String, String[]> parameters;

  private Map<String, List<FileItem>> fileItems;

  public TobagoMultipartFormdataRequest(final HttpServletRequest request) {
    this(request, System.getProperty("java.io.tmpdir"), ONE_MB);
  }

  public TobagoMultipartFormdataRequest(
      final HttpServletRequest request, final String repositoryPath, final long maxSize) {
    super(request);
    init(request, repositoryPath, maxSize);
  }

  private void init(final HttpServletRequest request, final String repositoryPath, final long maxSize) {
    if (!ServletFileUpload.isMultipartContent(request)) {
      final String errorText = "contentType is not multipart/form-data but '" + request.getContentType() + "'";
      LOG.error(errorText);
      throw new FacesException(errorText);
    } else {
      parameters = new HashMap<String, String[]>();
      fileItems = new HashMap<String, List<FileItem>>();
      final DiskFileItemFactory factory = new DiskFileItemFactory();

      factory.setRepository(new File(repositoryPath));

      final ServletFileUpload upload = new ServletFileUpload(factory);

      upload.setSizeMax(maxSize);

      if (upload.getHeaderEncoding() != null) {
        // TODO: enable configuration of  'accept-charset'
        upload.setHeaderEncoding(AbstractUIPage.FORM_ACCEPT_CHARSET);
      }
      final List<FileItem> itemList;
      try {
        itemList = (List<FileItem>) upload.parseRequest(request);
      } catch (final FileUploadException e) {
        //LOG.error(e);
        throw new FacesException(e);
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("parametercount = " + itemList.size() + " + " + request.getParameterMap().size());
      }
      for (final FileItem item : itemList) {
        final String key = item.getFieldName();
        if (LOG.isDebugEnabled()) {
          String value = item.getString();
          if (value.length() > 100) {
            value = value.substring(0, 100) + " [...]";
          }
          LOG.debug("Parameter: '" + key + "'='" + value + "' isFormField=" + item.isFormField()
              + " contentType='" + item.getContentType() + "'");
        }
        if (item.isFormField()) {
          String newValue;
          try {
            // TODO: enable configuration of 'accept-charset'
            newValue = item.getString(AbstractUIPage.FORM_ACCEPT_CHARSET);
          } catch (final UnsupportedEncodingException e) {
            LOG.error("Caught: " + e.getMessage(), e);
            newValue = item.getString();
          }

          addParameter(key, newValue);
        } else {
          List<FileItem> help = this.fileItems.get(key);
          if (help == null) {
            help = new ArrayList<FileItem>();
            this.fileItems.put(key, help);
          }
          help.add(item);
        }
      }

      // merging the GET parameters:
      final Enumeration e = request.getParameterNames();
      while(e.hasMoreElements()) {
        final String name = (String) e.nextElement();
        final String[] newValues = request.getParameterValues(name);
        if (LOG.isDebugEnabled()) {
          LOG.debug("Parameter: '" + name + "'='" + Arrays.toString(newValues) + "' (GET)");
        }
        for (final String newValue : newValues) {
          addParameter(name, newValue);
        }
      }
    }
  }

  private void addParameter(final String key, final String newValue) {
    final String[] inStock = parameters.get(key);
    final String[] values;
    if (inStock == null) {
      values = new String[]{newValue};
    } else {
      values = new String[inStock.length + 1];
      System.arraycopy(inStock, 0, values, 0, inStock.length);
      values[inStock.length] = newValue;
    }
    parameters.put(key, values);
  }

  public FileItem getFileItem(final String key) {
    if (fileItems != null) {
      final List<FileItem> fileItemsForKey = this.fileItems.get(key);
      if (fileItemsForKey != null && fileItemsForKey.size() > 0) {
        return fileItemsForKey.get(0);
      } else {
        return null;
      }
    }
    return null;
  }

  public FileItem[] getFileItems(final String key) {
    if (fileItems != null) {
      final List<FileItem> fileItemsForKey = this.fileItems.get(key);
      if (fileItemsForKey != null) {
        return fileItemsForKey.toArray(new FileItem[fileItemsForKey.size()]);
      }
    }
    return null;
  }

  public String getParameter(final String key) {
    String parameter = null;
    final String[] values = (String[]) parameters.get(key);
    if (values != null) {
      parameter = values[0];
    }
    return parameter;
  }

  public Enumeration getParameterNames() {
    return Collections.enumeration(parameters.keySet());
  }

  public String[] getParameterValues(final String key) {
    return (String[]) parameters.get(key);
  }

  public Map getParameterMap() {
    return parameters;
  }

  public static long getMaxSize(final String param) {
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
      } catch (final NumberFormatException e) {
        LOG.error("Given max file size for "
            + TobagoMultipartFormdataRequest.class.getName() + " " + param + " couldn't parsed to a number");
      }
    }
    return ONE_MB;
  }
}
