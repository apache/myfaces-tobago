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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:subview id="footer">

  <tc:panel>
    <f:facet name="layout">
      <tc:gridLayout columns="auto;auto;auto;auto;auto;*" rows="15px;auto"/>
    </f:facet>

    <tc:separator>
      <tc:gridLayoutConstraint columnSpan="6"/>
    </tc:separator>

    <tc:button immediate="true"
               image="image/prev.gif"
               action="#{navigationState.gotoPrevious}"
               disabled="#{navigationState.first}"
               label="#{overviewBundle.footer_previous}"/>

    <tc:button immediate="true"
               image="image/next.gif"
               action="#{navigationState.gotoNext}"
               disabled="#{navigationState.last}"
               label="#{overviewBundle.footer_next}"/>

    <tc:button action="#{navigationTree.viewSource}"
               immediate="true" label="#{overviewBundle.footer_viewSource}"
               target="Source Viewer" transition="false"/>

    <tc:button action="#{pageDeclarationLanguage.asJspx}" disabled="#{pageDeclarationLanguage.jspx}"
               immediate="true" label="As JSP" tip="Use JSP as render technologie"/>

    <tc:button action="#{pageDeclarationLanguage.asFacelets}" disabled="#{pageDeclarationLanguage.facelets}"
               immediate="true" label="As Facelet" tip="Use Facelets as render technologie"/>

    <tc:out value="#{overviewBundle.notTranslated}"/>

  </tc:panel>
</f:subview>
