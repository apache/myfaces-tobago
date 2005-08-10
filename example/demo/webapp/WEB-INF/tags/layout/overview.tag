<%-- Copyright (c) 2005 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: overview.tag 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view locale="#{clientConfigController.locale}">
  <t:loadBundle basename="demo" var="bundle" />
  <f:loadBundle basename="org.apache.myfaces.tobago.overview.Resources" var="overviewBundle" />
  <t:page label="#{bundle.pageTitle}" id="page"
      width="750px" height="600px">

    <t:include value="overview/menubar.jsp" />

    <f:facet name="layout">
      <t:gridLayout border="0" columns="1*;4*"
        margin="10px" rows="100px;*;fixed"  />
    </f:facet>

    <t:cell spanX="2">
      <t:include value="overview/header.jsp"/>
    </t:cell>

    <t:cell spanY="2" >
      <t:include value="overview/navigator.jsp"/>
    </t:cell>

    <t:cell>
      <jsp:doBody/>
    </t:cell>

    <t:cell>
      <t:include value="overview/footer.jsp" />
    </t:cell>

  </t:page>
</f:view>
