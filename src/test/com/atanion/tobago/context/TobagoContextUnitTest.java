/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 14.01.2004 14:45:31.
 * $Id$
 */
package com.atanion.tobago.context;

import junit.framework.TestCase;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.application.FacesMessage;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class TobagoContextUnitTest extends TestCase {

// ///////////////////////////////////////////// constant

  private static final FacesMessage M1 = new FacesMessage("m1", "m1");
  private static final FacesMessage M2 = new FacesMessage(FacesMessage.SEVERITY_INFO, "m2", "m2");
  private static final FacesMessage M3 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "m3", "m3");

// ///////////////////////////////////////////// attribute

  private FacesContext facesContext;

// ///////////////////////////////////////////// util

  protected void reset() {
    ExternalContext externalContext = new MockExternalServletContext();
    facesContext = new TobagoContext(externalContext, null);
  }

  protected List asList(Iterator iterator) {
    List list = new ArrayList();
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }
    return list;
  }

// ///////////////////////////////////////////// test 6.1.4 (jsf pfd)

  public void testGetMessagesClientId() {
    reset();
    List messages;

    messages = asList(facesContext.getMessages());
    assertEquals("size", 0, messages.size());

    facesContext.addMessage("car", M1);
    facesContext.addMessage(null, M1);
    facesContext.addMessage("tree", M2);
    facesContext.addMessage("tree", M3);

    messages = asList(facesContext.getMessages(null));
    assertEquals("size", M1, messages.get(0));
    messages = asList(facesContext.getMessages("car"));
    assertEquals("size", M1, messages.get(0));
    messages = asList(facesContext.getMessages("tree"));
    assertEquals("size", 2, messages.size());
    messages = asList(facesContext.getMessages());
    assertEquals("size", 4, messages.size());
  }

  public void testGetClientIdWithMessages() {
    reset();

    facesContext.addMessage("car", M1);
    facesContext.addMessage("tree", M2);
    facesContext.addMessage("tree", M3);

    List clientIds = asList(facesContext.getClientIdsWithMessages());
    assertEquals("size", 2, clientIds.size());
  }

  public void testSeverityNoSeverity() {
    reset();
    FacesMessage.Severity severity;

    facesContext.addMessage(null, M1);
    facesContext.addMessage("car", M1);

    severity = facesContext.getMaximumSeverity();
    assertEquals("severity", null, severity);

    facesContext.addMessage("car", M2);
    facesContext.addMessage(null, M3);

    severity = facesContext.getMaximumSeverity();
    assertEquals("severity", FacesMessage.SEVERITY_ERROR, severity);
  }


}
