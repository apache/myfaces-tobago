<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%><%@ page import="com.atanion.tobago.component.UIPage,
                       com.atanion.tobago.component.BodyContentHandler,
                       com.atanion.tobago.TobagoConstants,
                       com.atanion.tobago.renderkit.RenderUtil"
%><%

  UIPage component = (UIPage)RenderUtil.getComponent(request);
%><?xml version='1.0'?>
<!DOCTYPE wml PUBLIC '-//WAPFORUM//DTD WML 1.1//EN'
 'http://www.wapforum.org/DTD/wml_1.1.xml'>
<wml>
  <card>
<% BodyContentHandler bodyContentHandler = (BodyContentHandler)
    component.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT); %>
<%= bodyContentHandler.getBodyContent() %>
  </card>
</wml>