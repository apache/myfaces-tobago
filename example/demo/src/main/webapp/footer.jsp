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
<f:subview id="footer" >

  <tc:panel id="a">
    <f:facet name="layout">
      <tc:gridLayout columns="70px;70px;130px;1*"
          rows="15px;fixed" id="b"/>
    </f:facet>

    <tc:cell spanX="4" id="c">
     <f:verbatim><hr /></f:verbatim>
    </tc:cell>

    <%-- fixme: next and previous button are not working in them moment (rendered="false") --%>
    <tc:button immediate="true" rendered="false"
      image="image/prev.gif"
      action="#{overviewNavigation.gotoPrevious}"
      disabled="#{overviewNavigation.first}"
      label="#{overviewBundle.footer_previous}"  id="d"/>

    <%-- fixme: next and previous button are not working in them moment (rendered="false") --%>
    <tc:button immediate="true" rendered="false"
      image="image/next.gif"
      action="#{overviewNavigation.gotoNext}"
      disabled="#{overviewNavigation.last}"
      label="#{overviewBundle.footer_next}"  id="e"/>

    <tc:button action="#{overviewNavigation.viewSource}"
        immediate="true" label="#{overviewBundle.footer_viewSource}"  id="f"/>

    <tc:out value="#{overviewBundle.notTranslated}"/>

    <%--<tc:link id="atanion_link" action="http://www.atanion.com/"--%>
        <%--type="navigate" image="image/poweredBy.gif" />--%>

  </tc:panel>
</f:subview>
