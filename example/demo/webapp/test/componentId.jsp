 <%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: componentId.jsp 1178 2005-02-22 11:29:05 +0100 (Di, 22 Feb 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<jsp:useBean id="date" class="java.util.Date" scope="session" />
<jsp:useBean id="label" class="java.awt.Label" scope="session" />

<f:view>

  <t:page>

    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>

      <f:subview id="use_faces_2">

        <t:in id="date_time" value="#{date.time}" required="true" />

        <t:in id="label_text" value="#{label.text}" required="true" />

      </f:subview>


    <br />

    <t:button label="#{bundle.submit}" />

  </t:page>

</f:view>
