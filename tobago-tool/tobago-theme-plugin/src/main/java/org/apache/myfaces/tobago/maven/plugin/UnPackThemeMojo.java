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

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.archiver.zip.ZipEntry;
import org.codehaus.plexus.archiver.zip.ZipFile;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

/**
 *
 * @version $Id$
 * @goal resources
 * @phase process-resources
 * @requiresDependencyResolution runtime
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
   * Don't use the versioned information of the themes..
   *
   * @parameter
   */
  private boolean ignoreVersioned;


  private String getThemeDescriptor(File jarFile) throws MojoExecutionException {
    ZipFile zip = null;
    try {
      zip = new ZipFile(jarFile);
      Enumeration files = zip.getEntries();
      while (files.hasMoreElements()) {
        ZipEntry nextEntry = (ZipEntry) files.nextElement();
        if (nextEntry == null || nextEntry.isDirectory()) {
          continue;
        }
        String name = nextEntry.getName();
        if (name.equals("META-INF/tobago-theme.xml") || name.equals("META-INF/tobago-config.xml")) {
          XmlStreamReader xsr = null;
          try {
            StringWriter stringWriter = new StringWriter();
            xsr = ReaderFactory.newXmlReader(zip.getInputStream(nextEntry));
            IOUtil.copy(xsr, stringWriter);
            return stringWriter.toString();
          } finally {
            IOUtil.close(xsr);
          }
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
    return null;
  }

  public void execute() throws MojoExecutionException {
    try {
      Iterator artifacts =  getProject().getRuntimeClasspathElements().iterator();
      if (!workDirectory.exists()) {
        workDirectory.mkdirs();
      }
      while (artifacts.hasNext()) {

        String artifact = (String) artifacts.next();
        if (getLog().isDebugEnabled()) {
          getLog().debug("Testing jar "+ artifact);
        }

        File file = new File(artifact);
        if (file.isFile() && artifact.endsWith(".jar")) {
          String descriptor = getThemeDescriptor(file);
          if (descriptor != null) {

            String name = file.getName();
            File tempLocation = new File(workDirectory, name.substring(0, name.length() - 4));
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
                  Xpp3Dom xpp3Dom = Xpp3DomBuilder.build(new StringReader(descriptor));
                  Xpp3Dom themeDefinitions = xpp3Dom.getChild("theme-definitions");
                  if (themeDefinitions != null && !ignoreVersioned) {
                    for (Xpp3Dom themeDefinition : themeDefinitions.getChildren()) {
                      Xpp3Dom versionedDom = themeDefinition.getChild("versioned");
                      if (versionedDom != null) {
                        boolean versioned = Boolean.parseBoolean(versionedDom.getValue());
                        if (versioned) {
                          Xpp3Dom resourcePathDom = themeDefinition.getChild("resource-path");
                          resourcePath = resourcePathDom.getValue();
                          Properties properties = new Properties();
                          String metaInf = tempLocation + "/META-INF/MANIFEST.MF";
                          properties.load(new StringReader(FileUtils.fileRead(metaInf)));
                          version = properties.getProperty("Implementation-Version");
                          if (version == null) {
                            getLog().error("No Implementation-Version found in Manifest-File in " + name);
                          }
                        }
                      }
                    }
                  }
                } catch (IOException e) {
                  getLog().error(e);
                } catch (XmlPullParserException e) {
                  getLog().error(e);
                }
                if (getLog().isDebugEnabled()) {
                  getLog().debug("Expanding theme: "+ name);
                  getLog().debug("Version: " + version);
                  getLog().debug("ResourcePath: " + resourcePath);
                }
                String[] fileNames = getThemeFiles(tempLocation);
                for (int i = 0, fileNamesLength = fileNames.length; i < fileNamesLength; i++) {
                  String fileName = fileNames[i];
                  File fromFile = new File(tempLocation, fileName);
                  String toFileName = fileName;
                  if (resourcePath != null && version != null && toFileName.startsWith(resourcePath)) {
                    toFileName = resourcePath + "/" + version + "/" +toFileName.substring(resourcePath.length()+1);
                  }
                  if (getLog().isDebugEnabled()) {
                    getLog().debug("Copy file " + fromFile + " to: " + toFileName);
                  }
                  File toFile = new File(webappDirectory, toFileName);
                  try {
                    FileUtils.copyFile(fromFile, toFile);
                  } catch (IOException e) {
                    throw new MojoExecutionException("Error copy file: " + fromFile + " to: " + toFile, e);
                  }
                }
              } catch (NoSuchArchiverException e) {
                getLog().info("Skip unpacking dependency file with unknown extension: " + file.getPath());
              }
            }
          }
        }
      }
    } catch (DependencyResolutionRequiredException drre) {
      throw new MojoExecutionException(drre.getMessage(), drre);
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
    } catch (ArchiverException e) {
      throw new MojoExecutionException("Error unpacking file: " + file + "to: " + location, e);
    }
  }
}


