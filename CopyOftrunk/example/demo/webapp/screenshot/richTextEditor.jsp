<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="richTextEditor">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="150px;1*" />
        </f:facet>

        <t:richTextEditor />
        <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>