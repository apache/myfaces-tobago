<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<f:view>
  <tc:page>
    <f:facet name="layout">
      <tc:gridLayout/>
    </f:facet>
    <tc:out value="Hello World"/>
    <tx:file label="test" value="#{test.file}" >
      <tc:validateFileItem contentType="text/*" maxSize="299"/>
    </tx:file>
    <tc:messages/>
    <tc:button label="Submit" defaultCommand="true" />
  </tc:page>
</f:view>