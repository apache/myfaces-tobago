<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="text_jsp" >

  <t:messages />

  <t:box label="#{bundle.textBox}">
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>
    <t:in value="#{demo.text[0]}" labelWithAccessKey="#{bundle.textbox_0}" />
    <t:in value="#{demo.text[1]}" focus="true" required="true" labelWithAccessKey="#{bundle.textbox_1}" >
      <f:facet name="layout"><t:labeledInputLayout layout="100px;1*;0px"/></f:facet>
    </t:in>
    <t:in value="#{demo.text[2]}" label="#{bundle.textbox_2}" readonly="true" />
    <t:in value="#{demo.text[3]}" label="#{bundle.textbox_3}" disabled="true" />
    <t:in value="#{demo.text[4]}" labelWithAccessKey="#{bundle.textbox_4}" password="true" />
    <t:in value="#{demo.integer}" label="#{bundle.long_input}" />
    <t:in value="#{demo.aDouble}" label="#{bundle.double_input}" />
  </t:box>

  <t:box label="#{bundle.textArea}">
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>
    <t:textarea value="#{demo.text[5]}" labelWithAccessKey="#{bundle.textbox_10}" rows="4" />
    <t:textarea labelWithAccessKey="#{bundle.textbox_11}" value="#{demo.text[6]}" rows="4" readonly="true"  />
    <t:textarea labelWithAccessKey="#{bundle.textbox_12}" value="#{demo.text[7]}" rows="4" disabled="true"  />
  </t:box>

  <t:box label="#{bundle.inlineElements}">
    Eine Eingabe
    <t:in value="#{demo.text[8]}" inline="true" />
    innerhalb eines Textes.
    Eine Eingabe mit
    <t:in value="#{demo.text[9]}" inline="true">
      <t:label labelWithAccessKey="#{bundle.textInlinelabel}" inline="true" />
    </t:in>
    innerhalb eines Textes.
  </t:box>

  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="100px;*" />
    </f:facet>

    <t:button action="submit" defaultCommand="true"  label="#{bundle.submit}" />

    <t:cell />

  </t:panel>

</f:subview>
