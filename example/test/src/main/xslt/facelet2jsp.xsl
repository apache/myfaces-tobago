<?xml version="1.0" encoding="UTF-8"?>
<!--
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
-->
<!--
  This code is experimental and only for use in this example
-->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:jsp="http://java.sun.com/JSP/Page"
                xmlns:ui="http://java.sun.com/jsf/facelets"
                xmlns:tc="http://myfaces.apache.org/tobago/component"
                xmlns:tx="http://myfaces.apache.org/tobago/extension"
                xmlns:h="http://java.sun.com/jsf/html"
                xmlns:f="http://java.sun.com/jsf/core"
                xmlns:layout="urn:jsptagdir:/WEB-INF/tags/layout">

  <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

  <xsl:template match="/ui:composition">
    <xsl:comment>
      This Code is generated. Do not change.
    </xsl:comment>

    <xsl:comment>
      * Licensed to the Apache Software Foundation (ASF) under one or more
      * contributor license agreements. See the NOTICE file distributed with
      * this work for additional information regarding copyright ownership.
      * The ASF licenses this file to You under the Apache License, Version 2.0
      * (the "License"); you may not use this file except in compliance with
      * the License. You may obtain a copy of the License at
      *
      * http://www.apache.org/licenses/LICENSE-2.0
      *
      * Unless required by applicable law or agreed to in writing, software
      * distributed under the License is distributed on an "AS IS" BASIS,
      * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      * See the License for the specific language governing permissions and
      * limitations under the License.
    </xsl:comment>

    <jsp:root version="2.0">
      <jsp:directive.page contentType="text/html; charset=utf-8"/>   
      <layout:overview>
        <jsp:params>
          <xsl:apply-templates select="ui:param" mode="parameter"/>
        </jsp:params>
        <jsp:body>
          <xsl:apply-templates/>
        </jsp:body>
      </layout:overview>
    </jsp:root>
  </xsl:template>

  <xsl:template match="ui:param" mode="parameter">
    <jsp:param name="title" value="BASICS"/>
  </xsl:template>

  <xsl:template match="ui:param">
    <!-- ignore -->
  </xsl:template>

  <xsl:template match="*">
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
