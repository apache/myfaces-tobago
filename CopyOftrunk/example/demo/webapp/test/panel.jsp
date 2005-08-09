<%-- Copyright (c) 2003 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: panel.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<f:view>
  <t:page label="test of panel" id="page">

    <t:panel>
      <t:box label="test von panel">
        <t:out value="tobago:text" />
        HALLO WELT
      </t:box>
    </t:panel>

  </t:page>
</f:view>
