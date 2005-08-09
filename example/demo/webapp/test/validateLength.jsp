<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%!
  public class StringTest {
    private String value;
    public void setValue(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }
  }
%>
<%
  StringTest stringTest = (StringTest) session.getAttribute("stringTest");
  if (stringTest == null) {
    stringTest = new StringTest();
    stringTest.setValue("Hello");
    session.setAttribute("stringTest", stringTest);
  }
%>
<f:view>
  <t:page label="testing: validate length" id="page">

    &lt;f:validateLength minimum="3" maximum="10" />

    <t:in value="#{stringTest.value}" id="text-field" >
      <f:validateLength minimum="3" maximum="10" />
    </t:in>

    <t:button action="submit" id="submit-button">
      submit
    </t:button>

  </t:page>
</f:view>
