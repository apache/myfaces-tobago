<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="footer_jsp" >

  <f:verbatim>
    <hr />
  </f:verbatim>

  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="100px;100px;*;120px" />
    </f:facet>

    <%--<t:include value="configButton.jsp" id="configInclude" />--%>

    <t:include value="viewHtmlButton.jsp" id="viewHtmlInclude" />

    <t:include value="viewJspButton.jsp" id="viewJspInclude" />

    <t:cell />

    <t:link id="atanion_link" action="http://www.atanion.com/"
        type="navigate" image="image/poweredBy.gif" />

  </t:panel>
</f:subview>