<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <t:page label="Tobago Demo" id="page" width="750px" >
    <t:style style="style/tobago-demo.css" id="demostyle" />

    <t:panel>
      <f:facet name="layout">
        <t:gridLayout />
      </f:facet>

      <t:in value="#{demo.aDouble}" converter="Number" />

      <t:in value="#{demo.aDouble}" converter="Number">
        <f:attribute name="numberStyle" value="currency" />
      </t:in>

      <t:in value="#{demo.aDouble}" converter="Number">
        <f:attribute name="numberStyle" value="number" />
      </t:in>

      <t:in value="#{demo.aDouble}" converter="Number">
        <f:attribute name="numberStyle" value="percent" />
      </t:in>

      <t:in value="#{demo.aDouble}" converter="NumberFormat">
        <f:attribute name="formatPattern" value="0,000,000.000" />
      </t:in>

      <t:in value="#{demo.aDouble}" converter="Duration" />

      <t:in value="#{demo.aDouble}" converter="Duration">
        <f:attribute name="unit" value="second" />
      </t:in>

      <t:in value="#{demo.aDouble}" converter="Duration">
        <f:attribute name="unit" value="minute" />
      </t:in>

      <t:in value="#{demo.integer}" converter="Time">
        <f:attribute name="timezone" value="GMT" />
      </t:in>

      <t:in value="#{demo.integer}" converter="Date" />

      <t:in value="#{demo.date}" converter="Time" />

      <t:in value="#{demo.date}" converter="Time">
        <f:attribute name="timeStyle" value="medium" />
      </t:in>

      <t:in value="#{demo.date}" converter="Date" />

      <t:in value="#{demo.date}" converter="Date">
        <f:attribute name="dateStyle" value="medium" />
      </t:in>

      <t:in value="#{demo.date}" converter="Date">
        <f:attribute name="dateStyle" value="long" />
      </t:in>

      <t:in value="#{demo.date}" converter="Date">
        <f:attribute name="dateStyle" value="full" />
      </t:in>

      <t:in value="#{demo.date}" converter="DateTime" />

      <t:in value="#{demo.date}" converter="DateTime">
        <f:attribute name="dateStyle" value="medium" />
      </t:in>

      <t:in value="#{demo.date}" converter="DateTime">
        <f:attribute name="dateStyle" value="long" />
      </t:in>

      <t:in value="#{demo.date}" converter="DateTime">
        <f:attribute name="dateStyle" value="full" />
      </t:in>

      <t:in value="#{demo.date}" converter="DateFormat">
        <f:attribute name="formatPattern" value="EEEE: yyyy-MM-dd hh:mm:ss,SSS" />
      </t:in>

      <t:button label="#{bundle.submit}" />

    </t:panel>
  </t:page>
</f:view>
