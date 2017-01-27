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

package org.apache.myfaces.tobago.internal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

public class TobagoConfigEntityResolver extends DefaultHandler {

  private static final String TOBAGO_CONFIG_DTD_1_0 = "/org/apache/myfaces/tobago/config/tobago-config_1_0.dtd";
  private static final String TOBAGO_CONFIG_DTD_1_0_29 = "/org/apache/myfaces/tobago/config/tobago-config-1.0.29.dtd";
  private static final String TOBAGO_CONFIG_DTD_1_0_30 = "/org/apache/myfaces/tobago/config/tobago-config-1.0.30.dtd";
  private static final String TOBAGO_CONFIG_DTD_1_0_34 = "/org/apache/myfaces/tobago/config/tobago-config-1.0.34.dtd";
  protected static final String TOBAGO_CONFIG_XSD_1_5 = "/org/apache/myfaces/tobago/config/tobago-config-1.5.xsd";
  @Deprecated
  protected static final String TOBAGO_CONFIG_XSD_1_6 = "/org/apache/myfaces/tobago/config/tobago-config-1.6.xsd";
  protected static final String TOBAGO_CONFIG_XSD_2_0 = "/org/apache/myfaces/tobago/config/tobago-config-2.0.xsd";
  protected static final String TOBAGO_CONFIG_XSD_2_0_6 = "/org/apache/myfaces/tobago/config/tobago-config-2.0.6.xsd";
  protected static final String TOBAGO_CONFIG_XSD_3_0 = "/org/apache/myfaces/tobago/config/tobago-config-3.0.xsd";
  protected static final String TOBAGO_CONFIG_XSD_3_1 = "/org/apache/myfaces/tobago/config/tobago-config-3.1.xsd";

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfigEntityResolver.class);

  @Override
  public InputSource resolveEntity(final String publicId, final String systemId) throws IOException, SAXException {
    if (LOG.isInfoEnabled()) {
      LOG.info("Resolving publicId='" + publicId + "' and systemId='" + systemId + "'.");
    }
    final InputStream localStream;
    if (systemId.equals("http://myfaces.apache.org/tobago/tobago-config_1_0.dtd")) {
      localStream = getClass().getResourceAsStream(TOBAGO_CONFIG_DTD_1_0);
    } else if (systemId.equals("http://myfaces.apache.org/tobago/tobago-config-1.0.29.dtd")) {
      localStream = getClass().getResourceAsStream(TOBAGO_CONFIG_DTD_1_0_29);
    } else if (systemId.equals("http://myfaces.apache.org/tobago/tobago-config-1.0.30.dtd")) {
      localStream = getClass().getResourceAsStream(TOBAGO_CONFIG_DTD_1_0_30);
    } else if (systemId.equals("http://myfaces.apache.org/tobago/tobago-config-1.0.34.dtd")) {
      localStream = getClass().getResourceAsStream(TOBAGO_CONFIG_DTD_1_0_34);
    } else {
      localStream = null;
    }
    if (localStream != null) {
      return new InputSource(localStream);
    } else {
      LOG.warn("Didn't find local resource for publicId='" + publicId + "' and systemId='" + systemId + "'. "
          + "Trying to load with parent resolver (might be loaded over the internet).");
      return super.resolveEntity(publicId, systemId);
    }
  }
}
