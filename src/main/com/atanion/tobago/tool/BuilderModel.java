/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 10.10.2003 08:47:10.
 * $Id$
 */
package com.atanion.tobago.tool;

import com.atanion.util.io.Io2String;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.IOException;

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

      try {
        source = Io2String.read(servletContext.getResourceAsStream(page));
      } catch (IOException e) {
        LOG.error("", e);
        return "error"; // todo: error message
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
        Io2String.write(realPath, source);
      } catch (IOException e) {
        LOG.error("", e);
        return "error"; // todo: error message
      }

      return "viewSource";
    }

// /////////////////////////////////////////// action getter

}
