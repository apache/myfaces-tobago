<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%><%@ page import="com.atanion.tobago.component.BodyContentHandler,
                       com.atanion.tobago.TobagoConstants,
                       com.atanion.tobago.renderkit.RenderUtil,
                       com.atanion.tobago.component.UIPanel"
%><%
  UIPanel component = (UIPanel) RenderUtil.getComponent(request) ;
  BodyContentHandler bodyContentHandler = (BodyContentHandler)
      component.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT);
%><%= bodyContentHandler.getBodyContent() %><%--</card> fixme ? --%>