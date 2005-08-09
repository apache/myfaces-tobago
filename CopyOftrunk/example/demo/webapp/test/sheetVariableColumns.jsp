<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id: sheetVariableColumns.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
  --%>
<%@ page import="java.util.List,
                 java.util.ArrayList,
                 org.apache.commons.logging.Log,
                 org.apache.commons.logging.LogFactory"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<%!
  private static final Log LOG = LogFactory.getLog("tobago-demo.test.sheetVariableColumns_jsp");

  static final int WIDTH = 10;
  static final int HEIGHT = 100;
  static String columnLayout = null;
  static final String columnLayoutToken = "1*;";

  public class Sample {

    int[] data;

    Sample(int length, int multiplyer) {
      this.data = new int[length];
      for (int i = 0; i < data.length; i++) {
        data[i] = (i+1)*(multiplyer+1);
      }
    }

    public int[] getData() {
      return data;
    }

    public void setData(int[] data) {
      this.data = data;
    }
  }

%>

<%
  List model = new ArrayList();
  for (int i = 0; i < HEIGHT; i++) {
    model.add(new Sample(WIDTH, i));
  }
  if (columnLayout == null) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i<WIDTH; i++) {
      sb.append(columnLayoutToken);
    }
    sb.deleteCharAt(sb.lastIndexOf(";"));
    columnLayout = sb.toString();
  }

  request.setAttribute("demoModel", model);
%>

<f:view>
  <t:page label="demo - sheet with variable number of column" width="990px" >
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>

    Das Einmaleins

    <t:sheet value="#{demoModel}" var="row"
        columns="<%= columnLayout %>" >
<%
      for (int i = 0; i < WIDTH; i++) {
        String valueRef = "#{row.data[" + i + "]}";
        String id = "col" + i;
        String label = Integer.toString(i+1);
%>
        <t:column label="<%= label %>" id="<%= id %>">
          <t:out value="<%= valueRef %>"/>
        </t:column>
<%
      }
%>
    </t:sheet>
  </t:page>
</f:view>
