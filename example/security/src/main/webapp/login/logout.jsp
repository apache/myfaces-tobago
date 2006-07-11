<%-- Copyright (c) 2003 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:login title="#{loginBundle.login_logout_title}">
  <jsp:body>
  <tc:box label="#{loginBundle.login_logout_title}">
    <f:facet name="layout">
      <tc:gridLayout />
    </f:facet>

    <tc:out value="#{loginBundle.login_logout_text}" />

    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout columns="1*;100px" />
      </f:facet>

      <tc:cell />

      <tc:button action="/index.jsp" type="navigate" label="#{loginBundle.login_home_button}" />
    </tc:panel>

  </tc:box>
</jsp:body>
</layout:login>
