<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: singleselect.jsp 1228 2005-04-29 15:14:49 +0200 (Fr, 29 Apr 2005) hennes $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>

  <t:loadBundle basename="demo" var="bundle" />
  <t:page label="singleselect test" id="page">

    <t:selectOneChoice value="#{demo.one}" id="one">
      <f:selectItems value="#{demo.oneItems}" />
    </t:selectOneChoice>

    <t:selectOneChoice value="#{demo.salutation[0]}">
      <f:selectItems value="#{demo.salutationItems}" />
    </t:selectOneChoice>

    <t:button action="submit" id="submit-button" label="#{bundle.submit}" />

  </t:page>

</f:view>
