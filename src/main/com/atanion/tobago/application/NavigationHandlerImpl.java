/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 23.06.2003 17:54:17.
 * Id: $
 */
package com.atanion.tobago.application;

import com.atanion.tobago.config.FacesConfig;
import com.atanion.tobago.config.NavigationCase;
import com.atanion.tobago.config.NavigationRule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.Iterator;

public class NavigationHandlerImpl extends NavigationHandler {

// ///////////////////////////////////////////// constant

  private static Log log = LogFactory.getLog(NavigationHandlerImpl.class);

// ///////////////////////////////////////////// attribute

  private FacesConfig config;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void handleNavigation(
      FacesContext facesContext, String actionRef, String outcome) {
    if (log.isInfoEnabled()) {
      log.info("outcome: " + outcome);
    }
    if (outcome == null) {
      return;
    }
    String toViewId = findViewId(facesContext, actionRef, outcome);
    ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
    UIViewRoot viewRoot = viewHandler.createView(facesContext, toViewId);
    facesContext.setViewRoot(viewRoot);
    if (log.isInfoEnabled()) {
      log.info("toViewId: " + toViewId);
    }
  }

  private String findViewId(
      FacesContext facesContext, String actionRef, String outcome) {

    String toViewId;
    String viewId = facesContext.getViewRoot().getViewId();

    toViewId = findExactMatch(viewId, actionRef, outcome);

    if (toViewId == null) {
      toViewId = findPrefixMatch(viewId, actionRef, outcome);
    }

    if (toViewId == null) {
      toViewId = findDefaultMatch(actionRef, outcome);
    }

    if (toViewId == null) {
      if (log.isWarnEnabled()) {
        log.warn("No toViewId found!!! Using old");
      }
      toViewId = viewId;
    }

    return toViewId;
  }

  private String findExactMatch(String viewId, String actionRef, String outcome) {
    for (Iterator i = config.getNavigationRules(); i.hasNext();) {
      NavigationRule rule = (NavigationRule) i.next();
      if (rule.getFromViewId().endsWith("*")) {
        continue;
      }
      if (rule.getFromViewId().equals(viewId)) {
        String toViewId = findCaseMatch(rule, actionRef, outcome);
        if (toViewId != null) {
          return toViewId;
        }
      }
    }
    return null;
  }

  private String findPrefixMatch(String viewId, String actionRef, String outcome) {
    for (Iterator i = config.getNavigationRules(); i.hasNext();) {
      NavigationRule rule = (NavigationRule) i.next();
      String configFromViewId = rule.getFromViewId();
      if (configFromViewId.endsWith("*")) {
        if (viewId.startsWith(
            configFromViewId.substring(0, configFromViewId.length() - 1))) {
          String toViewId = findCaseMatch(rule, actionRef, outcome);
          if (toViewId != null) {
            return toViewId;
          }
        }
      }
    }
    return null;
  }

  private String findDefaultMatch(String actionRef, String outcome) {
    for (Iterator i = config.getNavigationRules(); i.hasNext();) {
      NavigationRule rule = (NavigationRule) i.next();
      if (rule.getFromViewId().equals("*")) {
        String toViewId = findCaseMatch(rule, actionRef, outcome);
        if (toViewId != null) {
          return toViewId;
        }
      }
    }
    return null;
  }

  private String findCaseMatch(
      NavigationRule rule, String actionRef, String outcome) {
    for (Iterator j = rule.getNavigationCases().iterator(); j.hasNext();) {
      NavigationCase navCase = (NavigationCase) j.next();
      String configOutcome = navCase.getFromOutcome();
      String configActionRef = navCase.getFromActionRef();
      if ((configOutcome == null || configOutcome.equals(outcome))
          && (configActionRef == null || configActionRef.equals(actionRef))) {
        return navCase.getToViewId();
      }
    }
    return null;
  }


// ///////////////////////////////////////////// bean getter + setter

  public void setConfig(FacesConfig config) {
    this.config = config;
  }
}
