<%-- Copyright (c) 2003 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%>
<%@ attribute name="title" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <tc:page label="${title}" id="page"
      width="#{layout.width}" height="#{layout.height}">
    <f:facet name="layout">
      <tc:gridLayout margin="5"/>
    </f:facet>

    <tc:box label="#{loginBundle.login_box}">
      <f:facet name="layout">
        <tc:gridLayout columns="1*;2*;1*" rows="1*;150px;1*" />
      </f:facet>

      <tc:cell spanX="3" />

      <tc:cell />
      <jsp:doBody/>
      <tc:cell />

      <tc:cell spanX="3" />

    </tc:box>
  </tc:page>
</f:view>
