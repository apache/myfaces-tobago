<%-- Copyright (c) 2003 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: action3.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>

  <t:page label="multiselect test" id="page">

    <h1>Page 3</h1>

    <br />
    This is a cycle with 3 pages
    <br />

    <t:button action="previous" label="previous" />

    <t:button action="next" label="next" />

  </t:page>

</f:view>
