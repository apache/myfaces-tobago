/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * All rights reserved. Created 10.10.2003 08:47:10.
 * $Id$
 */
package org.apache.myfaces.tobago.tool;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BuilderModel {

// /////////////////////////////////////////// constants

  private static final Log LOG = LogFactory.getLog(BuilderModel.class);

// /////////////////////////////////////////// attributes

  private String page = "builder/index.jsp";
  private String source;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

// /////////////////////////////////////////// utils

// /////////////////////////////////////////// bean getter + setter

  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

// /////////////////////////////////////////// actions

    public String open() {
      if (LOG.isDebugEnabled()) {
        LOG.debug("invoke!!!");
      }

      return "enterPagename";
    }

    public String loadPage() {
      if (LOG.isDebugEnabled()) {
        LOG.debug("invoke!!!");
      }

      FacesContext facesContext = FacesContext.getCurrentInstance();
      ServletContext servletContext =
          (ServletContext) facesContext.getExternalContext().getContext();

      InputStream stream = null;
      try {
        stream = servletContext.getResourceAsStream(page);
        source = IOUtils.toString(stream);
      } catch (IOException e) {
        LOG.error("", e);
        return "error"; // todo: error message
      } finally {
        IOUtils.closeQuietly(stream);
      }

      return "viewSource";
    }

    public String savePage() {
      if (LOG.isDebugEnabled()) {
        LOG.debug("invoke!!!");
      }

      FacesContext facesContext = FacesContext.getCurrentInstance();
      ServletContext servletContext =
          (ServletContext) facesContext.getExternalContext().getContext();

      String realPath = servletContext.getRealPath(page);
      try {
        // todo: use IOUtils.write when commons-io 1.1 is released
        FileUtils.writeStringToFile(new File(realPath), source, System.getProperty("file.encoding"));
      } catch (IOException e) {
        LOG.error("", e);
        return "error"; // todo: error message
      }

      return "viewSource";
    }

// /////////////////////////////////////////// action getter

}
