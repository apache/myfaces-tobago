<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<jsp:useBean id="numbers"
    class="com.atanion.tobago.demo.model.Numbers" scope="session" />

<f:view>
  <t:page label="Tobago Demo" id="page" width="750px" >

    <f:verbatim escape="true">
      automatic conversion
    </f:verbatim>

    <t:in value="#{numbers.intValue}">
<%--      <f:converter converterId="javax.faces.Integer"  />--%>
<%--      <f:convertNumber />--%>
    </t:in>

    <t:button>
      submit
    </t:button>

  </t:page>
</f:view>
