<%@ page import="javax.faces.context.FacesContext"%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="footer" >

  <t:panel id="a">
    <f:facet name="layout">
      <t:gridLayout columns="70px;70px;130px;1*;130px"
          rows="15px;fixed" id="b"/>
    </f:facet>

    <t:cell spanX="5" id="c">
      <hr />
    </t:cell>

    <t:button immediate="true"
      image="image/prev.gif"
      action="#{overviewNavigation.gotoPrevious}"
      disabled="#{overviewNavigation.first}"
      label="#{overviewBundle.footer_previous}"  id="d"/>

    <t:button immediate="true"
      image="image/next.gif"
      action="#{overviewNavigation.gotoNext}"
      disabled="#{overviewNavigation.last}"
      label="#{overviewBundle.footer_next}"  id="e"/>

    <t:button action="#{overviewNavigation.viewSource}"
        immediate="true" label="#{overviewBundle.footer_viewSource}"  id="f"/>

    <t:cell  id="g"/>

    <t:link id="atanion_link" action="http://www.atanion.com/"
        type="navigate" image="image/poweredBy.gif" />

  </t:panel>
</f:subview>
