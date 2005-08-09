<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="80px;200px;1*;" />
      </f:facet>

      <t:out escape="false" value="#{overviewBundle.validation_text}" />

      <t:box label="#{overviewBundle.validation_sampleTitle}" >
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*;fixed;fixed" />
        </f:facet>

        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="1*;1*" />
          </f:facet>
          <t:in label="#{overviewBundle.validation_number}"
              required="true">
            <f:validateLength minimum="7" maximum="7" />
            <f:validateLongRange />
          </t:in>
          <t:in label="#{overviewBundle.validation_price}">
            <f:validateDoubleRange minimum="0.01" maximum="1000" />
          </t:in>
        </t:panel>

        <t:textarea label="#{overviewBundle.validation_description}"
            required="true"  />

        <t:messages />

        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="1*;100px"   />
          </f:facet>
          <t:cell/>
          <t:button action="#{clientConfigController.submit}"
              label="#{overviewBundle.validation_submit}" />
        </t:panel>

      </t:box>
      <t:cell />
    </t:panel>
  </jsp:body>
</layout:overview>
