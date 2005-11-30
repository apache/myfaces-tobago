<%--
 * Copyright 2002-2005 The Apache Software Foundation.
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
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>

  <tc:page label="Start" width="640px" height="480px">

    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout rows="fixed; 1*" columns="100px; 1*"/>
      </f:facet>

      <tc:button label="Start" action="dialog:Adresslist" />
      <tc:cell/>

      <tc:cell spanX="2"/>
    </tc:panel>
  </tc:page>
</f:view>
