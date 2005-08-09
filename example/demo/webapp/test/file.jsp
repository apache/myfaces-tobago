<%@ page errorPage="/errorPage.jsp"
  import="org.apache.commons.logging.Log,
          org.apache.commons.logging.LogFactory" %>
<%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%!
  private static final Log log
      = LogFactory.getLog("com.atanion.tobago-demo.test.file_jsp");


%>
<jsp:useBean id="fileItem" class="org.apache.commons.fileupload.FileItem" scope="session"  />
<jsp:useBean id="fileList" class="java.util.ArrayList" scope="session"  />
<f:view>
<t:page width="750px" >
<%
  try{
%>
<%-- File --%>

  <t:box id="filepanel" label="#{bundle.boxFileUpload}"
      height="200px">
    <f:facet name="layout">
      <t:gridLayout columns="3*;1*;2*"  />
    </f:facet>

<%-- row --%>

    <t:file value="#{fileItem}" id="file2" label="fileUpload" />

    <t:button>
<%--      todo: implement the adding of the fileItem to the list --%>
      <t:out value="add" />
    </t:button>

    <t:sheet value="#{fileList}" var="file" columns="1*"
        showHeader="false" >
      <t:column>
        <t:out value="#{file.name}" />
      </t:column>
    </t:sheet>

<%-- row --%>

    <t:button label="submit" />

    <t:cell/>

    <t:cell/>

  </t:box>


<%
  } catch(Throwable e) {
    log.error("file.jsp", e);
  }
%>
</t:page>
</f:view>
