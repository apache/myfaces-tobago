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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class MockHttpServletResponse implements HttpServletResponse {

  public void addCookie(final Cookie cookie) {
  }

  public void addDateHeader(final String s, final long l) {
  }

  public void addHeader(final String s, final String s1) {
  }

  public void addIntHeader(final String s, final int i) {
  }

  public boolean containsHeader(final String s) {
    return false;
  }

  public String encodeRedirectURL(final String s) {
    return null;
  }

  /** @deprecated */
  public String encodeRedirectUrl(final String s) {
    return null;
  }

  public String encodeURL(final String s) {
    return s;
  }

  /** @deprecated */
  public String encodeUrl(final String s) {
    return s;
  }

  public void flushBuffer() throws IOException {
  }

  public int getBufferSize() {
    return 0;
  }

  public String getCharacterEncoding() {
    return null;
  }

  public Locale getLocale() {
    return null;
  }

  public ServletOutputStream getOutputStream() throws IOException {
    return null;
  }

  public PrintWriter getWriter() throws IOException {
    return null;
  }

  public boolean isCommitted() {
    return false;
  }

  public void reset() {
  }

  public void resetBuffer() {
  }

  public void sendError(final int i) throws IOException {
  }

  public void sendError(final int i, final String s) throws IOException {
  }

  public void sendRedirect(final String s) throws IOException {
  }

  public void setBufferSize(final int i) {
  }

  public void setContentLength(final int i) {
  }

  public void setContentType(final String s) {
  }

  public void setDateHeader(final String s, final long l) {
  }

  public void setHeader(final String s, final String s1) {
  }

  public void setIntHeader(final String s, final int i) {
  }

  public void setLocale(final Locale locale) {
  }

  /** @deprecated */
  public void setStatus(final int i) {
  }

  public void setStatus(final int i, final String s) {
  }

  public String getContentType() {
    return null;
  }

  public void setCharacterEncoding(final String reference) {
  }
}
