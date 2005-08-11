/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 26.08.2004 10:29:42.
 * $Id: MockApplicationFactory.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package org.apache.myfaces.tobago.mock.faces;

import org.apache.myfaces.tobago.mock.faces.MockApplication;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

public class MockApplicationFactory extends ApplicationFactory {

    private Application application;

    public Application getApplication() {
        if (application == null) {
            application = new MockApplication();
        }
        return (application);
    }

    public void setApplication(Application application) {
        this.application = application;
    }


}
