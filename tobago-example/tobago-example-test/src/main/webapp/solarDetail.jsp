<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
  <tc:page width="750px" height="300px">
    <f:facet name="layout">
      <tc:gridLayout/>
    </f:facet>
    <tc:box >
       <f:facet name="layout">
          <tc:gridLayout columns="1*" rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;fixed;*"/>
        </f:facet>
       <tx:in value="#{test.name}" label="Name" readonly="true" />
       <tx:in value="#{test.number}" label="Number" readonly="true" />
       <tx:in value="#{test.orbit}" label="Orbit" readonly="true" />
       <tx:in value="#{test.distance}" label="Distance" readonly="true" />
       <tx:in value="#{test.period}" label="Period" readonly="true" />
       <tx:in value="#{test.incl}" label="Incl" readonly="true" />
       <tx:in value="#{test.eccen}" label="Eccen" readonly="true" />
       <tx:in value="#{test.discoverer}" label="Discoverer" readonly="true" />
       <tx:in value="#{test.discoverYear}" label="DiscoverYear" readonly="true" />  
       <tc:cell/>
    </tc:box>
  </tc:page>
</f:view>
