 <%-- Copyright (c) 2004 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: text-emphasized.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
  <t:page label="text">
    <t:out markup="strong" value="#{demo.text[1]}"/>
    <t:out markup="deleted" value="#{demo.text[1]}"/>
  </t:page>
</f:view>
