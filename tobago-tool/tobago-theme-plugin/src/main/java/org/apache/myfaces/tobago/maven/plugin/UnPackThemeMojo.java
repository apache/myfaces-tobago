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

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Mojo(
  name = "resources",
  defaultPhase = LifecyclePhase.PROCESS_RESOURCES,
  requiresDependencyResolution = ResolutionScope.RUNTIME)
public class UnPackThemeMojo extends AbstractThemeMojo {

  /**
   * To look up Archiver/UnArchiver implementations
   */
  @Component
  private ArchiverManager archiverManager;

  /**
   * Directory to unpack JARs into if needed
   */
  @Parameter(defaultValue = "${project.build.directory}/theme/work", required = true)
  private File workDirectory;

  /**
   * The directory where the webapp is built.
   */
  @Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}", required = true)
  private File webappDirectory;

  /**
   * Don't use the versioned information of the themes..
   */
  @Parameter
  private boolean ignoreVersioned;


  private String getThemeDescriptor(final File jarFile) throws MojoExecutionException {
    try (ZipFile zip = new ZipFile(jarFile)) {
      final Enumeration<? extends ZipEntry> files = zip.entries();
      while (files.hasMoreElements()) {
        final ZipEntry nextEntry = files.nextElement();
        if (nextEntry == null || nextEntry.isDirectory()) {
          continue;
        }
        final String name = nextEntry.getName();
        if (name.equals("META-INF/tobago-theme.xml") || name.equals("META-INF/tobago-config.xml")) {
          try (XmlStreamReader xsr = ReaderFactory.newXmlReader(zip.getInputStream(nextEntry))) {
            final StringWriter stringWriter = new StringWriter();
            IOUtil.copy(xsr, stringWriter);
            return stringWriter.toString();
          }
        }
      }
    } catch (final IOException e) {
      throw new MojoExecutionException("Error find ThemeDescriptor in " + jarFile, e);
    }
    // ignore
    return null;
  }

  public void execute() throws MojoExecutionException {
    try {
      final Iterator<String> artifacts = getProject().getRuntimeClasspathElements().iterator();
      if (!workDirectory.exists()) {
        workDirectory.mkdirs();
      }
      while (artifacts.hasNext()) {

        final String artifact = artifacts.next();
        if (getLog().isDebugEnabled()) {
          getLog().debug("Testing jar " + artifact);
        }

        final File file = new File(artifact);
        if (file.isFile() && artifact.endsWith(".jar")) {
          final String descriptor = getThemeDescriptor(file);
          if (descriptor != null) {

            final String name = file.getName();
            final File tempLocation = new File(workDirectory, name.substring(0, name.length() - 4));
            boolean process = false;
            if (!tempLocation.exists()) {
              tempLocation.mkdirs();
              process = true;
            } else if (file.lastModified() > tempLocation.lastModified()) {
              process = true;
            }
            if (process) {
              try {
                unpack(file, tempLocation);
                String version = null;
                String resourcePath = null;
                try {
                  final Xpp3Dom xpp3Dom = Xpp3DomBuilder.build(new StringReader(descriptor));
                  final Xpp3Dom themeDefinitions = xpp3Dom.getChild("theme-definitions");
                  if (themeDefinitions != null && !ignoreVersioned) {
                    for (final Xpp3Dom themeDefinition : themeDefinitions.getChildren()) {
                      final Xpp3Dom versionedDom = themeDefinition.getChild("versioned");
                      if (versionedDom != null) {
                        final boolean versioned = Boolean.parseBoolean(versionedDom.getValue());
                        if (versioned) {
                          final Xpp3Dom resourcePathDom = themeDefinition.getChild("resource-path");
                          resourcePath = resourcePathDom.getValue();
                          final Properties properties = new Properties();
                          final String metaInf = tempLocation + "/META-INF/MANIFEST.MF";
                          properties.load(new ByteArrayInputStream(FileUtils.fileRead(metaInf).getBytes()));
                          version = properties.getProperty("Implementation-Version");
                          if (version == null) {
                            getLog().error("No Implementation-Version found in Manifest-File for theme: '"
                              + name + "'.");
                          }
                        }
                      }
                    }
                  }
                } catch (final IOException | XmlPullParserException e) {
                  getLog().error(e);
                }
                if (getLog().isDebugEnabled()) {
                  getLog().debug("Expanding theme: " + name);
                  getLog().debug("Version: " + version);
                  getLog().debug("ResourcePath: " + resourcePath);
                }
                final String[] fileNames = getThemeFiles(tempLocation);
                for (final String fileName : fileNames) {
                  final File fromFile = new File(tempLocation, fileName);
                  String toFileName = fileName;
                  if (resourcePath != null && version != null && toFileName.startsWith(resourcePath)
                    && !fileName.endsWith("blank.html")) {
                    toFileName = resourcePath + "/" + version + "/" + toFileName.substring(resourcePath.length() + 1);
                  }
                  if (getLog().isDebugEnabled()) {
                    getLog().debug("Copy file " + fromFile + " to: " + toFileName);
                  }
                  final File toFile = new File(webappDirectory, toFileName);
                  try {
                    FileUtils.copyFile(fromFile, toFile);
                  } catch (final IOException e) {
                    throw new MojoExecutionException("Error copy file: " + fromFile + " to: " + toFile, e);
                  }
                }
              } catch (final NoSuchArchiverException e) {
                getLog().info("Skip unpacking dependency file with unknown extension: " + file.getPath());
              }
            }
          }
        }
      }
    } catch (final DependencyResolutionRequiredException drre) {
      throw new MojoExecutionException(drre.getMessage(), drre);
    }
  }

  private void unpack(final File file, final File location)
    throws MojoExecutionException, NoSuchArchiverException {
    final String archiveExt = FileUtils.getExtension(file.getAbsolutePath()).toLowerCase(Locale.ENGLISH);
    try {
      final UnArchiver unArchiver = archiverManager.getUnArchiver(archiveExt);
      unArchiver.setSourceFile(file);
      unArchiver.setDestDirectory(location);
      unArchiver.extract();
    } catch (final ArchiverException e) {
      throw new MojoExecutionException("Error unpacking file: " + file + "to: " + location, e);
    }
  }
}


