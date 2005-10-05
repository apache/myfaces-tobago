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
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:subview id="footer" >

  <t:panel id="a">
    <f:facet name="layout">
      <t:gridLayout columns="70px;70px;130px;1*"
          rows="15px;fixed" id="b"/>
    </f:facet>

    <t:cell spanX="4" id="c">
     <f:verbatim><hr /></f:verbatim>
    </t:cell>

    <t:button immediate="true"
      image="image/prev.gif"
      action="#{overviewNavigation.gotoPrevious}"
      disabled="#{overviewNavigation.first}"
      label="#{overviewBundle.footer_previous}"  id="d"/>

    <t:button immediate="true"
      image="image/next.gif"
      action="#{overviewNavigation.gotoNext}"
      disabled="#{overviewNavigation.last}"
      label="#{overviewBundle.footer_next}"  id="e"/>

    <t:button action="#{overviewNavigation.viewSource}"
        immediate="true" label="#{overviewBundle.footer_viewSource}"  id="f"/>

    <t:out value="#{overviewBundle.notTranslated}"/>

    <%--<t:link id="atanion_link" action="http://www.atanion.com/"--%>
        <%--type="navigate" image="image/poweredBy.gif" />--%>

  </t:panel>
</f:subview>
