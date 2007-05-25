<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<f:view>
  <tc:page width="600" height="300">
   <tc:loadBundle basename="overview" var="overviewBundle" />
    <f:facet name="layout">
      <tc:gridLayout/>
    </f:facet>
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="1*" />
        </f:facet>

        <tc:box label="#{overviewBundle.ekawTitle}">
          <f:facet name="layout">
            <tc:gridLayout />
          </f:facet>

          <tc:tabGroup id="tabs" switchType="reloadTab"
            selectedIndex="#{controller.selectedIndex0}">

            <tc:tabChangeListener
              type="org.apache.myfaces.tobago.example.test.SimpleTabChangeListener"
              binding="#{controller.tabChangeListener}" />

            <tc:tab label="#{overviewBundle.ekawTabLastYear}" rendered="true">

              <tc:tabGroup id="tabLastYear" switchType="reloadTab"
                selectedIndex="#{controller.selectedIndex1}" >

                <tc:tabChangeListener
                  type="org.apache.myfaces.tobago.example.testSimpleTabChangeListener"
                  binding="#{controller.tabChangeListener}" />

                <tc:tab label="#{overviewBundle.ekawTabEkLetztesJahr}">
                </tc:tab>

                <tc:tab label="#{overviewBundle.ekawTabKeekLetztesJahr}">
                </tc:tab>

                <tc:tab label="#{overviewBundle.ekawTabVekLetztesJahr}">
                </tc:tab>
              </tc:tabGroup>
            </tc:tab>

            <tc:tab label="#{overviewBundle.ekawTabCurrentYear}">

              <tc:tabGroup id="tabCurrentYear" switchType="reloadTab"
                selectedIndex="#{controller.selectedIndex2}">

                <tc:tabChangeListener
                  type="org.apache.myfaces.tobago.example.test.SimpleTabChangeListener"
                  binding="#{controller.tabChangeListener}" />

                <tc:tab label="#{overviewBundle.ekawTabEkLaufendenesJahr}">
                </tc:tab>

                <tc:tab label="#{overviewBundle.ekawTabKeekLaufendenesJahr}">
                </tc:tab>

                <tc:tab label="#{overviewBundle.ekawTabLeekLaufendenesJahr}">
                </tc:tab>

                <tc:tab label="#{overviewBundle.ekawTabVekLaufendenesJahr}">
                </tc:tab>
              </tc:tabGroup>
            </tc:tab>
          </tc:tabGroup>
        </tc:box>
      </tc:panel>
 </tc:page>
</f:view>