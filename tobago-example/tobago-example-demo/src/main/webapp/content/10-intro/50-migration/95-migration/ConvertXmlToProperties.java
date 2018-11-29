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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;

public class ConvertXmlToProperties {

  public static void main(String[] args) throws Exception {

    final String inXmlFile = args[0];
    final InputStream inStream = new FileInputStream(inXmlFile);
    final Properties properties = new Properties();
    properties.loadFromXML(inStream);

    final String outPropertiesFile = inXmlFile.replace(".xml", ".properties");
    final OutputStream outStream = new FileOutputStream(outPropertiesFile);
    final Properties sorted = new Properties() {
      @Override
      public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
      }
    };
    sorted.putAll(properties);
    sorted.store(outStream, "Converted from '" + inXmlFile + "'");

    System.out.println("Converted items: " + properties.size());
  }
}
