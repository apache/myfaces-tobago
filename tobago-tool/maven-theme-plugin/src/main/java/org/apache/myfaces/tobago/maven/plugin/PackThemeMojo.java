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

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 23.10.2005
 * Time: 16:33:04
 *
 * @goal theme
 * @phase package
 * @requiresDependencyResolution compile
 */
public class PackThemeMojo extends AbstractThemeMojo {

  /**
   * Maven ProjectHelper
   *
   * @component
   */
  private MavenProjectHelper projectHelper;

  /**
   * The directory for the generated JAR.
   *
   * @parameter expression="${project.build.directory}"
   * @required
   */
  private String outputDirectory;


  /**
   * The name of the generated jar.
   *
   * @parameter expression="${project.build.finalName}"
   * @required
   */
  private String jarName;


  /**
   * The directory where the webapp is built.
   *
   * @parameter expression="${project.build.directory}/${project.build.finalName}"
   * @required
   */
  private File webappDirectory;

  /**
   * The Jar archiver.
   *
   * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#zip}"
   * @required
   */
  // TODO Zip or jar ????
  private ZipArchiver archiver;

  /**
   * Single directory for extra files to include in the ZIP.
   *
   * @parameter expression="${basedir}/src/main/resources/org/apache/myfaces/tobago/renderkit"
   * @required
   */
  private File warSourceDirectory;

  public File getWarSourceDirectory() {
    return warSourceDirectory;
  }

  public File getWebappDirectory() {
    return webappDirectory;
  }

  /**
   * The maven archive configuration to use.
   *
   * @parameter
   */
  private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

  public static final String WEB_INF = "WEB-INF";

  /**
   * Executes the WarMojo on the current project.
   *
   * @throws MojoExecutionException
   *          if an error occured while building the webapp
   */
  public void execute()
      throws MojoExecutionException {
    File jarFile = new File(outputDirectory, jarName +"-THEME.jar");

    try {
      performPackaging(jarFile);
      projectHelper.attachArtifact(getProject(), "jar", "THEME", jarFile);

    } catch (Exception e) {
      // TODO: improve error handling
      throw new MojoExecutionException("Error assembling theme", e);
    }
  }


  private void performPackaging(File jarFile)
      throws IOException, ArchiverException,
      MojoExecutionException {
    buildExplodedTheme(getWebappDirectory());

    getLog().info("Generating theme " + jarFile.getAbsolutePath());
    archiver.addDirectory(getWebappDirectory(), getIncludes(), getExcludes());
    archiver.setDestFile(jarFile);
    archiver.createArchive();
  }

  public void buildExplodedTheme(File zipDirectory)
      throws MojoExecutionException {
    getLog().info("Exploding theme...");
    zipDirectory.mkdirs();


    try {
      copyResources(getWarSourceDirectory(), zipDirectory);

      buildTheme(getProject(), getWebappDirectory());
    } catch (IOException e) {
      throw new MojoExecutionException("Could not explode theme...", e);
    }
  }

  public void copyResources(File sourceDirectory, File webappDirectory)
      throws IOException {
    if (!sourceDirectory.equals(webappDirectory)) {
      getLog().info("Copy theme resources to " + webappDirectory.getAbsolutePath());
      if (getWarSourceDirectory().exists()) {
        String[] fileNames = getThemeFiles(sourceDirectory);
        for (int i = 0; i < fileNames.length; i++) {
          FileUtils.copyFile(new File(sourceDirectory, fileNames[i]),
              new File(webappDirectory, "tobago/" + fileNames[i]));
        }
      }
    }
  }

  public void buildTheme(MavenProject project, File webappDirectory)
      throws IOException {
    getLog().info("Assembling theme " + project.getArtifactId() + " in " + webappDirectory);

    File libDirectory = new File(webappDirectory, WEB_INF + "/lib");
    libDirectory.mkdirs();
   // List artifacts =

    //for (Iterator iter = artifacts.iterator(); iter.hasNext();) {
      Artifact artifact = project.getArtifact();
      getLog().debug(artifact.toString());
      FileUtils.copyFile(artifact.getFile(), new File(libDirectory, artifact.getFile().getName()));
    //}
  }


  protected String[] getExcludes() {
    List excludeList = new ArrayList(FileUtils.getDefaultExcludesAsList());
    return (String[]) excludeList.toArray(new String[]{});
  }

  protected String[] getIncludes() {
    return new String[]{"**"};
  }

}
