package org.apache.myfaces.tobago.application;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.fop.apps.Driver;
import org.apache.myfaces.tobago.util.Slf4jLogger;
import org.xml.sax.InputSource;

import javax.faces.FacesException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.StringReader;

public class FopConverter {

  private static final Logger LOG = LoggerFactory.getLogger(FopConverter.class);

  public static void fo2Pdf(ServletResponse servletResponse, String buffer) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("buffer = '{}'", buffer);
    }
    try {
      Driver driver = new Driver();
      driver.setLogger(new Slf4jLogger(LOG));
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
