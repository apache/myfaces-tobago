<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <tobago:page title="Calendar" id="page" width="300px" >
    <f:facet name="layout"><tobago:gridlayout /></f:facet>
    <tobago:script file="calendar.js" i18n="true" />

    <tobago:box label="Calendar" >

      <tobago:calendar id="test1234" />

      <tobago:button label="OK"
          commandName="writeIntoField('test1234');window.close();" type="script"/>
      <tobago:button label="Cancel"  
        commandName="window.close();" type="script" />

    </tobago:box>
  </tobago:page>
</f:view>
