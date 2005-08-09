<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="tab2" >

  <t:tabGroup id="tabs" state="#{demo.tabState0}" >
  <t:tab label="#{bundle.tabClientSide}" >
      <t:tabGroup id="tabMarsOuterForm" state="#{demo.tabState1}" >
        <t:tab labelWithAccessKey="#{bundle.tabPlanet}">
          <jsp:include page="/solar/planetOuterForm.jsp" />
        </t:tab>
        <t:tab labelWithAccessKey="#{bundle.tabInsolar}">
          <jsp:include page="/solar/insolarOuterForm.jsp" />
        </t:tab>
        <t:tab label="#{bundle.tabMoons}" accessKey="#{bundle.tabMoonsAccessKey}" >
          <jsp:include page="/solar/moons.jsp" />
        </t:tab>
      </t:tabGroup>
    </t:tab>
    <t:tab label="#{bundle.tabServerSide}" >
      <t:tabGroup id="tabMarsOuterForm2" serverside="true" state="#{demo.tabState2}" >
        <t:tab labelWithAccessKey="#{bundle.tabPlanet}">
          <jsp:include page="/solar/planetOuterForm2.jsp" />
        </t:tab>
        <t:tab labelWithAccessKey="#{bundle.tabInsolar}">
          <jsp:include page="/solar/insolarOuterForm2.jsp" />
        </t:tab>
        <t:tab label="#{bundle.tabMoons}">
          <jsp:include page="/solar/moons2.jsp" />
        </t:tab>
      </t:tabGroup>
    </t:tab>

  </t:tabGroup>

</f:subview>
