<%@ page
  errorPage="/errorPage.jsp"
  import="com.atanion.model.DateModel,
          java.util.GregorianCalendar,
          javax.swing.BoundedRangeModel,
          javax.swing.DefaultBoundedRangeModel,
          java.util.Calendar"
%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%-- Progress --%>

<%
     DefaultBoundedRangeModel progress = new DefaultBoundedRangeModel(75, 0, 0, 100);
  pageContext.setAttribute("progress", progress, PageContext.REQUEST_SCOPE);
%>

<layout:screenshot>
  <f:subview id="progress">
    <jsp:body>
      <t:panel>
        <f:facet name="layout">
          <t:gridLayout rows="fixed;1*" />
        </f:facet>
      <t:panel>
        <t:label value="Progress: " />
        <t:progress value="#{progress}" />
      </t:panel>
      <t:cell/>

      </t:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>