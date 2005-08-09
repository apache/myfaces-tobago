/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 04.06.2004 09:27:14.
 * $Id: PresentationController.java 1269 2005-08-08 20:20:19 +0200 (Mo, 08 Aug 2005) lofwyr $
 */
package com.atanion.tobago.overview;

import com.atanion.tobago.demo.jsp.JspFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

public class PresentationController {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(PresentationController.class);

// ----------------------------------------------------------------- attributes

  protected List pages;

  protected int pageIndex;

// ----------------------------------------------------------- business methods

  public String gotoFirst() {
    pageIndex = 0;
    return getCurrentPage();
  }

  public String getCurrentPage() {
    String page;
    try {
      page = (String) pages.get(pageIndex);
    } catch (IndexOutOfBoundsException e) {
      page = "error";
    }
    return page;
  }

  public String gotoNext() {
    if (!isLast()) {
      pageIndex++;
    }
    return getCurrentPage();
  }

  public boolean isLast() {
    return pageIndex >= pages.size() - 1;
  }

  public String gotoPrevious() {
    if (!isFirst()) {
      pageIndex--;
    }
    return getCurrentPage();
  }

  public boolean isFirst() {
    return pageIndex <= 0;
  }

  public void navigate(ActionEvent event) {
    UICommand component = (UICommand) event.getComponent();

    String action = component.getAction().getExpressionString();
    if (LOG.isInfoEnabled()) {
      LOG.info("action = '" + action + "'");
    }

    pageIndex = pages.indexOf(action);
    if (pageIndex == -1) {
      LOG.error("page entry not defined: " + action);
      pageIndex = 0;
    }
  }

  public void navigate(String viewId) {
    String action = null;
    if (viewId.length() > 5) {
      // pattern: "/???.jsp"
      action = viewId.substring(1, viewId.length() - 4);
    } else {
      LOG.warn("Strange viewId: " + viewId);
    }

    if (LOG.isInfoEnabled()) {
      LOG.info("action = '" + action + "'");
    }

    pageIndex = pages.indexOf(action);
    if (pageIndex == -1) {
      LOG.error("page entry not defined: " + action);
      pageIndex = 0;
    }
  }

  public String viewSource() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext externalContext = facesContext.getExternalContext();
    String viewId = facesContext.getViewRoot().getViewId();
    ServletContext servletContext = (ServletContext) externalContext.getContext();
    HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
    response.setContentType("text/html;charset=UTF-8");

    try {
      InputStream resourceAsStream = servletContext.getResourceAsStream(viewId);
      InputStreamReader reader = new InputStreamReader(resourceAsStream);
      JspFormatter.writeJsp(reader,
          new PrintWriter(response.getOutputStream()));
    } catch (IOException e) {
      LOG.error("", e);
      return "error";
    }

    facesContext.responseComplete();
    return null;
  }

  public static PresentationController getCurrentInstance(
      FacesContext facesContext, String beanName) {
    return (PresentationController) facesContext.getApplication()
        .getVariableResolver().resolveVariable(facesContext, beanName);
  }
  
// ------------------------------------------------------------ getter + setter

  public List getPages() {
    return pages;
  }

  public void setPages(List pages) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("number of pages is: " + pages.size());
    }
    this.pages = pages;
  }
}

