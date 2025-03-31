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

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
@Named
public class SourceFileReader {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  protected static final String JAVA = "(?s)/\\*.*?Licensed.*?\\*/";
  protected static final String SHELL = "(?s)\n# Licensed.*License\\.\n";

  public String getJavaSource(final String filename, final boolean filterLicense) {
    return getSource(filename, filterLicense ? JAVA : null);
  }

  public String getShellSource(final String filename, final boolean filterLicense) {
    return getSource(filename, filterLicense ? SHELL : null);
  }

  protected String getSource(final String filename, final String ignoreRegex) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ExternalContext externalContext = facesContext.getExternalContext();
    final String viewId = facesContext.getViewRoot().getViewId();
    final String file = viewId.substring(0, viewId.lastIndexOf("/")) + "/" + filename;
    try (InputStream resourceAsStream = externalContext.getResourceAsStream(file)) {
      if (resourceAsStream != null) {
        final String string = IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
        if (ignoreRegex != null) {
          return string.replaceAll(ignoreRegex, "");
        } else {
          return string;
        }
      }
    } catch (final IOException e) {
      LOG.error("Error while loading '" + filename + "'!", e);
    }
    return null;
  }
}
