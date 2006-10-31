<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<f:view>
  <tc:page>
    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout margin="10px" rows="1*;fixed;1*" />
      </f:facet>

      <tc:box label="the outer box">

        <f:facet name="layout">
          <tc:gridLayout margin="10px" rows="fixed;fixed;fixed" />
        </f:facet>

        <tc:messages />

        <tx:in label="SerachCriteria" value="" />

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout margin="10px" columns="5*;1*" />
          </f:facet>

          <tc:cell />
          <tc:button action="" label="Search" defaultCommand="true" />

        </tc:panel>

      </tc:box>
    </tc:panel>
  </tc:page>
</f:view>
