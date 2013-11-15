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

package org.apache.myfaces.tobago.internal.mock.servlet;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MockJspWriter extends JspWriter {

  private PrintWriter out;

  public MockJspWriter(final PrintWriter out) {
    super(100, true);
    this.out = out;
  }

  public boolean checkError() {
    return out.checkError();
  }

  public void print(final boolean b) {
    out.print(b);
  }

  public void print(final char c) {
    out.print(c);
  }

  public void print(final int i) {
    out.print(i);
  }

  public void print(final long l) {
    out.print(l);
  }

  public void print(final float f) {
    out.print(f);
  }

  public void print(final double d) {
    out.print(d);
  }

  public void print(final char[] s) {
    out.print(s);
  }

  public void print(final String s) {
    out.print(s);
  }

  public void print(final Object obj) {
    out.print(obj);
  }

  public void println() {
    out.println();
  }

  public void println(final boolean x) {
    out.println(x);
  }

  public void println(final char x) {
    out.println(x);
  }

  public void println(final int x) {
    out.println(x);
  }

  public void println(final long x) {
    out.println(x);
  }

  public void println(final float x) {
    out.println(x);
  }

  public void println(final double x) {
    out.println(x);
  }

  public void println(final char[] x) {
    out.println(x);
  }

  public void println(final String x) {
    out.println(x);
  }

  public void println(final Object x) {
    out.println(x);
  }

  public void write(final int c) throws IOException {
    out.write(c);
  }

  public void write(final char[] cbuf) throws IOException {
    out.write(cbuf);
  }

  public void write(final String str) throws IOException {
    out.write(str);
  }

  public void write(final char[] cbuf, final int off, final int len) throws IOException {
    out.write(cbuf, off, len);
  }

  public void write(final String str, final int off, final int len) throws IOException {
    out.write(str, off, len);
  }

  public void flush() throws IOException {
    out.flush();
  }

  public void close() throws IOException {
    out.close();
  }

  public void clear() throws IOException {
  }

  public void clearBuffer() throws IOException {
  }

  public int getRemaining() {
    return 0;
  }

  public void newLine() throws IOException {
    print(System.getProperty("line.separator"));
  }

}
