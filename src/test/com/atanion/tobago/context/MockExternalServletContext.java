/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 15.12.2003 13:02:21.
 * $Id$
 */
package com.atanion.tobago.context;

import com.atanion.tobago.context.Theme;
import com.atanion.tobago.config.TobagoConfig;
import com.atanion.tobago.context.Theme;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletContext;

import javax.servlet.ServletContext;

public class MockExternalServletContext extends ExternalContextImpl {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

  public MockExternalServletContext() {
    super(
        new MockServletContext(),
        new MockHttpServletRequest(),
        new MockHttpServletResponse());
    MockHttpServletRequest request = (MockHttpServletRequest) getRequest();
    MockHttpSession session = new MockHttpSession();
    request.setSession(session);
    session.setupServletContext((ServletContext) getContext());

    TobagoConfig instance = TobagoConfig.getInstance();
    Theme theme = new Theme();
    theme.setName(Theme.SCARBOROUGH);
    instance.addTheme(theme);
    instance.propagate();
  }

// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// bean getter + setter



}
