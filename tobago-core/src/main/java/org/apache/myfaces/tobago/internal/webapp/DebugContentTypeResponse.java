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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.lang.invoke.MethodHandles;

public class DebugContentTypeResponse extends HttpServletResponseWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public DebugContentTypeResponse(final HttpServletResponse response) {
    super(response);
  }

  @Override
  public void setContentType(final String type) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Setting Content-Type to '" + type + "'.", new Exception());
    }
    super.setContentType(type);
  }

  @Override
  public String getContentType() {
    final String type = super.getContentType();
    if (LOG.isDebugEnabled()) {
      LOG.debug("Getting Content-Type '" + type + "'.", new Exception());
    }
    return type;
  }

  @Override
  public void setHeader(final String name, final String value) {
    if ("Content-Type".equals(name)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Setting Content-Type to '" + value + "'.", new Exception());
      }
    }
    super.setHeader(name, value);
  }

  @Override
  public void addHeader(final String name, final String value) {
    if ("Content-Type".equals(name)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Setting Content-Type to '" + value + "'.", new Exception());
      }
    }
    super.addHeader(name, value);
  }
}
