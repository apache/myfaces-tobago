<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout />
      </f:facet>

  <%--
      <t:out escape="false" value="#{overviewBundle.form_text1}" />
  --%>

      <t:box label="#{overviewBundle.form_outTitle}">
        <f:facet name="layout">
          <t:gridLayout rows="1*;fixed;fixed;fixed" />
        </f:facet>

        <t:out escape="false" value="#{overviewBundle.form_text2}" />

        <t:box label="#{overviewBundle.form_in1Title}">
          <f:facet name="layout">
            <t:gridLayout rows="fixed;fixed;" columns="1*;100px"  />
          </f:facet>

          <t:form id="form1" >
            <t:in value="#{demo.text[0]}" required="true" label="#{overviewBundle.newValue1}" />
            <t:cell spanY="2" >
              <t:button label="#{overviewBundle.submitForm1}" />
            </t:cell>
            <t:in value="#{demo.text[0]}" readonly="true" label="#{overviewBundle.modelValue1}" />
          </t:form>

        </t:box>

        <t:box label="#{overviewBundle.form_in2Title}">
          <f:facet name="layout">
            <t:gridLayout rows="fixed;fixed" columns="1*;100px"/>
          </f:facet>

          <t:form id="form2">
            <t:in value="#{demo.text[1]}" required="true" label="#{overviewBundle.newValue2}" />
            <t:cell spanY="2" >
               <t:button label="#{overviewBundle.submitForm2}" />
            </t:cell>
            <t:in value="#{demo.text[1]}" readonly="true" label="#{overviewBundle.modelValue2}" />
          </t:form>
        </t:box>

        <t:panel>
          <f:facet name="layout">
            <t:gridLayout columns="*;150px"   />
          </f:facet>

          <t:cell/>
          <t:button label="#{overviewBundle.submitAll}" />

        </t:panel>

      </t:box>
    </t:panel>
  </jsp:body>
</layout:overview>
