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
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.faces.context.FacesContext" %>

<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<%
  FacesContext facesContext = FacesContext.getCurrentInstance();
  facesContext.addMessage("message1", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info message.", "Example of an info message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn message.", "Example of a warn message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error message.", "Example of an error message."));
  facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal message.", "Example of a fatal message."));
  facesContext.addMessage(null, new FacesMessage("Message without a severity.", "Example of a message without a severity."));
%>

<layout:overview>
  <jsp:body>
    <tc:box label="Output Controls">
      <f:facet name="layout">
        <tc:gridLayout columns="150px;*" rows="auto;300px;auto;auto;90px;*" border="1"/>
      </f:facet>

      <%-- code-sniplet-start id="label" --%>
      <tc:label value="_Single Label"/>
      <%-- code-sniplet-end id="label" --%>
      <tc:cell/>

      <tc:cell spanX="2">
        <%-- code-sniplet-start id="out" --%>
        <tc:out value="Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Quisque consequat, libero eget porta mattis, risus velit congue magna, at posuere sem orci vitae turpis. Integer pulvinar. Cras libero. Proin vestibulum tempor urna. Nulla odio nisl, auctor vitae, faucibus pharetra, feugiat eget, justo. Suspendisse at tellus non justo dictum tincidunt. Aenean placerat nunc id tortor. Donec mollis ornare pede. Vestibulum ut arcu et dolor auctor varius. Praesent tincidunt, eros quis vulputate facilisis, orci turpis sollicitudin justo, id faucibus nunc orci sed purus. Proin ligula erat, sollicitudin id, rhoncus eget, nonummy sit amet, risus. Aenean arcu lorem, facilisis et, posuere sed, ultrices tincidunt, nunc. Sed ac massa. Quisque lacinia. Donec quis nibh.

          Aenean ac diam eget mi feugiat pulvinar. Etiam orci. Aliquam nec arcu nec eros ornare pulvinar. Sed nec velit. Ut ut orci. Nulla varius. Maecenas feugiat. Etiam varius ipsum et orci. Ut consectetuer odio sit amet libero. Nulla iaculis adipiscing purus. Maecenas a sed."/>
        <%-- code-sniplet-end id="out" --%>
      </tc:cell>

      <%-- code-sniplet-start id="messages" --%>
      <tc:messages />
      <%-- code-sniplet-end id="messages" --%>
      <tc:cell/>

      <%--<tc:messages maxNumber="2"/>--%>
      <tc:cell/>
      <tc:cell/>

      <%-- code-sniplet-start id="image" --%>
      <tc:image value="image/tobago_head.gif" width="150" height="83"/>
      <%-- code-sniplet-end id="image" --%>
      <tc:cell/>

      <tc:cell/>
      <tc:cell/>

    </tc:box>
  </jsp:body>
</layout:overview>
