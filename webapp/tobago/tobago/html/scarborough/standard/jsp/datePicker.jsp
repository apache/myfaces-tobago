<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <t:page title="Calendar" id="page" width="270px" >
    <f:facet name="layout">
      <t:gridLayout columns="10px;1*;10px" rows="10px;1*;10px" />
    </f:facet>

    <t:cell spanX="3">
      <t:script file="script/calendar.js" />
    </t:cell>

    <t:cell />
    <t:box label="Calendar" >

      <t:calendar id="test1234" />

      <t:button label="OK"
          action="writeIntoField('test1234');window.close();" type="script"/>
      <t:button label="Cancel"
        action="window.close();" type="script" />
    </t:box>
    <t:cell />

    <t:cell spanX="3" />

  </t:page>
</f:view>
