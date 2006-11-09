<%--
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
--%>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<layout:screenshot>
  <f:subview id="out">
    <jsp:body>
      <tc:panel>
        <f:facet name="layout">
          <tc:gridLayout rows="300px;1*" />
        </f:facet>
<%-- code-sniplet-start id="out" --%>
          <tc:out value="Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
            Quisque consequat, libero eget porta mattis, risus velit congue magna,
            at posuere sem orci vitae turpis. Integer pulvinar. Cras libero.
            Proin vestibulum tempor urna. Nulla odio nisl, auctor vitae, faucibus
            pharetra, feugiat eget, justo. Suspendisse at tellus non justo dictum
            tincidunt. Aenean placerat nunc id tortor. Donec mollis ornare pede.
            Vestibulum ut arcu et dolor auctor varius. Praesent tincidunt, eros
            quis vulputate facilisis, orci turpis sollicitudin justo, id faucibus
            nunc orci sed purus. Proin ligula erat, sollicitudin id, rhoncus
            eget, nonummy sit amet, risus. Aenean arcu lorem, facilisis et, posuere
            sed, ultrices tincidunt, nunc. Sed ac massa. Quisque lacinia. Donec
            quis nibh.

            Aenean ac diam eget mi feugiat pulvinar. Etiam orci. Aliquam nec arcu nec eros
            ornare pulvinar. Sed nec velit. Ut ut orci. Nulla varius. Maecenas feugiat.
            Etiam varius ipsum et orci. Ut consectetuer odio sit amet libero. Nulla iaculis
            adipiscing purus. Maecenas a sed." />
<%-- code-sniplet-end id="out" --%>
        <tc:cell/>

      </tc:panel>

    </jsp:body>
  </f:subview>
</layout:screenshot>