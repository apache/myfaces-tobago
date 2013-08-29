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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @goal index
 * @phase prepare-package
 */
public class IndexThemeMojo extends AbstractThemeMojo {

  private static final String[] EXCLUDES = new String[]{
      "META-INF/**/*",
      "**/*.class"
  };

  /**
   * Directory containing the resource files that should be listed into the tobago-resources.properties.
   *
   * @parameter default-value="${project.build.outputDirectory}"
   * @required
   */
  private File outputDirectory;

  /**
   * @parameter default-value="${project.build.outputDirectory}/META-INF/tobago-resources-index.txt"
   * @required
   */
  private File tobagoResourcesFile;

  public void execute() throws MojoExecutionException, MojoFailureException {
    if ("pom".equalsIgnoreCase(getProject().getPackaging())) {
      getLog().info("Not creating " + tobagoResourcesFile.getName() + " as the project is a pom package");
      return;
    }
    if (getLog().isDebugEnabled()) {
      getLog().debug("Scanning resource: " + outputDirectory.getName());
    }

    if (!outputDirectory.isAbsolute()) {
      outputDirectory = new File(getProject().getBasedir(), outputDirectory.getPath());
    }
    if (!outputDirectory.exists()) {
      getLog().info("Not creating " + tobagoResourcesFile.getName() + " as the project has no outputDirectory");
      return;
    }
    StaleCheckDirectoryScanner scanner = new StaleCheckDirectoryScanner(tobagoResourcesFile.lastModified());
    scanner.setBasedir(outputDirectory);
    scanner.setIncludes(getIncludes());
    scanner.setExcludes(getExcludes());
    scanner.scan();

    String[] fileNames = scanner.getIncludedFiles();
    if (fileNames != null && fileNames.length == 0) {
      getLog().info("Skipping create resource file " + tobagoResourcesFile.getName() + ". No resources found");
      return;
    }

    if (!scanner.isUp2date) {
      File metaInf = tobagoResourcesFile.getParentFile();
      if (!metaInf.exists()) {
        if (!metaInf.mkdirs()) {
          getLog().error("Error creating directory " + metaInf.getName());
        }
      }
      BufferedWriter bufferedWriter = null;
      try {
        StringWriter stringWriter = new StringWriter();
        bufferedWriter = new BufferedWriter(stringWriter);
        for (String file : scanner.getIncludedFiles()) {
          bufferedWriter.append('/');
          bufferedWriter.append(file);
          bufferedWriter.newLine();
        }
        bufferedWriter.flush();
        FileUtils.fileWrite(tobagoResourcesFile, "utf-8", stringWriter.toString());
      } catch (IOException e) {
        getLog().error("Error creating resource file " + tobagoResourcesFile.getName(), e);
      } finally {
        IOUtil.close(bufferedWriter);
      }
    } else {
      getLog().info("Skipping create resource file " + tobagoResourcesFile.getName());
    }
  }

  public String[] getExcludes() {
    return EXCLUDES;
  }

  private static class StaleCheckDirectoryScanner extends DirectoryScanner {
    private long lastModified;
    private boolean isUp2date = true;

    private StaleCheckDirectoryScanner(long lastModified) {
      this.lastModified = lastModified;
    }

    @Override
    protected boolean isSelected(String name, File file) {
      long lastModified = file.lastModified();
      if (lastModified > this.lastModified) {
        isUp2date = false;
      }
      return super.isSelected(name, file);
    }
  }
}
