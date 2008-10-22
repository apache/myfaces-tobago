<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<f:view>
  <tc:page width="300px" height="50px">
    <f:facet name="layout">
      <tc:gridLayout columns="*;fixed" rows="fixed;*"/>
    </f:facet>
    <tx:in label="Your Name" tip="Please enter your name here!" value="#{user.name}"/>
    <tc:button action="sayHello" label="Click Here"/>
    <tc:cell/>
    <tc:cell/>
  </tc:page>
</f:view>
