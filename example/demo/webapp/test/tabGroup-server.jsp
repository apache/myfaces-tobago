<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%!
  public class StateModel {
    int state;
    public void setState(int i) {state = i;}
    public int getState() {return state;}
  }
%>
<%
  StateModel model = (StateModel) session.getAttribute("serverModel");
  if (model == null) {
    model = new StateModel();
    session.setAttribute("serverModel", model);
  }
%>
<f:view>
  <t:page label="test" id="page" >
    <t:tabGroup state="#{serverModel.state}" id="tabGroup" serverside="true" >
      <t:tab id="tab1">
        Content of first Tab
      </t:tab>
      <t:tab id="tab2">
        Content of second Tab
      </t:tab>
      <t:tab id="tab3">
        Content of third Tab
      </t:tab>
    </t:tabGroup>

    <t:button id="submit-button" />

  </t:page>
</f:view>
