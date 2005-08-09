<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="2*;1*;fixed" />
      </f:facet>
      <t:box label="#{overviewBundle.license_title}">
        <t:cell scrollbars="auto" >
          <t:out escape="false" value="#{overviewBundle.license_text}" />
        </t:cell>
      </t:box>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*;fixed" />
        </f:facet>
        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="20px;1*" rows="fixed"  />
          </f:facet>
          <t:selectBooleanCheckbox id="ctrl_accepted" value="#{downloadController.accepted}" inline="true" />
          <t:label for="ctrl_accepted" value="#{overviewBundle.license_accepted}" />
        </t:panel>
        <t:out value="#{overviewBundle.download_explanation}" />
        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="5*;1*" />
          </f:facet>
          <t:in id="ctrl_email" required="true" value="#{downloadController.email}"
              label="#{overviewBundle.download_email}" />
          <t:button action="#{downloadController.sendLink}" label="#{overviewBundle.send_link}" />
        </t:panel>
      </t:panel>
      <t:messages />
    </t:panel>
  </jsp:body>
</layout:overview>
