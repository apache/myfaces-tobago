<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <tobago:page title="Calendar" id="page" width="300px" >
    <tobago:script file="calendar.js" i18n="true" />

    <tobago:groupbox>

      <tobago:calendar id="test1234" />

      <tobago:button commandName="writeIntoField('test1234');window.close();" type="script">
        OK
      </tobago:button>
      <tobago:button commandName="window.close();" type="script">
        Cancel
      </tobago:button>

    </tobago:groupbox>
  </tobago:page>
</f:view>
