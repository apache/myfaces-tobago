<%-- Copyright (c) 2003 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: layout.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <t:page label="test of panel" id="page">

    <t:box label="test von label innerhalb von layout" width="600px">
      <f:facet name="layout">
        <t:gridLayout columns="*;350px" border="1" />
      </f:facet>

      <t:selectOneChoice value="#{demo.salutation[0]}"
          label="labeled selectbox" >
        <f:selectItems value="#{demo.salutationItems}" />
      </t:selectOneChoice>

      <t:in value="#{demo.text[0]}" label="label 1" />

      <t:in value="#{demo.text[1]}" label="label 2" />

      <t:selectBooleanCheckbox value="#{demo.bool[1]}" label="label 3" />

      <t:in value="#{demo.text[1]}" label="label 4" />

      <t:selectBooleanCheckbox value="#{demo.bool[1]}">
        <f:facet name="label">
          <t:label value="label 5" width="75px" />
        </f:facet>
      </t:selectBooleanCheckbox>

    </t:box>

  </t:page>
</f:view>
