package com.atanion.tobago.webapp;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.ServletOutputStream;
import java.util.Locale;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 29, 2004 4:48:29 PM
 * User: bommel
 * $Id$
 */
public class TobagoResponse extends HttpServletResponseWrapper {

  private static final Log LOG = LogFactory.getLog(TobagoResponse.class);

  private PrintWriter printWriter = null;
  private StringWriter  bufferedWriter = null;


  public TobagoResponse(HttpServletResponse base) {
    super(base);
  }

  public void setBuffering() {
    if (bufferedWriter == null) {
      bufferedWriter = new StringWriter();
      printWriter = new PrintWriter(bufferedWriter) ;
    }
  }
  public String getBufferedString() {
    if (bufferedWriter !=null) {
      printWriter.flush();
      return bufferedWriter.toString();
    } else {
      return "";
    }
  }
  public ServletOutputStream getOutputStream() throws IOException {
    LOG.info("***** getOutputStream() from " + new Exception().getStackTrace()[1]);
    return getResponse().getOutputStream();
  }

  public PrintWriter getWriter() throws IOException {
    LOG.info("***** getWriter() from " + new Exception().getStackTrace()[1]);
    if (printWriter != null) {
      return printWriter;
    }
    return getResponse().getWriter();
  }


  public void setContentType(String s) {
    LOG.debug("***** setContentType(" + s + ") from " + new Exception().getStackTrace()[1]);
    getResponse().setContentType(s);
  }


}
