<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <tc:page width="750px" height="300px">
    <f:facet name="layout">
      <tc:gridLayout/>
    </f:facet>
    <tc:box >
       <tc:sheet binding="#{test.table}" value="#{test.solarObjects}" id="sheet"
            columns="1*;1*;1*;1*;1*;1*;1*;1*;2*" var="solarObject"
            showHeader="true"  showPageRange="center" rows="10" >
          <tc:column label="Name" sortable="true">
             <tc:link label="#{solarObject.name}" action="#{test.select}" >
             </tc:link>
          </tc:column>
          <tc:column label="Number" id="number" sortable="false"
                     align="center" >
            <tc:out value="#{solarObject.number}" />
          </tc:column>
          <tc:column label="Orbit" sortable="true" >
            <tc:out value="#{solarObject.orbit}" />
          </tc:column>
          <tc:column label="Distance" sortable="true" align="right" >
            <tc:out value="#{solarObject.distance}" />
          </tc:column>
          <tc:column label="Period" sortable="true" align="right" >
            <tc:out value="#{solarObject.period}" />
          </tc:column>
          <tc:column label="Incl" sortable="true" align="right" >
            <tc:out value="#{solarObject.incl}" />
          </tc:column>
          <tc:column label="Eccen" sortable="true" align="right" >
            <tc:out value="#{solarObject.eccen}" />
          </tc:column>
          <tc:column label="Discoverer" sortable="true" align="right" >
            <tc:out value="#{solarObject.discoverer}" />
          </tc:column>
          <tc:column label="DiscoverYear" sortable="true" align="right" >
            <tc:out value="#{solarObject.discoverYear}" />
          </tc:column>
        </tc:sheet>
    </tc:box>
  </tc:page>
</f:view>
