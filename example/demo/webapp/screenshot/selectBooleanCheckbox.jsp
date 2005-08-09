<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="selectBooleanCheckbox">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="fixed;fixed;1*" />
        </f:facet>

      <t:selectBooleanCheckbox label="#{bundle.solarSaturn}" value="#{demo.bool[0]}" id="bool0" />
      <t:selectBooleanCheckbox label="#{bundle.solarJupiter}" value="#{demo.bool[1]}" id="bool1" />
      <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>