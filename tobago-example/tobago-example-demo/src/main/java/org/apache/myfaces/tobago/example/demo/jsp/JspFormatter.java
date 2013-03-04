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

package org.apache.myfaces.tobago.example.demo.jsp;

import javax.servlet.jsp.JspWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;

public class JspFormatter {

  public static void main(String[] args) throws Exception {
    writeJsp(new FileReader(args[0]), new PrintWriter(System.out));
  }

  public static void writeJsp(String filename, JspWriter out)
      throws IOException {
    InputStream in
        = JspFormatter.class.getClassLoader().getResourceAsStream(filename);
    if (in == null) {
      throw new FileNotFoundException(
          "Resource not found in classpath: filename = '" + filename + "'");
    }
    writeJsp(new InputStreamReader(in), new PrintWriter(out));
  }

  public static void writeJsp(Reader reader, PrintWriter out)
      throws IOException {
    JspTagConverter formatter = new JspTagConverter();
    String source = readJsp(reader);
    out.println("<html><head>");
    // out.println("<link rel=\"stylesheet\" href=\"jsp.css\" type=\"text/css\">");
    out.println("<style>");
    out.println("body {background-color: white;}");
    out.println(".jsp-comment {background-color: rgb(227,227,227); color: rgb(128,128,128);}");
    out.println(".jsp-directive {background-color:  rgb(237,255,237);}");
    out.println(".jsp-declaration {background-color: rgb(255,252,228);}");
    out.println(".jsp-scriptlet {background-color: rgb(255,252,228); color: black; font-weight: normal;}");
    out.println(".jsp-tag {background-color: rgb(255,252,228);}");
    out.println(".html-tag {background-color: rgb(239,239,239);}");
    out.println(".string {color: rgb(0,128,0); font-weight: bold;}");
    out.println(".keyword {color: rgb(0,0,128); font-weight: bold;}");
    out.println("</style>");
    out.println("<title>formatted jsp code</title></head><body><pre>");
    out.println(formatter.convert(source));
    out.println("</pre></body></html>");
    out.flush();
  }

  private static String readJsp(Reader reader) throws IOException {
    LineNumberReader in = new LineNumberReader(reader);
    StringBuilder buffer = new StringBuilder();
    String line = null;
    while (null != (line = in.readLine())) {
      buffer.append(line);
      buffer.append("\n");
    }
    in.close();
    return buffer.toString();
  }

}
