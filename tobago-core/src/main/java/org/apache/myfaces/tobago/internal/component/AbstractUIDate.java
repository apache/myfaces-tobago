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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.internal.util.DateFormatUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;

public abstract class AbstractUIDate extends AbstractUIInput {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIDate.class);

  public String getPattern() {
    String pattern = null;
    final FacesContext facesContext = getFacesContext();
    final Converter converter = ComponentUtils.getConverter(facesContext, this);
    if (converter instanceof DateTimeConverter) {
      pattern = DateFormatUtils.findPattern((DateTimeConverter) converter);
    }
    if (pattern == null) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("Can't find the pattern for the converter in component {}! DatePicker may not work correctly.",
            getClientId(facesContext));
      }
    }
    return pattern;
  }
}
