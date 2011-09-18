package org.apache.myfaces.tobago.example.demo;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class ManifestReader {

  private static final Logger LOG = LoggerFactory.getLogger(ManifestReader.class);

  private List<ManifestEntry> manifestList = new ArrayList<ManifestEntry>();

  public ManifestReader(){
    try {
      Enumeration<URL> ul = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
      String line;
      StringBuilder content;
      BufferedReader in;
      URL url;

      while (ul.hasMoreElements()) {
        url = ul.nextElement();
        in = new BufferedReader(new InputStreamReader(url.openStream()));
        content  = new StringBuilder();
        Map.Entry<String, String> e;

        while((line= in.readLine()) != null){
          content.append(line+"\r\n");
        }
        in.close();

        manifestList.add(new ManifestEntry(url.toString(), content.toString()));
      }
    } catch (IOException e) {
      LOG.error("", e);
    }
  }

  public List<ManifestEntry> getManifestList() {
    return manifestList;
  }

  public void setManifestList(List<ManifestEntry> manifestList) {
    this.manifestList = manifestList;
  }
}
