<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: checkbox.jsp 1237 2005-06-27 12:19:41 +0200 (Mo, 27 Jun 2005) weber $
  --%>
<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="checkbox_jsp" >

  <t:box label="#{bundle.checkboxRadio}">
    <f:facet name="layout">
      <t:gridLayout columns="1*;1*" />
    </f:facet>
    <t:out value="#{bundle.checkboxRadiogroup_enabled}" />
    <t:out value="#{bundle.checkboxRadiogroup_disabled}" />
    <t:panel >

      <t:selectOneRadio value="#{demo.salutation[0]}" id="rg0" renderRange="0,2-3" required="true" >
        <f:facet name="label"><t:label value="#{bundle.radiogroup_0}" /></f:facet>
        <f:selectItems value="#{demo.salutationItems}" id="items0" />
      </t:selectOneRadio>
      <t:selectReference for="rg0" renderRange="1" />

</t:panel >
    <t:selectOneRadio value="#{demo.salutation[0]}" disabled="true" id="rg1">
      <f:facet name="label"><t:label value="#{bundle.radiogroup_1}" /></f:facet>
      <f:selectItems value="#{demo.salutationItems}" id="items1" />
    </t:selectOneRadio>
  </t:box>

  <t:box label="#{bundle.checkboxCheckbox}">
    <f:facet name="layout">
      <t:gridLayout columns="1*;1*;1*" />
    </f:facet>
    <t:out value="#{bundle.checkboxCheckboxsingle}" />
    <t:out value="#{bundle.checkboxCheckboxgroup_enabled}" />
    <t:out value="#{bundle.checkboxCheckboxgroup_disabled}" />
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout />
      </f:facet>
      <t:selectBooleanCheckbox label="#{bundle.solarSaturn}" value="#{demo.bool[0]}" id="bool0" />
      <t:selectBooleanCheckbox label="#{bundle.solarJupiter}" value="#{demo.bool[1]}" id="bool1" />
      <br />
      <t:selectBooleanCheckbox label="#{bundle.solarNeptune}" disabled="true" value="#{demo.bool[2]}" id="bool2" />
      <t:selectBooleanCheckbox label="#{bundle.solarUranus}" disabled="true" value="#{demo.bool[3]}" id="bool3" />
    </t:panel>
    <t:panel>
      <t:selectManyCheckbox value="#{demo.phoneProtocols[0]}" id="cbg0" renderRange="6-3">
        <f:facet name="label"><t:label value="#{bundle.checkboxgroup_0}" /></f:facet>
        <f:selectItems value="#{demo.phoneProtocolItems}" id="itemsg0" />
      </t:selectManyCheckbox>
      <t:selectReference for="cbg0" renderRange="2,1,0" />
    </t:panel>
    <t:selectManyCheckbox value="#{demo.phoneProtocols[0]}" disabled="true" id="cbg1">
      <f:facet name="label"><t:label value="#{bundle.checkboxgroup_1}" /></f:facet>
      <f:selectItems value="#{demo.phoneProtocolItems}" id="itemsg1" />
    </t:selectManyCheckbox>
  </t:box>

  <t:box label="#{bundle.checkboxCheckbox_inline}">
    Die Checkboxen k�nnen auch in inline verwendet werden:
    W�hlen Sie von den beiden Planeten
    <t:selectBooleanCheckbox value="#{demo.bool[4]}" inline="true" id="checkbox_neptun"
        label="#{bundle.solarNeptune}" />
    und
    <t:selectBooleanCheckbox value="#{demo.bool[5]}" inline="true" id="checkbox_uranus"
        label="#{bundle.solarUranus}" />
    die aus, die aus Gas bestehen.
    Radiogroups k�nnen auch inline verwendet werden:
    <t:selectOneRadio value="#{demo.salutation[0]}" inline="true">
      <f:facet name="label"><t:label value="#{bundle.radiogroup_0}" /></f:facet>
      <f:selectItems value="#{demo.salutationItems}" />
    </t:selectOneRadio>

  </t:box>

  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="100px;*" />
    </f:facet>

    <t:button action="submit" label="#{bundle.submit}" />

    <t:cell />

  </t:panel>

</f:subview>
