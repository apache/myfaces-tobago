<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
#1
<f:view>
#2
  <t:page label="include test" id="page" >

    #3
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout border="1" />
      </f:facet>

      <t:out value="#4" />

      <t:box label="#5">
        #6
        <t:out id="i5" value="#7" />
        #8
        <t:out id="i7" value="#9" />
        #10
<%--      <%@ include file="../tobago-app/html/scarborough/standard/jsp/include1.jsp" %>--%>
<%--      <jsp:include page="../tobago-app/html/scarborough/standard/jsp/include1.jsp" />--%>
        <t:include value="include1.jsp" />
        #16
      </t:box>

      <t:out value="#17" />

    </t:panel>

    #18

    <t:box label="#19">
<%--    fixme: #19 will not be in the output, because of bug TBG-34 --%>
      #20
      <t:out id="i13" value="#21" />
      #22
      <t:out id="i15" value="#23" />
      #24
<%--      <%@ include file="../tobago-app/html/scarborough/standard/jsp/include2.jsp" %>--%>
<%--      <jsp:include page="../tobago-app/html/scarborough/standard/jsp/include2.jsp" />--%>
      <t:include value="include2.jsp" />
      #30
    </t:box>

    #31

  </t:page>
#32
</f:view>
#33
