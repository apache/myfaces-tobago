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

package org.apache.myfaces.tobago.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;

public abstract class AbstractThemeMojo extends AbstractMojo {
  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  private static final String[] INCLUDES = new String[]{
      "**"
  };
  private static final String[] EXCLUDES = new String[]{
      "META-INF/**/*",
      "**/*.properties",
      "**/*.xml",
      "**/*.class"
  };

  public MavenProject getProject() {
    return project;
  }

  protected String[] getThemeFiles(File sourceDir) {
    DirectoryScanner scanner = new DirectoryScanner();
    scanner.setBasedir(sourceDir);
    scanner.setIncludes(INCLUDES);
    scanner.setExcludes(EXCLUDES);
    scanner.scan();
    return scanner.getIncludedFiles();
  }

  public String[] getIncludes() {
    return INCLUDES;
  }

  public String[] getExcludes() {
    return EXCLUDES;
  }


}
