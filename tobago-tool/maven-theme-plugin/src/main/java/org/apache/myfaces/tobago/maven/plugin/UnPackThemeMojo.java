package org.apache.myfaces.tobago.maven.plugin;

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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

/**
 *
 * @version $Id$
 * @goal resources
 * @phase process-resources
 * @requiresDependencyResolution compile
 */
public class UnPackThemeMojo extends AbstractThemeMojo {
  /**
   * To look up Archiver/UnArchiver implementations
   *
   * @parameter expression="${component.org.codehaus.plexus.archiver.manager.ArchiverManager}"
   * @required
   */
  private ArchiverManager archiverManager;

  /**
   * Directory to unpack JARs into if needed
   *
   * @parameter expression="${project.build.directory}/theme/work"
   * @required
   */
  private File workDirectory;

  /**
   * The directory where the webapp is built.
   *
   * @parameter expression="${project.build.directory}/${project.build.finalName}"
   * @required
   */
  private File webappDirectory;

  /**
   * @parameter expression="${plugin.artifacts}"
   * @required
   */
  private List pluginArtifacts;

  private boolean findThemeDescriptor(File jarFile) throws MojoExecutionException {
    ZipInputStream zip = null;
    try {
      zip = new ZipInputStream(new FileInputStream(jarFile));
      while (zip.available() > 0) {
        ZipEntry nextEntry = zip.getNextEntry();
        if (nextEntry == null || nextEntry.isDirectory()) {
          continue;
        }
        String name = nextEntry.getName();
        if (name.equals("META-INF/tobago-theme.xml")) {
          return true;
        }
      }
    } catch (IOException e) {
      throw new MojoExecutionException("Error find ThemeDescriptor in " + jarFile, e);
    } finally {
      if (zip != null) {
        try {
          zip.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }

    return false;
  }

  public void execute() throws MojoExecutionException {

    Iterator artifacts =  getProject().getDependencyArtifacts().iterator();
    while (artifacts.hasNext()) {
      if (!workDirectory.exists()) {
        workDirectory.mkdirs();
      }
      Artifact artifact = (Artifact) artifacts.next();
      getLog().debug("Expanding theme "+ artifact);
      File file = artifact.getFile();
      if (Artifact.SCOPE_COMPILE.equals(artifact.getScope()) && file.isFile()
          && "jar".equals(artifact.getType()) && findThemeDescriptor(file)) {

        String name = file.getName();
        getLog().debug("Expanding theme "+ name);
        File tempLocation = new File(workDirectory, name.substring(0, name.length() - 4));
        boolean process = false;
        if (!tempLocation.exists()) {
          tempLocation.mkdirs();
          process = true;
        } else if (artifact.getFile().lastModified() > tempLocation.lastModified()) {
          process = true;
        }
        if (process) {
          try {
            unpack(file, tempLocation);
            String[] fileNames = getThemeFiles(tempLocation);
            for (String fileName : fileNames) {

              File fromFile = new File(tempLocation, fileName);
              File toFile = new File(webappDirectory, fileName);
              try {
                FileUtils.copyFile(fromFile, toFile);
              } catch (IOException e) {
                throw new MojoExecutionException("Error copy file: " + fromFile + "to: " + toFile, e);
              }
            }
          } catch (NoSuchArchiverException e) {
            getLog().info("Skip unpacking dependency file with unknown extension: " + file.getPath());
          }
        }
      }
    }
  }

  private void unpack(File file, File location)
      throws MojoExecutionException, NoSuchArchiverException {
    String archiveExt = FileUtils.getExtension(file.getAbsolutePath()).toLowerCase(Locale.ENGLISH);
    try {
      UnArchiver unArchiver = archiverManager.getUnArchiver(archiveExt);
      unArchiver.setSourceFile(file);
      unArchiver.setDestDirectory(location);
      unArchiver.extract();
    } catch (IOException e) {
      throw new MojoExecutionException("Error unpacking file: " + file + "to: " + location, e);
    } catch (ArchiverException e) {
      throw new MojoExecutionException("Error unpacking file: " + file + "to: " + location, e);
    }
  }
}


