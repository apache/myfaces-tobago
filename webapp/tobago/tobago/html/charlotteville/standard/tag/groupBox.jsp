<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%><%@ page import="com.atanion.tobago.renderkit.HtmlUtils,
                       com.atanion.tobago.TobagoConstants,
                       javax.faces.component.UIComponent,
                       org.apache.commons.logging.Log,
                       com.atanion.tobago.renderkit.RenderUtil,
                       com.atanion.tobago.renderkit.html.charlotteville.standard.tag.GroupBoxRenderer,
                       java.util.Iterator,
                       javax.faces.component.UIPanel,
                       com.atanion.tobago.TobagoConstants,
                       com.atanion.tobago.renderkit.HtmlUtils,
                       javax.faces.context.FacesContext"
%><%! private static final Log LOG = RenderUtil.getLog(GroupBoxRenderer.class);
%><%

  UIPanel component = (UIPanel) RenderUtil.getComponent(request) ;
  UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
  FacesContext facesContext = FacesContext.getCurrentInstance();

  String headerStyle =
      (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE_HEADER);
  String bodyStyle =
      (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE_BODY);
  LOG.debug("headerStyle=" + headerStyle);

%>
<table <%= HtmlUtils.generateAttribute("style", headerStyle)
%> cellpadding="0" cellspacing="0" border="0" summary="">
  <tr>
    <td <%=
   HtmlUtils.generateAttributeAppend("class", component, "tobago-groupbox-head-td-left")
%> >
<%   if (label != null) {
        RenderUtil.encode(facesContext, label);
     } else {
        %>&nbsp;<%
     }
%></td>
      <td <%=
   HtmlUtils.generateAttributeAppend("class", component, "tobago-groupbox-head-td-right")
%>>&nbsp;</td>
  </tr>
</table>     <%
  String style = (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE);
  LOG.debug("style=" + style);
  LOG.debug("style : " + HtmlUtils.generateAttribute("style", bodyStyle) );
%>
<table class="tobago-groupbox-body-td-border" <%= HtmlUtils.generateAttribute("style", bodyStyle)
%> cellpadding="0" cellspacing="0" border="0" summary="">
  <tr>
    <td <%=
  HtmlUtils.generateAttributeAppend("class", component, "tobago-groupbox-body-td")
%> valign="top">
<div <%= HtmlUtils.generateAttributeFromKey("class", component, TobagoConstants.ATTR_STYLE_CLASS) %> <%=
         HtmlUtils.generateAttributeFromKey("style", component,
             TobagoConstants.ATTR_STYLE_INNER)%>><%

    RenderUtil.encodePanel(facesContext, component);
%></div>
    </td>
  </tr>
</table>
