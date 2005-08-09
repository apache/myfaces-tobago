<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="selectManyListbox">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*" />
        </f:facet>

        <t:selectManyListbox value="#{demo.phoneProtocols[3]}" inline="true"
            id="LabeledInlineMultiSelect" >
          <f:selectItems value="#{demo.phoneProtocolItems}" />
        </t:selectManyListbox>

        <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>