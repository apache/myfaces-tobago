<%-- Copyright (c) 2003 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: index_start.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%><%@ page errorPage="/errorPage.jsp"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.atanion.com/taglib/logging" prefix="log" %>

<log:factory var="LOG" />
<log:debug var="LOG" message="Startpage" />

<f:view>
  <t:loadBundle basename="demo" var="bundle" />
  <t:page label="#{bundle.pageTitle}" id="index_start">
    <t:style style="style/tobago-demo.css" id="demostyle" />
    <div id="layer" style="width:100%; height:100%; overflow : auto;" align="center">
      <t:link id="link" action="/welcome.view" type="navigate"
          image="image/tobago_splash.gif" />
    </div>
  </t:page>
</f:view>
