<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <tobago:page title="Calendar" id="page" width="270px" >
    <f:facet name="layout">
      <tobago:gridLayout columns="10px;1*;10px" rows="10px;1*;10px" />
    </f:facet>

    <tobago:cell spanX="3">
      <tobago:script file="calendar.js" i18n="true" />
    </tobago:cell>

    <tobago:cell />
    <tobago:box label="Calendar" >

      <tobago:calendar id="test1234" />

      <tobago:button label="OK"
          action="writeIntoField('test1234');window.close();" type="script"/>
      <tobago:button label="Cancel"
        action="window.close();" type="script" />
    </tobago:box>
    <tobago:cell />

    <tobago:cell spanX="3" />

  </tobago:page>
</f:view>
