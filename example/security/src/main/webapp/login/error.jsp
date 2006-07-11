<%-- Copyright (c) 2003 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:login title="#{loginBundle.login_error_title}">
  <jsp:body>
  <tc:box label="#{bundle.loginLoginError}">
    <f:facet name="layout">
      <tc:gridLayout rows="*;fixed" />
    </f:facet>

    <tc:out value="#{bundle.loginLoginErrorText}" />

    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout columns="1*;100px" />
      </f:facet>

      <tc:cell />

      <tc:button action="/index.jsp" type="navigate" label="#{bundle.loginHome}" />
    </tc:panel>

  </tc:box>
</jsp:body>
</layout:login>
