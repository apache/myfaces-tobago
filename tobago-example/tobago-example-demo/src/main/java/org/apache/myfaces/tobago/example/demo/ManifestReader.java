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

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.SheetState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@ApplicationScoped
@Named
public class ManifestReader {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ManifestEntry manifestTree;

  private final SheetState state;

  private Date buildTime;

  public ManifestReader() {

    state = new SheetState();
    state.setExpandedState(new ExpandedState(1));
    manifestTree = new ManifestEntry("Tobago Example Demo", null);

    URL url = null;
    try {
      final Enumeration<URL> ul = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");

      while (ul.hasMoreElements()) {
        url = ul.nextElement();

        String name = url.toString();
        name = name.replaceAll(".+/([^/]+\\.jar)\\!/META-INF/MANIFEST.MF", "$1");
        final ManifestEntry jar = new ManifestEntry(name, null);
        manifestTree.add(jar);

        final Manifest manifest = new Manifest(url.openStream());
        final Attributes attributes = manifest.getMainAttributes();
        for (final Object key : attributes.keySet()) {
          jar.add(new ManifestEntry(key.toString(), attributes.get(key).toString()));
        }
        if (name.startsWith("tobago-core")) {
          final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
          format.setTimeZone(TimeZone.getTimeZone("UTC"));
          try {
            buildTime = format.parse(attributes.getValue("Build-Time"));
          } catch (final ParseException e) {
            LOG.error("Can't parse '" + attributes.getValue("Build-Time") + "'", e);
          }
        }
      }
    } catch (final IOException e) {
      LOG.error("Problem while processing URL: " + url, e);
    }
  }

  public ManifestEntry getManifestTree() {
    return manifestTree;
  }

  public SheetState getState() {
    return state;
  }

  public Date getBuildTime() {
    return buildTime;
  }
}
