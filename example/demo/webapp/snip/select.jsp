<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="select" >

  <t:box label="#{bundle.selectSingleselect}">
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>
    <t:selectOneChoice value="#{demo.salutation[0]}" label="#{bundle.selectSingleselect_en}" required="true" >
      <f:selectItem itemLabel="Empty Value" />
      <f:selectItems value="#{demo.salutationItems}" />
    </t:selectOneChoice>
    <t:selectOneChoice value="#{demo.salutation[0]}"disabled="true" label="#{bundle.selectSingleselect_dis}">
      <f:selectItems value="#{demo.salutationItems}" />
    </t:selectOneChoice>
    <t:selectOneListbox value="#{demo.salutation[1]}" label="#{bundle.selectSingleselect_listbox}" height="60px" required="true" >
      <f:selectItems value="#{demo.salutationItems}" />
    </t:selectOneListbox>
    Without Label: <br />
    <t:selectOneChoice value="#{demo.salutation[1]}">
      <f:selectItems value="#{demo.salutationItems}" />
      <%--<f:selectItem itemLabel="test ohne value" itemValue="#{demo.null}" />  --%>
    </t:selectOneChoice>
    <t:selectOneChoice value="#{demo.salutation[1]}" disabled="true">
      <f:selectItems value="#{demo.salutationItems}" />
    </t:selectOneChoice>
    <t:selectOneListbox value="#{demo.salutation[1]}" >
      <f:selectItems value="#{demo.salutationItems}" />
    </t:selectOneListbox>
  </t:box>

  <t:box label="#{bundle.selectMultiselect}">
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>
    <t:selectManyListbox value="#{demo.phoneProtocols[0]}" label="#{bundle.selectMultiselect_en}">
      <f:selectItems value="#{demo.phoneProtocolItems}" />
    </t:selectManyListbox>
    <t:selectManyListbox value="#{demo.phoneProtocols[0]}" disabled="true" label="#{bundle.selectMultiselect_dis}">
      <f:selectItems value="#{demo.phoneProtocolItems}" />
    </t:selectManyListbox>
    <br /> Without Label: <br />
    <t:selectManyListbox value="#{demo.phoneProtocols[1]}">
      <f:selectItems value="#{demo.phoneProtocolItems}" />
    </t:selectManyListbox>
    <t:selectManyListbox value="#{demo.phoneProtocols[1]}" disabled="true">
      <f:selectItems value="#{demo.phoneProtocolItems}" />
    </t:selectManyListbox>
    <br /> With more rows: <br />
    <t:selectManyListbox value="#{demo.phoneProtocols[0]}" height="85"  label="#{bundle.selectMultiselect_en}">
      <f:selectItems value="#{demo.phoneProtocolItems}" />
    </t:selectManyListbox>
  </t:box>

  <t:box label="#{bundle.selectInlineElements}">
    <p>
      Als erstes die Einfachauswahl:
      Eine Inline-Eingabe
      <t:selectOneChoice value="#{demo.salutation[2]}" inline="true">
        <f:selectItems value="#{demo.salutationItems}" />
      </t:selectOneChoice>
      ohne Label.
      Eine Inline-Eingabe
      <t:label value="#{bundle.selectSalutation}" inline="true"
          for="LabeledInlineSingleSelect" />
      <t:selectOneChoice value="#{demo.salutation[3]}" inline="true"
          id="LabeledInlineSingleSelect" >
        <f:selectItems value="#{demo.salutationItems}" />
      </t:selectOneChoice>
      mit Label.
    </p>
    <p>
      Und nun das ganze für die Mehrfachauswahl:
      Eine Inline-Eingabe
      <t:selectManyListbox value="#{demo.phoneProtocols[2]}" inline="true">
        <f:selectItems value="#{demo.phoneProtocolItems}" />
      </t:selectManyListbox>
      ohne Label.
      Eine Inline-Eingabe
      <t:label value="#{bundle.selectPhoneProtocols}" inline="true"
          for="LabeledInlineMultiSelect" />
      <t:selectManyListbox value="#{demo.phoneProtocols[3]}" inline="true"
          id="LabeledInlineMultiSelect" >
        <f:selectItems value="#{demo.phoneProtocolItems}" />
      </t:selectManyListbox>
      mit Label.
    </p>
  </t:box>

  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="100px;*" />
    </f:facet>

    <t:button label="#{bundle.submit}" />

    <t:cell />

  </t:panel>

</f:subview>
