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

package org.apache.myfaces.tobago.ant.sniplet;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;

/**
 * This task extracts lines from source fileSets marked with a tag. Such a sniplet starts with
 * <p/>
 * code-sniplet-start id="id"
 * <p/>
 * and ends with
 * <p/>
 * code-sniplet-end id="id"
 * <p/>
 * Its allowed to have nested and even entangled tags. Each sniplet gets ist own output file
 * in the output directory. The name of the file is [id].txt.
 * <p/>
 * The fileSets to process are specified by a fileset.
 * <p/>
 * Example:
 * <pre>
 * &lt;target name="code-extract">
 *   &lt;taskdef name="code-extract" classname="org.apache.myfaces.tobago.ant.sniplet.CodeSnipletExtractTask"/>
 *   &lt;code-extract outputDir="build/sniplets">
 *     &lt;fileset dir="src">
 *       &lt;include name="*.java"/>
 *     &lt;/fileset>
 *   &lt;/code-extract>
 * &lt;/target>
 * </pre>
 */
public class CodeSnipletExtractTask extends Task {

  private List<CodeSniplet> sniplets;
  private List<FileSet> fileSets;
  private Pattern startPattern;
  private Pattern endPattern;
  private File outputDir;
  private String outputFileNamePattern;
  private boolean stripLeadingSpaces;

  public void init() throws BuildException {
    startPattern = Pattern.compile(".*code-sniplet-start\\s*id\\s*=\\s*\"(\\w*)\".*");
    endPattern = Pattern.compile(".*code-sniplet-end\\s*id\\s*=\\s*\"(\\w*)\".*");
    this.sniplets = new ArrayList<CodeSniplet>();
    this.fileSets = new ArrayList<FileSet>();
  }

  public String getOutputFileNamePattern() {
    return outputFileNamePattern;
  }

  public void setOutputFileNamePattern(String outputFileNamePattern) {
    this.outputFileNamePattern = outputFileNamePattern;
  }

  public File getOutputDir() {
    return outputDir;
  }

  public void setOutputDir(File outputDir) {
    this.outputDir = outputDir;
  }

  public void addConfiguredFileSet(FileSet fileSet) {
    this.fileSets.add(fileSet);
  }

  public boolean isStripLeadingSpaces() {
    return stripLeadingSpaces;
  }

  public void setStripLeadingSpaces(boolean stripLeadingSpaces) {
    this.stripLeadingSpaces = stripLeadingSpaces;
  }

  public void execute() throws BuildException {
    for (int k = 0; k < fileSets.size(); k++) {
      FileSet fileSet = fileSets.get(k);
      DirectoryScanner dirScanner = fileSet.getDirectoryScanner(getProject());
      dirScanner.scan();
      String[] includedFiles = dirScanner.getIncludedFiles();
      for (int i = 0; i < includedFiles.length; i++) {
        String fileS = includedFiles[i];
        LineNumberReader in = null;
        try {
          in = new LineNumberReader(new FileReader(fileSet.getDir(getProject())
              + File.separator + fileS));
          String line = in.readLine();
          while (line != null) {
            Matcher startMatcher = startPattern.matcher(line);
            if (startMatcher.matches()) {
              startSniplet(startMatcher.group(1), fileS, in.getLineNumber());
            } else {
              Matcher endMatcher = endPattern.matcher(line);
              if (endMatcher.matches()) {
                endSniplet(endMatcher.group(1), fileS, in.getLineNumber());
              } else {
                addLine(line);
              }
            }
            line = in.readLine();
          }
          for (int j = 0; j < sniplets.size(); j++) {
            CodeSniplet codeSniplet = (CodeSniplet) sniplets.get(j);
            if (codeSniplet.getLineEnd() == 0) {
              codeSniplet.setLineEnd(in.getLineNumber());
              log("Unclosed sniplet '" + codeSniplet.getId() + "' in file '" + codeSniplet.getFileName() + "' at line '"
                  + codeSniplet.getLineStart() + "'. Forcing close", Project.MSG_WARN);
            }
          }
          createOutput();
          sniplets = new ArrayList<CodeSniplet>();
        } catch (IOException e) {
          throw new BuildException(e);
        } finally {
          if (in != null) {
            try {
              in.close();
            } catch (IOException e) {
              throw new BuildException(e);
            }
          }
        }
      }
    }
  }

  private void createOutput() throws FileNotFoundException {
    for (int i = 0; i < sniplets.size(); i++) {
      CodeSniplet codeSniplet = (CodeSniplet) sniplets.get(i);
      String fileName = codeSniplet.getId() + ".snip";
      File file = new File(outputDir, fileName);
      PrintWriter out = new PrintWriter(new FileOutputStream(file));
      StringBuffer code = codeSniplet.getCode(stripLeadingSpaces);
      out.print(code);
      out.close();
      log("Wrote: " + file.getName(), Project.MSG_INFO);
    }
  }

  private void startSniplet(String id, String fileName, int lineNumber) {
    for (int i = 0; i < sniplets.size(); i++) {
      CodeSniplet codeSniplet = (CodeSniplet) sniplets.get(i);
      if (codeSniplet.getId().equals(id)) {
        throw new BuildException("Duplicate sniplet declaration '" + id + "' in file '" + fileName + "' at line '"
            + lineNumber + "'. First declaration was in file '" + codeSniplet.getFileName() + "' at line '"
            + codeSniplet.getLineStart() + "'.");
      }
    }
    CodeSniplet codeSniplet = new CodeSniplet(id, fileName, lineNumber);
    sniplets.add(codeSniplet);
  }

  private void endSniplet(String id, String fileName, int lineNumber) {
    for (int i = 0; i < sniplets.size(); i++) {
      CodeSniplet codeSniplet = (CodeSniplet) sniplets.get(i);
      if (codeSniplet.getId().equals(id)) {
        codeSniplet.setLineEnd(lineNumber);
        return;
      }
    }
    throw new BuildException("No start of sniplet '" + id + "' in file '" + fileName + "' at line '" + lineNumber
        + "' found.");
  }

  private void addLine(String line) {
    for (int i = 0; i < sniplets.size(); i++) {
      CodeSniplet codeSniplet = (CodeSniplet) sniplets.get(i);
      if (codeSniplet.getLineEnd() == 0) {
        codeSniplet.addLine(line);
        log("Adding: " + line + " -> " + codeSniplet.getFileName() + ":" + codeSniplet.getId(), Project.MSG_DEBUG);
      }
    }
  }

}
