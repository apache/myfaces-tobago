/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 30.01.2003 17:00:56.
 * $Id$
 */
package com.atanion.tobago.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class TobagoMultipartFormdataFilter implements Filter {

// ///////////////////////////////////////////// constant

  private static final Log LOG
      = LogFactory.getLog(TobagoMultipartFormdataFilter.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void doFilter(
      ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
//    Log.debug("filter filter ....");

    ServletRequest wrapper;
    if (request instanceof HttpServletRequest) {
      if (request instanceof TobagoMultipartFormdataRequest) {
        wrapper = request;
      } else {
        String contentType = request.getContentType();
        if (contentType != null &&
            contentType.toLowerCase().startsWith("multipart/form-data")) {
          wrapper = new TobagoMultipartFormdataRequest(
              (HttpServletRequest) request);
        } else {
          wrapper = request;
        }
      }
    } else {
      LOG.error("Not implemented for non HttpServletRequest");
      wrapper = request;
    }

    WrappedResponse wrappedResponse = new WrappedResponse((HttpServletResponse) response);
    chain.doFilter(wrapper, wrappedResponse);
  }

  public void destroy() {
  }

private static class WrappedResponse implements HttpServletResponse {
  private HttpServletResponse base;

  public WrappedResponse(HttpServletResponse base) {
    this.base = base;
  }

  public Locale getLocale() {
    return base.getLocale();
  }

  public String getCharacterEncoding() {
    return base.getCharacterEncoding();
  }

  public ServletOutputStream getOutputStream() throws IOException {
    LOG.info("***** getOutputStream() from " + new Exception().getStackTrace()[1]);
    return base.getOutputStream();
  }

  public PrintWriter getWriter() throws IOException {
    LOG.info("***** getWriter() from " + new Exception().getStackTrace()[1]);
    return base.getWriter();
  }

  public void setContentLength(int i) {
    base.setContentLength(i);
  }

  public void setContentType(String s) {
    LOG.debug("***** setContentType(" + s + ") from " + new Exception().getStackTrace()[1]);
    base.setContentType(s);
  }

  public void setBufferSize(int i) {
    base.setBufferSize(i);
  }

  public int getBufferSize() {
    return base.getBufferSize();
  }

  public void flushBuffer() throws IOException {
    base.flushBuffer();
  }

  public void resetBuffer() {
    base.resetBuffer();
  }

  public boolean isCommitted() {
    return base.isCommitted();
  }

  public void reset() {
    base.reset();
  }

  public void setLocale(Locale locale) {
    base.setLocale(locale);
  }

  public void addCookie(Cookie cookie) {
    base.addCookie(cookie);
  }

  public boolean containsHeader(String s) {
    return base.containsHeader(s);
  }

  public String encodeURL(String s) {
    return base.encodeURL(s);
  }

  public String encodeRedirectURL(String s) {
    return base.encodeRedirectURL(s);
  }

  public String encodeUrl(String s) {
    return base.encodeUrl(s);
  }

  public String encodeRedirectUrl(String s) {
    return base.encodeRedirectUrl(s);
  }

  public void sendError(int i, String s) throws IOException {
    base.sendError(i, s);
  }

  public void sendError(int i) throws IOException {
    base.sendError(i);
  }

  public void sendRedirect(String s) throws IOException {
    base.sendRedirect(s);
  }

  public void setDateHeader(String s, long l) {
    base.setDateHeader(s, l);
  }

  public void addDateHeader(String s, long l) {
    base.addDateHeader(s, l);
  }

  public void setHeader(String s, String s1) {
    base.setHeader(s, s1);
  }

  public void addHeader(String s, String s1) {
    base.addHeader(s, s1);
  }

  public void setIntHeader(String s, int i) {
    base.setIntHeader(s, i);
  }

  public void addIntHeader(String s, int i) {
    base.addIntHeader(s, i);
  }

  public void setStatus(int i) {
    base.setStatus(i);
  }

  public void setStatus(int i, String s) {
    base.setStatus(i, s);
  }
}


}
