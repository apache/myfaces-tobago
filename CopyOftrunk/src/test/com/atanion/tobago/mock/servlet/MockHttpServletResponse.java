/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 25.08.2004 11:10:04.
 * $Id: MockHttpServletResponse.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package com.atanion.tobago.mock.servlet;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class MockHttpServletResponse implements HttpServletResponse {

  public void addCookie(Cookie cookie) {
  }

  public void addDateHeader(String s, long l) {
  }

  public void addHeader(String s, String s1) {
  }

  public void addIntHeader(String s, int i) {
  }

  public boolean containsHeader(String s) {
    return false;
  }

  public String encodeRedirectURL(String s) {
    return null;
  }

  /** @deprecated */
  public String encodeRedirectUrl(String s) {
    return null;
  }

  public String encodeURL(String s) {
    return s;
  }

  /** @deprecated */
  public String encodeUrl(String s) {
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

  public void sendError(int i) throws IOException {
  }

  public void sendError(int i, String s) throws IOException {
  }

  public void sendRedirect(String s) throws IOException {
  }

  public void setBufferSize(int i) {
  }

  public void setContentLength(int i) {
  }

  public void setContentType(String s) {
  }

  public void setDateHeader(String s, long l) {
  }

  public void setHeader(String s, String s1) {
  }

  public void setIntHeader(String s, int i) {
  }

  public void setLocale(Locale locale) {
  }

  /** @deprecated */
  public void setStatus(int i) {
  }

  public void setStatus(int i, String s) {
  }

  public String getContentType() {
    return null;
  }

  public void setCharacterEncoding(String reference) {
  }
}
