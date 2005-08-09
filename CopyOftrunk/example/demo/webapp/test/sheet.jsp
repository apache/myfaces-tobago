<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.atanion.com/taglib/logging" prefix="log"%>
<log:factory var="LOG" />
<%!
  public class RowModel {
    String column0Value;
    int column1Value;
    double column2Value;
    public RowModel(String s, int i, double d) {
      column0Value = s;
      column1Value = i;
      column2Value = d;
    }
    public void setColumn0Value(String value) {column0Value = value;}
    public String getColumn0Value() {return column0Value;}
    public void setColumn1Value(int value) {column1Value = value;}
    public int getColumn1Value() {return column1Value;}
    public void setColumn2Value(double value) {column2Value = value;}
    public double getColumn2Value() {return column2Value;}
  }
  public class SheetStateHolder {
    Object state;
    public void setState(Object state) { this.state = state;}
    public Object getState() {return state;}
  }
%>
<%
  RowModel[] model = (RowModel[]) session.getAttribute("model");
  if (model == null) {
    %>  <log:info var="LOG" message="kein sheet gefunden erzeuge neues" /><%
    model = new RowModel[] {
      new RowModel("aaaaaa", 15, 0.0),
      new RowModel("bbbbbb", 14, 1.5),
      new RowModel("cccccc", 13, 1.0),
      new RowModel("dddddd", 12, 0.1),
      new RowModel("eeeeee", 11, 1.4),
      new RowModel("ffffff", 10, 0.9),
      new RowModel("gggggg", 9, 0.2),
      new RowModel("hhhhhh", 8, 1.3),
      new RowModel("iiiiii", 7, 0.8),
      new RowModel("jjjjjj", 6, 0.3),
      new RowModel("kkkkkk", 5, 1.2),
      new RowModel("llllll", 4, 0.7),
      new RowModel("mmmmmm", 3, 0.4),
      new RowModel("nnnnnn", 2, 1.1),
      new RowModel("oooooo", 1, 0.6)
    };
    session.setAttribute("model", model);
  }
  SheetStateHolder state = (SheetStateHolder) session.getAttribute("stateHolder");
  if (state == null) {
    %>  <log:info var="LOG" message="kein sheetState gefunden erzeuge neues" /><%
    state = new SheetStateHolder();
    session.setAttribute("stateHolder", state);
  }
%>
<f:view>
  <t:page label="test" id="page">
  <t:box width="300" height="300">
   <f:facet name="layout" >
     <t:gridLayout rows="*;fixed" />
   </f:facet>
    <t:sheet value="#{model}" columns="*;*;*" var="data" id="sheet"
                  pagingLength="5" state="#{stateHolder.state}" >
      <t:column label="chars" sortable="true">
        <t:out value="#{data.column0Value}" />
      </t:column>
      <t:column label="ints" sortable="true">
        <t:out value="#{data.column1Value}" />
      </t:column>
      <t:column label="doubles" sortable="true">
        <t:out value="#{data.column2Value}" />
      </t:column>
    </t:sheet>
    <t:button id="submit-button" />
  </t:box>

  </t:page>
</f:view>
