<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%!
  public class LongTest {
    private int value;
    public void setValue(int value) {
      this.value = value;
    }
    public int getValue() {
      return value;
    }
  }
%>
<%
  LongTest longTest = (LongTest) session.getAttribute("longTest");
  if (longTest == null) {
    longTest = new LongTest();
    longTest.setValue(333);
    session.setAttribute("longTest", longTest);
  }
%>
<f:view>
  <t:page label="test: validate long range" id="page">

    <t:in value="#{longTest.value}" id="text-field" >
      <f:validateLongRange minimum="3" maximum="1000000000" />
    </t:in>

    <t:button action="submit" id="submit-button" label="#{bundle.submit}" />

  </t:page>
</f:view>
