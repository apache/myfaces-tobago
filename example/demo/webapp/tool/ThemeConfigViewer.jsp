<%@ page import="org.apache.myfaces.tobago.clientConfig.ThemeConfigViewController"%>
 <%--
  $Id: ThemeConfigViewer.jsp 1203 2005-04-01 18:08:31 +0200 (Fr, 01 Apr 2005) lofwyr $
--%>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>



<%
  ThemeConfigViewController controller = (ThemeConfigViewController)
      pageContext.getAttribute("controller", PageContext.SESSION_SCOPE);
  if (controller == null) {
    controller = new ThemeConfigViewController();
  }
  pageContext.setAttribute("controller", controller, PageContext.SESSION_SCOPE);
%>

<!-- jsp:useBean id="controller" class="org.apache.myfaces.tobago.tobago.tool.ThemeConfigViewcontroller"
    scope="session" / -->

<f:view >
  <t:page label="Tobago ThemeConfigViewer" width="800" height="600"  >
    <f:facet name="layout" >
      <t:gridLayout columns="*;100px" rows="100px;fixed;*"/>
    </f:facet>

    <t:cell spanX="2" id="cell1" >
      <f:facet name="layout" >
        <t:gridLayout columns="*;100px" />
      </f:facet>

      <t:cell spanX="2" id="cell2" >
        Actual Theme : <t:out value="#{clientConfigController.localizedTheme}" id="out1" />
      </t:cell>

      <t:selectOneChoice value="#{clientConfigController.theme}"  id="select1"
                           label="Theme">
          <f:selectItems value="#{clientConfigController.themeItems}" />
      </t:selectOneChoice>

      <t:button action="#{controller.changeTheme}" id="button1" label="Wechseln" />
    </t:cell>


    <t:selectOneChoice value="#{controller.rendererType}" id="select2" >
      <f:selectItems value="#{controller.selectItems}"/>
    </t:selectOneChoice>
    <t:button action="#{controller.doRequest}" id="button2" label="Show" />

    <t:cell spanX="2" id="cell3" >
      <t:sheet value="#{controller.entrys}" columns="*;100px" var="entry" id="sheet">
        <t:column label="name" id="column1" >
          <t:out value="#{entry.key}" id="co1" />
        </t:column>

        <t:column label="value" id="column2">
          <t:out value="#{entry.value}" id="co2"/>
        </t:column>
      </t:sheet>
    </t:cell>

  </t:page>
</f:view>

