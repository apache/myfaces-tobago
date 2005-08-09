<%-- Copyright (c) 2003 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: action2.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<%!
  public static class Navigation {

    public String next() {
      return "next";
    }

    public String previous() {
      return "previous";
    }
  }
%>
<%
  session.setAttribute("navigation", new Navigation());
%>

<f:view>

  <t:page label="multiselect test" id="page">

    <h1>Page 2 (with navigation over method-call)</h1>

    <br />
    This is a cycle with 3 pages
    <br />

    <t:button action="#{navigation.previous}" label="previous" />

    <t:button action="#{navigation.next}" label="next" />

  </t:page>

</f:view>
