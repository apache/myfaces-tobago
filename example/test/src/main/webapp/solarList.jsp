<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <tc:page width="750px" height="300px">
    <f:facet name="layout">
      <tc:gridLayout/>
    </f:facet>
    <tc:box >
       <tc:sheet value="#{test.solarObjects}" id="sheet"
            columns="1*;1*;1*;1*;1*;1*;1*;1*;2*" var="solarObject"
            showHeader="true"  showPageRange="center" rows="10" >
          <tc:column label="Name" >
             <tc:link label="#{solarObject.name}" link="SolarDetail">
               <f:param value="#{solarObject.name}" name="id" />
             </tc:link>
          </tc:column>
          <tc:column label="Number" align="center" >
            <tc:out value="#{solarObject.number}" />
          </tc:column>
          <tc:column label="Orbit"  >
            <tc:out value="#{solarObject.orbit}" />
          </tc:column>
          <tc:column label="Distance" align="right" >
            <tc:out value="#{solarObject.distance}" />
          </tc:column>
          <tc:column label="Period" align="right" >
            <tc:out value="#{solarObject.period}" />
          </tc:column>
          <tc:column label="Incl" align="right" >
            <tc:out value="#{solarObject.incl}" />
          </tc:column>
          <tc:column label="Eccen" align="right" >
            <tc:out value="#{solarObject.eccen}" />
          </tc:column>
          <tc:column label="Discoverer" align="right" >
            <tc:out value="#{solarObject.discoverer}" />
          </tc:column>
          <tc:column label="DiscoverYear" align="right" >
            <tc:out value="#{solarObject.discoverYear}" />
          </tc:column>
        </tc:sheet>
    </tc:box>
  </tc:page>
</f:view>
