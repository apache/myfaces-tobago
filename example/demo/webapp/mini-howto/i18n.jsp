<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:mini-howto>
  <jsp:body>
    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="110px;1*;fixed;65px;38px;65px" />
      </f:facet>

      <t:out escape="false"  value="#{miniHowtoBundle.i18nText1}" />

      <t:box label="#{miniHowtoBundle.i18nCodeExampleBoxTitle1}" >
        <t:out value="#{miniHowtoBundle.i18nCodeExample1}" />
      </t:box>

      <t:out escape="false"  value="#{miniHowtoBundle.i18nText2}" />

      <t:box label="#{miniHowtoBundle.i18nCodeExampleBoxTitle2}" >
        <t:out escape="true" value="#{miniHowtoBundle.i18nCodeExample2}" />
      </t:box>


      <t:out escape="false"  value="#{miniHowtoBundle.i18nText3}" />

      <t:box label="#{miniHowtoBundle.i18nCodeExampleBoxTitle3}" >
        <t:out escape="true" value="#{miniHowtoBundle.i18nCodeExample3}" />
      </t:box>

    </t:panel>
  </jsp:body>
</layout:mini-howto>
