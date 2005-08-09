<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="messages">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*;fixed" />
        </f:facet>

        <t:messages />
        <t:textarea label="Required Field" required="true" />

        <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="fixed;" columns="1*;100px" />
        </f:facet>

         <t:cell/>
         <t:button action="#{clientConfigController.submit}"
             label="Submit" />
        </t:panel>

        <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>