<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: radiogroup.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>

  <t:page label="multiselect test" id="page">

<%--    <t:panel>--%>

      <t:selectOneRadio value="#{demo.salutation[0]}">
        <f:facet name="label"><t:label value="radiogroup_0" /></f:facet>
        <f:selectItems value="#{demo.salutationItems}" />
      </t:selectOneRadio>

      <t:button action="submit" id="submit-button" label="#{bundle.submit}" />

<%--    </t:panel>--%>

  </t:page>

</f:view>
