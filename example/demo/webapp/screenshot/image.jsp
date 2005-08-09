<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <jsp:body>
    <f:subview id="image">
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="30px;1*" />
        </f:facet>

        <t:image value="image/atanion.gif" height="27" width="399" />
        <t:cell/>

      </t:panel>
    </f:subview>
  </jsp:body>
</layout:screenshot>