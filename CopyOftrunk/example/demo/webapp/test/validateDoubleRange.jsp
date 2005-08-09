<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%!
  public class DoubleTest {
    private double value;
    public void setValue(double value) {
      this.value = value;
    }
    public double getValue() {
      return value;
    }
  }
%>
<%
  DoubleTest doubleTest = (DoubleTest) session.getAttribute("doubleTest");
  if (doubleTest == null) {
    doubleTest = new DoubleTest();
    doubleTest.setValue(333.333);
    session.setAttribute("doubleTest", doubleTest);
  }
%>
<f:view>
  <t:page label="test: validateDoubleRange" id="page">

    <t:in value="#{doubleTest.value}" id="text-field" >
      <f:validateDoubleRange minimum="3" maximum="10.001" />
    </t:in>

    <t:button action="submit" id="submit-button" label="#{bundle.submit}" />

  </t:page>
</f:view>
