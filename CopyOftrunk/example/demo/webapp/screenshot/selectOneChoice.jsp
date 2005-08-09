<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="selectOneChoice">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows=";1*" />
        </f:facet>

        <t:selectOneChoice value="#{demo.salutation[2]}" inline="true">
          <f:selectItems value="#{demo.salutationItems}" />
        </t:selectOneChoice>

        <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>