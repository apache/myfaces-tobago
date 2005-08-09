<%@ page import="javax.faces.context.FacesContext"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="footer" >

  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="70px;70px;130px;1*;111px"
          rows="15px;fixed"/>
    </f:facet>

    <t:cell spanX="5">
      <hr />
    </t:cell>

    <t:button immediate="true"
      action="#{miniHowtoNavigation.gotoPrevious}"
      disabled="#{miniHowtoNavigation.first}"
      label="#{overviewBundle.footer_previous}" />

    <t:button immediate="true"
      action="#{miniHowtoNavigation.gotoNext}"
      disabled="#{miniHowtoNavigation.last}"
      label="#{overviewBundle.footer_next}" />

    <t:button action="#{miniHowtoNavigation.viewSource}"
        immediate="true" label="#{overviewBundle.footer_viewSource}" />

    <t:cell />

    <t:link id="atanion_link" action="http://www.atanion.com/"
        type="navigate" image="image/poweredBy.gif" />

  </t:panel>
</f:subview>
