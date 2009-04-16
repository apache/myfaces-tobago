<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<f:view>
  <tc:page id="resetPage" width="400px" height="200px">
    <tc:tabGroup switchType="reloadTab">
      <tc:tab id="tab1" label="Tab1">
        <tc:out value="Tab1"/>
      </tc:tab>
      <tc:tab id="tab2" label="Tab2">
        <tc:sheet id="testTable"
          columns="1*;1*"
          showHeader="true"
          showRowRange="none"
          showPageRange="none"
          showDirectLinks="none"
          first="0"
          selectable="none"
          var="solarObject"
          value="#{test.solarObjects}">
          <tc:column label="Column 1">
            <tc:out id="column1"
              value="#{solarObject.name}"/>
          </tc:column>
          <tc:column label="Column 2">
            <tc:out id="column2"
              value="#{solarObject.number}"/>
          </tc:column>
        </tc:sheet>
      </tc:tab>
    </tc:tabGroup>
  </tc:page>
</f:view>
