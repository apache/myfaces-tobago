<%--
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
--%>
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