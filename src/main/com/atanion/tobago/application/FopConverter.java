/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 30.11.2004 13:24:45.
 * $Id$
 */
package com.atanion.tobago.application;

import com.atanion.tobago.util.CommonsLoggingLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.Driver;
import org.xml.sax.InputSource;

import javax.faces.FacesException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.StringReader;

public class FopConverter {

  private static final Log LOG = LogFactory.getLog(FopConverter.class);

  public static void fo2Pdf(ServletResponse servletResponse, String buffer) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("buffer = '" + buffer + "'");
    }
    try {
      Driver driver = new Driver();
      Logger logger = new CommonsLoggingLogger(LOG);
      driver.setLogger(logger);
      driver.setRenderer(Driver.RENDER_PDF);
      driver.setErrorDump(true);
//      driver.setInputSource(new InputSource(new FileInputStream("C:/simple.fo")));
//      driver.setInputSource(new InputSource(new StringReader(fo)));

      if (LOG.isDebugEnabled()) {
        LOG.debug("bufferString = '" + buffer + "'");

      }
      LOG.error("bufferString = '" + buffer + "'");
      driver.setInputSource(new InputSource(new StringReader(buffer)));
//      driver.setOutputStream(new FileOutputStream("C:/simple.pdf"));
      ServletOutputStream outputStream = servletResponse.getOutputStream();
//      FileOutputStream outputStream = new FileOutputStream("c:/simple.pdf");
//      ResponseStream outputStream = facesContext.getResponseStream();
      driver.setOutputStream(outputStream);
//      Map rendererOptions = new java.util.HashMap();
//      rendererOptions.put("ownerPassword", "mypassword");
//      rendererOptions.put("allowCopyContent", "FALSE");
//      rendererOptions.put("allowEditContent", "FALSE");
//      rendererOptions.put("allowPrint", "FALSE");
//      driver.getRenderer().setOptions(rendererOptions);
      driver.run();
      outputStream.flush();
      outputStream.close();
    } catch (Exception e) {
      LOG.error("", e);
      throw new FacesException(e);
    }
  }
}
