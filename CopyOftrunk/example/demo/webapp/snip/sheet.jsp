<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="demo_sheet_jsp" >

  <t:style>
    .custom-text-align-right {
      text-align: right;
    }
  </t:style>

  <t:box label="#{bundle.solarPagingSheet}"  height="550px">
    <f:facet name="layout">
      <t:gridLayout rows="*;fixed" />
    </f:facet>
    <t:sheet value="#{demo.solarArray}" id="sheet"
        columns="3*;1*;3*;fixed;#{demo.solarArrayColumnLayout}" var="luminary"
        state="#{demo.sheetState}"
        showRowRange="left" showPageRange="center" showDirectLinks="right" >
      <t:column label="#{bundle.solarArrayName}" id="name" sortable="true">
        <t:out value="#{luminary.name}" id="t_name" />
      </t:column>
      <t:column label="#{bundle.solarArrayNumber}" id="number" sortable="false" align="right" >
        <t:out value="#{luminary.number}" id="t_number"/>
      </t:column>
      <t:column label="#{bundle.solarArrayOrbit}" sortable="true" >
        <t:out value="#{luminary.orbit}" id="t_orbit" />
      </t:column>

      <t:columnSelector disabled="#{luminary.selectionDisabled}" />

      <t:forEach items="#{demo.solarArrayColumns}" var="column" >
         <t:column binding="#{column}"/>
      </t:forEach>

<%--      <t:columns value="#{demo.solarArrayColumns}"/>--%>

    </t:sheet>

    <t:panel>
      <f:facet name="layout">
        <t:gridLayout columns="100px;*" />
      </f:facet>

      <t:button labelWithAccessKey="#{bundle.submit2}" />

      <t:cell />

    </t:panel>

  </t:box>

</f:subview>
