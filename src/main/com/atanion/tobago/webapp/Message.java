/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Jan 20, 2003.
 * $Id$
 */

package com.atanion.tobago.webapp;

import javax.faces.application.FacesMessage;


// fixme: jsfbeta Is this class longer needed? Most functionality may be in the superclass now
public class Message extends FacesMessage {

// ///////////////////////////////////////////// constant

  private static final Object[] NO_PARAMS = new Object[0];

  public static final String SUMMARY_POSTFIX = ".summary";
  public static final String DETAIL_POSTFIX = ".detail";

// ///////////////////////////////////////////// attribute

  private String key;
  private String summary;
  private String detail;
  private Object[] parameters;
  private Severity severity = FacesMessage.SEVERITY_WARN; // fixme: only WARN is used in the moment

// ///////////////////////////////////////////// constructor


  public Message(String key) {
    this(key, NO_PARAMS);
  }

  public Message(String key, Object[] parameters) {
    this.key = key;
    this.parameters = parameters;
  }

  public Message(String summary, String detail) {
    this.summary = summary;
    this.detail = detail;
  }

// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// bean getter + setter

  public String getSummary() {
    return summary != null ? summary : key + SUMMARY_POSTFIX;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getDetail() {
    return detail != null ? detail : key + DETAIL_POSTFIX;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Object[] getParameters() {
    return parameters;
  }

  public void setParameters(Object[] parameters) {
    this.parameters = parameters;
  }

  public Severity getSeverity() {
    return severity;
  }

  public void setSeverity(Severity severity) {
    this.severity = severity;
  }
}
