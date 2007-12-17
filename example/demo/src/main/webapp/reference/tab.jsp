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
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:overview>
  <jsp:body>
    <tc:box label="Tab">
      <f:facet name="layout">
        <tc:gridLayout/>
      </f:facet>
      <tc:tabGroup switchType="client" immediate="true">
        <tc:tab label="Tab 1">
          <tc:panel>
            <f:facet name="layout">
              <tc:gridLayout rows="1*;fixed;fixed;1*"/>
            </f:facet>
            <tc:cell/>
            <tx:in label="label 1" required="true"/>
            <tx:in label="label 2"/>
            <tc:cell/>
          </tc:panel>
        </tc:tab>
        <tc:tab label="Tab 2" rendered="true">
          <tc:panel>
            <f:facet name="layout">
              <tc:gridLayout rows="1*;fixed;fixed;1*"/>
            </f:facet>
            <tc:cell/>
            <tx:in label="label 3"/>
            <tx:in label="label 4"/>
            <tc:cell/>
          </tc:panel>
        </tc:tab>
        <tc:tab label="Tab 3">
          <tc:panel>
            <f:facet name="layout">
              <tc:gridLayout rows="1*;fixed;fixed;1*"/>
            </f:facet>
            <tc:cell/>
            <tx:in label="label 5"/>
            <tx:in label="label 6"/>
            <tc:cell/>
          </tc:panel>
        </tc:tab>
        <tc:tab label="Tab 4 - JavaServerFaces 2.0 is in the JSR review ballot phase. This JSR will bring the best ideas in web application development (circa early 2007) to the Java EE platform.">
          <tc:panel>
            <f:facet name="layout">
              <tc:gridLayout rows="1*;fixed;fixed;1*"/>
            </f:facet>
            <tc:cell/>
            <tx:in label="label 7"/>
            <tx:in label="label 8"/>
            <tc:cell/>
          </tc:panel>
        </tc:tab>

      </tc:tabGroup>
    </tc:box>
  </jsp:body>
</layout:overview>