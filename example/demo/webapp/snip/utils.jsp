<%@ page
  errorPage="/errorPage.jsp"
  import="org.apache.myfaces.tobago.model.DateModel,
          java.util.GregorianCalendar,
          javax.swing.BoundedRangeModel,
          javax.swing.DefaultBoundedRangeModel,
          java.util.Calendar,
          org.apache.commons.logging.LogFactory,
          org.apache.commons.logging.Log,
          javax.faces.component.UIComponent,
          javax.faces.context.FacesContext"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %><%!
  private static final Log log = LogFactory.getLog("org.apache.myfaces.tobago-demo.utils.jsp");
%>
<f:subview id="utils_jsp" >
<%
  try{
%>
<%--
  <t:script onload="onloadTest();" >
      function onloadTest() {
        alert('function onload2');
      }
  </t:script>
--%>

<%-- File --%>

  <t:box id="filepanel" label="#{bundle.boxFileUpload_multi}"
      height="100px">

    <f:facet name="layout">
      <t:gridLayout columns="3*;2*" rows="fixed;fixed;1*"  />
    </f:facet>
    <t:file value="#{demo.fileItem}" id="file1" />
    <t:cell spanY="3">
      <t:sheet value="#{demo.files}" var="file" columns="2*;1*"
          showHeader="false" >
        <t:column>
          <t:out value="#{file.name}" />
        </t:column>
        <t:column>
          <t:button actionListener="#{demo.removeFile}" >
            <f:param name="file" value="#{file}" />
            <t:out value="remove" />
          </t:button>
        </t:column>
      </t:sheet>
    </t:cell>
    <t:button width="120px" actionListener="#{demo.addFile}"  label="upload" />
    <t:cell/>
  </t:box>

<%-- File --%>

  <t:box id="filepanel2" label="#{bundle.boxFileUpload}">
    <t:file value="#{demo.fileItem2}" id="file2" labelWithAccessKey="_file upload" />
  </t:box>

<%-- Progress --%>

<%
  GregorianCalendar now = new GregorianCalendar();
  BoundedRangeModel minutes = new DefaultBoundedRangeModel(
     now.get(Calendar.MINUTE), 0, 0, 60);
  pageContext.setAttribute("progressminutes", minutes, PageContext.REQUEST_SCOPE);
  BoundedRangeModel seconds = new DefaultBoundedRangeModel(
     now.get(Calendar.SECOND), 0, 0, 60);
  pageContext.setAttribute("progressseconds", seconds, PageContext.REQUEST_SCOPE);
  %>

  <t:box label="#{bundle.boxProgress}">
    The number of minutes in this hour has a progress of&nbsp;
    <t:progress value="#{progressminutes}" />
    &nbsp;and the seconds in this minute&nbsp;
    <t:progress value="#{progressseconds}" />.
  </t:box>

<%-- Raw --%>

<%--
  <t:box label="raw"
      width="<%= Dimensions.getWidth(0) %>">
    <t:layout columnCount="2">
      <t:out value="atanion:text 1 "/>
      <t:out value="atanion:text 1 2"/>
      <f:verbatim> raw text fuer 2 1 </f:verbatim>
      <t:box label="raw">
        <f:verbatim> raw text fuer 2 2 </f:verbatim>
      </t:box>
    </t:layout>
  </t:box>

  <t:box label="test"
      width="<%= Dimensions.getWidth(0) %>"> <BR/>
    <t:link url="testlink.html"> content 1
      irgendwo
      <t:out value="atanion:text content"/> noch ein content
      <f:verbatim> raw content in atanion:raw </f:verbatim>
    </t:link>
  </t:box>
--%>

<%
  DateModel date = new DateModel(Calendar.getInstance());
%>

  <t:box label="#{bundle.utilsBoxCalendar}" id="calendarpanel">
    <f:facet name="layout">
      <t:gridLayout />
    </f:facet>

    <t:in value="Vergleich" label="nur zum" readonly="true"  />

    <t:date value="#{demo.date}" label="#{bundle.utilsDateInput}">
      <f:convertDateTime pattern="dd.MM.yyyy" />
    </t:date>

    <t:date value="#{demo.date}">
      <f:convertDateTime />
    </t:date>

<%--    <t:link action="calendarWindow('page:utils_jsp:dateControl');" type="script"--%>
<%--       image="image/date.gif" />--%>

    <t:calendar value="#{demo.date}" />

    <t:panel>
      <f:facet name="layout">
        <t:gridLayout rows="100px;100px;1*"  />
      </f:facet>

      <t:button action="/calendar/day-view.jsp" type="navigate" label="#{bundle.calendarDay}" />

      <t:button action="/calendar/month-view.jsp" type="navigate" label="#{bundle.calendarMonth}" />

      <t:cell />
    </t:panel>

  </t:box>



<%-- popup --%>

  <t:box label="#{bundle.utilsBoxPopup}">
    <f:facet name="layout">
      <t:gridLayout rows="35px;fixed;fixed" />
    </f:facet>

    <t:out value="#{bundle.popupDescription}"/>
    <t:in value="#{demo.popupText}" label="#{bundle.popupText}" readonly="true" />
    <t:button action="#{demo.showPopup}" label="#{bundle.showPopup}" />


    <f:facet name="popup">
      <t:popup height="100" width="300" rendered="#{demo.popup}" id="popup" >

        <f:facet name="layout">
          <t:gridLayout rows="35px;fixed;1*;fixed" />
        </f:facet>

        <t:out value="#{bundle.popupEditDescription}"/>
        <t:in value="#{demo.popupText}" label="#{bundle.popupText}"
                        focus="#{demo.popup}" />
        <t:cell/>
        <t:button action="#{demo.hidePopup}" label="#{bundle.hidePopup}" />

      </t:popup>

    </f:facet>
  </t:box>

<%--  button--%>
  <t:panel>
    <f:facet name="layout">
      <t:gridLayout columns="100px;*" />
    </f:facet>

    <t:button label="#{bundle.submit}" />

    <t:cell />

  </t:panel>



<%
  } catch(Throwable e) {
    log.error("utils.jsp", e);
  }
%>
</f:subview>
