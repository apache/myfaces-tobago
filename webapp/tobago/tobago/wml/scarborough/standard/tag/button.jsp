<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%><%@ page import="com.atanion.tobago.TobagoConstants,
                       java.util.Iterator,
                       com.atanion.tobago.component.UIPage,
                       javax.faces.component.UIComponent,
                       com.atanion.tobago.component.ComponentUtil,
                       org.apache.commons.logging.Log,
                       com.atanion.tobago.renderkit.RenderUtil,
                       com.atanion.tobago.renderkit.wml.scarborough.standard.tag.ButtonRenderer,
                       javax.faces.component.UICommand,
                       com.atanion.tobago.renderkit.HtmlUtils,
                       com.atanion.util.KeyValuePair,
                       javax.faces.component.ValueHolder,
                       javax.faces.context.FacesContext"
%><%! private static final Log LOG = RenderUtil.getLog(ButtonRenderer.class);
%><%

  UICommand component =  (UICommand) RenderUtil.getComponent(request);
  UIPage uiPage = ComponentUtil.findPage(component);

  FacesContext facesContext = FacesContext.getCurrentInstance();

  String type = (String) component.getAttributes().get(TobagoConstants.ATTR_TYPE);
  String action = (String) component.getAttributes().get(TobagoConstants.ATTR_COMMAND_NAME);

  if ("submit".equals(type) && uiPage != null) {
    ValueHolder labelComponent
        = (ValueHolder) component.getFacet(TobagoConstants.FACET_LABEL);
    String label = (String) labelComponent.getValue();
    uiPage.getPostfields().add(
        new KeyValuePair(component.getClientId(facesContext), label));
%><anchor><%= label %>
     <go href="<%= action %>"><%
    Iterator iter = uiPage.getPostfields().iterator();
       while (iter.hasNext()) {
         KeyValuePair postField = (KeyValuePair) iter.next();%>
        <postfield name="<%= postField.getKey() %>" value="<%= postField.getValue() %>" />
<%
       }
%>   </go>
  </anchor>
<%
  } else {
    LOG.error("tobago.wml.scarborough.default.button.jsp: button type \""+
        type + "\" is not supported!");
  }
%>