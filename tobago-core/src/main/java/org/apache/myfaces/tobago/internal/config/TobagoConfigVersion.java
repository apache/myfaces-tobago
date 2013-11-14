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

import org.apache.myfaces.tobago.internal.util.IoUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TobagoConfigVersion extends TobagoConfigEntityResolver {

  /** Schema or DTD? */
  private boolean schema;

  private String version;

  public TobagoConfigVersion(URL url) throws ParserConfigurationException, SAXException, IOException {

    // simple reading with no validation, at this time
    InputStream inputStream = null;
    try {
      inputStream = url.openStream();
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse(inputStream, this);
    } finally {
      IoUtils.closeQuietly(inputStream);
    }
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    if ("tobago-config".equals(qName)) {
      version = attributes.getValue("version");
      schema = version != null;
    }
  }

  public boolean isSchema() {
    return schema;
  }

  public String getVersion() {
    return version;
  }
}
