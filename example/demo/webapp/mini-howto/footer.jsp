<%--
 * Copyright 2002-2005 atanion GmbH.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
--%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="footer" >

  <tc:panel>
    <f:facet name="layout">
      <tc:gridLayout columns="70px;70px;130px;1*"
          rows="15px;fixed"/>
    </f:facet>

    <tc:cell spanX="4">
      <f:verbatim><hr /></f:verbatim>
    </tc:cell>

    <tc:button immediate="true"
      image="image/prev.gif"
      action="#{miniHowtoNavigation.gotoPrevious}"
      disabled="#{miniHowtoNavigation.first}"
      label="#{overviewBundle.footer_previous}" />

    <tc:button immediate="true"
      image="image/next.gif"
      action="#{miniHowtoNavigation.gotoNext}"
      disabled="#{miniHowtoNavigation.last}"
      label="#{overviewBundle.footer_next}" />

    <tc:button action="#{miniHowtoNavigation.viewSource}"
        immediate="true" label="#{overviewBundle.footer_viewSource}" />

    <tc:out value="#{overviewBundle.notTranslated}"/>

    <%--<tc:link id="atanion_link" action="http://www.atanion.com/"--%>
        <%--type="navigate" image="image/poweredBy.gif" />--%>

  </tc:panel>
</f:subview>
