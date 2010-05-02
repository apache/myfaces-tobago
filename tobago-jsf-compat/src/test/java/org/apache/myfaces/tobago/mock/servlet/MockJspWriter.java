package org.apache.myfaces.tobago.mock.servlet;

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

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MockJspWriter extends JspWriter {

  private PrintWriter out;

  public MockJspWriter(PrintWriter out) {
    super(100, true);
    this.out = out;
  }

  public boolean checkError() {
    return out.checkError();
  }

  public void print(boolean b) {
    out.print(b);
  }

  public void print(char c) {
    out.print(c);
  }

  public void print(int i) {
    out.print(i);
  }

  public void print(long l) {
    out.print(l);
  }

  public void print(float f) {
    out.print(f);
  }

  public void print(double d) {
    out.print(d);
  }

  public void print(char[] s) {
    out.print(s);
  }

  public void print(String s) {
    out.print(s);
  }

  public void print(Object obj) {
    out.print(obj);
  }

  public void println() {
    out.println();
  }

  public void println(boolean x) {
    out.println(x);
  }

  public void println(char x) {
    out.println(x);
  }

  public void println(int x) {
    out.println(x);
  }

  public void println(long x) {
    out.println(x);
  }

  public void println(float x) {
    out.println(x);
  }

  public void println(double x) {
    out.println(x);
  }

  public void println(char[] x) {
    out.println(x);
  }

  public void println(String x) {
    out.println(x);
  }

  public void println(Object x) {
    out.println(x);
  }

  public void write(int c) throws IOException {
    out.write(c);
  }

  public void write(char[] cbuf) throws IOException {
    out.write(cbuf);
  }

  public void write(String str) throws IOException {
    out.write(str);
  }

  public void write(char[] cbuf, int off, int len) throws IOException {
    out.write(cbuf, off, len);
  }

  public void write(String str, int off, int len) throws IOException {
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
